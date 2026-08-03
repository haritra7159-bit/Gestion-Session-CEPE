package mg.cepe.gestion.service.impl;

import mg.cepe.gestion.dao.EleveDao;
import mg.cepe.gestion.dao.impl.EleveDaoImpl;
import mg.cepe.gestion.model.Eleve;
import mg.cepe.gestion.service.EleveService;
import java.util.List;

public class EleveServiceImpl implements EleveService {
    private final EleveDao eleveDao = new EleveDaoImpl();

    @Override public void ajouter(Eleve eleve) { eleveDao.save(eleve); }
    @Override public void modifier(Eleve eleve) { eleveDao.update(eleve); }
    @Override public void supprimer(String numEleve) { eleveDao.delete(numEleve); }
    @Override public Eleve trouverParId(String numEleve) { return eleveDao.findById(numEleve).orElse(null); }
    @Override public List<Eleve> listerTous() { return eleveDao.findAll(); }
    @Override public List<Eleve> rechercher(String critere) { return eleveDao.searchByNomOrPrenom(critere); }
}
