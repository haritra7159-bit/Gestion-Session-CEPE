package mg.cepe.gestion.model;

import java.time.LocalDate;

public class Eleve {
    private String numEleve, numEcole, nom, prenom;
    private LocalDate dateNaissance;
    public Eleve() {}
    public Eleve(String numEleve,String numEcole,String nom,String prenom,LocalDate dateNaissance){this.numEleve=numEleve;this.numEcole=numEcole;this.nom=nom;this.prenom=prenom;this.dateNaissance=dateNaissance;}
    public String getNumEleve(){return numEleve;} public void setNumEleve(String v){this.numEleve=v;}
    public String getNumEcole(){return numEcole;} public void setNumEcole(String v){this.numEcole=v;}
    public String getNom(){return nom;} public void setNom(String v){this.nom=v;}
    public String getPrenom(){return prenom;} public void setPrenom(String v){this.prenom=v;}
    public LocalDate getDateNaissance(){return dateNaissance;} public void setDateNaissance(LocalDate v){this.dateNaissance=v;}
    public String getNomComplet(){return nom+" "+prenom;}
}
