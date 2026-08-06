package mg.cepe.gestion.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import mg.cepe.gestion.model.ResultatEleve;
import mg.cepe.gestion.service.DeliberationService;
import mg.cepe.gestion.service.NoteService;
import mg.cepe.gestion.service.impl.DeliberationServiceImpl;
import mg.cepe.gestion.service.impl.NoteServiceImpl;
import mg.cepe.gestion.util.CodeFormat;
import mg.cepe.gestion.util.UiDialogs;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ClassementController implements Initializable {
    @FXML private ComboBox<String> cbAnnee;
    @FXML private TableView<ResultatEleve> tableClassement;
    @FXML private TableColumn<ResultatEleve, Number> colRang, colMoyenne;
    @FXML private TableColumn<ResultatEleve, String> colNom, colPrenom, colEcole;
    private final DeliberationService service = new DeliberationServiceImpl();
    private final NoteService noteService = new NoteServiceImpl();
    private final ObservableList<ResultatEleve> data = FXCollections.observableArrayList();

    @Override public void initialize(URL u, ResourceBundle r) {
        cbAnnee.setEditable(true);
        cbAnnee.setPromptText("Choisir une année scolaire");
        List<String> annees = noteService.listerAnnees();
        cbAnnee.setItems(FXCollections.observableArrayList(annees));
        if (!annees.isEmpty()) cbAnnee.setValue(annees.get(0));
        colRang.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(tableClassement.getItems().indexOf(c.getValue()) + 1));
        colNom.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNom()));
        colPrenom.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPrenom()));
        colEcole.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNomEcole()));
        colMoyenne.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getMoyenne()));
        tableClassement.setItems(data);
    }
    private String resolveAnnee() {
        String v = cbAnnee.getEditor().getText();
        if (v == null || v.isBlank()) v = cbAnnee.getValue();
        return v == null ? "" : v.trim();
    }
    @FXML private void handleClassement() {
        String a = resolveAnnee();
        if (!CodeFormat.matches(a, CodeFormat.REGEX_ANNEE)) {
            UiDialogs.warn("Année invalide. Format AAAA-AAAA"); return;
        }
        data.setAll(service.classementParMerite(a));
    }
}
