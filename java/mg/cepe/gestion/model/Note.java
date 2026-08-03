package mg.cepe.gestion.model;

public class Note {
    private String anneeScolaire, numEleve, numMat;
    private double note;
    public Note() {}
    public Note(String anneeScolaire,String numEleve,String numMat,double note){this.anneeScolaire=anneeScolaire;this.numEleve=numEleve;this.numMat=numMat;this.note=note;}
    public String getAnneeScolaire(){return anneeScolaire;} public void setAnneeScolaire(String v){this.anneeScolaire=v;}
    public String getNumEleve(){return numEleve;} public void setNumEleve(String v){this.numEleve=v;}
    public String getNumMat(){return numMat;} public void setNumMat(String v){this.numMat=v;}
    public double getNote(){return note;} public void setNote(double v){this.note=v;}
}
