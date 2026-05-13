package uz.itpu.pt2.jaxb;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import uz.itpu.model.Address;
import uz.itpu.model.Employee;
import uz.itpu.pt2.jaxb.xml.AddressXml;
import uz.itpu.pt2.jaxb.xml.EmployeeXml;
import uz.itpu.pt2.jaxb.xml.EmployeesXml;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * JAXB parser service for employee XML resources.
 */
public class EmployeeJaxbParser {

    /**
     * Unmarshals XML from resources and maps it to the shared domain model.
     *
     * @param resourcePath classpath resource path
     * @return JAXB parse result including marshalled output sample
     */
    public JaxbParseResult parseFromResource(String resourcePath) {
        try (InputStream inputStream = getRequiredResource(resourcePath)) {
            JAXBContext context = JAXBContext.newInstance(EmployeesXml.class);

            Unmarshaller unmarshaller = context.createUnmarshaller();
            EmployeesXml employeesXml = (EmployeesXml) unmarshaller.unmarshal(inputStream);

            List<Employee> employees = mapEmployees(employeesXml);
            String marshalledXml = marshalForPreview(context, employeesXml);

            return new JaxbParseResult(
                    employees,
                    "employees",
                    JaxbXmlConstants.NAMESPACE,
                    marshalledXml
            );
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to parse XML resource: " + resourcePath, exception);
        }
    }

    private List<Employee> mapEmployees(EmployeesXml root) {
        List<Employee> employees = new ArrayList<Employee>();

        for (EmployeeXml employeeXml : root.getEmployees()) {
            AddressXml addressXml = employeeXml.getAddress();
            Address address = new Address(addressXml.getCity(), addressXml.getCountry(), addressXml.getZipCode());

            Employee employee = new Employee(
                    employeeXml.getId(),
                    employeeXml.getFirstName(),
                    employeeXml.getLastName(),
                    employeeXml.isActive(),
                    employeeXml.getSalary(),
                    employeeXml.getDepartment(),
                    employeeXml.getSkills().getSkills(),
                    address
            );
            employees.add(employee);
        }

        return employees;
    }

    private String marshalForPreview(JAXBContext context, EmployeesXml employeesXml) throws Exception {
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        StringWriter writer = new StringWriter();
        marshaller.marshal(employeesXml, writer);
        return writer.toString();
    }

    private InputStream getRequiredResource(String resourcePath) throws IOException {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }
        return inputStream;
    }
}

