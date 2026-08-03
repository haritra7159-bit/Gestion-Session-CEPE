package mg.cepe.gestion.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import mg.cepe.gestion.model.Eleve;
import mg.cepe.gestion.model.Matiere;
import mg.cepe.gestion.model.Note;
import mg.cepe.gestion.service.EleveService;
import mg.cepe.gestion.service.MatiereService;
import mg.cepe.gestion.service.NoteService;
import mg.cepe.gestion.service.impl.EleveServiceImpl;
import mg.cepe.gestion.service.impl.MatiereServiceImpl;
import mg.cepe.gestion.service.impl.NoteServiceImpl;

import java.net.URL;
import java.util.ResourceBundle;

public class NoteController implements Initializable {

    @FXML private TextField txtAnnee;
    @FXML private ComboBox<Eleve> cbEleve;
    @FXML private ComboBox<Matiere> cbMatiere;
    @FXML private TextField txtNote;
    @FXML private TableView<Note> tableNotes;
    @FXML private TableColumn<Note, String> colAnnee;
    @FXML private TableColumn<Note, String> colEleve;
    @FXML private TableColumn<Note, String> colMatiere;
    @FXML private TableColumn<Note, Number> colNote;

    private final NoteService noteService = new NoteServiceImpl();
    private final EleveService eleveService = new EleveServiceImpl();
    private final MatiereService matiereService = new MatiereServiceImpl();
    private final ObservableList<Note> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbEleve.setItems(FXCollections.observableArrayList(eleveService.listerTous()));
        cbMatiere.setItems(FXCollections.observableArrayList(matiereService.listerTous()));
        colAnnee.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAnneeScolaire()));
        colEleve.setCellValueFactory(c -> {
            Eleve e = eleveService.trouverParId(c.getValue().getNumEleve());
            return new javafx.beans.property.SimpleStringProperty(e != null ? e.getNomComplet() : "");
        });
        colMatiere.setCellValueFactory(c -> {
            Matiere m = matiereService.trouverParId(c.getValue().getNumMat());
            return new javafx.beans.property.SimpleStringProperty(m != null ? m.getDesignMat() : "");
        });
        colNote.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getNote()).asObject());
        tableNotes.setItems(data);
        tableNotes.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> { if (val != null) fillForm(val); });
        refresh();
    }

    private void fillForm(Note n) {
        txtAnnee.setText(n.getAnneeScolaire());
        txtNote.setText(String.valueOf(n.getNote()));
        cbEleve.getItems().stream().filter(e -> e.getNumEleve().equals(n.getNumEleve())).findFirst().ifPresent(cbEleve::setValue);
        cbMatiere.getItems().stream().filter(m -> m.getNumMat().equals(n.getNumMat())).findFirst().ifPresent(cbMatiere::setValue);
    }

    @FXML private void handleAjouter() {
        Note n = new Note(txtAnnee.getText(), cbEleve.getValue().getNumEleve(), cbMatiere.getValue().getNumMat(), Double.parseDouble(txtNote.getText()));
        noteService.ajouter(n); refresh(); handleClear();
    }
    @FXML private void handleModifier() {
        Note n = new Note(txtAnnee.getText(), cbEleve.getValue().getNumEleve(), cbMatiere.getValue().getNumMat(), Double.parseDouble(txtNote.getText()));
        noteService.modifier(n); refresh();
    }
    @FXML private void handleSupprimer() {
        noteService.supprimer(txtAnnee.getText(), cbEleve.getValue().getNumEleve(), cbMatiere.getValue().getNumMat());
        refresh(); handleClear();
    }
    @FXML private void handleClear() {
        txtAnnee.clear(); txtNote.clear(); cbEleve.setValue(null); cbMatiere.setValue(null);
    }
    private void refresh() { data.setAll(noteService.listerTous()); }
}
