package mg.cepe.gestion.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;

/** Coque principale : navigation entre les modules. */
public final class MainController {

    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);

    @FXML
    private StackPane contentArea;

    @FXML
    private void showEcoles() {
        chargerVue("/fxml/ecoles-view.fxml");
    }

    @FXML
    private void showComingSoon() {
        contentArea.getChildren().setAll(createPlaceholder(
                "Module en cours de réalisation",
                "Ce module sera ajouté prochainement."));
    }

    private void chargerVue(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(MainController.class.getResource(fxmlPath));
            if (loader.getLocation() == null) {
                throw new IOException("Ressource introuvable sur le classpath : " + fxmlPath
                        + "\nVérifie que le fichier est dans src/main/resources" + fxmlPath);
            }
            Node view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (Exception e) {
            LOG.error("Impossible de charger {}", fxmlPath, e);
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Chargement de la vue impossible");
            alert.setContentText(e.getMessage());
            TextArea area = new TextArea(sw.toString());
            area.setEditable(false);
            area.setWrapText(true);
            area.setPrefWidth(560);
            area.setPrefHeight(280);
            alert.getDialogPane().setExpandableContent(area);
            alert.getDialogPane().setExpanded(true);
            alert.showAndWait();
        }
    }

    private static Node createPlaceholder(String title, String description) {
        javafx.scene.layout.VBox box = new javafx.scene.layout.VBox(12);
        box.getStyleClass().add("content");
        box.setPadding(new javafx.geometry.Insets(42, 48, 42, 48));
        javafx.scene.control.Label t = new javafx.scene.control.Label(title);
        t.getStyleClass().add("page-title");
        javafx.scene.control.Label d = new javafx.scene.control.Label(description);
        d.getStyleClass().add("page-description");
        box.getChildren().addAll(t, d);
        return box;
    }
}