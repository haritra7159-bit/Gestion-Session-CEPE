package mg.cepe.gestion.model;

public class Note {
    private String anneeScolaire;
    private String numEleve;
    private String numMat;
    private double note;

    public Note() {}
    public Note(String anneeScolaire, String numEleve, String numMat, double note) {
        this.anneeScolaire = anneeScolaire; this.numEleve = numEleve;
        this.numMat = numMat; this.note = note;
    }
    public String getAnneeScolaire() { return anneeScolaire; }
    public void setAnneeScolaire(String anneeScolaire) { this.anneeScolaire = anneeScolaire; }
    public String getNumEleve() { return numEleve; }
    public void setNumEleve(String numEleve) { this.numEleve = numEleve; }
    public String getNumMat() { return numMat; }
    public void setNumMat(String numMat) { this.numMat = numMat; }
    public double getNote() { return note; }
    public void setNote(double note) { this.note = note; }
}
