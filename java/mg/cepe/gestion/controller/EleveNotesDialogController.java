package mg.cepe.gestion.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mg.cepe.gestion.controller.eleve.EleveReleveActions;
import mg.cepe.gestion.controller.notes.NotesFormHelper;
import mg.cepe.gestion.model.Eleve;
import mg.cepe.gestion.model.Matiere;
import mg.cepe.gestion.model.Note;
import mg.cepe.gestion.service.impl.EcoleServiceImpl;
import mg.cepe.gestion.service.impl.MatiereServiceImpl;
import mg.cepe.gestion.service.impl.NoteServiceImpl;
import mg.cepe.gestion.util.CodeFormat;
import mg.cepe.gestion.util.UiDialogs;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Dialogue notes d'un élève.
 * - Sans notes : saisie libre année/matière ; matières déjà notées retirées du combo.
 * - Avec notes : année par défaut = dernière année de l'élève (modifiable pour redoublement).
 */
public class EleveNotesDialogController {
    @FXML private Label lblTitre;
    @FXML private ComboBox<String> cbAnnee;
    @FXML private ComboBox<Matiere> cbMatiere;
    @FXML private TextField txtNote;
    @FXML private TableView<Note> tableNotes;
    @FXML private TableColumn<Note, String> colAnnee, colMatiere;
    @FXML private TableColumn<Note, Number> colNote;
    @FXML private Button btnAjouter, btnModifier, btnSupprimer, btnClear, btnReleve;

    private final NoteServiceImpl noteService = new NoteServiceImpl();
    private final MatiereServiceImpl matiereService = new MatiereServiceImpl();
    private final ObservableList<Note> data = FXCollections.observableArrayList();
    private NotesFormHelper form;
    private Eleve eleve;
    private boolean loading;

    @FXML
    private void initialize() {
        form = new NotesFormHelper(cbAnnee, cbMatiere, txtNote);
        cbAnnee.setEditable(true);
        cbAnnee.setPromptText("AAAA-AAAA");
        setupColumns();
        tableNotes.setItems(data);
        tableNotes.getSelectionModel().selectedItemProperty()
                .addListener((o, a, v) -> { if (v != null && !loading) onSelectNote(v); });
        cbAnnee.valueProperty().addListener((o, a, v) -> { if (!loading) onAnneeChanged(); });
        cbAnnee.getEditor().textProperty().addListener((o, a, v) -> { if (!loading) onAnneeChanged(); });
    }

