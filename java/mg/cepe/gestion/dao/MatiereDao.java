package mg.cepe.gestion.dao;

import mg.cepe.gestion.model.Matiere;
import java.util.List;
import java.util.Optional;

public interface MatiereDao {
    void save(Matiere matiere);
    void update(Matiere matiere);
    void delete(String numMat);
    Optional<Matiere> findById(String numMat);
    List<Matiere> findAll();
}
