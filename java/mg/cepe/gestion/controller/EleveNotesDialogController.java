package mg.cepe.gestion.controller;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import mg.cepe.gestion.controller.eleve.EleveReleveActions;
import mg.cepe.gestion.model.*;
import mg.cepe.gestion.service.impl.EcoleServiceImpl;
import mg.cepe.gestion.service.impl.MatiereServiceImpl;
import mg.cepe.gestion.service.impl.NoteServiceImpl;
import mg.cepe.gestion.util.CodeFormat;
import mg.cepe.gestion.util.UiDialogs;

public class EleveNotesDialogController {
    @FXML
    private Label lblTitre;
    @FXML
    private ComboBox<String> cbAnnee;
    @FXML
    private TilePane gridMatieres;
    @FXML
    private Button btnReleve;

    // Injecté automatiquement par JavaFX car l'inclus FXML a
    // fx:id="relevePreview"
    @FXML
    private RelevePreviewController relevePreviewController;

    private final NoteServiceImpl noteService = new NoteServiceImpl();
    private final MatiereServiceImpl matiereService = new MatiereServiceImpl();
    private final EcoleServiceImpl ecoleService = new EcoleServiceImpl();

    private Eleve eleve;
    private boolean loading;

    @FXML
    private void initialize() {
        cbAnnee.setEditable(true);
        cbAnnee.setPromptText("AAAA-AAAA");
        cbAnnee.valueProperty().addListener((o, a, v) -> {
            if (!loading)
                onAnneeChanged();
        });
        cbAnnee.getEditor().textProperty().addListener((o, a, v) -> {
            if (!loading)
                onAnneeChanged();
        });
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

            java.util.LinkedHashSet<String> merged = new java.util.LinkedHashSet<>(anneesEleve);
            merged.addAll(anneesGlobal);
            cbAnnee.setItems(FXCollections.observableArrayList(merged));

            if (!anneesEleve.isEmpty()) {
                cbAnnee.setValue(anneesEleve.get(0));
                if (cbAnnee.getEditor() != null) {
                    cbAnnee.getEditor().setText(anneesEleve.get(0));
                }
            } else {
                cbAnnee.setValue(null);
                if (cbAnnee.getEditor() != null)
                    cbAnnee.getEditor().clear();
            }
        } finally {
            loading = false;
        }
        refreshGrid();
    }

    private void onAnneeChanged() {
        refreshGrid();
    }

    private String getSelectedAnnee() {
        String v = cbAnnee.getEditor() != null ? cbAnnee.getEditor().getText() : null;
        if (v == null || v.isBlank())
            v = cbAnnee.getValue();
        return v == null ? "" : v.trim();
    }

    private void refreshGrid() {
        if (eleve == null || gridMatieres == null)
            return;

        gridMatieres.getChildren().clear();
        String annee = getSelectedAnnee();

        List<Matiere> matieres = matiereService.listerTous();
        Map<String, Note> notesMap = Map.of();

        if (CodeFormat.matches(annee, CodeFormat.REGEX_ANNEE)) {
            notesMap = noteService.listerParEleveEtAnnee(eleve.getNumEleve(), annee).stream()
                    .collect(Collectors.toMap(Note::getNumMat, n -> n, (n1, n2) -> n1));
        }

        for (Matiere m : matieres) {
            Note existingNote = notesMap.get(m.getNumMat());
            VBox card = createSubjectCard(m, existingNote);
            gridMatieres.getChildren().add(card);
        }

        if (btnReleve != null) {
            btnReleve.setDisable(!noteService.aDesNotes(eleve.getNumEleve()));
        }

        updateLivePreview(annee, matieres, notesMap);
    }

    private String obtenirNomEcole() {
        return ecoleService.listerTous().stream()
                .findFirst()
                .map(Ecole::getDesign)
                .orElse("");
    }

    private void updateLivePreview(String annee, List<Matiere> matieres, Map<String, Note> notesMap) {
        if (relevePreviewController == null)
            return;

        String nomEcole = obtenirNomEcole();
        relevePreviewController.updateInfosEleve(eleve, annee, nomEcole);

        List<LigneReleve> lignes = matieres.stream()
                .map(m -> {
                    Note n = notesMap.get(m.getNumMat());
                    double valNote = (n != null) ? n.getNote() : 0.0;
                    double notePonderee = valNote * m.getCoef();
                    return new LigneReleve(m.getDesignMat(), m.getCoef(), valNote, notePonderee);
                })
                .filter(l -> l.getNote() > 0)
                .toList();

        relevePreviewController.updateNotes(lignes);
    }

    private VBox createSubjectCard(Matiere matiere, Note noteExistance) {
        VBox card = new VBox();
        card.getStyleClass().add("subject-card");
        card.setAlignment(Pos.CENTER);
        card.setSpacing(6);

        Label lblNom = new Label(matiere.getDesignMat());
        lblNom.getStyleClass().add("subject-card-title");

        Label lblCoef = new Label("Coefficient : " + matiere.getCoef());
        lblCoef.getStyleClass().add("subject-card-coef");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        TextField txtNote = new TextField();
        txtNote.setPromptText("Note /20");
        txtNote.getStyleClass().add("subject-card-input");
        if (noteExistance != null) {
            txtNote.setText(String.valueOf(noteExistance.getNote()));
        }

        Button btnConfirm = new Button("✔");
        btnConfirm.getStyleClass().addAll("btn", "btn-primary", "btn-icon-only");

        btnConfirm.setOnAction(e -> handleSaveCardNote(matiere, txtNote.getText()));

        HBox inputRow = new HBox(8, txtNote, btnConfirm);
        inputRow.setAlignment(Pos.CENTER);
        HBox.setHgrow(txtNote, Priority.ALWAYS);

        card.getChildren().addAll(lblNom, lblCoef, spacer, inputRow);
        return card;
    }

    private void handleSaveCardNote(Matiere matiere, String noteText) {
        String annee = getSelectedAnnee();
        if (!CodeFormat.matches(annee, CodeFormat.REGEX_ANNEE)) {
            UiDialogs.warn("Veuillez d'abord saisir une année scolaire valide (ex. 2024-2025).");
            return;
        }

        if (noteText == null || noteText.isBlank()) {
            try {
                noteService.supprimer(annee, eleve.getNumEleve(), matiere.getNumMat());
                UiDialogs.info("Note de « " + matiere.getDesignMat() + " » supprimée.");
                refreshGrid();
            } catch (Exception ex) {
                UiDialogs.warn("Erreur lors de la suppression : " + ex.getMessage());
            }
            return;
        }

        try {
            double val = Double.parseDouble(noteText.trim().replace(',', '.'));
            if (val < 0 || val > 20) {
                UiDialogs.warn("La note doit être entre 0 et 20.");
                return;
            }

            Note note = new Note(annee, eleve.getNumEleve(), matiere.getNumMat(), val);

            Optional<Note> opt = noteService.listerParEleveEtAnnee(eleve.getNumEleve(), annee)
                    .stream().filter(n -> n.getNumMat().equals(matiere.getNumMat())).findFirst();

            if (opt.isPresent()) {
                noteService.modifier(note);
            } else {
                noteService.ajouter(note);
            }

            UiDialogs.info("Note enregistrée pour « " + matiere.getDesignMat() + " ».");
            refreshGrid();
        } catch (NumberFormatException ex) {
            UiDialogs.warn("Veuillez saisir un nombre valide (ex. 15.5).");
        } catch (Exception ex) {
            UiDialogs.warn("Erreur : " + ex.getMessage());
        }
    }

    @FXML
    private void handleFermer() {
        ((Stage) gridMatieres.getScene().getWindow()).close();
    }

    @FXML
    private void handleGenererReleve() {
        if (eleve == null || !noteService.aDesNotes(eleve.getNumEleve())) {
            UiDialogs.warn("Cet élève n'a aucune note. Saisissez d'abord les notes.");
            return;
        }
        String annee = getSelectedAnnee();
        if (!CodeFormat.matches(annee, CodeFormat.REGEX_ANNEE)) {
            List<String> ys = noteService.listerParEleve(eleve.getNumEleve()).stream()
                    .map(Note::getAnneeScolaire).distinct().sorted(Comparator.reverseOrder()).toList();
            if (ys.isEmpty()) {
                UiDialogs.warn("Aucune année scolaire disponible.");
                return;
            }
            annee = ys.get(0);
        }
        if (!UiDialogs.confirm("Générer le relevé PDF pour " + eleve.getNom() + " " + eleve.getPrenom()
                + "\nAnnée scolaire : " + annee + " ?"))
            return;

        EleveReleveActions actions = new EleveReleveActions(
                noteService, matiereService, new EcoleServiceImpl(), null);
        actions.generer(eleve, annee);
    }
}