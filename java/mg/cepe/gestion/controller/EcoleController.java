package mg.cepe.gestion.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import mg.cepe.gestion.controller.ecole.EcoleFormHelper;
import mg.cepe.gestion.model.Ecole;
import mg.cepe.gestion.service.EcoleService;
import mg.cepe.gestion.service.impl.EcoleServiceImpl;
import mg.cepe.gestion.util.UiDialogs;

import java.net.URL;
import java.util.ResourceBundle;

public class EcoleController implements Initializable {
    @FXML private ComboBox<String> cbNumEcole;
    @FXML private TextField txtDesign, txtAdresse;
    @FXML private TableView<Ecole> tableEcoles;
    @FXML private TableColumn<Ecole, String> colNum, colDesign, colAdresse;

    private final EcoleService service = new EcoleServiceImpl();
    private final ObservableList<Ecole> data = FXCollections.observableArrayList();
    private EcoleFormHelper form;

    @Override
    public void initialize(URL u, ResourceBundle r) {
        form = new EcoleFormHelper(cbNumEcole, txtDesign, txtAdresse);
        cbNumEcole.setEditable(true);
        colNum.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNumEcole()));
        colDesign.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDesign()));
        colAdresse.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAdresse()));
        tableEcoles.setItems(data);
        tableEcoles.getSelectionModel().selectedItemProperty()
                .addListener((o, a, v) -> { if (v != null) form.fillForm(v); });
        refresh();
    }

    @FXML private void handleAjouter() {
        if (!form.valider()) return;
        try {
            service.ajouter(form.build());
            UiDialogs.info("École ajoutée avec succès.");
            refresh();
            clearForm();
        } catch (Exception ex) {
            UiDialogs.warn("Erreur : " + ex.getMessage());
        }
    }

    @FXML private void handleModifier() {
        if (!form.valider()) return;
        if (!UiDialogs.confirm("Modifier l'école " + form.resolveCode() + " ?")) return;
        try {
            service.modifier(form.build());
            UiDialogs.info("École modifiée avec succès.");
            refresh();
        } catch (Exception ex) {
            UiDialogs.warn("Erreur : " + ex.getMessage());
        }
    }

    @FXML private void handleSupprimer() {
        if (!UiDialogs.confirm("Supprimer l'école " + form.resolveCode() + " ?")) return;
        try {
            service.supprimer(form.resolveCode());
            UiDialogs.info("École supprimée avec succès.");
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
        tableEcoles.getSelectionModel().clearSelection();
        form.clearFields();
        form.refreshCodeCombo(data);
    }

    private void refresh() {
        data.setAll(service.listerTous());
        form.refreshCodeCombo(data);
    }
}
