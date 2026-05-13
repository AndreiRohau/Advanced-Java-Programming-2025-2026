package uz.itpu.pt1.stax;

import uz.itpu.model.Address;
import uz.itpu.model.Employee;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * StAX parser service that reads employee XML from classpath resources.
 */
public class EmployeeStaxParser {

    /**
     * Parses employees from resource XML using a pull-based StAX parser.
     *
     * @param resourcePath classpath resource path
     * @return StAX parsing result with employees and event counters
     */
    public StaxParseResult parseFromResource(String resourcePath) {
        XMLStreamReader reader = null;

        try (InputStream inputStream = getRequiredResource(resourcePath)) {
            XMLInputFactory factory = createConfiguredFactory();
            reader = factory.createXMLStreamReader(inputStream);

            List<Employee> employees = new ArrayList<>();
            StringBuilder textBuffer = new StringBuilder();

            int cdataSections = 0;
            int startElements = 0;
            int endElements = 0;
            int characterEvents = 0;

            int id = 0;
            String firstName = null;
            String lastName = null;
            boolean active = false;
            BigDecimal salary = null;
            String department = null;
            List<String> skills = null;
            String city = null;
            String country = null;
            String zipCode = null;

            while (reader.hasNext()) {
                int event = reader.next();

                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        startElements++;
                        textBuffer.setLength(0);

                        if ("employee".equals(reader.getLocalName())) {
                            id = Integer.parseInt(reader.getAttributeValue(null, "id"));
                            skills = new ArrayList<>();
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        characterEvents++;
                        textBuffer.append(reader.getText());
                        break;
                    case XMLStreamConstants.CDATA:
                        characterEvents++;
                        cdataSections++;
                        textBuffer.append(reader.getText());
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        endElements++;

                        String localName = reader.getLocalName();
                        String value = textBuffer.toString().trim();

                        if (!value.isEmpty()) {
                            if ("firstName".equals(localName)) {
                                firstName = value;
                            } else if ("lastName".equals(localName)) {
                                lastName = value;
                            } else if ("active".equals(localName)) {
                                active = Boolean.parseBoolean(value);
                            } else if ("salary".equals(localName)) {
                                salary = new BigDecimal(value);
                            } else if ("department".equals(localName)) {
                                department = value;
                            } else if ("skill".equals(localName) && skills != null) {
                                skills.add(value);
                            } else if ("city".equals(localName)) {
                                city = value;
                            } else if ("country".equals(localName)) {
                                country = value;
                            } else if ("zipCode".equals(localName)) {
                                zipCode = value;
                            }
                        }

                        if ("employee".equals(localName) && skills != null) {
                            Address address = new Address(city, country, zipCode);
                            Employee employee = new Employee(id, firstName, lastName, active, salary, department, skills, address);
                            employees.add(employee);
                        }

                        textBuffer.setLength(0);
                        break;
                    default:
                        break;
                }
            }

            return new StaxParseResult(employees, cdataSections, startElements, endElements, characterEvents);
        } catch (IOException | XMLStreamException exception) {
            throw new IllegalStateException("Unable to parse XML resource: " + resourcePath, exception);
        } finally {
            closeQuietly(reader);
        }
    }

    /**
     * Reads XML from resources, appends a new employee, and writes updated XML next to the source resource.
     *
     * @param sourceResourcePath source classpath XML resource
     * @param outputFileName output file name
     * @param newEmployee new employee to append
     * @return absolute path to the saved XML file
     */
    public Path createUpdatedXmlFromResource(String sourceResourcePath, String outputFileName, Employee newEmployee) {
        XMLStreamReader reader = null;
        XMLStreamWriter writer = null;

        try (InputStream inputStream = getRequiredResource(sourceResourcePath)) {
            XMLInputFactory inputFactory = createConfiguredFactory();
            reader = inputFactory.createXMLStreamReader(inputStream);

            Path outputPath = resolveOutputPathNearSourceResource(sourceResourcePath, outputFileName);
            Files.createDirectories(outputPath.getParent());

            writer = XMLOutputFactory.newFactory().createXMLStreamWriter(Files.newOutputStream(outputPath), "UTF-8");

            int depth = 0;
            String rootLocalName = null;
            String rootNamespaceUri = null;

            while (reader.hasNext()) {
                int event = reader.next();

                switch (event) {
                    case XMLStreamConstants.START_DOCUMENT:
                        writer.writeStartDocument("UTF-8", "1.0");
                        break;
                    case XMLStreamConstants.START_ELEMENT:
                        if (depth == 0) {
                            rootLocalName = reader.getLocalName();
                            rootNamespaceUri = reader.getNamespaceURI();
                        }

                        writeStartElement(reader, writer);
                        depth++;
                        break;
                    case XMLStreamConstants.CHARACTERS:
                    case XMLStreamConstants.SPACE:
                        writer.writeCharacters(reader.getText());
                        break;
                    case XMLStreamConstants.CDATA:
                        writer.writeCData(reader.getText());
                        break;
                    case XMLStreamConstants.COMMENT:
                        writer.writeComment(reader.getText());
                        break;
                    case XMLStreamConstants.PROCESSING_INSTRUCTION:
                        writer.writeProcessingInstruction(reader.getPITarget(), reader.getPIData());
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        if (depth == 1
                                && equalsNullable(rootLocalName, reader.getLocalName())
                                && equalsNullable(rootNamespaceUri, reader.getNamespaceURI())) {
                            writeEmployeeElement(writer, newEmployee, rootNamespaceUri, reader.getPrefix());
                        }

                        writer.writeEndElement();
                        depth--;
                        break;
                    case XMLStreamConstants.END_DOCUMENT:
                        writer.writeEndDocument();
                        break;
                    default:
                        break;
                }
            }

            writer.flush();
            return outputPath;
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to update and save XML resource: " + sourceResourcePath, exception);
        } finally {
            closeQuietly(reader);
            closeQuietly(writer);
        }
    }

