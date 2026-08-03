package mg.cepe.gestion.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import mg.cepe.gestion.model.Eleve;
import mg.cepe.gestion.service.EcoleService;
import mg.cepe.gestion.service.EleveService;
import mg.cepe.gestion.service.impl.EcoleServiceImpl;
import mg.cepe.gestion.service.impl.EleveServiceImpl;

import java.net.URL;
import java.util.ResourceBundle;

public class RechercheController implements Initializable {

    @FXML private TextField txtCritere;
    @FXML private TableView<Eleve> tableEleves;
    @FXML private TableColumn<Eleve, String> colNum;
    @FXML private TableColumn<Eleve, String> colNom;
    @FXML private TableColumn<Eleve, String> colPrenom;
    @FXML private TableColumn<Eleve, String> colEcole;
    @FXML private TableColumn<Eleve, String> colNaissance;

    private final EleveService eleveService = new EleveServiceImpl();
    private final EcoleService ecoleService = new EcoleServiceImpl();
    private final ObservableList<Eleve> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colNum.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNumEleve()));
        colNom.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNom()));
        colPrenom.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPrenom()));
        colEcole.setCellValueFactory(c -> {
            var ec = ecoleService.trouverParId(c.getValue().getNumEcole());
            return new javafx.beans.property.SimpleStringProperty(ec != null ? ec.getDesign() : "");
        });
        colNaissance.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDateNaissance().toString()));
        tableEleves.setItems(data);
    }

    @FXML private void handleRechercher() { data.setAll(eleveService.rechercher(txtCritere.getText())); }
}
