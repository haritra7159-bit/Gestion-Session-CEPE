package mg.cepe.gestion.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import mg.cepe.gestion.model.ResultatEleve;
import mg.cepe.gestion.service.DeliberationService;
import mg.cepe.gestion.service.impl.DeliberationServiceImpl;

public class DeliberationController implements Initializable {
    @FXML
    private TextField txtAnnee;
    @FXML
    private TableView<ResultatEleve> tableResultats;
    @FXML
    private TableColumn<ResultatEleve, String> colNum, colNom, colPrenom, colEcole, colDecision;
    @FXML
    private TableColumn<ResultatEleve, Number> colMoyenne;
    private final DeliberationService service = new DeliberationServiceImpl();
    private final ObservableList<ResultatEleve> data = FXCollections.observableArrayList();

    private static final String REGEX_ANNEE = "^[0-9]{4}-[0-9]{4}$";

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

    @FXML
    @SuppressWarnings("unused")
    private void handleDeliberer() {
        if (!validerAnnee())
            return;
        data.setAll(service.deliberer(txtAnnee.getText().trim()));
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleReussis() {
        if (!validerAnnee())
            return;
        data.setAll(service.listerReussis(txtAnnee.getText().trim()));
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleEchoues() {
        if (!validerAnnee())
            return;
        data.setAll(service.listerEchoues(txtAnnee.getText().trim()));
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleAdmis() {
        if (!validerAnnee())
            return;
        data.setAll(service.listerAdmisSixieme(txtAnnee.getText().trim()));
    }

    private boolean validerAnnee() {
        String annee = txtAnnee.getText().trim();
        if (annee.isEmpty()) {
            alert(Alert.AlertType.WARNING, "Veuillez saisir une année scolaire.");
            return false;
        }
        if (!annee.matches(REGEX_ANNEE)) {
            alert(Alert.AlertType.WARNING, "Format invalide. Utilisez : YYYY-YYYY (ex: 2022-2023)");
            return false;
        }
        return true;
    }

    private void alert(Alert.AlertType type, String msg) {
        new Alert(type, msg, ButtonType.OK).showAndWait();
    }
}