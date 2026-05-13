package uz.itpu.pt1.stax;

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

class EmployeeStaxParserTest {

    @Test
    void shouldParseEmployeesFromNamespacedResourceAndExposeStaxFeatures() {
        // Arrange
        EmployeeStaxParser parser = new EmployeeStaxParser();

        // Act
        StaxParseResult result = parser.parseFromResource("employee-example-ns.xml");
        List<Employee> employees = result.getEmployees();

        // Assert
        assertEquals(3, employees.size());
        assertTrue(result.getCdataSections() >= 0);
        assertTrue(result.getStartElements() > 0);
        assertEquals(result.getStartElements(), result.getEndElements());
        assertTrue(result.getCharacterEvents() > 0);
        assertEquals(101, employees.get(0).getId());
        assertEquals("Qa", employees.get(1).getDepartment());
        assertTrue(employees.get(2).getSkills().contains("Docker"));
    }

    @Test
    void shouldCreateSeparateUpdatedXmlWithNewEmployee() throws Exception {
        // Arrange
        EmployeeStaxParser parser = new EmployeeStaxParser();
        Employee newEmployee = new Employee(
                184,
                "Dilshod",
                "Tursunov",
                true,
                new BigDecimal("9100.00"),
                "Platform",
                List.of("Java", "Kafka", "PostgreSQL"),
                new Address("Nukus", "Uzbekistan", "230100")
        );
        Path outputPath = Path.of("src", "main", "resources", "employee-example-stax-updated.xml")
                .toAbsolutePath()
                .normalize();
        Files.deleteIfExists(outputPath);

        try {
            // Act
            Path createdPath = parser.createUpdatedXmlFromResource(
                    "employee-example-ns.xml",
                    "employee-example-stax-updated.xml",
                    newEmployee
            );

            // Assert
            assertEquals(outputPath, createdPath);
            assertTrue(Files.exists(createdPath));

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document document = factory.newDocumentBuilder().parse(createdPath.toFile());

            NodeList employeeNodes = document.getElementsByTagNameNS("*", "employee");
            assertEquals(4, employeeNodes.getLength());

            Element addedEmployee = (Element) employeeNodes.item(employeeNodes.getLength() - 1);
            assertEquals("184", addedEmployee.getAttribute("id"));
            assertEquals(
                    "Dilshod",
                    addedEmployee.getElementsByTagNameNS("*", "firstName").item(0).getTextContent().trim()
            );
            assertEquals(
                    "Nukus",
                    addedEmployee.getElementsByTagNameNS("*", "city").item(0).getTextContent().trim()
            );
        } finally {
//            Files.deleteIfExists(outputPath);
        }
    }
}

