package mg.cepe.gestion.service.impl;

import mg.cepe.gestion.config.DatabaseConfig;
import mg.cepe.gestion.dao.EleveDao;
import mg.cepe.gestion.dao.MatiereDao;
import mg.cepe.gestion.dao.NoteDao;
import mg.cepe.gestion.dao.impl.EleveDaoImpl;
import mg.cepe.gestion.dao.impl.MatiereDaoImpl;
import mg.cepe.gestion.dao.impl.NoteDaoImpl;
import mg.cepe.gestion.exception.DatabaseException;
import mg.cepe.gestion.model.Eleve;
import mg.cepe.gestion.model.Matiere;
import mg.cepe.gestion.model.Note;
import mg.cepe.gestion.model.ResultatEleve;
import mg.cepe.gestion.service.DeliberationService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DeliberationServiceImpl implements DeliberationService {

    private final NoteDao noteDao = new NoteDaoImpl();
    private final EleveDao eleveDao = new EleveDaoImpl();
    private final MatiereDao matiereDao = new MatiereDaoImpl();

    @Override
    public double calculerMoyenne(String numEleve, String anneeScolaire) {
        List<Note> notes = noteDao.findByEleveAndAnnee(numEleve, anneeScolaire);
        if (notes.isEmpty()) return 0.0;

        Map<String, Integer> coefs = matiereDao.findAll().stream()
                .collect(Collectors.toMap(Matiere::getNumMat, Matiere::getCoef));

        double totalPondere = 0;
        int totalCoef = 0;
        for (Note n : notes) {
            int c = coefs.getOrDefault(n.getNumMat(), 1);
            totalPondere += n.getNote() * c;
            totalCoef += c;
        }
        return totalCoef == 0 ? 0.0 : totalPondere / totalCoef;
    }

    @Override
    public List<ResultatEleve> deliberer(String anneeScolaire) {
        List<Eleve> eleves = eleveDao.findAll();
        List<ResultatEleve> resultats = new ArrayList<>();
        for (Eleve e : eleves) {
            resultats.add(buildResultat(e, anneeScolaire));
        }
        return resultats;
    }

    @Override
    public List<ResultatEleve> listerReussis(String anneeScolaire) {
        return deliberer(anneeScolaire).stream()
                .filter(r -> r.getMoyenne() >= 9.75)
                .sorted(Comparator.comparingDouble(ResultatEleve::getMoyenne).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<ResultatEleve> listerEchoues(String anneeScolaire) {
        return deliberer(anneeScolaire).stream()
                .filter(r -> r.getMoyenne() < 9.75)
                .sorted(Comparator.comparingDouble(ResultatEleve::getMoyenne).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<ResultatEleve> listerAdmisSixieme(String anneeScolaire) {
        return deliberer(anneeScolaire).stream()
                .filter(r -> r.getMoyenne() > 12.0)
                .sorted(Comparator.comparingDouble(ResultatEleve::getMoyenne).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<ResultatEleve> classementParMerite(String anneeScolaire) {
        return deliberer(anneeScolaire).stream()
                .filter(r -> r.getMoyenne() >= 9.75)
                .sorted(Comparator.comparingDouble(ResultatEleve::getMoyenne).reversed())
                .collect(Collectors.toList());
    }

    private ResultatEleve buildResultat(Eleve e, String annee) {
        List<Note> notes = noteDao.findByEleveAndAnnee(e.getNumEleve(), annee);
        Map<String, Integer> coefs = matiereDao.findAll().stream()
                .collect(Collectors.toMap(Matiere::getNumMat, Matiere::getCoef));

        double totalPondere = 0;
        int totalCoef = 0;
        for (Note n : notes) {
            int c = coefs.getOrDefault(n.getNumMat(), 1);
            totalPondere += n.getNote() * c;
            totalCoef += c;
        }

        double moyenne = totalCoef == 0 ? 0.0 : totalPondere / totalCoef;
        String decision = moyenne >= 9.75 ? "Réussi" : "Échoué";

        ResultatEleve r = new ResultatEleve();
        r.setNumEleve(e.getNumEleve());
        r.setNom(e.getNom());
        r.setPrenom(e.getPrenom());
        r.setMoyenne(moyenne);
        r.setTotalPondere(totalPondere);
        r.setTotalCoef(totalCoef);
        r.setDecision(decision);
        r.setAdmisSixieme(moyenne > 12.0);

        try {
            String sql = "SELECT design FROM ecole WHERE numEcole = ?";
            try (Connection c = DatabaseConfig.getDataSource().getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, e.getNumEcole());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) r.setNomEcole(rs.getString("design"));
            }
        } catch (SQLException ex) {
            r.setNomEcole("Inconnue");
        }
        return r;
    }
}
