package uz.itpu.pt2.jaxb;

import uz.itpu.model.Employee;

import java.util.List;

/**
 * Result of JAXB processing with parsed employees and XML metadata.
 */
public final class JaxbParseResult {

    private final List<Employee> employees;
    private final String rootElementName;
    private final String namespaceUri;
    private final String marshalledXml;

    /**
     * Creates immutable JAXB parse result.
     *
     * @param employees parsed employees
     * @param rootElementName XML root element name
     * @param namespaceUri XML namespace URI
     * @param marshalledXml marshalled XML output generated from bound objects
     */
    public JaxbParseResult(List<Employee> employees, String rootElementName, String namespaceUri, String marshalledXml) {
        this.employees = List.copyOf(employees);
        this.rootElementName = rootElementName;
        this.namespaceUri = namespaceUri;
        this.marshalledXml = marshalledXml;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public String getRootElementName() {
        return rootElementName;
    }

    public String getNamespaceUri() {
        return namespaceUri;
    }

    public String getMarshalledXml() {
        return marshalledXml;
    }
}

