package mg.cepe.gestion.model;

public class ResultatEleve {
    private String numEleve;
    private String nom;
    private String prenom;
    private String nomEcole;
    private double moyenne;
    private double totalPondere;
    private int totalCoef;
    private String decision;
    private boolean admisSixieme;

    public ResultatEleve() {}

    public String getNumEleve() { return numEleve; }
    public void setNumEleve(String numEleve) { this.numEleve = numEleve; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getNomEcole() { return nomEcole; }
    public void setNomEcole(String nomEcole) { this.nomEcole = nomEcole; }
    public double getMoyenne() { return moyenne; }
    public void setMoyenne(double moyenne) { this.moyenne = moyenne; }
    public double getTotalPondere() { return totalPondere; }
    public void setTotalPondere(double totalPondere) { this.totalPondere = totalPondere; }
    public int getTotalCoef() { return totalCoef; }
    public void setTotalCoef(int totalCoef) { this.totalCoef = totalCoef; }
    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }
    public boolean isAdmisSixieme() { return admisSixieme; }
    public void setAdmisSixieme(boolean admisSixieme) { this.admisSixieme = admisSixieme; }
    public String getNomComplet() { return nom + " " + prenom; }
}
