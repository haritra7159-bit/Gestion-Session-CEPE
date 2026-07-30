package mg.cepe.gestion.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/** JavaFX entry point for the CEPE application. */
public final class CepeApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(CepeApplication.class.getResource("/fxml/main-layout.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 760);
        scene.getStylesheets().add(CepeApplication.class.getResource("/css/application.css").toExternalForm());
        stage.setTitle("Gestion d'une session de CEPE");
        stage.setMinWidth(1000);
        stage.setMinHeight(650);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) { launch(args); }
}
