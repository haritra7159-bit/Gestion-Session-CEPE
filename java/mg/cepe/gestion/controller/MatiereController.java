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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import mg.cepe.gestion.model.Matiere;
import mg.cepe.gestion.service.MatiereService;
import mg.cepe.gestion.service.impl.MatiereServiceImpl;

public class MatiereController implements Initializable {
    @FXML
    private TextField txtNumMat, txtDesignMat;
    @FXML
    private Spinner<Integer> spCoef;
    @FXML
    private TableView<Matiere> tableMatieres;
    @FXML
    private TableColumn<Matiere, String> colNum, colDesign;
    @FXML
    private TableColumn<Matiere, Number> colCoef;
    private final MatiereService service = new MatiereServiceImpl();
    private final ObservableList<Matiere> data = FXCollections.observableArrayList();

    private static final String REGEX_CODE = "^[A-Za-z0-9_-]{3,20}$";
    private static final String REGEX_NOM = "^[A-Za-zÀ-ÿ\\s\\-'']{2,100}$";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        spCoef.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        colNum.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNumMat()));
        colDesign.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDesignMat()));
        colCoef.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getCoef()));
        tableMatieres.setItems(data);
        tableMatieres.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> {
            if (val != null)
                fillForm(val);
        });
        refresh();
    }

    private void fillForm(Matiere m) {
        txtNumMat.setText(m.getNumMat());
        txtDesignMat.setText(m.getDesignMat());
        spCoef.getValueFactory().setValue(m.getCoef());
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleAjouter() {
        if (!valider())
            return;
        try {
            service.ajouter(new Matiere(txtNumMat.getText().trim(), txtDesignMat.getText().trim(), spCoef.getValue()));
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
        Optional<ButtonType> res = confirm("Modifier la matière " + txtNumMat.getText() + " ?");
        if (res.isEmpty() || res.get() != ButtonType.OK)
            return;
        try {
            service.modifier(new Matiere(txtNumMat.getText().trim(), txtDesignMat.getText().trim(), spCoef.getValue()));
            refresh();
        } catch (Exception ex) {
            alert(Alert.AlertType.ERROR, "Erreur lors de la modification : " + ex.getMessage());
        }
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleSupprimer() {
        if (txtNumMat.getText().isBlank()) {
            alert(Alert.AlertType.WARNING, "Veuillez sélectionner une matière à supprimer.");
            return;
        }
        Optional<ButtonType> res = confirm("Supprimer définitivement la matière " + txtNumMat.getText()
                + " ?\\n\\nAttention : impossible si des notes y sont associées.");
        if (res.isEmpty() || res.get() != ButtonType.OK)
            return;
        try {
            service.supprimer(txtNumMat.getText().trim());
            refresh();
            handleClear();
        } catch (Exception ex) {
            alert(Alert.AlertType.ERROR, "Impossible de supprimer : " + ex.getMessage());
        }
    }

    @FXML
    private void handleClear() {
        txtNumMat.clear();
        txtDesignMat.clear();
        spCoef.getValueFactory().setValue(1);
        tableMatieres.getSelectionModel().clearSelection();
    }

    private void refresh() {
        data.setAll(service.listerTous());
    }

    private boolean valider() {
        String num = txtNumMat.getText().trim();
        String design = txtDesignMat.getText().trim();

        if (num.isEmpty() || design.isEmpty()) {
            alert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs.");
            return false;
        }
        if (!num.matches(REGEX_CODE)) {
            alert(Alert.AlertType.WARNING, "Code matière invalide.\\n3 à 20 caractères alphanumériques.");
            return false;
        }
        if (!design.matches(REGEX_NOM)) {
            alert(Alert.AlertType.WARNING, "Nom de matière invalide.\\n2 à 100 caractères (lettres, espaces, tirets).");
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