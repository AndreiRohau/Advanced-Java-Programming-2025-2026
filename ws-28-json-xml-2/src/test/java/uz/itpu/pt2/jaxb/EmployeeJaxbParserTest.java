package uz.itpu.pt2.jaxb;

import org.junit.jupiter.api.Test;
import uz.itpu.model.Employee;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmployeeJaxbParserTest {

    @Test
    void shouldUnmarshalNamespacedEmployeesAndProvideMarshallingPreview() {
        // Arrange
        EmployeeJaxbParser parser = new EmployeeJaxbParser();

        // Act
        JaxbParseResult result = parser.parseFromResource("employee-example-ns.xml");
        List<Employee> employees = result.getEmployees();

        // Assert
        assertEquals(3, employees.size());
        assertEquals("employees", result.getRootElementName());
        assertEquals("http://itpu.uz/employee-ns", result.getNamespaceUri());
        assertTrue(result.getMarshalledXml().contains("employees"));
        assertEquals(101, employees.get(0).getId());
        assertEquals("Qa", employees.get(1).getDepartment());
        assertTrue(employees.get(2).getSkills().contains("Linux"));
    }
}

