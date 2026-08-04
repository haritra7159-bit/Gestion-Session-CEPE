package mg.cepe.gestion.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class MainController implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());
    @FXML
    private BorderPane mainContainer;
    @FXML
    private Button btnAccueil, btnEcoles, btnEleves, btnMatieres, btnDeliberation, btnClassement;

    private Button currentActiveBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnAccueil.setOnAction(e -> {
            setActiveButton(btnAccueil);
            loadView("/fxml/accueil-view.fxml");
        });
        btnEcoles.setOnAction(e -> {
            setActiveButton(btnEcoles);
            loadView("/fxml/ecole-view.fxml");
        });
        btnEleves.setOnAction(e -> {
            setActiveButton(btnEleves);
            loadView("/fxml/eleve-view.fxml");
        });
        btnMatieres.setOnAction(e -> {
            setActiveButton(btnMatieres);
            loadView("/fxml/matiere-view.fxml");
        });
        btnDeliberation.setOnAction(e -> {
            setActiveButton(btnDeliberation);
            loadView("/fxml/deliberation-view.fxml");
        });
        btnClassement.setOnAction(e -> {
            setActiveButton(btnClassement);
            loadView("/fxml/classement-view.fxml");
        });

        setActiveButton(btnAccueil);
        loadView("/fxml/accueil-view.fxml");
    }

    private void setActiveButton(Button activeBtn) {
        if (currentActiveBtn != null) {
            currentActiveBtn.getStyleClass().remove("active");
        }
        currentActiveBtn = activeBtn;
        if (!activeBtn.getStyleClass().contains("active")) {
            activeBtn.getStyleClass().add("active");
        }
    }

    private void loadView(String fxmlPath) {
        try {
            Node view = FXMLLoader.load(getClass().getResource(fxmlPath));
            mainContainer.setCenter(view);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load view: " + fxmlPath, e);
        }
    }
}