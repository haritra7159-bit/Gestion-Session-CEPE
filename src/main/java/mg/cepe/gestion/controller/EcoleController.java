package mg.cepe.gestion.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import mg.cepe.gestion.entity.Ecole;
import mg.cepe.gestion.exception.BusinessException;
import mg.cepe.gestion.exception.DataAccessException;
import mg.cepe.gestion.service.EcoleService;
import mg.cepe.gestion.service.impl.EcoleServiceImpl;

public final class EcoleController {

    private static final Logger LOG = LoggerFactory.getLogger(EcoleController.class);

    @FXML
    private TableView<Ecole> tableEcoles;
    @FXML
    private TableColumn<Ecole, String> colCode;
    @FXML
    private TableColumn<Ecole, String> colDesign;
    @FXML
    private TableColumn<Ecole, String> colAdresse;
    @FXML
    private TextField txtCode;
    @FXML
    private TextField txtDesign;
    @FXML
    private TextField txtAdresse;

    private EcoleService ecoleService;
    private boolean modeEdition;

    @FXML
    private void initialize() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("numEcole"));
        colDesign.setCellValueFactory(new PropertyValueFactory<>("design"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        tableEcoles.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                remplirFormulaire(selected);
                modeEdition = true;
                txtCode.setDisable(true);
            }
        });
        try {
            ecoleService = new EcoleServiceImpl();
            rafraichir();
        } catch (Exception e) {
            LOG.error("Initialisation module Écoles impossible", e);
            alerte("Base de données",
                    "Impossible d'initialiser le module Écoles.\n"
                            + "Vérifiez PostgreSQL et application-local.properties.\n\n"
                            + causeMessage(e));
        }
    }

    @FXML
    private void onNouveau() {
        viderFormulaire();
        modeEdition = false;
        txtCode.setDisable(false);
        txtCode.requestFocus();
    }

    @FXML
    private void onEnregistrer() {
        if (ecoleService == null) {
            alerte("Base de données", "Service non initialisé.");
            return;
        }
        try {
            Ecole ecole = new Ecole(txtCode.getText(), txtDesign.getText(), txtAdresse.getText());
            if (modeEdition) {
                ecoleService.modifier(ecole);
            } else {
                ecoleService.creer(ecole);
            }
            rafraichir();
            viderFormulaire();
            modeEdition = false;
            txtCode.setDisable(false);
            info("Enregistrement", "École enregistrée avec succès.");
        } catch (BusinessException e) {
            alerte("Validation", e.getMessage());
        } catch (DataAccessException e) {
            LOG.error("Erreur DB école", e);
            alerte("Base de données", e.getMessage() + "\n\n" + causeMessage(e));
        }
    }

    @FXML
    private void onSupprimer() {
        if (ecoleService == null) {
            alerte("Base de données", "Service non initialisé.");
            return;
        }
        Ecole selected = tableEcoles.getSelectionModel().getSelectedItem();
        if (selected == null) {
            alerte("Suppression", "Sélectionnez une école dans le tableau.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Supprimer l'école ?");
        confirm.setContentText(selected.getNumEcole() + " — " + selected.getDesign());
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }
        try {
            ecoleService.supprimer(selected.getNumEcole());
            rafraichir();
            viderFormulaire();
            modeEdition = false;
            txtCode.setDisable(false);
            info("Suppression", "École supprimée.");
        } catch (BusinessException e) {
            alerte("Suppression impossible", e.getMessage());
        } catch (DataAccessException e) {
            LOG.error("Erreur suppression école", e);
            alerte("Base de données", e.getMessage() + "\n\n" + causeMessage(e));
        }
    }

    @FXML
    private void onRafraichir() {
        rafraichir();
    }

    private void rafraichir() {
        if (ecoleService == null) {
            return;
        }
        try {
            tableEcoles.setItems(FXCollections.observableArrayList(ecoleService.lister()));
            tableEcoles.getSelectionModel().clearSelection();
        } catch (DataAccessException e) {
            LOG.error("Erreur chargement écoles", e);
            alerte("Base de données",
                    "Impossible de charger les écoles.\n\n" + e.getMessage() + "\n\n" + causeMessage(e));
        }
    }

    private void remplirFormulaire(Ecole ecole) {
        txtCode.setText(ecole.getNumEcole());
        txtDesign.setText(ecole.getDesign());
        txtAdresse.setText(ecole.getAdresse() != null ? ecole.getAdresse() : "");
    }

    private void viderFormulaire() {
        txtCode.clear();
        txtDesign.clear();
        txtAdresse.clear();
        tableEcoles.getSelectionModel().clearSelection();
    }

    private void alerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void info(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static String causeMessage(Throwable e) {
        Throwable c = e;
        while (c.getCause() != null) {
            c = c.getCause();
        }
        return c.getClass().getSimpleName() + ": " + c.getMessage();
    }
}