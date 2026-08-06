package mg.cepe.gestion.controller.matiere;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import mg.cepe.gestion.model.Matiere;
import mg.cepe.gestion.util.CodeFormat;
import mg.cepe.gestion.util.UiDialogs;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class MatiereFormHelper {
    private final ComboBox<String> cbNumMat;
    private final TextField txtDesignMat;
    private final Spinner<Integer> spCoef;
    private String nextDefaultCode = "MAT-0001";

    public MatiereFormHelper(ComboBox<String> cbNumMat, TextField txtDesignMat, Spinner<Integer> spCoef) {
        this.cbNumMat = cbNumMat;
        this.txtDesignMat = txtDesignMat;
        this.spCoef = spCoef;
    }

    public void refreshCodeCombo(Collection<Matiere> matieres) {
        List<String> ids = matieres.stream().map(Matiere::getNumMat).collect(Collectors.toList());
        nextDefaultCode = CodeFormat.nextCode(CodeFormat.PREFIX_MATIERE, ids);
        cbNumMat.setItems(FXCollections.observableArrayList(ids));
        cbNumMat.setPromptText(nextDefaultCode);
        cbNumMat.getEditor().clear();
        cbNumMat.setValue(null);
    }

    public void fillForm(Matiere m) {
        cbNumMat.setValue(m.getNumMat());
        txtDesignMat.setText(m.getDesignMat());
        spCoef.getValueFactory().setValue(m.getCoef());
    }

    public void clearFields() {
        txtDesignMat.clear();
        spCoef.getValueFactory().setValue(1);
    }

    public String resolveCode() {
        String v = cbNumMat.getEditor().getText();
        if (v == null || v.isBlank()) v = cbNumMat.getValue();
        if (v == null || v.isBlank()) return nextDefaultCode;
        return v.trim().toUpperCase();
    }

    public boolean valider() {
        if (!CodeFormat.matches(resolveCode(), CodeFormat.REGEX_MATIERE)) {
            UiDialogs.warn("Code matière invalide.\nFormat attendu : MAT-XXXX (ex. MAT-0001)");
            return false;
        }
        if (txtDesignMat.getText() == null || txtDesignMat.getText().isBlank()) {
            UiDialogs.warn("La désignation de la matière est obligatoire.");
            return false;
        }
        return true;
    }

    public Matiere build() {
        return new Matiere(resolveCode(), txtDesignMat.getText().trim(), spCoef.getValue());
    }
}
