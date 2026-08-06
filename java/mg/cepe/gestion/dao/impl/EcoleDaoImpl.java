package mg.cepe.gestion.dao.impl;

import mg.cepe.gestion.config.DatabaseConfig;
import mg.cepe.gestion.dao.EcoleDao;
import mg.cepe.gestion.exception.DatabaseException;
import mg.cepe.gestion.model.Ecole;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EcoleDaoImpl implements EcoleDao {
    @Override public void save(Ecole e) {
        String sql="INSERT INTO ecole (numEcole,design,adresse) VALUES (?,?,?)";
        try(Connection c=DatabaseConfig.getDataSource().getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,e.getNumEcole());ps.setString(2,e.getDesign());ps.setString(3,e.getAdresse());ps.executeUpdate();
        }catch(SQLException ex){throw new DatabaseException("Erreur création école",ex);}
    }
    @Override public void update(Ecole e) {
        String sql="UPDATE ecole SET design=?,adresse=? WHERE numEcole=?";
        try(Connection c=DatabaseConfig.getDataSource().getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,e.getDesign());ps.setString(2,e.getAdresse());ps.setString(3,e.getNumEcole());ps.executeUpdate();
        }catch(SQLException ex){throw new DatabaseException("Erreur modif école",ex);}
    }
    @Override public void delete(String numEcole) {
        String sql="DELETE FROM ecole WHERE numEcole=?";
        try(Connection c=DatabaseConfig.getDataSource().getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,numEcole);ps.executeUpdate();
        }catch(SQLException ex){throw new DatabaseException("Erreur suppression école",ex);}
    }
    @Override public Optional<Ecole> findById(String numEcole) {
        String sql="SELECT * FROM ecole WHERE numEcole=?";
        try(Connection c=DatabaseConfig.getDataSource().getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,numEcole);ResultSet rs=ps.executeQuery();if(rs.next())return Optional.of(map(rs));
        }catch(SQLException ex){throw new DatabaseException("Erreur recherche école",ex);}return Optional.empty();
    }
    @Override public List<Ecole> findAll() {
        String sql="SELECT * FROM ecole ORDER BY numecole DESC";List<Ecole>list=new ArrayList<>();
        try(Connection c=DatabaseConfig.getDataSource().getConnection();Statement st=c.createStatement();ResultSet rs=st.executeQuery(sql)){
            while(rs.next())list.add(map(rs));
        }catch(SQLException ex){throw new DatabaseException("Erreur listage écoles",ex);}return list;
    }
    private Ecole map(ResultSet rs)throws SQLException{Ecole e=new Ecole();e.setNumEcole(rs.getString("numEcole"));e.setDesign(rs.getString("design"));e.setAdresse(rs.getString("adresse"));return e;}
}
