package mg.cepe.gestion.model;

public class LigneReleve {
    private String designMat;
    private int coef;
    private double note, notePonderee;
    public LigneReleve(String designMat,int coef,double note,double notePonderee){this.designMat=designMat;this.coef=coef;this.note=note;this.notePonderee=notePonderee;}
    public String getDesignMat(){return designMat;} public int getCoef(){return coef;} public double getNote(){return note;} public double getNotePonderee(){return notePonderee;}
}
