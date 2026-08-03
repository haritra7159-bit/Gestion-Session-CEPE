package mg.cepe.gestion.service.impl;

import mg.cepe.gestion.dao.EleveDao;
import mg.cepe.gestion.dao.impl.EleveDaoImpl;
import mg.cepe.gestion.model.Eleve;
import mg.cepe.gestion.service.EleveService;
import java.util.List;

public class EleveServiceImpl implements EleveService {
    private final EleveDao dao=new EleveDaoImpl();
    @Override public void ajouter(Eleve e){dao.save(e);}
    @Override public void modifier(Eleve e){dao.update(e);}
    @Override public void supprimer(String id){dao.delete(id);}
    @Override public Eleve trouverParId(String id){return dao.findById(id).orElse(null);}
    @Override public List<Eleve> listerTous(){return dao.findAll();}
    @Override public List<Eleve> rechercher(String c){return dao.searchByNomOrPrenom(c);}
}
