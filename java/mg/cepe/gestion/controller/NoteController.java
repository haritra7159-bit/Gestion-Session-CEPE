package mg.cepe.gestion.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import mg.cepe.gestion.model.Eleve;
import mg.cepe.gestion.model.Matiere;
import mg.cepe.gestion.model.Note;
import mg.cepe.gestion.service.EleveService;
import mg.cepe.gestion.service.MatiereService;
import mg.cepe.gestion.service.NoteService;
import mg.cepe.gestion.service.impl.EleveServiceImpl;
import mg.cepe.gestion.service.impl.MatiereServiceImpl;
import mg.cepe.gestion.service.impl.NoteServiceImpl;

public class NoteController implements Initializable {
    @FXML
    private TextField txtAnnee, txtNote;
    @FXML
    private ComboBox<Eleve> cbEleve;
    @FXML
    private ComboBox<Matiere> cbMatiere;
    @FXML
    private TableView<Note> tableNotes;
    @FXML
    private TableColumn<Note, String> colAnnee, colEleve, colMatiere;
    @FXML
    private TableColumn<Note, Number> colNote;

    private final NoteService noteService = new NoteServiceImpl();
    private final EleveService eleveService = new EleveServiceImpl();
    private final MatiereService matiereService = new MatiereServiceImpl();
    private final ObservableList<Note> data = FXCollections.observableArrayList();

    // Regex année scolaire : YYYY-YYYY
    private static final String REGEX_ANNEE = "^[0-9]{4}-[0-9]{4}$";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbEleve.setItems(FXCollections.observableArrayList(eleveService.listerTous()));
        cbMatiere.setItems(FXCollections.observableArrayList(matiereService.listerTous()));

        colAnnee.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAnneeScolaire()));
        colEleve.setCellValueFactory(c -> {
            Eleve e = eleveService.trouverParId(c.getValue().getNumEleve());
            return new javafx.beans.property.SimpleStringProperty(e != null ? e.getNomComplet() : "");
        });
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
        refresh();
    }

    private void fillForm(Note n) {
        txtAnnee.setText(n.getAnneeScolaire());
        txtNote.setText(String.valueOf(n.getNote()));
        cbEleve.getItems().stream()
                .filter(e -> e.getNumEleve().equals(n.getNumEleve()))
                .findFirst().ifPresent(cbEleve::setValue);
        cbMatiere.getItems().stream()
                .filter(m -> m.getNumMat().equals(n.getNumMat()))
                .findFirst().ifPresent(cbMatiere::setValue);
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleAjouter() {
        if (!valider())
            return;
        try {
            Note n = new Note(txtAnnee.getText().trim(), cbEleve.getValue().getNumEleve(),
                    cbMatiere.getValue().getNumMat(), Double.parseDouble(txtNote.getText().trim()));
            noteService.ajouter(n);
            refresh();
            handleClear();
        } catch (NumberFormatException ex) {
            alert(Alert.AlertType.ERROR, "Erreur lors de l'ajout : " + ex.getMessage());
        }
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleModifier() {
        if (!valider())
            return;
        Optional<ButtonType> res = confirm("Modifier cette note ?");
        if (res.isEmpty() || res.get() != ButtonType.OK)
            return;
        try {
            Note n = new Note(txtAnnee.getText().trim(), cbEleve.getValue().getNumEleve(),
                    cbMatiere.getValue().getNumMat(), Double.parseDouble(txtNote.getText().trim()));
            noteService.modifier(n);
            refresh();
        } catch (NumberFormatException ex) {
            alert(Alert.AlertType.ERROR, "Erreur lors de la modification : " + ex.getMessage());
        }
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleSupprimer() {
        if (txtAnnee.getText().isBlank() || cbEleve.getValue() == null || cbMatiere.getValue() == null) {
            alert(Alert.AlertType.WARNING, "Veuillez sélectionner une note à supprimer.");
            return;
        }
        Optional<ButtonType> res = confirm("Supprimer cette note ?");
        if (res.isEmpty() || res.get() != ButtonType.OK)
            return;
        try {
            noteService.supprimer(txtAnnee.getText().trim(), cbEleve.getValue().getNumEleve(),
                    cbMatiere.getValue().getNumMat());
            refresh();
            handleClear();
        } catch (Exception ex) {
            alert(Alert.AlertType.ERROR, "Erreur lors de la suppression : " + ex.getMessage());
        }
    }

    @FXML
    private void handleClear() {
        txtAnnee.clear();
        txtNote.clear();
        cbEleve.setValue(null);
        cbMatiere.setValue(null);
        tableNotes.getSelectionModel().clearSelection();
    }

    private void refresh() {
        data.setAll(noteService.listerTous());
    }

    private boolean valider() {
        String annee = txtAnnee.getText().trim();
        String noteStr = txtNote.getText().trim();

        if (annee.isEmpty() || noteStr.isEmpty() || cbEleve.getValue() == null || cbMatiere.getValue() == null) {
            alert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs et sélectionner un élève et une matière.");
            return false;
        }
        if (!annee.matches(REGEX_ANNEE)) {
            alert(Alert.AlertType.WARNING, "Format d'année invalide.\\nUtilisez le format : YYYY-YYYY (ex: 2022-2023)");
            return false;
        }
        try {
            double note = Double.parseDouble(noteStr);
            if (note < 0 || note > 20) {
                alert(Alert.AlertType.WARNING, "La note doit être comprise entre 0 et 20.");
                return false;
            }
        } catch (NumberFormatException e) {
            alert(Alert.AlertType.WARNING, "La note doit être un nombre valide (ex: 15.5).");
            return false;
        }
        return true;
    }

    private void alert(Alert.AlertType type, String msg) {
        new Alert(type, msg, ButtonType.OK).showAndWait();
    }

    private Optional<ButtonType> confirm(String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.OK, ButtonType.CANCEL);
        return a.showAndWait();
    }
}