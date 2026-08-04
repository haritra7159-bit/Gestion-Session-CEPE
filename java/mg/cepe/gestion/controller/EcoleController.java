package mg.cepe.gestion.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import mg.cepe.gestion.model.Ecole;
import mg.cepe.gestion.service.EcoleService;
import mg.cepe.gestion.service.impl.EcoleServiceImpl;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class EcoleController implements Initializable {
    @FXML private TextField txtNumEcole, txtDesign, txtAdresse;
    @FXML private TableView<Ecole> tableEcoles;
    @FXML private TableColumn<Ecole,String> colNum, colDesign, colAdresse;
    private final EcoleService service = new EcoleServiceImpl();
    private final ObservableList<Ecole> data = FXCollections.observableArrayList();
    private static final String REGEX_CODE = "^[A-Za-z0-9_-]{3,20}$";
    private static final String REGEX_NOM = "^[A-Za-z\\u00C0-\\u017F\\s\\-'']{2,150}$";

    @Override public void initialize(URL location, ResourceBundle resources) {
        colNum.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNumEcole()));
        colDesign.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDesign()));
        colAdresse.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAdresse()));
        tableEcoles.setItems(data);
        tableEcoles.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> { if (val != null) fillForm(val); });
        refresh();
    }
    private void fillForm(Ecole e) { txtNumEcole.setText(e.getNumEcole()); txtDesign.setText(e.getDesign()); txtAdresse.setText(e.getAdresse()); }
    @FXML private void handleAjouter() {
        if (!valider()) return;
        try { service.ajouter(new Ecole(txtNumEcole.getText().trim(), txtDesign.getText().trim(), txtAdresse.getText().trim())); refresh(); handleClear(); }
        catch (Exception ex) { alert(Alert.AlertType.ERROR, "Erreur : " + ex.getMessage()); }
    }
    @FXML private void handleModifier() {
        if (!valider()) return;
        Optional<ButtonType> res = confirm("Modifier l\'école " + txtNumEcole.getText() + " ?");
        if (res.isEmpty() || res.get() != ButtonType.OK) return;
        try { service.modifier(new Ecole(txtNumEcole.getText().trim(), txtDesign.getText().trim(), txtAdresse.getText().trim())); refresh(); }
        catch (Exception ex) { alert(Alert.AlertType.ERROR, "Erreur : " + ex.getMessage()); }
    }
    @FXML private void handleSupprimer() {
        if (txtNumEcole.getText().isBlank()) { alert(Alert.AlertType.WARNING, "Sélectionnez une école."); return; }
        Optional<ButtonType> res = confirm("Supprimer l\'école " + txtNumEcole.getText() + " ?\nAttention : impossible si des élèves y sont inscrits.");
        if (res.isEmpty() || res.get() != ButtonType.OK) return;
        try { service.supprimer(txtNumEcole.getText().trim()); refresh(); handleClear(); }
        catch (Exception ex) { alert(Alert.AlertType.ERROR, "Impossible de supprimer : " + ex.getMessage()); }
    }
    @FXML private void handleClear() { txtNumEcole.clear(); txtDesign.clear(); txtAdresse.clear(); tableEcoles.getSelectionModel().clearSelection(); }
    private void refresh() { data.setAll(service.listerTous()); }
    private boolean valider() {
        if (txtNumEcole.getText().isBlank() || txtDesign.getText().isBlank() || txtAdresse.getText().isBlank()) {
            alert(Alert.AlertType.WARNING, "Remplissez tous les champs."); return false;
        }
        if (!txtNumEcole.getText().trim().matches(REGEX_CODE)) { alert(Alert.AlertType.WARNING, "Numéro invalide (3-20 caractères)."); return false; }
        if (!txtDesign.getText().trim().matches(REGEX_NOM)) { alert(Alert.AlertType.WARNING, "Nom invalide."); return false; }
        if (txtAdresse.getText().trim().length() < 3 || txtAdresse.getText().trim().length() > 255) { alert(Alert.AlertType.WARNING, "Adresse invalide (3-255 caractères)."); return false; }
        return true;
    }
    private void alert(Alert.AlertType type, String msg) { new Alert(type, msg, ButtonType.OK).showAndWait(); }
    private Optional<ButtonType> confirm(String msg) { return new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.OK, ButtonType.CANCEL).showAndWait(); }
}
