public class FxmlLoadCheck {
    public static void main(String[] args) throws Exception {
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(FxmlLoadCheck.class.getResource("/fxml/eleve-notes-dialog.fxml"));
        loader.load();
        System.out.println("FXML_OK");
    }
}
