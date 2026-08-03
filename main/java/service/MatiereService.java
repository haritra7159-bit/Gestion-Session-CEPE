package mg.cepe.gestion.service;

import mg.cepe.gestion.model.Matiere;
import java.util.List;

public interface MatiereService {
    void ajouter(Matiere matiere);
    void modifier(Matiere matiere);
    void supprimer(String numMat);
    Matiere trouverParId(String numMat);
    List<Matiere> listerTous();
}
