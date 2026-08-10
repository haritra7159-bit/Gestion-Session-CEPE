package mg.cepe.gestion.model;

public class Matiere {
    private String numMat, designMat;
    private double coef;

    public Matiere() {
    }

    public Matiere(String numMat, String designMat, double coef) {
        this.numMat = numMat;
        this.designMat = designMat;
        this.coef = coef;
    }

    public String getNumMat() {
        return numMat;
    }

    public void setNumMat(String v) {
        this.numMat = v;
    }

    public String getDesignMat() {
        return designMat;
    }

    public void setDesignMat(String v) {
        this.designMat = v;
    }

    public double getCoef() {
        return coef;
    }

    public void setCoef(double v) {
        this.coef = v;
    }

    @Override
    public String toString() {
        return designMat;
    }
}
