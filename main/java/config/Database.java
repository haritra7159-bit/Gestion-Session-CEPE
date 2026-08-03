package mg.cepe.gestion.config;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/** Pool de connexions HikariCP partagé par toute l'application. */
public final class Database {

    private static final Logger LOG = LoggerFactory.getLogger(Database.class);
    private static Database instance;

    private final HikariDataSource dataSource;

    private Database(AppProperties props) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(props.get("db.url"));
        config.setUsername(props.get("db.user"));
        config.setPassword(props.get("db.password", ""));
        config.setMaximumPoolSize(props.getInt("db.pool.size", 5));
        config.setPoolName("cepe-pool");
        config.addDataSourceProperty("ApplicationName", "gestion-session-cepe");
        this.dataSource = new HikariDataSource(config);
        LOG.info("Pool HikariCP initialisé ({})", props.get("db.url"));
    }

    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database(new AppProperties());
        }
        return instance;
    }

    public static synchronized void resetForTests() {
        if (instance != null) {
            instance.dataSource.close();
            instance = null;
        }
    }

    public DataSource dataSource() {
        return dataSource;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            LOG.info("Pool HikariCP fermé");
        }
    }
}
