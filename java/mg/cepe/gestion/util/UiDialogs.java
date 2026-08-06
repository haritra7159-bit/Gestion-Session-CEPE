package mg.cepe.gestion.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/** Alertes et confirmations partagées (évite la duplication dans les controllers). */
public final class UiDialogs {
    private UiDialogs() {}

    public static void warn(String message) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }

    public static void info(String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }

    public static boolean confirm(String message) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText(null);
        a.setContentText(message);
        return a.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}
