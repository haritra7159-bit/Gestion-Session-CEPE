package mg.cepe.gestion.service;

import mg.cepe.gestion.entity.Ecole;

import java.util.List;
import java.util.Optional;

public interface EcoleService {
    List<Ecole> lister();
    Optional<Ecole> trouver(String numEcole);
    void creer(Ecole ecole);
    void modifier(Ecole ecole);
    void supprimer(String numEcole);
}
