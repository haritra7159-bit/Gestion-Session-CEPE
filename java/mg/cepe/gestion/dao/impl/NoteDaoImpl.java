package mg.cepe.gestion.dao.impl;

import mg.cepe.gestion.config.DatabaseConfig;
import mg.cepe.gestion.dao.NoteDao;
import mg.cepe.gestion.exception.DatabaseException;
import mg.cepe.gestion.model.Note;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NoteDaoImpl implements NoteDao {
    @Override public void save(Note n){String sql="INSERT INTO note (annee_scolaire,numEleve,numMat,note) VALUES (?,?,?,?)";try(Connection c=DatabaseConfig.getDataSource().getConnection();PreparedStatement ps=c.prepareStatement(sql)){ps.setString(1,n.getAnneeScolaire());ps.setString(2,n.getNumEleve());ps.setString(3,n.getNumMat());ps.setDouble(4,n.getNote());ps.executeUpdate();}catch(SQLException e){throw new DatabaseException("Erreur création note",e);}}
    @Override public void update(Note n){String sql="UPDATE note SET note=? WHERE annee_scolaire=? AND numEleve=? AND numMat=?";try(Connection c=DatabaseConfig.getDataSource().getConnection();PreparedStatement ps=c.prepareStatement(sql)){ps.setDouble(1,n.getNote());ps.setString(2,n.getAnneeScolaire());ps.setString(3,n.getNumEleve());ps.setString(4,n.getNumMat());ps.executeUpdate();}catch(SQLException e){throw new DatabaseException("Erreur modif note",e);}}
    @Override public void delete(String annee,String numEleve,String numMat){String sql="DELETE FROM note WHERE annee_scolaire=? AND numEleve=? AND numMat=?";try(Connection c=DatabaseConfig.getDataSource().getConnection();PreparedStatement ps=c.prepareStatement(sql)){ps.setString(1,annee);ps.setString(2,numEleve);ps.setString(3,numMat);ps.executeUpdate();}catch(SQLException e){throw new DatabaseException("Erreur suppression note",e);}}
    @Override public Optional<Note> findById(String annee,String numEleve,String numMat){String sql="SELECT * FROM note WHERE annee_scolaire=? AND numEleve=? AND numMat=?";try(Connection c=DatabaseConfig.getDataSource().getConnection();PreparedStatement ps=c.prepareStatement(sql)){ps.setString(1,annee);ps.setString(2,numEleve);ps.setString(3,numMat);ResultSet rs=ps.executeQuery();if(rs.next())return Optional.of(map(rs));}catch(SQLException e){throw new DatabaseException("Erreur recherche note",e);}return Optional.empty();}
    @Override public List<Note> findAll(){String sql="SELECT * FROM note ORDER BY annee_scolaire,numEleve,numMat";List<Note>list=new ArrayList<>();try(Connection c=DatabaseConfig.getDataSource().getConnection();Statement st=c.createStatement();ResultSet rs=st.executeQuery(sql)){while(rs.next())list.add(map(rs));}catch(SQLException e){throw new DatabaseException("Erreur listage notes",e);}return list;}
    @Override public List<Note> findByEleveAndAnnee(String numEleve,String annee){String sql="SELECT * FROM note WHERE numEleve=? AND annee_scolaire=?";List<Note>list=new ArrayList<>();try(Connection c=DatabaseConfig.getDataSource().getConnection();PreparedStatement ps=c.prepareStatement(sql)){ps.setString(1,numEleve);ps.setString(2,annee);ResultSet rs=ps.executeQuery();while(rs.next())list.add(map(rs));}catch(SQLException e){throw new DatabaseException("Erreur notes élève",e);}return list;}
    @Override public List<Note> findByAnnee(String annee){String sql="SELECT * FROM note WHERE annee_scolaire=?";List<Note>list=new ArrayList<>();try(Connection c=DatabaseConfig.getDataSource().getConnection();PreparedStatement ps=c.prepareStatement(sql)){ps.setString(1,annee);ResultSet rs=ps.executeQuery();while(rs.next())list.add(map(rs));}catch(SQLException e){throw new DatabaseException("Erreur notes année",e);}return list;}
    private Note map(ResultSet rs)throws SQLException{Note n=new Note();n.setAnneeScolaire(rs.getString("annee_scolaire"));n.setNumEleve(rs.getString("numEleve"));n.setNumMat(rs.getString("numMat"));n.setNote(rs.getDouble("note"));return n;}
}
