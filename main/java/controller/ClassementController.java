package mg.cepe.gestion.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import mg.cepe.gestion.model.ResultatEleve;
import mg.cepe.gestion.service.DeliberationService;
import mg.cepe.gestion.service.impl.DeliberationServiceImpl;

import java.net.URL;
import java.util.ResourceBundle;

public class ClassementController implements Initializable {

    @FXML private TextField txtAnnee;
    @FXML private TableView<ResultatEleve> tableClassement;
    @FXML private TableColumn<ResultatEleve, Number> colRang;
    @FXML private TableColumn<ResultatEleve, String> colNom;
    @FXML private TableColumn<ResultatEleve, String> colPrenom;
    @FXML private TableColumn<ResultatEleve, String> colEcole;
    @FXML private TableColumn<ResultatEleve, Number> colMoyenne;

    private final DeliberationService service = new DeliberationServiceImpl();
    private final ObservableList<ResultatEleve> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // CORRECTION : suppression de .asObject() - SimpleIntegerProperty implements ObservableValue<Number>
        colRang.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(
            tableClassement.getItems().indexOf(c.getValue()) + 1));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEcole.setCellValueFactory(new PropertyValueFactory<>("nomEcole"));
        colMoyenne.setCellValueFactory(new PropertyValueFactory<>("moyenne"));
        tableClassement.setItems(data);
    }

    @FXML private void handleClassement() { data.setAll(service.classementParMerite(txtAnnee.getText())); }
}
