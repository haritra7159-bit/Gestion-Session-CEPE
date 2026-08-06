package mg.cepe.gestion.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public final class CepeApplication extends Application {
    private static final String[] STYLES = {
        "/css/base.css", "/css/sidebar.css", "/css/buttons.css",
        "/css/forms.css", "/css/tables.css", "/css/cards.css", "/css/dialogs.css"
    };
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(CepeApplication.class.getResource("/fxml/main-layout.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 760);
        for (String css : STYLES) {
            var url = CepeApplication.class.getResource(css);
            if (url != null) scene.getStylesheets().add(url.toExternalForm());
        }
        stage.setTitle("Gestion d'une session de CEPE");
        stage.setMinWidth(1000); stage.setMinHeight(650);
        stage.setScene(scene); stage.show();
    }
    public static void main(String[] args) { launch(args); }
}
