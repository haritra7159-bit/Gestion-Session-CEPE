package mg.cepe.gestion.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import mg.cepe.gestion.model.Eleve;
import mg.cepe.gestion.model.LigneReleve;
import mg.cepe.gestion.model.Matiere;
import mg.cepe.gestion.model.Note;
import mg.cepe.gestion.pdf.RelevePdfGenerator;
import mg.cepe.gestion.service.EleveService;
import mg.cepe.gestion.service.MatiereService;
import mg.cepe.gestion.service.NoteService;
import mg.cepe.gestion.service.impl.EleveServiceImpl;
import mg.cepe.gestion.service.impl.MatiereServiceImpl;
import mg.cepe.gestion.service.impl.NoteServiceImpl;
import mg.cepe.gestion.service.impl.EcoleServiceImpl;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ReleveController implements Initializable {
    @FXML private TextField txtAnnee;
    @FXML private ComboBox<Eleve> cbEleve;
    @FXML private Label lblStatus;
    private final EleveService eleveService = new EleveServiceImpl();
    private final MatiereService matiereService = new MatiereServiceImpl();
    private final NoteService noteService = new NoteServiceImpl();
    private static final String REGEX_ANNEE = "^[0-9]{4}-[0-9]{4}$";

    @Override public void initialize(URL location, ResourceBundle resources) {
        cbEleve.setItems(javafx.collections.FXCollections.observableArrayList(eleveService.listerTous()));
    }
    @FXML private void handleGenerer() {
        Eleve eleve = cbEleve.getValue(); String annee = txtAnnee.getText().trim();
        if (eleve == null) { lblStatus.setText("Sélectionnez un élève."); lblStatus.setTextFill(Color.RED); return; }
        if (annee.isEmpty()) { lblStatus.setText("Saisissez une année."); lblStatus.setTextFill(Color.RED); return; }
        if (!annee.matches(REGEX_ANNEE)) { lblStatus.setText("Format année invalide (YYYY-YYYY)."); lblStatus.setTextFill(Color.RED); return; }

        DirectoryChooser dc = new DirectoryChooser(); dc.setTitle("Dossier de sauvegarde");
        File dir = dc.showDialog(cbEleve.getScene().getWindow()); if (dir == null) return;

        List<Note> notes = noteService.listerParEleveEtAnnee(eleve.getNumEleve(), annee);
        if (notes.isEmpty()) { lblStatus.setText("Aucune note pour cette année."); lblStatus.setTextFill(Color.ORANGE); return; }

        List<Matiere> matieres = matiereService.listerTous();
        List<LigneReleve> lignes = new ArrayList<>();
        double totalPondere = 0; int totalCoef = 0;
        for (Note n : notes) {
            Matiere mat = matieres.stream().filter(m -> m.getNumMat().equals(n.getNumMat())).findFirst().orElse(null);
            if (mat != null) { double np = n.getNote() * mat.getCoef(); lignes.add(new LigneReleve(mat.getDesignMat(), mat.getCoef(), n.getNote(), np)); totalPondere += np; totalCoef += mat.getCoef(); }
        }
        double moyenne = totalCoef == 0 ? 0 : totalPondere / totalCoef;
        String nomEcole = "Inconnue";
        var ec = new EcoleServiceImpl().trouverParId(eleve.getNumEcole());
        if (ec != null) nomEcole = ec.getDesign();

        String chemin = dir.getAbsolutePath() + "/Releve_" + eleve.getNom() + "_" + annee + ".pdf";
        try { RelevePdfGenerator.generer(chemin, annee, eleve, nomEcole, lignes, totalPondere, totalCoef, moyenne); lblStatus.setText("PDF généré : " + chemin); lblStatus.setTextFill(Color.GREEN); }
        catch (Exception ex) { lblStatus.setText("Erreur : " + ex.getMessage()); lblStatus.setTextFill(Color.RED); }
    }
}
