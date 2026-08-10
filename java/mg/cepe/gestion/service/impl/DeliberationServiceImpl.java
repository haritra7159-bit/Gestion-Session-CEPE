package mg.cepe.gestion.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import mg.cepe.gestion.dao.EcoleDao;
import mg.cepe.gestion.dao.EleveDao;
import mg.cepe.gestion.dao.MatiereDao;
import mg.cepe.gestion.dao.NoteDao;
import mg.cepe.gestion.dao.impl.EcoleDaoImpl;
import mg.cepe.gestion.dao.impl.EleveDaoImpl;
import mg.cepe.gestion.dao.impl.MatiereDaoImpl;
import mg.cepe.gestion.dao.impl.NoteDaoImpl;
import mg.cepe.gestion.model.*;
import mg.cepe.gestion.service.DeliberationService;

public class DeliberationServiceImpl implements DeliberationService {
    private final NoteDao noteDao = new NoteDaoImpl();
    private final EleveDao eleveDao = new EleveDaoImpl();
    private final MatiereDao matiereDao = new MatiereDaoImpl();
    private final EcoleDao ecoleDao = new EcoleDaoImpl();

    @Override
    public double calculerMoyenne(String numEleve, String annee) {
        List<Note> notes = noteDao.findByEleveAndAnnee(numEleve, annee);
        if (notes.isEmpty())
            return 0.0;
        Map<String, Double> coefs = matiereDao.findAll().stream()
                .collect(Collectors.toMap(Matiere::getNumMat, Matiere::getCoef, (a, b) -> a));
        double tp = 0.0;
        double tc = 0.0;
        for (Note n : notes) {
            double c = coefs.getOrDefault(n.getNumMat(), 1.0);
            tp += n.getNote() * c;
            tc += c;
        }
        return tc == 0.0 ? 0.0 : tp / tc;
    }

    @Override
    public List<ResultatEleve> deliberer(String annee) {
        Set<String> ids = noteDao.findByAnnee(annee).stream().map(Note::getNumEleve).collect(Collectors.toSet());
        List<ResultatEleve> res = new ArrayList<>();
        for (Eleve e : eleveDao.findAll()) {
            if (ids.contains(e.getNumEleve())) {
                res.add(build(e, annee));
            }
        }
        return res;
    }

    @Override
    public List<ResultatEleve> listerReussis(String annee) {
        return deliberer(annee).stream().filter(r -> r.getMoyenne() >= 9.75)
                .sorted(Comparator.comparingDouble(ResultatEleve::getMoyenne).reversed()).collect(Collectors.toList());
    }

    @Override
    public List<ResultatEleve> listerEchoues(String annee) {
        return deliberer(annee).stream().filter(r -> r.getMoyenne() < 9.75)
                .sorted(Comparator.comparingDouble(ResultatEleve::getMoyenne).reversed()).collect(Collectors.toList());
    }

    @Override
    public List<ResultatEleve> listerAdmisSixieme(String annee) {
        return deliberer(annee).stream().filter(r -> r.getMoyenne() > 12.0)
                .sorted(Comparator.comparingDouble(ResultatEleve::getMoyenne).reversed()).collect(Collectors.toList());
    }

    @Override
    public List<ResultatEleve> classementParMerite(String annee) {
        return listerReussis(annee);
    }

    private ResultatEleve build(Eleve e, String annee) {
        double moy = calculerMoyenne(e.getNumEleve(), annee);
        String decision = moy > 12.0 ? "Admis 6ème" : (moy >= 9.75 ? "Réussi" : "Échoué");
        String nomEcole = ecoleDao.findById(e.getNumEcole()).map(Ecole::getDesign).orElse(e.getNumEcole());
        ResultatEleve r = new ResultatEleve();
        r.setNumEleve(e.getNumEleve());
        r.setNom(e.getNom());
        r.setPrenom(e.getPrenom());
        r.setNomEcole(nomEcole);
        r.setMoyenne(moy);
        r.setDecision(decision);
        r.setAdmisSixieme(moy > 12.0);
        return r;
    }
}
