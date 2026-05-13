package uz.itpu.pt1.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uz.itpu.model.Address;
import uz.itpu.model.Employee;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * DOM parser service that reads employee XML from classpath resources.
 */
public class EmployeeDomParser {

    /**
     * Parses employees from resource XML using namespace-aware DOM traversal.
     *
     * @param resourcePath classpath resource path
     * @return DOM parsing result with mapped employees and node statistics
     */
    public DomParseResult parseFromResource(String resourcePath) {
        try (InputStream inputStream = getRequiredResource(resourcePath)) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);

            DocumentBuilder builder = factory.newDocumentBuilder(); // HERE new document builder
            Document document = builder.parse(inputStream); // HERE parse the document
            document.getDocumentElement().normalize();

            NodeList employeeNodes = document.getElementsByTagNameNS("*", "employee");
            List<Employee> employees = new ArrayList<>();
            for (int i = 0; i < employeeNodes.getLength(); i++) {
                Element employeeElement = (Element) employeeNodes.item(i);
                employees.add(mapEmployee(employeeElement));
            }

            NodeStats nodeStats = new NodeStats();
            collectStats(document.getDocumentElement(), nodeStats);

            return new DomParseResult(employees, nodeStats.cdataSections, nodeStats.elementNodes, nodeStats.textNodes);
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to parse XML resource: " + resourcePath, exception);
        }
    }

    /**
     * Reads XML from resources, appends a new employee, and writes the updated XML next to the source resource.
     *
     * @param sourceResourcePath source classpath XML resource
     * @param outputFileName output file name
     * @param newEmployee new employee to append
     * @return absolute path to the saved XML file
     */
    public Path createUpdatedXmlFromResource(String sourceResourcePath, String outputFileName, Employee newEmployee) {
        try (InputStream inputStream = getRequiredResource(sourceResourcePath)) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            document.getDocumentElement().normalize();

            Element root = document.getDocumentElement();
            appendEmployee(document, root, newEmployee);

            Path outputPath = resolveOutputPathNearSourceResource(sourceResourcePath, outputFileName);
            writeDocument(document, outputPath);
            return outputPath;
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to update and save XML from resource: " + sourceResourcePath, exception);
        }
    }

    private Employee mapEmployee(Element employeeElement) {
        int id = Integer.parseInt(employeeElement.getAttribute("id"));
        String firstName = getChildText(employeeElement, "firstName");
        String lastName = getChildText(employeeElement, "lastName");
        boolean active = Boolean.parseBoolean(getChildText(employeeElement, "active"));
        BigDecimal salary = new BigDecimal(getChildText(employeeElement, "salary"));
        String department = getChildText(employeeElement, "department");

        Element skillsElement = getChildElement(employeeElement, "skills");
        NodeList skillNodes = skillsElement.getElementsByTagNameNS("*", "skill");
        List<String> skills = new ArrayList<>();
        for (int i = 0; i < skillNodes.getLength(); i++) {
            skills.add(skillNodes.item(i).getTextContent().trim());
        }

        Element addressElement = getChildElement(employeeElement, "address");
        Address address = new Address(
                getChildText(addressElement, "city"),
                getChildText(addressElement, "country"),
                getChildText(addressElement, "zipCode")
        );

        return new Employee(id, firstName, lastName, active, salary, department, skills, address);
    }

    private String getChildText(Element parent, String localName) {
        Element element = getChildElement(parent, localName);
        return element.getTextContent().trim();
    }

    private Element getChildElement(Element parent, String localName) {
        NodeList nodeList = parent.getElementsByTagNameNS("*", localName);
        if (nodeList.getLength() == 0) {
            throw new IllegalStateException("Element not found: " + localName);
        }
        return (Element) nodeList.item(0);
    }

    private InputStream getRequiredResource(String resourcePath) throws IOException {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }
        return inputStream;
    }

    private void collectStats(Node node, NodeStats stats) {
        if (node == null) {
            return;
        }

        short nodeType = node.getNodeType();
        if (nodeType == Node.ELEMENT_NODE) {
            stats.elementNodes++;
        } else if (nodeType == Node.CDATA_SECTION_NODE) {
            stats.cdataSections++;
            if (!node.getTextContent().trim().isEmpty()) {
                stats.textNodes++;
            }
        } else if (nodeType == Node.TEXT_NODE && !node.getTextContent().trim().isEmpty()) {
            stats.textNodes++;
        }

        Node child = node.getFirstChild();
        while (child != null) {
            collectStats(child, stats);
            child = child.getNextSibling();
        }
    }

    private void appendEmployee(Document document, Element root, Employee employee) {
        Element employeeElement = createElementForRootNamespace(document, root, "employee");
        employeeElement.setAttribute("id", String.valueOf(employee.getId()));

        appendTextElement(document, root, employeeElement, "firstName", employee.getFirstName());
        appendTextElement(document, root, employeeElement, "lastName", employee.getLastName());
        appendTextElement(document, root, employeeElement, "active", String.valueOf(employee.isActive()));
        appendTextElement(document, root, employeeElement, "salary", employee.getSalary().toPlainString());
        appendCdataElement(document, root, employeeElement, "department", employee.getDepartment());

        Element skillsElement = createElementForRootNamespace(document, root, "skills");
        for (String skill : employee.getSkills()) {
            appendTextElement(document, root, skillsElement, "skill", skill);
        }
        employeeElement.appendChild(skillsElement);

        Address address = employee.getAddress();
        Element addressElement = createElementForRootNamespace(document, root, "address");
        appendTextElement(document, root, addressElement, "city", address.getCity());
        appendTextElement(document, root, addressElement, "country", address.getCountry());
        appendTextElement(document, root, addressElement, "zipCode", address.getZipCode());
        employeeElement.appendChild(addressElement);

        root.appendChild(employeeElement);
    }

    private void appendTextElement(Document document, Element root, Element parent, String localName, String value) {
        Element child = createElementForRootNamespace(document, root, localName);
        child.setTextContent(value);
        parent.appendChild(child);
    }

    private void appendCdataElement(Document document, Element root, Element parent, String localName, String value) {
        Element child = createElementForRootNamespace(document, root, localName);
        child.appendChild(document.createCDATASection(value));
        parent.appendChild(child);
    }

    private Element createElementForRootNamespace(Document document, Element root, String localName) {
        String namespaceUri = root.getNamespaceURI();
        String prefix = root.getPrefix();
        if (namespaceUri == null || namespaceUri.isBlank()) {
            return document.createElement(localName);
        }

        String qualifiedName = (prefix == null || prefix.isBlank())
                ? localName
                : prefix + ":" + localName;
        return document.createElementNS(namespaceUri, qualifiedName);
    }

    private void writeDocument(Document document, Path outputPath) throws Exception {
        Files.createDirectories(outputPath.getParent());

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        try (var outputStream = Files.newOutputStream(outputPath)) {
            transformer.transform(new DOMSource(document), new StreamResult(outputStream));
        }
    }

    private Path resolveOutputPathNearSourceResource(String sourceResourcePath, String outputFileName) throws Exception {
        URL resourceUrl = Thread.currentThread().getContextClassLoader().getResource(sourceResourcePath);
        if (resourceUrl == null || !"file".equalsIgnoreCase(resourceUrl.getProtocol())) {
            throw new IOException("Resource path is not file-backed: " + sourceResourcePath);
        }

        Path resourcePath = Path.of(resourceUrl.toURI()).toAbsolutePath().normalize();
        Path classpathRoot = resolveClasspathRoot(resourcePath, sourceResourcePath);
        Path mappedSourceRoot = mapClasspathRootToSourceResources(classpathRoot);

        Path sourceRelativeParent = Path.of(sourceResourcePath).getParent();
        Path outputDirectory = sourceRelativeParent == null
                ? mappedSourceRoot
                : mappedSourceRoot.resolve(sourceRelativeParent).normalize();
        return outputDirectory.resolve(outputFileName).normalize();
    }

    private Path resolveClasspathRoot(Path resourcePath, String sourceResourcePath) {
        Path sourceRelativePath = Path.of(sourceResourcePath).normalize();
        Path classpathRoot = resourcePath;
        for (int i = 0; i < sourceRelativePath.getNameCount(); i++) {
            classpathRoot = classpathRoot.getParent();
            if (classpathRoot == null) {
                throw new IllegalStateException("Unable to resolve classpath root for resource: " + sourceResourcePath);
            }
        }
        return classpathRoot;
    }

    private Path mapClasspathRootToSourceResources(Path classpathRoot) {
        Path normalizedRoot = classpathRoot.toAbsolutePath().normalize();
        if (normalizedRoot.endsWith(Path.of("target", "classes"))) {
            Path moduleRoot = normalizedRoot.getParent().getParent();
            return moduleRoot.resolve(Path.of("src", "main", "resources")).normalize();
        }
        return normalizedRoot;
    }

    private static final class NodeStats {
        private int cdataSections;
        private int elementNodes;
        private int textNodes;
    }
}

