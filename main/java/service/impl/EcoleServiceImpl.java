package mg.cepe.gestion.service.impl;

import mg.cepe.gestion.dao.EcoleDao;
import mg.cepe.gestion.dao.impl.EcoleDaoImpl;
import mg.cepe.gestion.model.Ecole;
import mg.cepe.gestion.service.EcoleService;
import java.util.List;

public class EcoleServiceImpl implements EcoleService {
    private final EcoleDao ecoleDao = new EcoleDaoImpl();

    @Override public void ajouter(Ecole ecole) { ecoleDao.save(ecole); }
    @Override public void modifier(Ecole ecole) { ecoleDao.update(ecole); }
    @Override public void supprimer(String numEcole) { ecoleDao.delete(numEcole); }
    @Override public Ecole trouverParId(String numEcole) { return ecoleDao.findById(numEcole).orElse(null); }
    @Override public List<Ecole> listerTous() { return ecoleDao.findAll(); }
}
