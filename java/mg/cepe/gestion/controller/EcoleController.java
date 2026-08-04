package mg.cepe.gestion.controller;

import java.net.URL;
import java.util.Optional;
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
import mg.cepe.gestion.model.Ecole;
import mg.cepe.gestion.service.EcoleService;
import mg.cepe.gestion.service.impl.EcoleServiceImpl;

public class EcoleController implements Initializable {
    @FXML
    private TextField txtNumEcole, txtDesign, txtAdresse;
    @FXML
    private TableView<Ecole> tableEcoles;
    @FXML
    private TableColumn<Ecole, String> colNum, colDesign, colAdresse;
    private final EcoleService service = new EcoleServiceImpl();
    private final ObservableList<Ecole> data = FXCollections.observableArrayList();

    // Regex : alphanumérique, tiret, underscore, 3-20 caractères
    private static final String REGEX_CODE = "^[A-Za-z0-9_-]{3,20}$";
    // Regex : lettres, espaces, accents, tirets, apostrophes, 2-150 caractères
    private static final String REGEX_NOM = "^[A-Za-zÀ-ÿ\\s\\-'']{2,150}$";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colNum.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNumEcole()));
        colDesign.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDesign()));
        colAdresse.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAdresse()));
        tableEcoles.setItems(data);
        tableEcoles.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> {
            if (val != null)
                fillForm(val);
        });
        refresh();
    }

    private void fillForm(Ecole e) {
        txtNumEcole.setText(e.getNumEcole());
        txtDesign.setText(e.getDesign());
        txtAdresse.setText(e.getAdresse());
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleAjouter() {
        if (!valider())
            return;
        try {
            service.ajouter(
                    new Ecole(txtNumEcole.getText().trim(), txtDesign.getText().trim(), txtAdresse.getText().trim()));
            refresh();
            handleClear();
        } catch (Exception ex) {
            alert(Alert.AlertType.ERROR, "Erreur lors de l'ajout : " + ex.getMessage());
        }
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleModifier() {
        if (!valider())
            return;
        Optional<ButtonType> res = confirm("Modifier l'école " + txtNumEcole.getText() + " ?");
        if (res.isEmpty() || res.get() != ButtonType.OK)
            return;
        try {
            service.modifier(
                    new Ecole(txtNumEcole.getText().trim(), txtDesign.getText().trim(), txtAdresse.getText().trim()));
            refresh();
        } catch (Exception ex) {
            alert(Alert.AlertType.ERROR, "Erreur lors de la modification : " + ex.getMessage());
        }
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleSupprimer() {
        if (txtNumEcole.getText().isBlank()) {
            alert(Alert.AlertType.WARNING, "Veuillez sélectionner une école à supprimer.");
            return;
        }
        Optional<ButtonType> res = confirm("Supprimer définitivement l'école " + txtNumEcole.getText()
                + " ?\\n\\nAttention : impossible si des élèves y sont inscrits.");
        if (res.isEmpty() || res.get() != ButtonType.OK)
            return;
        try {
            service.supprimer(txtNumEcole.getText().trim());
            refresh();
            handleClear();
        } catch (Exception ex) {
            alert(Alert.AlertType.ERROR, "Impossible de supprimer : " + ex.getMessage());
        }
    }

    @FXML
    private void handleClear() {
        txtNumEcole.clear();
        txtDesign.clear();
        txtAdresse.clear();
        tableEcoles.getSelectionModel().clearSelection();
    }

    private void refresh() {
        data.setAll(service.listerTous());
    }

    private boolean valider() {
        String num = txtNumEcole.getText().trim();
        String design = txtDesign.getText().trim();
        String adresse = txtAdresse.getText().trim();

        if (num.isEmpty() || design.isEmpty() || adresse.isEmpty()) {
            alert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs.");
            return false;
        }
        if (!num.matches(REGEX_CODE)) {
            alert(Alert.AlertType.WARNING, "Numéro invalide.\\n3 à 20 caractères alphanumériques.");
            return false;
        }
        if (!design.matches(REGEX_NOM)) {
            alert(Alert.AlertType.WARNING, "Nom d'école invalide.\\n2 à 150 caractères (lettres, espaces, tirets).");
            return false;
        }
        if (adresse.length() < 3 || adresse.length() > 255) {
            alert(Alert.AlertType.WARNING, "Adresse invalide.\\n3 à 255 caractères.");
            return false;
        }
        return true;
    }

    private void alert(Alert.AlertType type, String msg) {
        new Alert(type, msg, ButtonType.OK).showAndWait();
    }

    private Optional<ButtonType> confirm(String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.OK, ButtonType.CANCEL);
        return a.showAndWait();
    }
}