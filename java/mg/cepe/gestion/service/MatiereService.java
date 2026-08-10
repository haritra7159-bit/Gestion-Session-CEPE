package mg.cepe.gestion.service;

import java.util.List;

import mg.cepe.gestion.model.Matiere;

public interface MatiereService {
    void ajouter(Matiere matiere);

    void modifier(Matiere matiere);

    void supprimer(String numMat);

    Matiere trouverParId(String numMat);

    List<Matiere> listerTous();
}
