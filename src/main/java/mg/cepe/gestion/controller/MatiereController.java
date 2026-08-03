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
import java.util.ResourceBundle;

public class MatiereController implements Initializable {

    @FXML private TextField txtNumMat;
    @FXML private TextField txtDesignMat;
    @FXML private Spinner<Integer> spCoef;
    @FXML private TableView<Matiere> tableMatieres;
    @FXML private TableColumn<Matiere, String> colNum;
    @FXML private TableColumn<Matiere, String> colDesign;
    @FXML private TableColumn<Matiere, Number> colCoef;

    private final MatiereService service = new MatiereServiceImpl();
    private final ObservableList<Matiere> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        spCoef.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        colNum.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNumMat()));
        colDesign.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDesignMat()));
        colCoef.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getCoef()).asObject());
        tableMatieres.setItems(data);
        tableMatieres.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> { if (val != null) fillForm(val); });
        refresh();
    }

    private void fillForm(Matiere m) {
        txtNumMat.setText(m.getNumMat());
        txtDesignMat.setText(m.getDesignMat());
        spCoef.getValueFactory().setValue(m.getCoef());
    }

    @FXML private void handleAjouter() {
        service.ajouter(new Matiere(txtNumMat.getText(), txtDesignMat.getText(), spCoef.getValue()));
        refresh(); handleClear();
    }
    @FXML private void handleModifier() {
        service.modifier(new Matiere(txtNumMat.getText(), txtDesignMat.getText(), spCoef.getValue()));
        refresh();
    }
    @FXML private void handleSupprimer() {
        service.supprimer(txtNumMat.getText()); refresh(); handleClear();
    }
    @FXML private void handleClear() {
        txtNumMat.clear(); txtDesignMat.clear(); spCoef.getValueFactory().setValue(1);
    }
    private void refresh() { data.setAll(service.listerTous()); }
}
