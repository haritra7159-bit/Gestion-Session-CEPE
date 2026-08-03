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

public class DeliberationController implements Initializable {

    @FXML private TextField txtAnnee;
    @FXML private TableView<ResultatEleve> tableResultats;
    @FXML private TableColumn<ResultatEleve, String> colNum;
    @FXML private TableColumn<ResultatEleve, String> colNom;
    @FXML private TableColumn<ResultatEleve, String> colPrenom;
    @FXML private TableColumn<ResultatEleve, String> colEcole;
    @FXML private TableColumn<ResultatEleve, Double> colMoyenne;
    @FXML private TableColumn<ResultatEleve, String> colDecision;

    private final DeliberationService service = new DeliberationServiceImpl();
    private final ObservableList<ResultatEleve> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colNum.setCellValueFactory(new PropertyValueFactory<>("numEleve"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEcole.setCellValueFactory(new PropertyValueFactory<>("nomEcole"));
        colMoyenne.setCellValueFactory(new PropertyValueFactory<>("moyenne"));
        colDecision.setCellValueFactory(new PropertyValueFactory<>("decision"));
        tableResultats.setItems(data);
    }

    @FXML private void handleDeliberer() { data.setAll(service.deliberer(txtAnnee.getText())); }
    @FXML private void handleReussis() { data.setAll(service.listerReussis(txtAnnee.getText())); }
    @FXML private void handleEchoues() { data.setAll(service.listerEchoues(txtAnnee.getText())); }
    @FXML private void handleAdmis() { data.setAll(service.listerAdmisSixieme(txtAnnee.getText())); }
}
