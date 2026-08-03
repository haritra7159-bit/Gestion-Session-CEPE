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

    @Override
    public void save(Ecole ecole) {
        String sql = "INSERT INTO ecole (numEcole, design, adresse) VALUES (?, ?, ?)";
        try (Connection c = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, ecole.getNumEcole());
            ps.setString(2, ecole.getDesign());
            ps.setString(3, ecole.getAdresse());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur création école : " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Ecole ecole) {
        String sql = "UPDATE ecole SET design = ?, adresse = ? WHERE numEcole = ?";
        try (Connection c = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, ecole.getDesign());
            ps.setString(2, ecole.getAdresse());
            ps.setString(3, ecole.getNumEcole());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur modification école : " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String numEcole) {
        String sql = "DELETE FROM ecole WHERE numEcole = ?";
        try (Connection c = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, numEcole);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur suppression école : " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Ecole> findById(String numEcole) {
        String sql = "SELECT * FROM ecole WHERE numEcole = ?";
        try (Connection c = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, numEcole);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(map(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Erreur recherche école : " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Ecole> findAll() {
        String sql = "SELECT * FROM ecole ORDER BY design";
        List<Ecole> list = new ArrayList<>();
        try (Connection c = DatabaseConfig.getDataSource().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Erreur listage écoles : " + e.getMessage(), e);
        }
        return list;
    }

    private Ecole map(ResultSet rs) throws SQLException {
        Ecole e = new Ecole();
        e.setNumEcole(rs.getString("numEcole"));
        e.setDesign(rs.getString("design"));
        e.setAdresse(rs.getString("adresse"));
        return e;
    }
}
