package mg.cepe.gestion.dao.impl;

import mg.cepe.gestion.config.DatabaseConfig;
import mg.cepe.gestion.dao.EleveDao;
import mg.cepe.gestion.exception.DatabaseException;
import mg.cepe.gestion.model.Eleve;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EleveDaoImpl implements EleveDao {
    @Override public void save(Eleve e) {
        String sql="INSERT INTO eleve (numEleve,numEcole,nom,prenom,date_naissance) VALUES (?,?,?,?,?)";
        try(Connection c=DatabaseConfig.getDataSource().getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,e.getNumEleve());ps.setString(2,e.getNumEcole());ps.setString(3,e.getNom());ps.setString(4,e.getPrenom());ps.setDate(5,Date.valueOf(e.getDateNaissance()));ps.executeUpdate();
        }catch(SQLException ex){throw new DatabaseException("Erreur création élève",ex);}
    }
    @Override public void update(Eleve e) {
        String sql="UPDATE eleve SET numEcole=?,nom=?,prenom=?,date_naissance=? WHERE numEleve=?";
        try(Connection c=DatabaseConfig.getDataSource().getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,e.getNumEcole());ps.setString(2,e.getNom());ps.setString(3,e.getPrenom());ps.setDate(4,Date.valueOf(e.getDateNaissance()));ps.setString(5,e.getNumEleve());ps.executeUpdate();
        }catch(SQLException ex){throw new DatabaseException("Erreur modif élève",ex);}
    }
    @Override public void delete(String numEleve) {
        String sql="DELETE FROM eleve WHERE numEleve=?";
        try(Connection c=DatabaseConfig.getDataSource().getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,numEleve);ps.executeUpdate();
        }catch(SQLException ex){throw new DatabaseException("Erreur suppression élève",ex);}
    }
    @Override public Optional<Eleve> findById(String numEleve) {
        String sql="SELECT * FROM eleve WHERE numEleve=?";
        try(Connection c=DatabaseConfig.getDataSource().getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,numEleve);ResultSet rs=ps.executeQuery();if(rs.next())return Optional.of(map(rs));
        }catch(SQLException ex){throw new DatabaseException("Erreur recherche élève",ex);}return Optional.empty();
    }
    @Override public List<Eleve> findAll() {
        String sql="SELECT * FROM eleve ORDER BY numeleve DESC";List<Eleve>list=new ArrayList<>();
        try(Connection c=DatabaseConfig.getDataSource().getConnection();Statement st=c.createStatement();ResultSet rs=st.executeQuery(sql)){while(rs.next())list.add(map(rs));}
        catch(SQLException ex){throw new DatabaseException("Erreur listage élèves",ex);}return list;
    }
    @Override public List<Eleve> searchByNomOrPrenom(String critere) {
        String sql="SELECT * FROM eleve WHERE nom ILIKE ? OR prenom ILIKE ? ORDER BY numeleve DESC";List<Eleve>list=new ArrayList<>();
        try(Connection c=DatabaseConfig.getDataSource().getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            String like="%"+critere+"%";ps.setString(1,like);ps.setString(2,like);ResultSet rs=ps.executeQuery();while(rs.next())list.add(map(rs));
        }catch(SQLException ex){throw new DatabaseException("Erreur recherche LIKE",ex);}return list;
    }
    private Eleve map(ResultSet rs)throws SQLException{Eleve e=new Eleve();e.setNumEleve(rs.getString("numEleve"));e.setNumEcole(rs.getString("numEcole"));e.setNom(rs.getString("nom"));e.setPrenom(rs.getString("prenom"));e.setDateNaissance(rs.getDate("date_naissance").toLocalDate());return e;}
}
