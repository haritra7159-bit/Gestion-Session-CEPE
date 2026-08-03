package mg.cepe.gestion.service;

import mg.cepe.gestion.model.Eleve;
import java.util.List;

public interface EleveService {
    void ajouter(Eleve eleve);
    void modifier(Eleve eleve);
    void supprimer(String numEleve);
    Eleve trouverParId(String numEleve);
    List<Eleve> listerTous();
    List<Eleve> rechercher(String critere);
}
