package mg.cepe.gestion.controller;

import java.util.List;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import mg.cepe.gestion.model.Eleve;
import mg.cepe.gestion.model.LigneReleve;

public class RelevePreviewController {

    @FXML
    private Label lblAnnee;
    @FXML
    private Label lblNom;
    @FXML
    private Label lblPrenom;
    @FXML
    private Label lblDateNaissance;
    @FXML
    private Label lblEcole;

    @FXML
    private TableView<LigneReleve> tvLignes;
    @FXML
    private TableColumn<LigneReleve, String> colMatiere;
    @FXML
    private TableColumn<LigneReleve, Double> colCoef;
    @FXML
    private TableColumn<LigneReleve, Double> colNote;
    @FXML
    private TableColumn<LigneReleve, Double> colPondere;

    @FXML
    private Label lblTotalCoef;
    @FXML
    private Label lblTotalPondere;
    @FXML
    private Label lblMoyenne;

    @FXML
    public void initialize() {
        colMatiere.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDesignMat()));
        colCoef.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getCoef()));
        colNote.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getNote()));
        colPondere.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getNotePonderee()));

        colCoef.setCellFactory(column -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("%.0f", item));
            }
        });

        colNote.setCellFactory(column -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("%.2f", item));
            }
        });

        colPondere.setCellFactory(column -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("%.2f", item));
            }
        });
    }

    public void updateInfosEleve(Eleve eleve, String anneeScolaire, String nomEcole) {
        if (lblAnnee != null) {
            lblAnnee.setText("Année scolaire : " + (anneeScolaire != null ? anneeScolaire : "-"));
        }
        if (eleve != null) {
            if (lblNom != null)
                lblNom.setText("Nom : " + (eleve.getNom() != null ? eleve.getNom() : ""));
            if (lblPrenom != null)
                lblPrenom.setText("Prénoms : " + (eleve.getPrenom() != null ? eleve.getPrenom() : ""));
            if (lblDateNaissance != null) {
                lblDateNaissance.setText("Date de naissance : " +
                        (eleve.getDateNaissance() != null ? eleve.getDateNaissance().toString() : "-"));
            }
        }
        if (lblEcole != null) {
            lblEcole.setText("École : " + (nomEcole != null ? nomEcole : "-"));
        }
    }

    public void updateNotes(List<LigneReleve> lignes) {
        if (tvLignes == null)
            return;
        tvLignes.getItems().setAll(lignes);

        double totalPondere = 0;
        double totalCoef = 0;

        for (LigneReleve l : lignes) {
            totalCoef += l.getCoef();
            totalPondere += l.getNotePonderee();
        }

        double moyenne = totalCoef > 0 ? totalPondere / totalCoef : 0;

        if (lblTotalCoef != null)
            lblTotalCoef.setText("Total Coef : " + (int) totalCoef);
        if (lblTotalPondere != null)
            lblTotalPondere.setText("Total : " + String.format("%.0f", totalPondere));
        if (lblMoyenne != null)
            lblMoyenne.setText("Moyenne : " + String.format("%.2f / 20", moyenne));
    }
}