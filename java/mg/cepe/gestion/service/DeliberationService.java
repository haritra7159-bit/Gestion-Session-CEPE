package mg.cepe.gestion.service;

import mg.cepe.gestion.model.ResultatEleve;
import java.util.List;

public interface DeliberationService {
    double calculerMoyenne(String numEleve,String anneeScolaire);
    List<ResultatEleve> deliberer(String anneeScolaire);
    List<ResultatEleve> listerReussis(String anneeScolaire);
    List<ResultatEleve> listerEchoues(String anneeScolaire);
    List<ResultatEleve> listerAdmisSixieme(String anneeScolaire);
    List<ResultatEleve> classementParMerite(String anneeScolaire);
}
