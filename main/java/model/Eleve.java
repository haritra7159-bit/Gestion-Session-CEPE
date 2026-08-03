package mg.cepe.gestion.model;

import java.time.LocalDate;

public class Eleve {
    private String numEleve;
    private String numEcole;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;

    public Eleve() {}
    public Eleve(String numEleve, String numEcole, String nom, String prenom, LocalDate dateNaissance) {
        this.numEleve = numEleve; this.numEcole = numEcole;
        this.nom = nom; this.prenom = prenom; this.dateNaissance = dateNaissance;
    }
    public String getNumEleve() { return numEleve; }
    public void setNumEleve(String numEleve) { this.numEleve = numEleve; }
    public String getNumEcole() { return numEcole; }
    public void setNumEcole(String numEcole) { this.numEcole = numEcole; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }
    public String getNomComplet() { return nom + " " + prenom; }
}
