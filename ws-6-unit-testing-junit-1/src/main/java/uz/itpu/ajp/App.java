package uz.itpu.ajp;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.Properties;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        // Example: load a properties file from src/main/resources
        Properties properties = loadPropertiesFromResources("app.properties");
        System.out.println("app.name=" + properties.getProperty("app.name"));
    }

    /**
     * Loads a {@link Properties} file from the application's classpath (typically from {@code src/main/resources}).
     *
     * @param resourceName resource path relative to the classpath root (e.g. {@code "app.properties"} or
     *                     {@code "config/app.properties"})
     * @return loaded properties
     * @throws IllegalArgumentException if the resource doesn't exist
     * @throws UncheckedIOException     if the resource can't be read
     */
    public static Properties loadPropertiesFromResources(String resourceName) {
        Objects.requireNonNull(resourceName, "resourceName must not be null");

        Properties properties = new Properties();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(resourceName)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource not found on classpath: " + resourceName);
            }

            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load properties from resource: " + resourceName, e);
        }
    }
}
