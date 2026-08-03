package mg.cepe.gestion.service.impl;

import mg.cepe.gestion.dao.MatiereDao;
import mg.cepe.gestion.dao.impl.MatiereDaoImpl;
import mg.cepe.gestion.model.Matiere;
import mg.cepe.gestion.service.MatiereService;
import java.util.List;

public class MatiereServiceImpl implements MatiereService {
    private final MatiereDao dao=new MatiereDaoImpl();
    @Override public void ajouter(Matiere m){dao.save(m);}
    @Override public void modifier(Matiere m){dao.update(m);}
    @Override public void supprimer(String id){dao.delete(id);}
    @Override public Matiere trouverParId(String id){return dao.findById(id).orElse(null);}
    @Override public List<Matiere> listerTous(){return dao.findAll();}
}
