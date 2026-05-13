package uz.itpu.pt1.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;
import uz.itpu.model.Address;
import uz.itpu.model.Employee;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * SAX event handler that maps employee XML into immutable domain objects.
 */
public class EmployeeSaxHandler extends DefaultHandler implements LexicalHandler {

    private final List<Employee> employees = new ArrayList<>();
    private final StringBuilder textBuffer = new StringBuilder();

    private int cdataSections;

    private int id;
    private String firstName;
    private String lastName;
    private boolean active;
    private BigDecimal salary;
    private String department;
    private List<String> skills;

    private String city;
    private String country;
    private String zipCode;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        textBuffer.setLength(0);

        if ("employee".equals(localName)) {
            id = Integer.parseInt(attributes.getValue("id"));
            skills = new ArrayList<>();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        textBuffer.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        String value = textBuffer.toString().trim();
        if (value.isEmpty()) {
            return;
        }

        switch (localName) {
            case "firstName":
                firstName = value;
                break;
            case "lastName":
                lastName = value;
                break;
            case "active":
                active = Boolean.parseBoolean(value);
                break;
            case "salary":
                salary = new BigDecimal(value);
                break;
            case "department":
                department = value;
                break;
            case "skill":
                skills.add(value);
                break;
            case "city":
                city = value;
                break;
            case "country":
                country = value;
                break;
            case "zipCode":
                zipCode = value;
                break;
            case "employee":
                Address address = new Address(city, country, zipCode);
                Employee employee = new Employee(id, firstName, lastName, active, salary, department, List.copyOf(skills), address);
                employees.add(employee);
                break;
            default:
                // Ignore other tags for this training example.
                break;
        }
    }

    /**
     * Builds parse result with mapped employees and lexical event count.
     *
     * @return parse result
     */
    public SaxParseResult buildResult() {
        return new SaxParseResult(List.copyOf(employees), cdataSections);
    }

    @Override
    public void startCDATA() throws SAXException {
        cdataSections++;
    }

    @Override
    public void endCDATA() {
    }

    @Override
    public void startDTD(String name, String publicId, String systemId) {
    }

    @Override
    public void endDTD() {
    }

    @Override
    public void startEntity(String name) {
    }

    @Override
    public void endEntity(String name) {
    }

    @Override
    public void comment(char[] ch, int start, int length) {
    }
}

