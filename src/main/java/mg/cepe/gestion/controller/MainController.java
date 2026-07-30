package mg.cepe.gestion.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/** Controls the initial application shell. */
public final class MainController {
    @FXML private Label pageTitle;
    @FXML private void showComingSoon() { pageTitle.setText("Module en cours de réalisation"); }
}
