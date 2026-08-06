package mg.cepe.gestion.dao;
import mg.cepe.gestion.model.Note;
import java.util.List;
import java.util.Optional;
public interface NoteDao {
    void save(Note note);
    void update(Note note);
    void delete(String annee, String numEleve, String numMat);
    Optional<Note> findById(String annee, String numEleve, String numMat);
    List<Note> findAll();
    List<Note> findByEleveAndAnnee(String numEleve, String annee);
    List<Note> findByEleve(String numEleve);
    List<Note> findByAnnee(String annee);
    List<String> findDistinctAnnees();
}
