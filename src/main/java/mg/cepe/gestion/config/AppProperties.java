package mg.cepe.gestion.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Properties;

/** Charge application.properties puis surcharge avec application-local.properties. */
public final class AppProperties {

    private static final String DEFAULT = "/application.properties";
    private static final String LOCAL = "/application-local.properties";

    private final Properties properties = new Properties();

    public AppProperties() {
        loadFromClasspath(DEFAULT);
        loadFromClasspath(LOCAL);
        loadFromWorkingDirectory("application-local.properties");
    }

    private void loadFromClasspath(String resource) {
        try (InputStream in = AppProperties.class.getResourceAsStream(resource)) {
            if (in != null) {
                properties.load(in);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Impossible de lire " + resource, e);
        }
    }

    private void loadFromWorkingDirectory(String fileName) {
        Path path = Path.of(fileName);
        if (!Files.isRegularFile(path)) {
            return;
        }
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch (IOException e) {
            throw new IllegalStateException("Impossible de lire " + fileName, e);
        }
    }

    public String get(String key) {
        return Objects.requireNonNull(properties.getProperty(key), "Propriété manquante: " + key);
    }

    public String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return Integer.parseInt(value.trim());
    }
}
