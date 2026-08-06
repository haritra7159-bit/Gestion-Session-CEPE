package mg.cepe.gestion.controller.eleve;

import javafx.scene.control.ChoiceDialog;
import javafx.stage.DirectoryChooser;
import mg.cepe.gestion.model.*;
import mg.cepe.gestion.pdf.RelevePdfGenerator;
import mg.cepe.gestion.service.EcoleService;
import mg.cepe.gestion.service.MatiereService;
import mg.cepe.gestion.service.NoteService;
import mg.cepe.gestion.util.CodeFormat;
import mg.cepe.gestion.util.UiDialogs;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Bouton « Relevé » de la table élèves.
 * - Sans notes → ouvre le dialogue de saisie.
 * - Avec notes → année par défaut + confirmation + PDF.
 */
public final class EleveReleveActions {
    private final NoteService noteService;
    private final MatiereService matiereService;
    private final EcoleService ecoleService;
    private final EleveNotesOpener notesOpener;

    public EleveReleveActions(NoteService noteService, MatiereService matiereService,
                              EcoleService ecoleService, EleveNotesOpener notesOpener) {
        this.noteService = noteService;
        this.matiereService = matiereService;
        this.ecoleService = ecoleService;
        this.notesOpener = notesOpener;
    }

    public void onReleveClick(Eleve eleve) {
        if (eleve == null) return;
        // Vérification silencieuse
        if (!noteService.aDesNotes(eleve.getNumEleve())) {
            // Cas 1 : aucune note → dialogue de saisie
            if (notesOpener != null) {
                notesOpener.ouvrir(eleve);
            } else {
                UiDialogs.warn("Cet élève n'a aucune note. Utilisez « Voir notes » pour les saisir.");
            }
            return;
        }
        // Cas 2 : notes existantes → choix année (défaut = plus récente) + confirmation
        List<String> annees = noteService.listerParEleve(eleve.getNumEleve()).stream()
                .map(Note::getAnneeScolaire).distinct()
                .sorted(Comparator.reverseOrder()).toList();
        String defaut = annees.isEmpty() ? "2024-2025" : annees.get(0);
        ChoiceDialog<String> dialog = new ChoiceDialog<>(defaut, annees);
        dialog.setTitle("Relevé PDF");
        dialog.setHeaderText("Élève : " + eleve.getNom() + " " + eleve.getPrenom());
        dialog.setContentText("Année scolaire :");
        Optional<String> chosen = dialog.showAndWait();
        if (chosen.isEmpty()) return;
        String annee = chosen.get();
        if (!UiDialogs.confirm("Générer le relevé PDF pour " + eleve.getNom() + " " + eleve.getPrenom()
                + "\nAnnée scolaire : " + annee + " ?")) return;
        generer(eleve, annee);
    }

    public void generer(Eleve eleve, String annee) {
        if (!CodeFormat.matches(annee, CodeFormat.REGEX_ANNEE)) {
            UiDialogs.warn("Année scolaire invalide.\nFormat attendu : AAAA-AAAA");
            return;
        }
        try {
            List<Note> notes = noteService.listerParEleveEtAnnee(eleve.getNumEleve(), annee);
            if (notes.isEmpty()) {
                UiDialogs.warn("Aucune note pour l'année " + annee + ".");
                return;
            }
            List<LigneReleve> lignes = new ArrayList<>();
            double totalP = 0;
            int totalC = 0;
            for (Note n : notes) {
                Matiere m = matiereService.trouverParId(n.getNumMat());
                int coef = m != null ? m.getCoef() : 1;
                String design = m != null ? m.getDesignMat() : n.getNumMat();
                double pond = n.getNote() * coef;
                totalP += pond;
                totalC += coef;
                lignes.add(new LigneReleve(design, coef, n.getNote(), pond));
            }
            double moyenne = totalC == 0 ? 0 : totalP / totalC;
            String nomEcole = Optional.ofNullable(ecoleService.trouverParId(eleve.getNumEcole()))
                    .map(Ecole::getDesign).orElse(eleve.getNumEcole());
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Enregistrer le relevé PDF");
            File dir = chooser.showDialog(null);
            if (dir == null) return;
            File out = new File(dir, "releve_" + eleve.getNumEleve() + "_" + annee + ".pdf");
            RelevePdfGenerator.generer(out.getAbsolutePath(), annee, eleve, nomEcole,
                    lignes, totalP, totalC, moyenne);
            UiDialogs.info("Relevé généré avec succès :\n" + out.getAbsolutePath());
        } catch (Exception ex) {
            UiDialogs.warn("Erreur PDF : " + ex.getMessage());
        }
    }
}
