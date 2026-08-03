package mg.cepe.gestion.model;

public class Matiere {
    private String numMat, designMat;
    private int coef;
    public Matiere() {}
    public Matiere(String numMat,String designMat,int coef){this.numMat=numMat;this.designMat=designMat;this.coef=coef;}
    public String getNumMat(){return numMat;} public void setNumMat(String v){this.numMat=v;}
    public String getDesignMat(){return designMat;} public void setDesignMat(String v){this.designMat=v;}
    public int getCoef(){return coef;} public void setCoef(int v){this.coef=v;}
    @Override public String toString(){return designMat;}
}
