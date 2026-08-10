package mg.cepe.gestion.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class DatabaseConfigTest {

    @Test
    void shouldPreferCepeEnvironmentVariablesOverDefaults() {
        System.setProperty("CEPE_DB_URL", "jdbc:postgresql://localhost:5432/cepe_test");
        System.setProperty("CEPE_DB_USER", "cepe_user");
        System.setProperty("CEPE_DB_PASSWORD", "mdp1706");

        assertEquals("jdbc:postgresql://localhost:5432/cepe_test",
                DatabaseConfig.resolveProperty("db.url", "jdbc:postgresql://localhost:5432/cepe"));
        assertEquals("cepe_user",
                DatabaseConfig.resolveProperty("db.user", "postgres"));
        assertEquals("mdp1706",
                DatabaseConfig.resolveProperty("db.password", "postgres"));
    }
}
