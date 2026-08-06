package mg.cepe.gestion.controller.notes;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import mg.cepe.gestion.model.Matiere;
import mg.cepe.gestion.model.Note;
import mg.cepe.gestion.util.CodeFormat;
import mg.cepe.gestion.util.UiDialogs;

/** Résolution année, validation note, construction Note. */
public final class NotesFormHelper {
    private final ComboBox<String> cbAnnee;
    private final ComboBox<Matiere> cbMatiere;
    private final TextField txtNote;

    public NotesFormHelper(ComboBox<String> cbAnnee, ComboBox<Matiere> cbMatiere, TextField txtNote) {
        this.cbAnnee = cbAnnee;
        this.cbMatiere = cbMatiere;
        this.txtNote = txtNote;
    }

    public String resolveAnnee() {
        String v = cbAnnee.getEditor().getText();
        if (v == null || v.isBlank()) v = cbAnnee.getValue();
        return v == null ? "" : v.trim();
    }

    public void fillFromNote(Note n) {
        cbAnnee.setValue(n.getAnneeScolaire());
        txtNote.setText(String.valueOf(n.getNote()));
    }

    public void clearFields() {
        txtNote.clear();
        cbMatiere.setValue(null);
    }

    public boolean valider() {
        if (!CodeFormat.matches(resolveAnnee(), CodeFormat.REGEX_ANNEE)) {
            UiDialogs.warn("Année scolaire invalide.\nFormat attendu : AAAA-AAAA (ex. 2024-2025)");
            return false;
        }
        if (cbMatiere.getValue() == null) {
            UiDialogs.warn("Veuillez choisir une matière.");
            return false;
        }
        try {
            double n = Double.parseDouble(txtNote.getText().trim().replace(',', '.'));
            if (n < 0 || n > 20) {
                UiDialogs.warn("La note doit être comprise entre 0 et 20.");
                return false;
            }
        } catch (Exception e) {
            UiDialogs.warn("Note invalide. Saisissez un nombre (ex. 15.5).");
            return false;
        }
        return true;
    }

    public Note buildNote(String numEleve) {
        double n = Double.parseDouble(txtNote.getText().trim().replace(',', '.'));
        return new Note(resolveAnnee(), numEleve, cbMatiere.getValue().getNumMat(), n);
    }

    public Matiere getMatiere() {
        return cbMatiere.getValue();
    }
}
