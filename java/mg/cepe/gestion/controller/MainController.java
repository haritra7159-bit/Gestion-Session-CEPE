package mg.cepe.gestion.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class MainController implements Initializable {
    @FXML
    private BorderPane mainContainer;
    @FXML
    private Button btnAccueil, btnEcoles, btnEleves, btnMatieres, btnDeliberation, btnClassement;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnAccueil.setOnAction(e -> loadView("/fxml/accueil-view.fxml"));
        btnEcoles.setOnAction(e -> loadView("/fxml/ecole-view.fxml"));
        btnEleves.setOnAction(e -> loadView("/fxml/eleve-view.fxml"));
        btnMatieres.setOnAction(e -> loadView("/fxml/matiere-view.fxml"));
        btnDeliberation.setOnAction(e -> loadView("/fxml/deliberation-view.fxml"));
        btnClassement.setOnAction(e -> loadView("/fxml/classement-view.fxml"));

        // Charger l'accueil par défaut
        loadView("/fxml/accueil-view.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            Node view = FXMLLoader.load(getClass().getResource(fxmlPath));
            mainContainer.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}