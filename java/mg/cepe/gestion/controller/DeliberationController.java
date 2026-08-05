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
    private String anneeScolaireSelectionnee;

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
    private void handleDeliberer() {
        if (preparerAnneeSelectionnee())
            data.setAll(service.deliberer(anneeScolaireSelectionnee));
    }

    @FXML
    private void handleReussis() {
        if (preparerAnneeSelectionnee())
            data.setAll(service.listerReussis(anneeScolaireSelectionnee));
    }

    @FXML
    private void handleEchoues() {
        if (preparerAnneeSelectionnee())
            data.setAll(service.listerEchoues(anneeScolaireSelectionnee));
    }

    @FXML
    private void handleAdmis() {
        if (preparerAnneeSelectionnee())
            data.setAll(service.listerAdmisSixieme(anneeScolaireSelectionnee));
    }

    private boolean preparerAnneeSelectionnee() {
        String annee = txtAnnee.getText().trim();
        if (annee.isEmpty()) {
            alert(Alert.AlertType.WARNING, "Saisissez une année scolaire.");
            return false;
        }
        if (!annee.matches(REGEX_ANNEE)) {
            alert(Alert.AlertType.WARNING, "Format invalide (YYYY-YYYY).");
            return false;
        }
        anneeScolaireSelectionnee = annee;
        return true;
    }

    private void alert(Alert.AlertType type, String msg) {
        new Alert(type, msg, ButtonType.OK).showAndWait();
    }
}