    private XMLInputFactory createConfiguredFactory() {
        XMLInputFactory factory = XMLInputFactory.newFactory();
        setPropertyIfSupported(factory, XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE);
        setPropertyIfSupported(factory, XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        setPropertyIfSupported(factory, XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        setPropertyIfSupported(factory, XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        return factory;
    }

    private void setPropertyIfSupported(XMLInputFactory factory, String property, Object value) {
        try {
            factory.setProperty(property, value);
        } catch (IllegalArgumentException ignored) {
            // Some StAX implementations do not expose every property.
        }
    }

    private InputStream getRequiredResource(String resourcePath) throws IOException {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }
        return inputStream;
    }

    private void closeQuietly(XMLStreamReader reader) {
        if (reader == null) {
            return;
        }

        try {
            reader.close();
        } catch (XMLStreamException ignored) {
        }
    }

    private void writeStartElement(XMLStreamReader reader, XMLStreamWriter writer) throws XMLStreamException {
        String localName = reader.getLocalName();
        String prefix = reader.getPrefix();
        String namespaceUri = reader.getNamespaceURI();

        if (namespaceUri == null || namespaceUri.isBlank()) {
            writer.writeStartElement(localName);
        } else if (prefix == null || prefix.isBlank()) {
            writer.writeStartElement(namespaceUri, localName);
        } else {
            writer.writeStartElement(prefix, localName, namespaceUri);
        }

        for (int i = 0; i < reader.getNamespaceCount(); i++) {
            String nsPrefix = reader.getNamespacePrefix(i);
            String nsUri = reader.getNamespaceURI(i);
            if (nsPrefix == null || nsPrefix.isBlank()) {
                writer.writeDefaultNamespace(nsUri);
            } else {
                writer.writeNamespace(nsPrefix, nsUri);
            }
        }

        for (int i = 0; i < reader.getAttributeCount(); i++) {
            String attrLocalName = reader.getAttributeLocalName(i);
            String attrPrefix = reader.getAttributePrefix(i);
            String attrNamespace = reader.getAttributeNamespace(i);
            String attrValue = reader.getAttributeValue(i);

            if (attrNamespace == null || attrNamespace.isBlank()) {
                writer.writeAttribute(attrLocalName, attrValue);
            } else if (attrPrefix == null || attrPrefix.isBlank()) {
                writer.writeAttribute(attrNamespace, attrLocalName, attrValue);
            } else {
                writer.writeAttribute(attrPrefix, attrNamespace, attrLocalName, attrValue);
            }
        }
    }

    private void writeEmployeeElement(
            XMLStreamWriter writer,
            Employee employee,
            String namespaceUri,
            String preferredPrefix
    ) throws XMLStreamException {
        writeStartElement(writer, namespaceUri, preferredPrefix, "employee");
        writer.writeAttribute("id", String.valueOf(employee.getId()));

        writeSimpleElement(writer, namespaceUri, preferredPrefix, "firstName", employee.getFirstName());
        writeSimpleElement(writer, namespaceUri, preferredPrefix, "lastName", employee.getLastName());
        writeSimpleElement(writer, namespaceUri, preferredPrefix, "active", String.valueOf(employee.isActive()));
        writeSimpleElement(writer, namespaceUri, preferredPrefix, "salary", employee.getSalary().toPlainString());

        writeStartElement(writer, namespaceUri, preferredPrefix, "department");
        writer.writeCData(employee.getDepartment());
        writer.writeEndElement();

        writeStartElement(writer, namespaceUri, preferredPrefix, "skills");
        for (String skill : employee.getSkills()) {
            writeSimpleElement(writer, namespaceUri, preferredPrefix, "skill", skill);
        }
        writer.writeEndElement();

        Address address = employee.getAddress();
        writeStartElement(writer, namespaceUri, preferredPrefix, "address");
        writeSimpleElement(writer, namespaceUri, preferredPrefix, "city", address.getCity());
        writeSimpleElement(writer, namespaceUri, preferredPrefix, "country", address.getCountry());
        writeSimpleElement(writer, namespaceUri, preferredPrefix, "zipCode", address.getZipCode());
        writer.writeEndElement();

        writer.writeEndElement();
    }

    private void writeSimpleElement(
            XMLStreamWriter writer,
            String namespaceUri,
            String preferredPrefix,
            String localName,
            String value
    ) throws XMLStreamException {
        writeStartElement(writer, namespaceUri, preferredPrefix, localName);
        writer.writeCharacters(value);
        writer.writeEndElement();
    }

    private void writeStartElement(
            XMLStreamWriter writer,
            String namespaceUri,
            String preferredPrefix,
            String localName
    ) throws XMLStreamException {
        if (namespaceUri == null || namespaceUri.isBlank()) {
            writer.writeStartElement(localName);
        } else if (preferredPrefix == null || preferredPrefix.isBlank()) {
            writer.writeStartElement(namespaceUri, localName);
        } else {
            writer.writeStartElement(preferredPrefix, localName, namespaceUri);
        }
    }

    private boolean equalsNullable(String left, String right) {
        return left == null ? right == null : left.equals(right);
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

    private void closeQuietly(XMLStreamWriter writer) {
        if (writer == null) {
            return;
        }

        try {
            writer.close();
        } catch (XMLStreamException ignored) {
        }
    }
}

