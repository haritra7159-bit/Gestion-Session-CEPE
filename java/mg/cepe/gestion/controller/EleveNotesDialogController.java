package mg.cepe.gestion.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import mg.cepe.gestion.model.Eleve;
import mg.cepe.gestion.model.LigneReleve;
import mg.cepe.gestion.model.Matiere;
import mg.cepe.gestion.model.Note;
import mg.cepe.gestion.pdf.RelevePdfGenerator;
import mg.cepe.gestion.service.EcoleService;
import mg.cepe.gestion.service.MatiereService;
import mg.cepe.gestion.service.NoteService;
import mg.cepe.gestion.service.impl.EcoleServiceImpl;
import mg.cepe.gestion.service.impl.MatiereServiceImpl;
import mg.cepe.gestion.service.impl.NoteServiceImpl;

public class EleveNotesDialogController implements Initializable {
    @FXML
    private Label lblTitre;
    @FXML
    private TextField txtAnnee, txtNote;
    @FXML
    private ComboBox<Matiere> cbMatiere;
    @FXML
    private TableView<Note> tableNotes;
    @FXML
    private TableColumn<Note, String> colAnnee, colMatiere;
    @FXML
    private TableColumn<Note, Number> colNote;

    private final NoteService noteService = new NoteServiceImpl();
    private final MatiereService matiereService = new MatiereServiceImpl();
    private final EcoleService ecoleService = new EcoleServiceImpl();
    private final ObservableList<Note> data = FXCollections.observableArrayList();

    private Eleve eleve;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbMatiere.setItems(FXCollections.observableArrayList(matiereService.listerTous()));

        colAnnee.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAnneeScolaire()));
        colMatiere.setCellValueFactory(c -> {
            Matiere m = matiereService.trouverParId(c.getValue().getNumMat());
            return new javafx.beans.property.SimpleStringProperty(m != null ? m.getDesignMat() : "");
        });
        colNote.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getNote()));

        tableNotes.setItems(data);
        tableNotes.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> {
            if (val != null)
                fillForm(val);
        });
    }

    public void setEleve(Eleve eleve, Stage stage) {
        this.eleve = eleve;
        this.stage = stage;
        lblTitre.setText("Notes de " + eleve.getNomComplet());
        refresh();
    }

    private void fillForm(Note n) {
        txtAnnee.setText(n.getAnneeScolaire());
        txtNote.setText(String.valueOf(n.getNote()));
        cbMatiere.getItems().stream()
                .filter(m -> m.getNumMat().equals(n.getNumMat()))
                .findFirst().ifPresent(cbMatiere::setValue);
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleAjouter() {
        if (!valider())
            return;
        Note n = new Note(txtAnnee.getText(), eleve.getNumEleve(),
                cbMatiere.getValue().getNumMat(), Double.parseDouble(txtNote.getText()));
        noteService.ajouter(n);
        refresh();
        handleClear();
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleModifier() {
        Note selected = tableNotes.getSelectionModel().getSelectedItem();
        if (selected == null) {
            alert(Alert.AlertType.WARNING, "Veuillez sélectionner une note à modifier.");
            return;
        }
        if (!valider())
            return;
        Note n = new Note(txtAnnee.getText(), eleve.getNumEleve(),
                cbMatiere.getValue().getNumMat(), Double.parseDouble(txtNote.getText()));
        noteService.modifier(n);
        refresh();
        handleClear();
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleSupprimer() {
        Note selected = tableNotes.getSelectionModel().getSelectedItem();
        if (selected == null) {
            alert(Alert.AlertType.WARNING, "Veuillez sélectionner une note à supprimer.");
            return;
        }
        noteService.supprimer(selected.getAnneeScolaire(), eleve.getNumEleve(), selected.getNumMat());
        refresh();
        handleClear();
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleClear() {
        txtAnnee.clear();
        txtNote.clear();
        cbMatiere.setValue(null);
        tableNotes.getSelectionModel().clearSelection();
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleGenererReleve() {
        if (txtAnnee.getText().isBlank()) {
            alert(Alert.AlertType.WARNING, "Veuillez renseigner l'année scolaire.");
            return;
        }
        String annee = txtAnnee.getText();

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Génération du relevé PDF");
        confirm.setContentText("Générer le relevé de " + eleve.getNomComplet() + " pour l'année " + annee + " ?");
        Optional<ButtonType> res = confirm.showAndWait();
        if (res.isEmpty() || res.get() != ButtonType.OK)
            return;

        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Dossier de sauvegarde");
        File dir = dc.showDialog(stage);
        if (dir == null)
            return;

        List<Note> notes = noteService.listerParEleveEtAnnee(eleve.getNumEleve(), annee);
        List<Matiere> matieres = matiereService.listerTous();
        List<LigneReleve> lignes = new ArrayList<>();
        double totalPondere = 0;
        int totalCoef = 0;

        for (Note n : notes) {
            Matiere mat = matieres.stream()
                    .filter(m -> m.getNumMat().equals(n.getNumMat()))
                    .findFirst().orElse(null);
            if (mat != null) {
                double np = n.getNote() * mat.getCoef();
                lignes.add(new LigneReleve(mat.getDesignMat(), mat.getCoef(), n.getNote(), np));
                totalPondere += np;
                totalCoef += mat.getCoef();
            }
        }

        double moyenne = totalCoef == 0 ? 0 : totalPondere / totalCoef;
        String nomEcole = "Inconnue";
        var ec = ecoleService.trouverParId(eleve.getNumEcole());
        if (ec != null)
            nomEcole = ec.getDesign();

        String chemin = dir.getAbsolutePath() + "/Releve_" + eleve.getNom() + "_" + annee + ".pdf";
        try {
            RelevePdfGenerator.generer(chemin, annee, eleve, nomEcole, lignes, totalPondere, totalCoef, moyenne);
            alert(Alert.AlertType.INFORMATION, "PDF généré avec succès !\\n" + chemin);
        } catch (IOException ex) {
            alert(Alert.AlertType.ERROR, "Erreur : " + ex.getMessage());
        }
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleFermer() {
        stage.close();
    }

    private void refresh() {
        if (eleve != null) {
            data.setAll(noteService.listerParEleveEtAnnee(eleve.getNumEleve(), ""));
            // Si on veut TOUTES les notes de l'élève quelle que soit l'année :
            // On filtre depuis listerTous car listerParEleveEtAnnee demande une année
            // Alternative : récupérer toutes les notes et filtrer
            data.setAll(noteService.listerTous().stream()
                    .filter(n -> n.getNumEleve().equals(eleve.getNumEleve()))
                    .toList());
        }
    }

    private boolean valider() {
        if (txtAnnee.getText().isBlank() || txtNote.getText().isBlank() || cbMatiere.getValue() == null) {
            alert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs.");
            return false;
        }
        try {
            double note = Double.parseDouble(txtNote.getText());
            if (note < 0 || note > 20) {
                alert(Alert.AlertType.WARNING, "La note doit être comprise entre 0 et 20.");
                return false;
            }
        } catch (NumberFormatException e) {
            alert(Alert.AlertType.WARNING, "La note doit être un nombre valide.");
            return false;
        }
        return true;
    }

    private void alert(Alert.AlertType type, String message) {
        new Alert(type, message, ButtonType.OK).showAndWait();
    }
}