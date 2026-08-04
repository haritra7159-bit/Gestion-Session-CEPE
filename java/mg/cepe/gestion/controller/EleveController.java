package mg.cepe.gestion.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.DirectoryChooser;
import mg.cepe.gestion.model.Ecole;
import mg.cepe.gestion.model.Eleve;
import mg.cepe.gestion.model.LigneReleve;
import mg.cepe.gestion.model.Matiere;
import mg.cepe.gestion.model.Note;
import mg.cepe.gestion.pdf.RelevePdfGenerator;
import mg.cepe.gestion.service.EcoleService;
import mg.cepe.gestion.service.EleveService;
import mg.cepe.gestion.service.MatiereService;
import mg.cepe.gestion.service.NoteService;
import mg.cepe.gestion.service.impl.EcoleServiceImpl;
import mg.cepe.gestion.service.impl.EleveServiceImpl;
import mg.cepe.gestion.service.impl.MatiereServiceImpl;
import mg.cepe.gestion.service.impl.NoteServiceImpl;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class EleveController implements Initializable {
    @FXML private TextField txtNumEleve, txtNom, txtPrenom, txtRecherche;
    @FXML private DatePicker dpNaissance;
    @FXML private ComboBox<Ecole> cbEcole;
    @FXML private TableView<Eleve> tableEleves;
    @FXML private TableColumn<Eleve, String> colNum, colNom, colPrenom, colEcole, colNaissance;
    @FXML private TableColumn<Eleve, Void> colActions;

    private final EleveService eleveService = new EleveServiceImpl();
    private final EcoleService ecoleService = new EcoleServiceImpl();
    private final NoteService noteService = new NoteServiceImpl();
    private final MatiereService matiereService = new MatiereServiceImpl();
    private final ObservableList<Eleve> data = FXCollections.observableArrayList();

    private static final String REGEX_CODE = "^[A-Za-z0-9_-]{3,20}$";
    private static final String REGEX_NOM = "^[A-Za-z\\u00C0-\\u017F\\s\\-'']{2,100}$";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbEcole.setItems(FXCollections.observableArrayList(ecoleService.listerTous()));

        colNum.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNumEleve()));
        colNom.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNom()));
        colPrenom.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPrenom()));
        colEcole.setCellValueFactory(c -> {
            var ec = ecoleService.trouverParId(c.getValue().getNumEcole());
            return new javafx.beans.property.SimpleStringProperty(ec != null ? ec.getDesign() : "");
        });
        colNaissance.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDateNaissance().toString()));

        setupActionsColumn();
        tableEleves.setItems(data);
        tableEleves.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> {
            if (val != null) fillForm(val);
        });
        refresh();
    }

    private void setupActionsColumn() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnNotes = new Button("Voir notes");
            private final Button btnReleve = new Button("Relevé");
            {
                btnNotes.getStyleClass().addAll("btn", "btn-info");
                btnNotes.setStyle("-fx-font-size: 11px; -fx-padding: 6 12;");
                btnReleve.getStyleClass().addAll("btn", "btn-success");
                btnReleve.setStyle("-fx-font-size: 11px; -fx-padding: 6 12;");

                btnNotes.setOnAction(event -> {
                    Eleve eleve = getTableView().getItems().get(getIndex());
                    ouvrirGestionNotes(eleve);
                });
                btnReleve.setOnAction(event -> {
                    Eleve eleve = getTableView().getItems().get(getIndex());
                    demanderAnneeEtGenererReleve(eleve);
                });
            }
            private final HBox pane = new HBox(8, btnNotes, btnReleve);

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void fillForm(Eleve e) {
        txtNumEleve.setText(e.getNumEleve());
        txtNom.setText(e.getNom());
        txtPrenom.setText(e.getPrenom());
        dpNaissance.setValue(e.getDateNaissance());
        cbEcole.getItems().stream()
            .filter(ec -> ec.getNumEcole().equals(e.getNumEcole()))
            .findFirst().ifPresent(cbEcole::setValue);
    }

    @FXML private void handleAjouter() {
        if (!valider()) return;
        Eleve e = new Eleve(txtNumEleve.getText().trim(), cbEcole.getValue().getNumEcole(),
                            txtNom.getText().trim(), txtPrenom.getText().trim(), dpNaissance.getValue());
        eleveService.ajouter(e);
        refresh(); handleClear();
    }

    @FXML private void handleModifier() {
        if (!valider()) return;
        Optional<ButtonType> res = confirm("Modifier l\'élève " + txtNumEleve.getText() + " ?");
        if (res.isEmpty() || res.get() != ButtonType.OK) return;
        Eleve e = new Eleve(txtNumEleve.getText().trim(), cbEcole.getValue().getNumEcole(),
                            txtNom.getText().trim(), txtPrenom.getText().trim(), dpNaissance.getValue());
        eleveService.modifier(e);
        refresh();
    }

    @FXML private void handleSupprimer() {
        if (txtNumEleve.getText().isBlank()) {
            alert(Alert.AlertType.WARNING, "Veuillez sélectionner un élève à supprimer.");
            return;
        }
        Optional<ButtonType> res = confirm("Supprimer définitivement l\'élève " + txtNumEleve.getText() + " ?\n\nAttention : impossible si des notes y sont associées.");
        if (res.isEmpty() || res.get() != ButtonType.OK) return;
        try {
            eleveService.supprimer(txtNumEleve.getText().trim());
            refresh(); handleClear();
        } catch (Exception ex) {
            alert(Alert.AlertType.ERROR, "Impossible de supprimer : " + ex.getMessage());
        }
    }

    @FXML private void handleClear() {
        txtNumEleve.clear(); txtNom.clear(); txtPrenom.clear();
        dpNaissance.setValue(null); cbEcole.setValue(null);
        tableEleves.getSelectionModel().clearSelection();
    }

    @FXML private void handleRechercher() {
        String critere = txtRecherche.getText();
        if (critere == null || critere.isBlank()) refresh();
        else data.setAll(eleveService.rechercher(critere));
    }

    @FXML private void handleResetRecherche() {
        txtRecherche.clear(); refresh();
    }

    private void refresh() { data.setAll(eleveService.listerTous()); }

    private boolean valider() {
        if (txtNumEleve.getText().isBlank() || txtNom.getText().isBlank()
            || txtPrenom.getText().isBlank() || dpNaissance.getValue() == null
            || cbEcole.getValue() == null) {
            alert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs.");
            return false;
        }
        if (!txtNumEleve.getText().trim().matches(REGEX_CODE)) {
            alert(Alert.AlertType.WARNING, "Numéro invalide. 3 à 20 caractères alphanumériques.");
            return false;
        }
        return true;
    }

    private void alert(Alert.AlertType type, String msg) {
        new Alert(type, msg, ButtonType.OK).showAndWait();
    }
    private Optional<ButtonType> confirm(String msg) {
        return new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.OK, ButtonType.CANCEL).showAndWait();
    }

    private void ouvrirGestionNotes(Eleve eleve) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/eleve-notes-dialog.fxml"));
            Parent root = loader.load();
            EleveNotesDialogController controller = loader.getController();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Notes – " + eleve.getNomComplet());
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
            stage.setScene(scene);
            controller.setEleve(eleve, stage);
            stage.showAndWait();
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void demanderAnneeEtGenererReleve(Eleve eleve) {
        TextInputDialog dialog = new TextInputDialog("2022-2023");
        dialog.setTitle("Générer le relevé PDF");
        dialog.setHeaderText("Relevé de notes pour " + eleve.getNomComplet());
        dialog.setContentText("Année scolaire :");
        dialog.showAndWait().ifPresent(annee -> genererReleve(eleve, annee));
    }

    private void genererReleve(Eleve eleve, String annee) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Génération du relevé PDF");
        confirm.setContentText("Générer le relevé de " + eleve.getNomComplet() + " pour l\'année " + annee + " ?");
        Optional<ButtonType> res = confirm.showAndWait();
        if (res.isEmpty() || res.get() != ButtonType.OK) return;

        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Dossier de sauvegarde");
        File dir = dc.showDialog(tableEleves.getScene().getWindow());
        if (dir == null) return;

        List<Note> notes = noteService.listerParEleveEtAnnee(eleve.getNumEleve(), annee);
        List<Matiere> matieres = matiereService.listerTous();
        List<LigneReleve> lignes = new ArrayList<>();
        double totalPondere = 0; int totalCoef = 0;

        for (Note n : notes) {
            Matiere mat = matieres.stream().filter(m -> m.getNumMat().equals(n.getNumMat())).findFirst().orElse(null);
            if (mat != null) {
                double np = n.getNote() * mat.getCoef();
                lignes.add(new LigneReleve(mat.getDesignMat(), mat.getCoef(), n.getNote(), np));
                totalPondere += np; totalCoef += mat.getCoef();
            }
        }
        double moyenne = totalCoef == 0 ? 0 : totalPondere / totalCoef;
        String nomEcole = "Inconnue";
        var ec = ecoleService.trouverParId(eleve.getNumEcole());
        if (ec != null) nomEcole = ec.getDesign();

        String chemin = dir.getAbsolutePath() + "/Releve_" + eleve.getNom() + "_" + annee + ".pdf";
        try {
            RelevePdfGenerator.generer(chemin, annee, eleve, nomEcole, lignes, totalPondere, totalCoef, moyenne);
            alert(Alert.AlertType.INFORMATION, "PDF généré avec succès !\n" + chemin);
        } catch (Exception ex) {
            alert(Alert.AlertType.ERROR, "Erreur : " + ex.getMessage());
        }
    }
}
