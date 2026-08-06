package mg.cepe.gestion.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import mg.cepe.gestion.controller.eleve.EleveFormHelper;
import mg.cepe.gestion.controller.eleve.EleveTableActions;
import mg.cepe.gestion.model.Ecole;
import mg.cepe.gestion.model.Eleve;
import mg.cepe.gestion.service.EcoleService;
import mg.cepe.gestion.service.EleveService;
import mg.cepe.gestion.service.impl.*;
import mg.cepe.gestion.util.UiDialogs;

import java.net.URL;
import java.util.ResourceBundle;

public class EleveController implements Initializable {
    @FXML private ComboBox<String> cbNumEleve;
    @FXML private TextField txtNom, txtPrenom, txtRecherche;
    @FXML private DatePicker dpNaissance;
    @FXML private ComboBox<Ecole> cbEcole;
    @FXML private TableView<Eleve> tableEleves;
    @FXML private TableColumn<Eleve, String> colNum, colNom, colPrenom, colEcole, colNaissance;
    @FXML private TableColumn<Eleve, Void> colActions;

    private final EleveService eleveService = new EleveServiceImpl();
    private final EcoleService ecoleService = new EcoleServiceImpl();
    private final ObservableList<Eleve> data = FXCollections.observableArrayList();
    private EleveFormHelper form;
    private EleveTableActions tableActions;

    @Override
    public void initialize(URL u, ResourceBundle r) {
        form = new EleveFormHelper(cbNumEleve, txtNom, txtPrenom, dpNaissance, cbEcole);
        tableActions = new EleveTableActions(new NoteServiceImpl(), new MatiereServiceImpl(), ecoleService);
        cbNumEleve.setEditable(true);
        setupColumns();
        tableEleves.setItems(data);
        tableEleves.getSelectionModel().selectedItemProperty()
                .addListener((o, a, v) -> { if (v != null) form.fillForm(v); });
        refresh();
    }

    private void setupColumns() {
        colNum.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNumEleve()));
        colNom.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNom()));
        colPrenom.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPrenom()));
        colEcole.setCellValueFactory(c -> {
            var ec = ecoleService.trouverParId(c.getValue().getNumEcole());
            return new javafx.beans.property.SimpleStringProperty(ec != null ? ec.getDesign() : "");
        });
        colNaissance.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getDateNaissance().toString()));
        tableActions.setupActionsColumn(colActions);
        cbEcole.setItems(FXCollections.observableArrayList(ecoleService.listerTous()));
    }

    @FXML private void handleAjouter() {
        if (!form.valider()) return;
        try {
            eleveService.ajouter(form.buildEleve());
            UiDialogs.info("Élève ajouté avec succès.");
            refresh();
            clearForm();
        } catch (Exception ex) {
            UiDialogs.warn("Erreur : " + ex.getMessage());
        }
    }

    @FXML private void handleModifier() {
        if (!form.valider()) return;
        if (!UiDialogs.confirm("Modifier l'élève " + form.resolveCode() + " ?")) return;
        try {
            eleveService.modifier(form.buildEleve());
            UiDialogs.info("Élève modifié avec succès.");
            refresh();
        } catch (Exception ex) {
            UiDialogs.warn("Erreur : " + ex.getMessage());
        }
    }

    @FXML private void handleSupprimer() {
        if (!UiDialogs.confirm("Supprimer " + form.resolveCode() + " ?")) return;
        try {
            eleveService.supprimer(form.resolveCode());
            UiDialogs.info("Élève supprimé avec succès.");
            refresh();
            clearForm();
        } catch (Exception ex) {
            UiDialogs.warn("Erreur : " + ex.getMessage());
        }
    }

    @FXML private void handleClear() {
        if (!UiDialogs.confirm("Réinitialiser le formulaire ?")) return;
        clearForm();
    }

    private void clearForm() {
        tableEleves.getSelectionModel().clearSelection();
        form.clearFields();
        form.refreshCodeCombo(data);
    }

    @FXML private void handleRechercher() {
        String c = txtRecherche.getText();
        data.setAll(c == null || c.isBlank() ? eleveService.listerTous() : eleveService.rechercher(c));
        form.refreshCodeCombo(data);
    }

    @FXML private void handleResetRecherche() { txtRecherche.clear(); refresh(); }

    private void refresh() {
        data.setAll(eleveService.listerTous());
        cbEcole.setItems(FXCollections.observableArrayList(ecoleService.listerTous()));
        form.refreshCodeCombo(data);
    }
}
