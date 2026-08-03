package mg.cepe.gestion.model;

public class Ecole {
    private String numEcole, design, adresse;
    public Ecole() {}
    public Ecole(String numEcole, String design, String adresse) { this.numEcole=numEcole; this.design=design; this.adresse=adresse; }
    public String getNumEcole(){return numEcole;} public void setNumEcole(String v){this.numEcole=v;}
    public String getDesign(){return design;} public void setDesign(String v){this.design=v;}
    public String getAdresse(){return adresse;} public void setAdresse(String v){this.adresse=v;}
    @Override public String toString(){return design;}
}
