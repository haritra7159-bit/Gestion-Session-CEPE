package mg.cepe.gestion.service.impl;

import mg.cepe.gestion.dao.NoteDao;
import mg.cepe.gestion.dao.impl.NoteDaoImpl;
import mg.cepe.gestion.model.Note;
import mg.cepe.gestion.service.NoteService;
import java.util.List;

public class NoteServiceImpl implements NoteService {
    private final NoteDao noteDao = new NoteDaoImpl();

    @Override public void ajouter(Note n) { noteDao.save(n); }
    @Override public void modifier(Note n) { noteDao.update(n); }
    @Override public void supprimer(String annee, String numEleve, String numMat) { noteDao.delete(annee, numEleve, numMat); }
    @Override public Note trouver(String annee, String numEleve, String numMat) { return noteDao.findById(annee, numEleve, numMat).orElse(null); }
    @Override public List<Note> listerTous() { return noteDao.findAll(); }
    @Override public List<Note> listerParEleveEtAnnee(String numEleve, String annee) { return noteDao.findByEleveAndAnnee(numEleve, annee); }
}
