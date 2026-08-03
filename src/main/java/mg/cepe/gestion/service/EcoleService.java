package mg.cepe.gestion.service;

import mg.cepe.gestion.model.Ecole;
import java.util.List;

public interface EcoleService {
    void ajouter(Ecole ecole);
    void modifier(Ecole ecole);
    void supprimer(String numEcole);
    Ecole trouverParId(String numEcole);
    List<Ecole> listerTous();
}
