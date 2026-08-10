package mg.cepe.gestion.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public final class DatabaseConfig {
    private static final HikariDataSource DATA_SOURCE;
    static {
        Properties props = new Properties();
        try (InputStream is = DatabaseConfig.class.getResourceAsStream("/application.properties")) {
            if (is == null)
                throw new RuntimeException("application.properties introuvable dans resources/");
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Erreur chargement properties", e);
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(firstNonBlank(
                System.getenv("CEPE_DB_URL"),
                System.getProperty("db.url"),
                props.getProperty("db.url"),
                "jdbc:postgresql://localhost:5432/cepe"));
        config.setUsername(firstNonBlank(
                System.getenv("CEPE_DB_USER"),
                System.getProperty("db.user"),
                props.getProperty("db.user"),
                "postgres"));
        config.setPassword(firstNonBlank(
                System.getenv("CEPE_DB_PASSWORD"),
                System.getProperty("db.password"),
                props.getProperty("db.password"),
                "postgres"));
        config.setDriverClassName("org.postgresql.Driver");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        DATA_SOURCE = new HikariDataSource(config);
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }

    public static String resolveProperty(String key, String defaultValue) {
        String aliasKey = "CEPE_" + key.replace('.', '_').replace('-', '_').toUpperCase();

        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }

        String envValue = System.getenv(aliasKey);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }

        String aliasSystemValue = System.getProperty(aliasKey);
        if (aliasSystemValue != null && !aliasSystemValue.isBlank()) {
            return aliasSystemValue;
        }

        return defaultValue;
    }

    private DatabaseConfig() {
    }

    public static DataSource getDataSource() {
        return DATA_SOURCE;
    }
}
