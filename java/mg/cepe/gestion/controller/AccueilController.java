package mg.cepe.gestion.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import mg.cepe.gestion.service.EcoleService;
import mg.cepe.gestion.service.EleveService;
import mg.cepe.gestion.service.MatiereService;
import mg.cepe.gestion.service.impl.EcoleServiceImpl;
import mg.cepe.gestion.service.impl.EleveServiceImpl;
import mg.cepe.gestion.service.impl.MatiereServiceImpl;
import java.net.URL;
import java.util.ResourceBundle;

public class AccueilController implements Initializable {
    @FXML private Label lblNbEleves;
    @FXML private Label lblNbEcoles;
    @FXML private Label lblNbMatieres;

    private final EleveService eleveService = new EleveServiceImpl();
    private final EcoleService ecoleService = new EcoleServiceImpl();
    private final MatiereService matiereService = new MatiereServiceImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblNbEleves.setText(String.valueOf(eleveService.listerTous().size()));
        lblNbEcoles.setText(String.valueOf(ecoleService.listerTous().size()));
        lblNbMatieres.setText(String.valueOf(matiereService.listerTous().size()));
    }
}
