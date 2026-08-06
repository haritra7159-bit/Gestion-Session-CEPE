package mg.cepe.gestion.controller.eleve;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import mg.cepe.gestion.model.Ecole;
import mg.cepe.gestion.model.Eleve;
import mg.cepe.gestion.util.CodeFormat;
import mg.cepe.gestion.util.UiDialogs;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/** Remplissage formulaire élève, codes auto, validation. */
public final class EleveFormHelper {
    private final ComboBox<String> cbNumEleve;
    private final TextField txtNom;
    private final TextField txtPrenom;
    private final DatePicker dpNaissance;
    private final ComboBox<Ecole> cbEcole;
    private String nextDefaultCode = "ELV-0001";

    public EleveFormHelper(ComboBox<String> cbNumEleve, TextField txtNom, TextField txtPrenom,
                           DatePicker dpNaissance, ComboBox<Ecole> cbEcole) {
        this.cbNumEleve = cbNumEleve;
        this.txtNom = txtNom;
        this.txtPrenom = txtPrenom;
        this.dpNaissance = dpNaissance;
        this.cbEcole = cbEcole;
    }

    public void refreshCodeCombo(Collection<Eleve> eleves) {
        List<String> ids = eleves.stream().map(Eleve::getNumEleve).collect(Collectors.toList());
        nextDefaultCode = CodeFormat.nextCode(CodeFormat.PREFIX_ELEVE, ids);
        cbNumEleve.setItems(FXCollections.observableArrayList(ids));
        cbNumEleve.setPromptText(nextDefaultCode);
        cbNumEleve.getEditor().clear();
        cbNumEleve.setValue(null);
    }

    public void fillForm(Eleve e) {
        cbNumEleve.setValue(e.getNumEleve());
        txtNom.setText(e.getNom());
        txtPrenom.setText(e.getPrenom());
        dpNaissance.setValue(e.getDateNaissance());
        cbEcole.getItems().stream()
                .filter(ec -> ec.getNumEcole().equals(e.getNumEcole()))
                .findFirst()
                .ifPresent(cbEcole::setValue);
    }

    public void clearFields() {
        txtNom.clear();
        txtPrenom.clear();
        dpNaissance.setValue(null);
        cbEcole.setValue(null);
    }

    public String resolveCode() {
        String v = cbNumEleve.getEditor().getText();
        if (v == null || v.isBlank()) v = cbNumEleve.getValue();
        if (v == null || v.isBlank()) return nextDefaultCode;
        return v.trim().toUpperCase();
    }

    public boolean valider() {
        if (!CodeFormat.matches(resolveCode(), CodeFormat.REGEX_ELEVE)) {
            UiDialogs.warn("Code élève invalide.\nFormat attendu : ELV-XXXX (ex. ELV-0001)");
            return false;
        }
        if (txtNom.getText() == null || !CodeFormat.matches(txtNom.getText().trim(), CodeFormat.REGEX_NOM)) {
            UiDialogs.warn("Nom invalide.\nUtilisez des lettres (2 à 150 caractères).");
            return false;
        }
        if (txtPrenom.getText() == null || txtPrenom.getText().isBlank()) {
            UiDialogs.warn("Le prénom est obligatoire.");
            return false;
        }
        if (cbEcole.getValue() == null) {
            UiDialogs.warn("Veuillez sélectionner une école.");
            return false;
        }
        if (dpNaissance.getValue() == null) {
            UiDialogs.warn("La date de naissance est obligatoire.");
            return false;
        }
        return true;
    }

    public Eleve buildEleve() {
        return new Eleve(resolveCode(), cbEcole.getValue().getNumEcole(),
                txtNom.getText().trim(), txtPrenom.getText().trim(), dpNaissance.getValue());
    }
}
