package mg.cepe.gestion.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML private BorderPane mainContainer;
    @FXML private Button btnEcoles, btnEleves, btnMatieres, btnNotes, btnDeliberation, btnClassement, btnRecherche, btnReleve;
    @Override public void initialize(URL location, ResourceBundle resources) {
        btnEcoles.setOnAction(e->loadView("/fxml/ecole-view.fxml"));
        btnEleves.setOnAction(e->loadView("/fxml/eleve-view.fxml"));
        btnMatieres.setOnAction(e->loadView("/fxml/matiere-view.fxml"));
        btnNotes.setOnAction(e->loadView("/fxml/note-view.fxml"));
        btnDeliberation.setOnAction(e->loadView("/fxml/deliberation-view.fxml"));
        btnClassement.setOnAction(e->loadView("/fxml/classement-view.fxml"));
        btnRecherche.setOnAction(e->loadView("/fxml/recherche-view.fxml"));
        btnReleve.setOnAction(e->loadView("/fxml/releve-view.fxml"));
    }
    private void loadView(String fxmlPath){try{Node view=FXMLLoader.load(getClass().getResource(fxmlPath));mainContainer.setCenter(view);}catch(IOException e){e.printStackTrace();}}
}
