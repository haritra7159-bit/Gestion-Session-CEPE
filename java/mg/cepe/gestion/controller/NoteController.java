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
import java.util.Optional;
import java.util.ResourceBundle;

public class NoteController implements Initializable {
    @FXML private TextField txtAnnee, txtNote;
    @FXML private ComboBox<Eleve> cbEleve;
    @FXML private ComboBox<Matiere> cbMatiere;
    @FXML private TableView<Note> tableNotes;
    @FXML private TableColumn<Note,String> colAnnee, colEleve, colMatiere;
    @FXML private TableColumn<Note,Number> colNote;
    private final NoteService noteService = new NoteServiceImpl();
    private final EleveService eleveService = new EleveServiceImpl();
    private final MatiereService matiereService = new MatiereServiceImpl();
    private final ObservableList<Note> data = FXCollections.observableArrayList();
    private static final String REGEX_ANNEE = "^[0-9]{4}-[0-9]{4}$";

    @Override public void initialize(URL location, ResourceBundle resources) {
        cbEleve.setItems(FXCollections.observableArrayList(eleveService.listerTous()));
        cbMatiere.setItems(FXCollections.observableArrayList(matiereService.listerTous()));
        colAnnee.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAnneeScolaire()));
        colEleve.setCellValueFactory(c -> { Eleve e = eleveService.trouverParId(c.getValue().getNumEleve()); return new javafx.beans.property.SimpleStringProperty(e != null ? e.getNomComplet() : ""); });
        colMatiere.setCellValueFactory(c -> { Matiere m = matiereService.trouverParId(c.getValue().getNumMat()); return new javafx.beans.property.SimpleStringProperty(m != null ? m.getDesignMat() : ""); });
        colNote.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getNote()));
        tableNotes.setItems(data);
        tableNotes.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> { if (val != null) fillForm(val); });
        refresh();
    }
    private void fillForm(Note n) {
        txtAnnee.setText(n.getAnneeScolaire()); txtNote.setText(String.valueOf(n.getNote()));
        cbEleve.getItems().stream().filter(e -> e.getNumEleve().equals(n.getNumEleve())).findFirst().ifPresent(cbEleve::setValue);
        cbMatiere.getItems().stream().filter(m -> m.getNumMat().equals(n.getNumMat())).findFirst().ifPresent(cbMatiere::setValue);
    }
    @FXML private void handleAjouter() {
        if (!valider()) return;
        try { noteService.ajouter(new Note(txtAnnee.getText().trim(), cbEleve.getValue().getNumEleve(), cbMatiere.getValue().getNumMat(), Double.parseDouble(txtNote.getText().trim()))); refresh(); handleClear(); }
        catch (Exception ex) { alert(Alert.AlertType.ERROR, "Erreur : " + ex.getMessage()); }
    }
    @FXML private void handleModifier() {
        if (!valider()) return;
        Optional<ButtonType> res = confirm("Modifier cette note ?");
        if (res.isEmpty() || res.get() != ButtonType.OK) return;
        try { noteService.modifier(new Note(txtAnnee.getText().trim(), cbEleve.getValue().getNumEleve(), cbMatiere.getValue().getNumMat(), Double.parseDouble(txtNote.getText().trim()))); refresh(); }
        catch (Exception ex) { alert(Alert.AlertType.ERROR, "Erreur : " + ex.getMessage()); }
    }
    @FXML private void handleSupprimer() {
        if (txtAnnee.getText().isBlank() || cbEleve.getValue() == null || cbMatiere.getValue() == null) { alert(Alert.AlertType.WARNING, "Sélectionnez une note."); return; }
        Optional<ButtonType> res = confirm("Supprimer cette note ?");
        if (res.isEmpty() || res.get() != ButtonType.OK) return;
        try { noteService.supprimer(txtAnnee.getText().trim(), cbEleve.getValue().getNumEleve(), cbMatiere.getValue().getNumMat()); refresh(); handleClear(); }
        catch (Exception ex) { alert(Alert.AlertType.ERROR, "Erreur : " + ex.getMessage()); }
    }
    @FXML private void handleClear() { txtAnnee.clear(); txtNote.clear(); cbEleve.setValue(null); cbMatiere.setValue(null); tableNotes.getSelectionModel().clearSelection(); }
    private void refresh() { data.setAll(noteService.listerTous()); }
    private boolean valider() {
        if (txtAnnee.getText().isBlank() || txtNote.getText().isBlank() || cbEleve.getValue() == null || cbMatiere.getValue() == null) { alert(Alert.AlertType.WARNING, "Remplissez tous les champs."); return false; }
        if (!txtAnnee.getText().trim().matches(REGEX_ANNEE)) { alert(Alert.AlertType.WARNING, "Format année invalide (YYYY-YYYY)."); return false; }
        try { double note = Double.parseDouble(txtNote.getText().trim()); if (note < 0 || note > 20) { alert(Alert.AlertType.WARNING, "Note entre 0 et 20."); return false; } }
        catch (NumberFormatException e) { alert(Alert.AlertType.WARNING, "Note invalide."); return false; }
        return true;
    }
    private void alert(Alert.AlertType type, String msg) { new Alert(type, msg, ButtonType.OK).showAndWait(); }
    private Optional<ButtonType> confirm(String msg) { return new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.OK, ButtonType.CANCEL).showAndWait(); }
}
