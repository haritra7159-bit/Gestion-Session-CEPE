package mg.cepe.gestion.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import mg.cepe.gestion.model.Ecole;
import mg.cepe.gestion.service.EcoleService;
import mg.cepe.gestion.service.impl.EcoleServiceImpl;
import java.net.URL;
import java.util.ResourceBundle;

public class EcoleController implements Initializable {
    @FXML private TextField txtNumEcole, txtDesign, txtAdresse;
    @FXML private TableView<Ecole> tableEcoles;
    @FXML private TableColumn<Ecole,String> colNum, colDesign, colAdresse;
    private final EcoleService service=new EcoleServiceImpl();
    private final ObservableList<Ecole> data=FXCollections.observableArrayList();
    @Override public void initialize(URL location,ResourceBundle resources){
        colNum.setCellValueFactory(c->new javafx.beans.property.SimpleStringProperty(c.getValue().getNumEcole()));
        colDesign.setCellValueFactory(c->new javafx.beans.property.SimpleStringProperty(c.getValue().getDesign()));
        colAdresse.setCellValueFactory(c->new javafx.beans.property.SimpleStringProperty(c.getValue().getAdresse()));
        tableEcoles.setItems(data);tableEcoles.getSelectionModel().selectedItemProperty().addListener((obs,old,val)->{if(val!=null)fillForm(val);});refresh();
    }
    private void fillForm(Ecole e){txtNumEcole.setText(e.getNumEcole());txtDesign.setText(e.getDesign());txtAdresse.setText(e.getAdresse());}
    @FXML private void handleAjouter(){service.ajouter(new Ecole(txtNumEcole.getText(),txtDesign.getText(),txtAdresse.getText()));refresh();handleClear();}
    @FXML private void handleModifier(){service.modifier(new Ecole(txtNumEcole.getText(),txtDesign.getText(),txtAdresse.getText()));refresh();}
    @FXML private void handleSupprimer(){service.supprimer(txtNumEcole.getText());refresh();handleClear();}
    @FXML private void handleClear(){txtNumEcole.clear();txtDesign.clear();txtAdresse.clear();}
    private void refresh(){data.setAll(service.listerTous());}
}
