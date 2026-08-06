package mg.cepe.gestion.controller.ecole;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import mg.cepe.gestion.model.Ecole;
import mg.cepe.gestion.util.CodeFormat;
import mg.cepe.gestion.util.UiDialogs;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class EcoleFormHelper {
    private final ComboBox<String> cbNumEcole;
    private final TextField txtDesign;
    private final TextField txtAdresse;
    private String nextDefaultCode = "ECO-0001";

    public EcoleFormHelper(ComboBox<String> cbNumEcole, TextField txtDesign, TextField txtAdresse) {
        this.cbNumEcole = cbNumEcole;
        this.txtDesign = txtDesign;
        this.txtAdresse = txtAdresse;
    }

    public void refreshCodeCombo(Collection<Ecole> ecoles) {
        List<String> ids = ecoles.stream().map(Ecole::getNumEcole).collect(Collectors.toList());
        nextDefaultCode = CodeFormat.nextCode(CodeFormat.PREFIX_ECOLE, ids);
        cbNumEcole.setItems(FXCollections.observableArrayList(ids));
        cbNumEcole.setPromptText(nextDefaultCode);
        cbNumEcole.getEditor().clear();
        cbNumEcole.setValue(null);
    }

    public void fillForm(Ecole e) {
        cbNumEcole.setValue(e.getNumEcole());
        txtDesign.setText(e.getDesign());
        txtAdresse.setText(e.getAdresse());
    }

    public void clearFields() {
        txtDesign.clear();
        txtAdresse.clear();
    }

    public String resolveCode() {
        String v = cbNumEcole.getEditor().getText();
        if (v == null || v.isBlank()) v = cbNumEcole.getValue();
        if (v == null || v.isBlank()) return nextDefaultCode;
        return v.trim().toUpperCase();
    }

    public boolean valider() {
        if (!CodeFormat.matches(resolveCode(), CodeFormat.REGEX_ECOLE)) {
            UiDialogs.warn("Code école invalide.\nFormat attendu : ECO-XXXX (ex. ECO-0001)");
            return false;
        }
        if (txtDesign.getText() == null || !CodeFormat.matches(txtDesign.getText().trim(), CodeFormat.REGEX_NOM)) {
            UiDialogs.warn("Désignation invalide.\nUtilisez des lettres (2 à 150 caractères).");
            return false;
        }
        if (txtAdresse.getText() == null || txtAdresse.getText().isBlank()) {
            UiDialogs.warn("L'adresse est obligatoire.");
            return false;
        }
        return true;
    }

    public Ecole build() {
        return new Ecole(resolveCode(), txtDesign.getText().trim(), txtAdresse.getText().trim());
    }
}
