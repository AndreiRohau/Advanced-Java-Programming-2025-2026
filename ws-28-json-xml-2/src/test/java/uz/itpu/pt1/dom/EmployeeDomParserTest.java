package uz.itpu.pt1.dom;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import uz.itpu.model.Address;
import uz.itpu.model.Employee;

import javax.xml.parsers.DocumentBuilderFactory;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmployeeDomParserTest {

    @Test
    void shouldParseEmployeesFromResourceAndExposeDomFeatures() {
        // Arrange
        EmployeeDomParser parser = new EmployeeDomParser();

        // Act
        DomParseResult result = parser.parseFromResource("employee-example.xml");
        List<Employee> employees = result.getEmployees();

        // Assert
        assertEquals(3, employees.size());
        assertEquals(3, result.getCdataSections());
        assertTrue(result.getElementNodes() > 0);
        assertTrue(result.getTextNodes() > 0);
        assertEquals(101, employees.get(0).getId());
        assertEquals("Qa", employees.get(1).getDepartment());
        assertEquals("Bukhara", employees.get(2).getAddress().getCity());
    }

    @Test
    void shouldParseEmployeesFromNamespacedResourceAndExposeDomFeatures() {
        // Arrange
        EmployeeDomParser parser = new EmployeeDomParser();

        // Act
        DomParseResult result = parser.parseFromResource("employee-example-ns.xml");
        List<Employee> employees = result.getEmployees();

        // Assert
        assertEquals(3, employees.size());
        assertEquals(3, result.getCdataSections());
        assertTrue(result.getElementNodes() > 0);
        assertTrue(result.getTextNodes() > 0);
        assertEquals(101, employees.get(0).getId());
        assertEquals("Qa", employees.get(1).getDepartment());
        assertEquals("Bukhara", employees.get(2).getAddress().getCity());
    }

    @Test
    void shouldCreateSeparateUpdatedXmlWithNewEmployee() throws Exception {
        // Arrange
        EmployeeDomParser parser = new EmployeeDomParser();
        Employee newEmployee = new Employee(
                134,
                "Dilshod",
                "Tursunov",
                true,
                new BigDecimal("9100.00"),
                "Platform",
                List.of("Java", "Kafka", "PostgreSQL"),
                new Address("Nukus", "Uzbekistan", "230100")
        );
        Path outputPath = Path.of("src", "main", "resources", "employee-example-dom-updated.xml")
                .toAbsolutePath()
                .normalize();
        Files.deleteIfExists(outputPath);

        try {
            // Act
            Path createdPath = parser.createUpdatedXmlFromResource(
                    "employee-example.xml",
                    "employee-example-dom-updated.xml",
                    newEmployee
            );

            // Assert
            assertEquals(outputPath, createdPath);
            assertTrue(Files.exists(createdPath));

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document document = factory.newDocumentBuilder().parse(createdPath.toFile());

            NodeList employeeNodes = document.getElementsByTagName("employee");
            assertEquals(4, employeeNodes.getLength());

            Element addedEmployee = (Element) employeeNodes.item(employeeNodes.getLength() - 1);
            assertEquals("134", addedEmployee.getAttribute("id"));
            assertEquals("Dilshod", addedEmployee.getElementsByTagName("firstName").item(0).getTextContent().trim());
            assertEquals("Nukus", addedEmployee.getElementsByTagName("city").item(0).getTextContent().trim());
        } finally {
//            Files.deleteIfExists(outputPath);
        }
    }
}

