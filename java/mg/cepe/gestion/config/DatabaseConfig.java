package mg.cepe.gestion.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.sql.DataSource;

public final class DatabaseConfig {
    private static final HikariDataSource DATA_SOURCE;
    static {
        Properties props = new Properties();
        try (InputStream is = DatabaseConfig.class.getResourceAsStream("/application.properties")) {
            if (is == null) throw new RuntimeException("application.properties introuvable dans resources/");
            props.load(is);
        } catch (IOException e) { throw new RuntimeException("Erreur chargement properties", e); }
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(props.getProperty("db.url", "jdbc:postgresql://localhost:5432/cepe"));
        config.setUsername(props.getProperty("db.user", "postgres"));
        config.setPassword(props.getProperty("db.password", "postgres"));
        config.setDriverClassName("org.postgresql.Driver");
        config.setMaximumPoolSize(10); config.setMinimumIdle(2); config.setConnectionTimeout(30000);
        DATA_SOURCE = new HikariDataSource(config);
    }
    private DatabaseConfig() {}
    public static DataSource getDataSource() { return DATA_SOURCE; }
}
