package mg.cepe.gestion.dao;

import mg.cepe.gestion.entity.Ecole;

import java.util.List;
import java.util.Optional;

public interface EcoleDao {
    List<Ecole> findAll();
    Optional<Ecole> findById(String numEcole);
    void insert(Ecole ecole);
    void update(Ecole ecole);
    void deleteById(String numEcole);
    boolean existsById(String numEcole);
}
