package mg.cepe.gestion.dao;

import mg.cepe.gestion.model.Eleve;
import java.util.List;
import java.util.Optional;

public interface EleveDao {
    void save(Eleve eleve);
    void update(Eleve eleve);
    void delete(String numEleve);
    Optional<Eleve> findById(String numEleve);
    List<Eleve> findAll();
    List<Eleve> searchByNomOrPrenom(String critere);
}
