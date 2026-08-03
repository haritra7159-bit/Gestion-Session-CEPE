package mg.cepe.gestion.service.impl;

import mg.cepe.gestion.dao.NoteDao;
import mg.cepe.gestion.dao.impl.NoteDaoImpl;
import mg.cepe.gestion.model.Note;
import mg.cepe.gestion.service.NoteService;
import java.util.List;

public class NoteServiceImpl implements NoteService {
    private final NoteDao dao=new NoteDaoImpl();
    @Override public void ajouter(Note n){dao.save(n);}
    @Override public void modifier(Note n){dao.update(n);}
    @Override public void supprimer(String a,String e,String m){dao.delete(a,e,m);}
    @Override public Note trouver(String a,String e,String m){return dao.findById(a,e,m).orElse(null);}
    @Override public List<Note> listerTous(){return dao.findAll();}
    @Override public List<Note> listerParEleveEtAnnee(String e,String a){return dao.findByEleveAndAnnee(e,a);}
}
