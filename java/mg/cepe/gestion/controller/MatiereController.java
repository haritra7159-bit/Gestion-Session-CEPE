package mg.cepe.gestion.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import mg.cepe.gestion.model.Matiere;
import mg.cepe.gestion.service.MatiereService;
import mg.cepe.gestion.service.impl.MatiereServiceImpl;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MatiereController implements Initializable {
    @FXML private TextField txtNumMat, txtDesignMat;
    @FXML private Spinner<Integer> spCoef;
    @FXML private TableView<Matiere> tableMatieres;
    @FXML private TableColumn<Matiere,String> colNum, colDesign;
    @FXML private TableColumn<Matiere,Number> colCoef;
    private final MatiereService service = new MatiereServiceImpl();
    private final ObservableList<Matiere> data = FXCollections.observableArrayList();
    private static final String REGEX_CODE = "^[A-Za-z0-9_-]{3,20}$";
    private static final String REGEX_NOM = "^[A-Za-z\\u00C0-\\u017F\\s\\-'']{2,100}$";

    @Override public void initialize(URL location, ResourceBundle resources) {
        spCoef.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        colNum.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNumMat()));
        colDesign.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDesignMat()));
        colCoef.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getCoef()));
        tableMatieres.setItems(data);
        tableMatieres.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> { if (val != null) fillForm(val); });
        refresh();
    }
    private void fillForm(Matiere m) { txtNumMat.setText(m.getNumMat()); txtDesignMat.setText(m.getDesignMat()); spCoef.getValueFactory().setValue(m.getCoef()); }
    @FXML private void handleAjouter() {
        if (!valider()) return;
        try { service.ajouter(new Matiere(txtNumMat.getText().trim(), txtDesignMat.getText().trim(), spCoef.getValue())); refresh(); handleClear(); }
        catch (Exception ex) { alert(Alert.AlertType.ERROR, "Erreur : " + ex.getMessage()); }
    }
    @FXML private void handleModifier() {
        if (!valider()) return;
        Optional<ButtonType> res = confirm("Modifier la matière " + txtNumMat.getText() + " ?");
        if (res.isEmpty() || res.get() != ButtonType.OK) return;
        try { service.modifier(new Matiere(txtNumMat.getText().trim(), txtDesignMat.getText().trim(), spCoef.getValue())); refresh(); }
        catch (Exception ex) { alert(Alert.AlertType.ERROR, "Erreur : " + ex.getMessage()); }
    }
    @FXML private void handleSupprimer() {
        if (txtNumMat.getText().isBlank()) { alert(Alert.AlertType.WARNING, "Sélectionnez une matière."); return; }
        Optional<ButtonType> res = confirm("Supprimer la matière " + txtNumMat.getText() + " ?\nAttention : impossible si des notes y sont associées.");
        if (res.isEmpty() || res.get() != ButtonType.OK) return;
        try { service.supprimer(txtNumMat.getText().trim()); refresh(); handleClear(); }
        catch (Exception ex) { alert(Alert.AlertType.ERROR, "Impossible de supprimer : " + ex.getMessage()); }
    }
    @FXML private void handleClear() { txtNumMat.clear(); txtDesignMat.clear(); spCoef.getValueFactory().setValue(1); tableMatieres.getSelectionModel().clearSelection(); }
    private void refresh() { data.setAll(service.listerTous()); }
    private boolean valider() {
        if (txtNumMat.getText().isBlank() || txtDesignMat.getText().isBlank()) { alert(Alert.AlertType.WARNING, "Remplissez tous les champs."); return false; }
        if (!txtNumMat.getText().trim().matches(REGEX_CODE)) { alert(Alert.AlertType.WARNING, "Code invalide (3-20 caractères)."); return false; }
        if (!txtDesignMat.getText().trim().matches(REGEX_NOM)) { alert(Alert.AlertType.WARNING, "Nom invalide."); return false; }
        return true;
    }
    private void alert(Alert.AlertType type, String msg) { new Alert(type, msg, ButtonType.OK).showAndWait(); }
    private Optional<ButtonType> confirm(String msg) { return new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.OK, ButtonType.CANCEL).showAndWait(); }
}
