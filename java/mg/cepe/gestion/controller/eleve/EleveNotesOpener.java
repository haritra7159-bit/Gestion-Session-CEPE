package mg.cepe.gestion.controller.eleve;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mg.cepe.gestion.controller.EleveNotesDialogController;
import mg.cepe.gestion.model.Eleve;
import mg.cepe.gestion.util.UiDialogs;

/** Ouverture modale du dialogue de gestion des notes d'un élève. */
public final class EleveNotesOpener {

    private static final String[] STYLES = {
        "/css/base.css", "/css/sidebar.css", "/css/buttons.css",
        "/css/forms.css", "/css/tables.css", "/css/cards.css", "/css/dialogs.css"
    };

    public void ouvrir(Eleve eleve) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/eleve-notes-dialog.fxml"));
            Parent root = loader.load();
            EleveNotesDialogController ctrl = loader.getController();
            ctrl.setEleve(eleve);

            Scene scene = new Scene(root, 720, 580);
            for (String css : STYLES) {
                var url = getClass().getResource(css);
                if (url != null) {
                    scene.getStylesheets().add(url.toExternalForm());
                }
            }

            Stage stage = new Stage();
            stage.setTitle("Notes – " + eleve.getNom() + " " + eleve.getPrenom());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setMinWidth(640);
            stage.setMinHeight(520);
            stage.showAndWait();
        } catch (Exception ex) {
            UiDialogs.warn("Impossible d'ouvrir les notes : " + ex.getMessage());
        }
    }
}