    private void setupColumns() {
        colAnnee.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getAnneeScolaire()));
        colMatiere.setCellValueFactory(c -> {
            Matiere m = matiereService.trouverParId(c.getValue().getNumMat());
            return new javafx.beans.property.SimpleStringProperty(
                    m != null ? m.getDesignMat() : c.getValue().getNumMat());
        });
        colNote.setCellValueFactory(c ->
                new javafx.beans.property.SimpleDoubleProperty(c.getValue().getNote()));
    }

    public void setEleve(Eleve eleve) {
        this.eleve = eleve;
        if (lblTitre != null) {
            lblTitre.setText("Notes de " + eleve.getNom() + " " + eleve.getPrenom());
        }
        loading = true;
        try {
            List<Note> all = noteService.listerParEleve(eleve.getNumEleve());
            List<String> anneesEleve = all.stream()
                    .map(Note::getAnneeScolaire).distinct()
                    .sorted(Comparator.reverseOrder()).toList();
            List<String> anneesGlobal = noteService.listerAnnees();
            // Combo : années de l'élève en tête + autres années globales
            java.util.LinkedHashSet<String> merged = new java.util.LinkedHashSet<>(anneesEleve);
            merged.addAll(anneesGlobal);
            cbAnnee.setItems(FXCollections.observableArrayList(merged));

            if (!anneesEleve.isEmpty()) {
                // Cas 2 : notes existantes → année par défaut = plus récente
                cbAnnee.setValue(anneesEleve.get(0));
                if (cbAnnee.getEditor() != null) {
                    cbAnnee.getEditor().setText(anneesEleve.get(0));
                }
            } else {
                // Cas 1 : aucune note → saisie libre
                cbAnnee.setValue(null);
                if (cbAnnee.getEditor() != null) cbAnnee.getEditor().clear();
            }
        } finally {
            loading = false;
        }
        refreshTableAndMatieres();
        updateButtons();
    }

    private void onAnneeChanged() {
        refreshTableAndMatieres();
        updateButtons();
    }

    private void refreshTableAndMatieres() {
        if (eleve == null) return;
        String annee = form.resolveAnnee();
        if (CodeFormat.matches(annee, CodeFormat.REGEX_ANNEE)) {
            data.setAll(noteService.listerParEleveEtAnnee(eleve.getNumEleve(), annee));
        } else {
            data.setAll(noteService.listerParEleve(eleve.getNumEleve()));
        }
        refreshMatiereCombo();
    }

    /** Matières déjà notées pour l'année choisie → absentes du combo. */
    private void refreshMatiereCombo() {
        if (eleve == null) return;
        String annee = form.resolveAnnee();
        List<Matiere> all = matiereService.listerTous();
        Set<String> used = Set.of();
        if (CodeFormat.matches(annee, CodeFormat.REGEX_ANNEE)) {
            used = noteService.listerParEleveEtAnnee(eleve.getNumEleve(), annee).stream()
                    .map(Note::getNumMat).collect(Collectors.toSet());
        }
        Note selected = tableNotes.getSelectionModel().getSelectedItem();
        String keepMat = selected != null ? selected.getNumMat() : null;
        Set<String> finalUsed = used;
        List<Matiere> available = all.stream()
                .filter(m -> !finalUsed.contains(m.getNumMat())
                        || (keepMat != null && keepMat.equals(m.getNumMat())))
                .toList();
        Matiere current = cbMatiere.getValue();
        cbMatiere.setItems(FXCollections.observableArrayList(available));
        if (current != null && available.stream().anyMatch(m -> m.getNumMat().equals(current.getNumMat()))) {
            cbMatiere.setValue(current);
        } else if (keepMat != null) {
            available.stream().filter(m -> m.getNumMat().equals(keepMat)).findFirst()
                    .ifPresent(cbMatiere::setValue);
        } else {
            cbMatiere.setValue(null);
        }
    }

    private void onSelectNote(Note n) {
        form.fillFromNote(n);
        refreshMatiereCombo();
        matiereService.listerTous().stream()
                .filter(m -> m.getNumMat().equals(n.getNumMat()))
                .findFirst().ifPresent(cbMatiere::setValue);
    }

    private void updateButtons() {
        boolean hasAny = eleve != null && noteService.aDesNotes(eleve.getNumEleve());
        boolean tableHas = !data.isEmpty();
        // Ajouter toujours possible (nouvelle matière / nouvelle année)
        if (btnAjouter != null) btnAjouter.setDisable(false);
        // Modifier / Supprimer / Relevé si des notes existent pour le contexte
        if (btnModifier != null) btnModifier.setDisable(!tableHas && !hasAny);
        if (btnSupprimer != null) btnSupprimer.setDisable(!tableHas && !hasAny);
        if (btnReleve != null) btnReleve.setDisable(!hasAny);
        if (btnClear != null) btnClear.setDisable(false);
    }

    @FXML private void handleAjouter() {
        if (!form.valider()) return;
        try {
            noteService.ajouter(form.buildNote(eleve.getNumEleve()));
            UiDialogs.info("Note ajoutée avec succès.");
            tableNotes.getSelectionModel().clearSelection();
            form.clearFields();
            // garder l'année saisie
            String a = form.resolveAnnee();
            loading = true;
            try {
                if (CodeFormat.matches(a, CodeFormat.REGEX_ANNEE)) {
                    cbAnnee.setValue(a);
                    if (cbAnnee.getEditor() != null) cbAnnee.getEditor().setText(a);
                }
            } finally { loading = false; }
            refreshTableAndMatieres();
            updateButtons();
        } catch (Exception ex) {
            UiDialogs.warn("Erreur : " + ex.getMessage());
        }
    }

    @FXML private void handleModifier() {
        if (data.isEmpty()) {
            UiDialogs.warn("Aucune note à modifier pour cette année.");
            return;
        }
        if (!form.valider()) return;
        if (!UiDialogs.confirm("Modifier la note sélectionnée ?")) return;
        try {
            noteService.modifier(form.buildNote(eleve.getNumEleve()));
            UiDialogs.info("Note modifiée avec succès.");
            refreshTableAndMatieres();
            updateButtons();
        } catch (Exception ex) {
            UiDialogs.warn("Erreur : " + ex.getMessage());
        }
    }

    @FXML private void handleSupprimer() {
        if (form.getMatiere() == null) {
            UiDialogs.warn("Sélectionnez une ligne (matière) à supprimer.");
            return;
        }
        if (!UiDialogs.confirm("Supprimer la note de « " + form.getMatiere().getDesignMat()
                + " » pour " + form.resolveAnnee() + " ?")) return;
        try {
            noteService.supprimer(form.resolveAnnee(), eleve.getNumEleve(), form.getMatiere().getNumMat());
            UiDialogs.info("Note supprimée avec succès.");
            tableNotes.getSelectionModel().clearSelection();
            form.clearFields();
            refreshTableAndMatieres();
            updateButtons();
        } catch (Exception ex) {
            UiDialogs.warn("Erreur : " + ex.getMessage());
        }
    }

    @FXML private void handleClear() {
        if (!UiDialogs.confirm("Réinitialiser le formulaire de saisie ?")) return;
        tableNotes.getSelectionModel().clearSelection();
        form.clearFields();
        refreshMatiereCombo();
    }

    @FXML private void handleFermer() {
        ((Stage) tableNotes.getScene().getWindow()).close();
    }

    @FXML private void handleGenererReleve() {
        if (eleve == null || !noteService.aDesNotes(eleve.getNumEleve())) {
            UiDialogs.warn("Cet élève n'a aucune note. Saisissez d'abord les notes.");
            return;
        }
        String annee = form.resolveAnnee();
        if (!CodeFormat.matches(annee, CodeFormat.REGEX_ANNEE)) {
            // fallback dernière année de l'élève
            List<String> ys = noteService.listerParEleve(eleve.getNumEleve()).stream()
                    .map(Note::getAnneeScolaire).distinct().sorted(Comparator.reverseOrder()).toList();
            if (ys.isEmpty()) {
                UiDialogs.warn("Aucune année scolaire disponible.");
                return;
            }
            annee = ys.get(0);
        }
        if (!UiDialogs.confirm("Générer le relevé PDF pour " + eleve.getNom() + " " + eleve.getPrenom()
                + "\nAnnée scolaire : " + annee + " ?")) return;
        EleveReleveActions actions = new EleveReleveActions(
                noteService, matiereService, new EcoleServiceImpl(), null);
        actions.generer(eleve, annee);
    }
}
