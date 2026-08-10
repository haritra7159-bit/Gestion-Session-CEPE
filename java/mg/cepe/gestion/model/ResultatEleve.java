package mg.cepe.gestion.model;

public class ResultatEleve {
    private String numEleve, nom, prenom, nomEcole, decision;
    private double moyenne, totalPondere, totalCoef;
    private boolean admisSixieme;

    public ResultatEleve() {
    }

    public String getNumEleve() {
        return numEleve;
    }

    public void setNumEleve(String v) {
        this.numEleve = v;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String v) {
        this.nom = v;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String v) {
        this.prenom = v;
    }

    public String getNomEcole() {
        return nomEcole;
    }

    public void setNomEcole(String v) {
        this.nomEcole = v;
    }

    public double getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(double v) {
        this.moyenne = v;
    }

    public double getTotalPondere() {
        return totalPondere;
    }

    public void setTotalPondere(double v) {
        this.totalPondere = v;
    }

    public double getTotalCoef() {
        return totalCoef;
    }

    public void setTotalCoef(double v) {
        this.totalCoef = v;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String v) {
        this.decision = v;
    }

    public boolean isAdmisSixieme() {
        return admisSixieme;
    }

    public void setAdmisSixieme(boolean v) {
        this.admisSixieme = v;
    }

    public String getNomComplet() {
        return nom + " " + prenom;
    }
}
