package mg.cepe.gestion.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import mg.cepe.gestion.controller.matiere.MatiereFormHelper;
import mg.cepe.gestion.model.Matiere;
import mg.cepe.gestion.service.MatiereService;
import mg.cepe.gestion.service.impl.MatiereServiceImpl;
import mg.cepe.gestion.util.UiDialogs;

import java.net.URL;
import java.util.ResourceBundle;

public class MatiereController implements Initializable {
    @FXML private ComboBox<String> cbNumMat;
    @FXML private TextField txtDesignMat;
    @FXML private Spinner<Integer> spCoef;
    @FXML private TableView<Matiere> tableMatieres;
    @FXML private TableColumn<Matiere, String> colNum, colDesign;
    @FXML private TableColumn<Matiere, Number> colCoef;

    private final MatiereService service = new MatiereServiceImpl();
    private final ObservableList<Matiere> data = FXCollections.observableArrayList();
    private MatiereFormHelper form;

    @Override
    public void initialize(URL u, ResourceBundle r) {
        form = new MatiereFormHelper(cbNumMat, txtDesignMat, spCoef);
        cbNumMat.setEditable(true);
        if (spCoef.getValueFactory() == null) {
            spCoef.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        }
        colNum.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNumMat()));
        colDesign.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDesignMat()));
        colCoef.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getCoef()));
        tableMatieres.setItems(data);
        tableMatieres.getSelectionModel().selectedItemProperty()
                .addListener((o, a, v) -> { if (v != null) form.fillForm(v); });
        refresh();
    }

    @FXML private void handleAjouter() {
        if (!form.valider()) return;
        try {
            service.ajouter(form.build());
            UiDialogs.info("Matière ajoutée avec succès.");
            refresh();
            clearForm();
        } catch (Exception ex) {
            UiDialogs.warn("Erreur : " + ex.getMessage());
        }
    }

    @FXML private void handleModifier() {
        if (!form.valider()) return;
        if (!UiDialogs.confirm("Modifier la matière " + form.resolveCode() + " ?")) return;
        try {
            service.modifier(form.build());
            UiDialogs.info("Matière modifiée avec succès.");
            refresh();
        } catch (Exception ex) {
            UiDialogs.warn("Erreur : " + ex.getMessage());
        }
    }

    @FXML private void handleSupprimer() {
        if (!UiDialogs.confirm("Supprimer " + form.resolveCode() + " ?")) return;
        try {
            service.supprimer(form.resolveCode());
            UiDialogs.info("Matière supprimée avec succès.");
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
        tableMatieres.getSelectionModel().clearSelection();
        form.clearFields();
        form.refreshCodeCombo(data);
    }

    private void refresh() {
        data.setAll(service.listerTous());
        form.refreshCodeCombo(data);
    }
}
