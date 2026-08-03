package mg.cepe.gestion.dao.impl;

import mg.cepe.gestion.config.Database;
import mg.cepe.gestion.dao.EcoleDao;
import mg.cepe.gestion.entity.Ecole;
import mg.cepe.gestion.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class EcoleDaoJdbc implements EcoleDao {

    private static final String SELECT_ALL =
            "SELECT numecole AS \"numEcole\", design, adresse FROM ecole ORDER BY design";
    private static final String SELECT_BY_ID =
            "SELECT numecole AS \"numEcole\", design, adresse FROM ecole WHERE numecole = ?";
    private static final String INSERT =
            "INSERT INTO ecole (numecole, design, adresse) VALUES (?, ?, ?)";
    private static final String UPDATE =
            "UPDATE ecole SET design = ?, adresse = ? WHERE numecole = ?";
    private static final String DELETE =
            "DELETE FROM ecole WHERE numecole = ?";
    private static final String EXISTS =
            "SELECT 1 FROM ecole WHERE numecole = ?";

    private final Database database;

    public EcoleDaoJdbc() {
        this(Database.getInstance());
    }

    public EcoleDaoJdbc(Database database) {
        this.database = database;
    }

    @Override
    public List<Ecole> findAll() {
        List<Ecole> list = new ArrayList<>();
        try (Connection c = database.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la lecture des écoles", e);
        }
    }

    @Override
    public Optional<Ecole> findById(String numEcole) {
        try (Connection c = database.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BY_ID)) {
            ps.setString(1, numEcole);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la lecture de l'école " + numEcole, e);
        }
    }

    @Override
    public void insert(Ecole ecole) {
        try (Connection c = database.getConnection();
             PreparedStatement ps = c.prepareStatement(INSERT)) {
            ps.setString(1, ecole.getNumEcole());
            ps.setString(2, ecole.getDesign());
            ps.setString(3, ecole.getAdresse());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de l'insertion de l'école " + ecole.getNumEcole(), e);
        }
    }

    @Override
    public void update(Ecole ecole) {
        try (Connection c = database.getConnection();
             PreparedStatement ps = c.prepareStatement(UPDATE)) {
            ps.setString(1, ecole.getDesign());
            ps.setString(2, ecole.getAdresse());
            ps.setString(3, ecole.getNumEcole());
            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new DataAccessException("Aucune école mise à jour pour " + ecole.getNumEcole());
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la mise à jour de l'école " + ecole.getNumEcole(), e);
        }
    }

    @Override
    public void deleteById(String numEcole) {
        try (Connection c = database.getConnection();
             PreparedStatement ps = c.prepareStatement(DELETE)) {
            ps.setString(1, numEcole);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la suppression de l'école " + numEcole, e);
        }
    }

    @Override
    public boolean existsById(String numEcole) {
        try (Connection c = database.getConnection();
             PreparedStatement ps = c.prepareStatement(EXISTS)) {
            ps.setString(1, numEcole);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la vérification de l'école " + numEcole, e);
        }
    }

    private static Ecole map(ResultSet rs) throws SQLException {
        return new Ecole(
                rs.getString("numEcole"),
                rs.getString("design"),
                rs.getString("adresse")
        );
    }
}
