package mg.cepe.gestion.service.impl;

import mg.cepe.gestion.dao.MatiereDao;
import mg.cepe.gestion.dao.impl.MatiereDaoImpl;
import mg.cepe.gestion.model.Matiere;
import mg.cepe.gestion.service.MatiereService;
import java.util.List;

public class MatiereServiceImpl implements MatiereService {
    private final MatiereDao matiereDao = new MatiereDaoImpl();

    @Override public void ajouter(Matiere m) { matiereDao.save(m); }
    @Override public void modifier(Matiere m) { matiereDao.update(m); }
    @Override public void supprimer(String numMat) { matiereDao.delete(numMat); }
    @Override public Matiere trouverParId(String numMat) { return matiereDao.findById(numMat).orElse(null); }
    @Override public List<Matiere> listerTous() { return matiereDao.findAll(); }
}
