package mg.cepe.gestion.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import mg.cepe.gestion.model.Ecole;
import mg.cepe.gestion.model.Eleve;
import mg.cepe.gestion.service.EcoleService;
import mg.cepe.gestion.service.EleveService;
import mg.cepe.gestion.service.impl.EcoleServiceImpl;
import mg.cepe.gestion.service.impl.EleveServiceImpl;

import java.net.URL;
import java.util.ResourceBundle;

public class EleveController implements Initializable {

    @FXML private TextField txtNumEleve;
    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private DatePicker dpNaissance;
    @FXML private ComboBox<Ecole> cbEcole;
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
        cbEcole.setItems(FXCollections.observableArrayList(ecoleService.listerTous()));
        colNum.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNumEleve()));
        colNom.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNom()));
        colPrenom.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPrenom()));
        colEcole.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
            ecoleService.trouverParId(c.getValue().getNumEcole()) != null ? ecoleService.trouverParId(c.getValue().getNumEcole()).getDesign() : ""));
        colNaissance.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDateNaissance().toString()));
        tableEleves.setItems(data);
        tableEleves.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> { if (val != null) fillForm(val); });
        refresh();
    }

    private void fillForm(Eleve e) {
        txtNumEleve.setText(e.getNumEleve());
        txtNom.setText(e.getNom());
        txtPrenom.setText(e.getPrenom());
        dpNaissance.setValue(e.getDateNaissance());
        cbEcole.getItems().stream().filter(ec -> ec.getNumEcole().equals(e.getNumEcole())).findFirst().ifPresent(cbEcole::setValue);
    }

    @FXML private void handleAjouter() {
        Eleve e = new Eleve(txtNumEleve.getText(), cbEcole.getValue().getNumEcole(), txtNom.getText(), txtPrenom.getText(), dpNaissance.getValue());
        eleveService.ajouter(e); refresh(); handleClear();
    }
    @FXML private void handleModifier() {
        Eleve e = new Eleve(txtNumEleve.getText(), cbEcole.getValue().getNumEcole(), txtNom.getText(), txtPrenom.getText(), dpNaissance.getValue());
        eleveService.modifier(e); refresh();
    }
    @FXML private void handleSupprimer() {
        eleveService.supprimer(txtNumEleve.getText()); refresh(); handleClear();
    }
    @FXML private void handleClear() {
        txtNumEleve.clear(); txtNom.clear(); txtPrenom.clear(); dpNaissance.setValue(null); cbEcole.setValue(null);
    }
    private void refresh() { data.setAll(eleveService.listerTous()); }
}
