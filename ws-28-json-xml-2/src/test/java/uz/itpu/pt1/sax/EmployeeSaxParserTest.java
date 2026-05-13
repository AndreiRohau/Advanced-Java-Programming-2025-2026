package uz.itpu.pt1.sax;

import org.junit.jupiter.api.Test;
import uz.itpu.model.Employee;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmployeeSaxParserTest {

    @Test
    void shouldParseEmployeesFromResourceAndCaptureCDataSections() {
        // Arrange
        EmployeeSaxParser parser = new EmployeeSaxParser();

        // Act
        SaxParseResult result = parser.parseFromResource("employee-example.xml");
        List<Employee> employees = result.getEmployees();

        // Assert
        assertEquals(3, employees.size());
        assertEquals(3, result.getCdataSections());
        assertEquals(101, employees.get(0).getId());
        assertEquals("Andrei", employees.get(0).getFirstName());
        assertEquals("Qa", employees.get(1).getDepartment());
        assertTrue(employees.get(2).getSkills().contains("Kubernetes"));
    }

    @Test
    void shouldParseEmployeesFromNamespacedResourceAndCaptureCDataSections() {
        // Arrange
        EmployeeSaxParser parser = new EmployeeSaxParser();

        // Act
        SaxParseResult result = parser.parseFromResource("employee-example-ns.xml");
        List<Employee> employees = result.getEmployees();

        // Assert
        assertEquals(3, employees.size());
        assertEquals(3, result.getCdataSections());
        assertEquals(101, employees.get(0).getId());
        assertEquals("Andrei", employees.get(0).getFirstName());
        assertEquals("Qa", employees.get(1).getDepartment());
        assertTrue(employees.get(2).getSkills().contains("Kubernetes"));
    }
}

