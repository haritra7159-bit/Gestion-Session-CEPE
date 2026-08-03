package mg.cepe.gestion.dao;

import mg.cepe.gestion.model.Ecole;
import java.util.List;
import java.util.Optional;

public interface EcoleDao {
    void save(Ecole ecole);
    void update(Ecole ecole);
    void delete(String numEcole);
    Optional<Ecole> findById(String numEcole);
    List<Ecole> findAll();
}
