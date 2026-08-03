package mg.cepe.gestion.service.impl;

import mg.cepe.gestion.config.DatabaseConfig;
import mg.cepe.gestion.dao.EleveDao;
import mg.cepe.gestion.dao.MatiereDao;
import mg.cepe.gestion.dao.NoteDao;
import mg.cepe.gestion.dao.impl.EleveDaoImpl;
import mg.cepe.gestion.dao.impl.MatiereDaoImpl;
import mg.cepe.gestion.dao.impl.NoteDaoImpl;
import mg.cepe.gestion.model.Eleve;
import mg.cepe.gestion.model.Matiere;
import mg.cepe.gestion.model.Note;
import mg.cepe.gestion.model.ResultatEleve;
import mg.cepe.gestion.service.DeliberationService;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class DeliberationServiceImpl implements DeliberationService {
    private final NoteDao noteDao=new NoteDaoImpl();
    private final EleveDao eleveDao=new EleveDaoImpl();
    private final MatiereDao matiereDao=new MatiereDaoImpl();

    @Override public double calculerMoyenne(String numEleve,String annee){
        List<Note> notes=noteDao.findByEleveAndAnnee(numEleve,annee);if(notes.isEmpty())return 0.0;
        Map<String,Integer> coefs=matiereDao.findAll().stream().collect(Collectors.toMap(Matiere::getNumMat,Matiere::getCoef));
        double tp=0;int tc=0;for(Note n:notes){int c=coefs.getOrDefault(n.getNumMat(),1);tp+=n.getNote()*c;tc+=c;}
        return tc==0?0.0:tp/tc;
    }
    @Override public List<ResultatEleve> deliberer(String annee){
        List<Eleve> eleves=eleveDao.findAll();List<ResultatEleve> res=new ArrayList<>();
        for(Eleve e:eleves)res.add(buildResultat(e,annee));return res;
    }
    @Override public List<ResultatEleve> listerReussis(String annee){return deliberer(annee).stream().filter(r->r.getMoyenne()>=9.75).sorted(Comparator.comparingDouble(ResultatEleve::getMoyenne).reversed()).collect(Collectors.toList());}
    @Override public List<ResultatEleve> listerEchoues(String annee){return deliberer(annee).stream().filter(r->r.getMoyenne()<9.75).sorted(Comparator.comparingDouble(ResultatEleve::getMoyenne).reversed()).collect(Collectors.toList());}
    @Override public List<ResultatEleve> listerAdmisSixieme(String annee){return deliberer(annee).stream().filter(r->r.getMoyenne()>12.0).sorted(Comparator.comparingDouble(ResultatEleve::getMoyenne).reversed()).collect(Collectors.toList());}
    @Override public List<ResultatEleve> classementParMerite(String annee){return listerReussis(annee);}

    private ResultatEleve buildResultat(Eleve e,String annee){
        List<Note> notes=noteDao.findByEleveAndAnnee(e.getNumEleve(),annee);
        Map<String,Integer> coefs=matiereDao.findAll().stream().collect(Collectors.toMap(Matiere::getNumMat,Matiere::getCoef));
        double tp=0;int tc=0;for(Note n:notes){int c=coefs.getOrDefault(n.getNumMat(),1);tp+=n.getNote()*c;tc+=c;}
        double moy=tc==0?0.0:tp/tc;String dec=moy>=9.75?"Réussi":"Échoué";
        ResultatEleve r=new ResultatEleve();r.setNumEleve(e.getNumEleve());r.setNom(e.getNom());r.setPrenom(e.getPrenom());r.setMoyenne(moy);r.setTotalPondere(tp);r.setTotalCoef(tc);r.setDecision(dec);r.setAdmisSixieme(moy>12.0);
        try{String sql="SELECT design FROM ecole WHERE numEcole=?";try(Connection c=DatabaseConfig.getDataSource().getConnection();PreparedStatement ps=c.prepareStatement(sql)){ps.setString(1,e.getNumEcole());ResultSet rs=ps.executeQuery();if(rs.next())r.setNomEcole(rs.getString("design"));}}catch(SQLException ex){r.setNomEcole("Inconnue");}return r;
    }
}
