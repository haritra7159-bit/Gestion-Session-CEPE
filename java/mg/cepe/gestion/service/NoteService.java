package mg.cepe.gestion.service;
import mg.cepe.gestion.model.Note;
import java.util.List;
public interface NoteService {
    void ajouter(Note note);
    void modifier(Note note);
    void supprimer(String annee,String numEleve,String numMat);
    Note trouver(String annee,String numEleve,String numMat);
    List<Note> listerTous();
    List<Note> listerParEleveEtAnnee(String numEleve,String annee);
    List<Note> listerParEleve(String numEleve);
    boolean aDesNotes(String numEleve);
    List<String> listerAnnees();
}
