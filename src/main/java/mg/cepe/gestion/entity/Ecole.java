package mg.cepe.gestion.entity;

import java.util.Objects;

/** Entité persistée : école. */
public final class Ecole {

    private String numEcole;
    private String design;
    private String adresse;

    public Ecole() {
    }

    public Ecole(String numEcole, String design, String adresse) {
        this.numEcole = numEcole;
        this.design = design;
        this.adresse = adresse;
    }

    public String getNumEcole() { return numEcole; }
    public void setNumEcole(String numEcole) { this.numEcole = numEcole; }
    public String getDesign() { return design; }
    public void setDesign(String design) { this.design = design; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ecole ecole)) return false;
        return Objects.equals(numEcole, ecole.numEcole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numEcole);
    }

    @Override
    public String toString() {
        return design + " (" + numEcole + ")";
    }
}
