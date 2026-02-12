package uz.itpu.ajp;

import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test for simple App.
 */
public class AppTest {

    /**
     * Rigorous Test :-)
     */
    @Test
    public void testExample_positive() {
        assertTrue(true);
    }

    @Test
    void testLoadPropertiesFromResources_positive() {
        // Arrange
        String resourceName = "app.properties";

        // Act
        Properties properties = App.loadPropertiesFromResources(resourceName);

        // Assert
        assertNotNull(properties);
        assertEquals("ws-6", properties.getProperty("app.name"));
    }

    @Test
    void testLoadPropertiesFromResources_negative_resourceIsMissing() {
        // Arrange
        String resourceName = "missing.properties";

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> App.loadPropertiesFromResources(resourceName));
    }
}
