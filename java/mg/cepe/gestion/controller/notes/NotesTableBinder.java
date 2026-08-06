package mg.cepe.gestion.controller.notes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import mg.cepe.gestion.model.Eleve;
import mg.cepe.gestion.model.Matiere;
import mg.cepe.gestion.model.Note;
import mg.cepe.gestion.service.MatiereService;
import mg.cepe.gestion.service.NoteService;
import mg.cepe.gestion.util.CodeFormat;

/** Liaison table notes + refresh selon élève / année. */
public final class NotesTableBinder {
    private final NoteService noteService;
    private final MatiereService matiereService;
    private final TableView<Note> tableNotes;
    private final ComboBox<String> cbAnnee;
    private final ObservableList<Note> data;
    private final NotesFormHelper form;

    public NotesTableBinder(NoteService noteService, MatiereService matiereService,
                            TableView<Note> tableNotes, ComboBox<String> cbAnnee,
                            ObservableList<Note> data, NotesFormHelper form,
                            TableColumn<Note, String> colAnnee,
                            TableColumn<Note, String> colMatiere,
                            TableColumn<Note, Number> colNote) {
        this.noteService = noteService;
        this.matiereService = matiereService;
        this.tableNotes = tableNotes;
        this.cbAnnee = cbAnnee;
        this.data = data;
        this.form = form;
        colAnnee.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getAnneeScolaire()));
        colMatiere.setCellValueFactory(c -> {
            Matiere m = matiereService.trouverParId(c.getValue().getNumMat());
            return new javafx.beans.property.SimpleStringProperty(
                    m != null ? m.getDesignMat() : c.getValue().getNumMat());
        });
        if (colNote != null) {
            colNote.setCellValueFactory(c ->
                    new javafx.beans.property.SimpleDoubleProperty(c.getValue().getNote()));
        }
        tableNotes.setItems(data);
        tableNotes.getSelectionModel().selectedItemProperty()
                .addListener((o, a, v) -> { if (v != null) form.fillFromNote(v); });
    }

    public void refresh(Eleve eleve) {
        if (eleve == null) return;
        String annee = form.resolveAnnee();
        if (CodeFormat.matches(annee, CodeFormat.REGEX_ANNEE)) {
            data.setAll(noteService.listerParEleveEtAnnee(eleve.getNumEleve(), annee));
        } else {
            data.setAll(noteService.listerTous().stream()
                    .filter(n -> n.getNumEleve().equals(eleve.getNumEleve())).toList());
        }
        cbAnnee.setItems(FXCollections.observableArrayList(noteService.listerAnnees()));
    }

    public void clearSelection() {
        tableNotes.getSelectionModel().clearSelection();
        form.clearFields();
    }
}
