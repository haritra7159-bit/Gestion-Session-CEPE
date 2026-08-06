package mg.cepe.gestion.controller.eleve;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import mg.cepe.gestion.model.Eleve;
import mg.cepe.gestion.service.EcoleService;
import mg.cepe.gestion.service.MatiereService;
import mg.cepe.gestion.service.NoteService;

/** Colonne d'actions (Notes / Relevé) de la table élèves. */
public final class EleveTableActions {
    private final EleveNotesOpener notesOpener = new EleveNotesOpener();
    private final EleveReleveActions releveActions;

    public EleveTableActions(NoteService noteService, MatiereService matiereService, EcoleService ecoleService) {
        this.releveActions = new EleveReleveActions(noteService, matiereService, ecoleService, notesOpener);
    }

    public void setupActionsColumn(TableColumn<Eleve, Void> colActions) {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnNotes = new Button("Voir notes");
            private final Button btnReleve = new Button("Relevé");
            private final HBox pane = new HBox(8, btnNotes, btnReleve);
            {
                btnNotes.getStyleClass().addAll("btn", "btn-info");
                btnNotes.setStyle("-fx-font-size: 11px; -fx-padding: 6 12;");
                btnReleve.getStyleClass().addAll("btn", "btn-success");
                btnReleve.setStyle("-fx-font-size: 11px; -fx-padding: 6 12;");
                btnNotes.setOnAction(e ->
                        notesOpener.ouvrir(getTableView().getItems().get(getIndex())));
                btnReleve.setOnAction(e ->
                        releveActions.onReleveClick(getTableView().getItems().get(getIndex())));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }
}
