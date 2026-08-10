package mg.cepe.gestion.controller;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

import javafx.fxml.FXMLLoader;

class FxmlLoadSmokeTest {

    @Test
    void shouldLoadEleveNotesDialogFxml() {
        assertDoesNotThrow(() -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/eleve-notes-dialog.fxml"));
            loader.load();
        });
    }
}
