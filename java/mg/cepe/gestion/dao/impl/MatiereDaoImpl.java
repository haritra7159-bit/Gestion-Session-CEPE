package mg.cepe.gestion.dao.impl;

import mg.cepe.gestion.config.DatabaseConfig;
import mg.cepe.gestion.dao.MatiereDao;
import mg.cepe.gestion.exception.DatabaseException;
import mg.cepe.gestion.model.Matiere;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MatiereDaoImpl implements MatiereDao {
    @Override public void save(Matiere m){String sql="INSERT INTO matiere (numMat,designMat,coef) VALUES (?,?,?)";try(Connection c=DatabaseConfig.getDataSource().getConnection();PreparedStatement ps=c.prepareStatement(sql)){ps.setString(1,m.getNumMat());ps.setString(2,m.getDesignMat());ps.setInt(3,m.getCoef());ps.executeUpdate();}catch(SQLException e){throw new DatabaseException("Erreur création matière",e);}}
    @Override public void update(Matiere m){String sql="UPDATE matiere SET designMat=?,coef=? WHERE numMat=?";try(Connection c=DatabaseConfig.getDataSource().getConnection();PreparedStatement ps=c.prepareStatement(sql)){ps.setString(1,m.getDesignMat());ps.setInt(2,m.getCoef());ps.setString(3,m.getNumMat());ps.executeUpdate();}catch(SQLException e){throw new DatabaseException("Erreur modif matière",e);}}
    @Override public void delete(String numMat){String sql="DELETE FROM matiere WHERE numMat=?";try(Connection c=DatabaseConfig.getDataSource().getConnection();PreparedStatement ps=c.prepareStatement(sql)){ps.setString(1,numMat);ps.executeUpdate();}catch(SQLException e){throw new DatabaseException("Erreur suppression matière",e);}}
    @Override public Optional<Matiere> findById(String numMat){String sql="SELECT * FROM matiere WHERE numMat=?";try(Connection c=DatabaseConfig.getDataSource().getConnection();PreparedStatement ps=c.prepareStatement(sql)){ps.setString(1,numMat);ResultSet rs=ps.executeQuery();if(rs.next())return Optional.of(map(rs));}catch(SQLException e){throw new DatabaseException("Erreur recherche matière",e);}return Optional.empty();}
    @Override public List<Matiere> findAll(){String sql="SELECT * FROM matiere ORDER BY nummat DESC";List<Matiere>list=new ArrayList<>();try(Connection c=DatabaseConfig.getDataSource().getConnection();Statement st=c.createStatement();ResultSet rs=st.executeQuery(sql)){while(rs.next())list.add(map(rs));}catch(SQLException e){throw new DatabaseException("Erreur listage matières",e);}return list;}
    private Matiere map(ResultSet rs)throws SQLException{Matiere m=new Matiere();m.setNumMat(rs.getString("numMat"));m.setDesignMat(rs.getString("designMat"));m.setCoef(rs.getInt("coef"));return m;}
}
