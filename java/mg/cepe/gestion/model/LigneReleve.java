package mg.cepe.gestion.model;

public class LigneReleve {
    private String designMat;
    private double coef;
    private double note, notePonderee;

    public LigneReleve(String designMat, double coef, double note, double notePonderee) {
        this.designMat = designMat;
        this.coef = coef;
        this.note = note;
        this.notePonderee = notePonderee;
    }

    public String getDesignMat() {
        return designMat;
    }

    public double getCoef() {
        return coef;
    }

    public double getNote() {
        return note;
    }

    public double getNotePonderee() {
        return notePonderee;
    }
}
