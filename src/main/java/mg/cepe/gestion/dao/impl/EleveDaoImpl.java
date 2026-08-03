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

    @Override
    public void save(Eleve eleve) {
        String sql = "INSERT INTO eleve (numEleve, numEcole, nom, prenom, date_naissance) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, eleve.getNumEleve());
            ps.setString(2, eleve.getNumEcole());
            ps.setString(3, eleve.getNom());
            ps.setString(4, eleve.getPrenom());
            ps.setDate(5, Date.valueOf(eleve.getDateNaissance()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur création élève : " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Eleve eleve) {
        String sql = "UPDATE eleve SET numEcole = ?, nom = ?, prenom = ?, date_naissance = ? WHERE numEleve = ?";
        try (Connection c = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, eleve.getNumEcole());
            ps.setString(2, eleve.getNom());
            ps.setString(3, eleve.getPrenom());
            ps.setDate(4, Date.valueOf(eleve.getDateNaissance()));
            ps.setString(5, eleve.getNumEleve());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur modification élève : " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String numEleve) {
        String sql = "DELETE FROM eleve WHERE numEleve = ?";
        try (Connection c = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, numEleve);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur suppression élève : " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Eleve> findById(String numEleve) {
        String sql = "SELECT * FROM eleve WHERE numEleve = ?";
        try (Connection c = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, numEleve);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(map(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Erreur recherche élève : " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Eleve> findAll() {
        String sql = "SELECT * FROM eleve ORDER BY nom, prenom";
        List<Eleve> list = new ArrayList<>();
        try (Connection c = DatabaseConfig.getDataSource().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Erreur listage élèves : " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<Eleve> searchByNomOrPrenom(String critere) {
        String sql = "SELECT * FROM eleve WHERE nom ILIKE ? OR prenom ILIKE ? ORDER BY nom, prenom";
        List<Eleve> list = new ArrayList<>();
        try (Connection c = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            String like = "%" + critere + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Erreur recherche élève LIKE : " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<Eleve> findByEcole(String numEcole) {
        String sql = "SELECT * FROM eleve WHERE numEcole = ? ORDER BY nom, prenom";
        List<Eleve> list = new ArrayList<>();
        try (Connection c = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, numEcole);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Erreur listage élèves par école : " + e.getMessage(), e);
        }
        return list;
    }

    private Eleve map(ResultSet rs) throws SQLException {
        Eleve e = new Eleve();
        e.setNumEleve(rs.getString("numEleve"));
        e.setNumEcole(rs.getString("numEcole"));
        e.setNom(rs.getString("nom"));
        e.setPrenom(rs.getString("prenom"));
        e.setDateNaissance(rs.getDate("date_naissance").toLocalDate());
        return e;
    }
}
