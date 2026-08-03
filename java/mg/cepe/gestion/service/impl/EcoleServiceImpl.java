package mg.cepe.gestion.service.impl;

import mg.cepe.gestion.dao.EcoleDao;
import mg.cepe.gestion.dao.impl.EcoleDaoImpl;
import mg.cepe.gestion.model.Ecole;
import mg.cepe.gestion.service.EcoleService;
import java.util.List;

public class EcoleServiceImpl implements EcoleService {
    private final EcoleDao dao=new EcoleDaoImpl();
    @Override public void ajouter(Ecole e){dao.save(e);}
    @Override public void modifier(Ecole e){dao.update(e);}
    @Override public void supprimer(String id){dao.delete(id);}
    @Override public Ecole trouverParId(String id){return dao.findById(id).orElse(null);}
    @Override public List<Ecole> listerTous(){return dao.findAll();}
}
