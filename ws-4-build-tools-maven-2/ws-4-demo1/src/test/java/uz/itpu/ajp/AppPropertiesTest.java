package uz.itpu.ajp;

import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class AppPropertiesTest {

    @Test
    void shouldLoadPropertiesFromResources() {
        // Arrange
        String resourceName = "app.properties";

        // Act
        Properties properties = App.loadPropertiesFromResources(resourceName);

        // Assert
        assertNotNull(properties);
        assertEquals("ws-4-demo1", properties.getProperty("app.name"));
    }

    @Test
    void shouldThrowWhenResourceIsMissing() {
        // Arrange
        String resourceName = "missing.properties";

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> App.loadPropertiesFromResources(resourceName));
    }
}
