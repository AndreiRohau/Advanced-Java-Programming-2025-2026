package uz.itpu.pt1.sax;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;

/**
 * SAX parser service that reads XML from classpath resources.
 */
public class EmployeeSaxParser {

    /**
     * Parses employee XML from resources using a secure, namespace-aware SAX parser.
     *
     * @param resourcePath classpath resource path, for example {@code employee-example-ns.xml}
     * @return parsed result with employees and CDATA section statistics
     */
    public SaxParseResult parseFromResource(String resourcePath) {
        try (InputStream inputStream = getRequiredResource(resourcePath)) {
            SAXParserFactory factory = SAXParserFactory.newInstance(); // HERE new factory
            factory.setNamespaceAware(true); // HERE enable namespaces
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

            SAXParser saxParser = factory.newSAXParser(); // HERE
            XMLReader xmlReader = saxParser.getXMLReader(); // HERE processor

            EmployeeSaxHandler handler = new EmployeeSaxHandler(); // HERE custom handler
            xmlReader.setContentHandler(handler);
            xmlReader.setProperty("http://xml.org/sax/properties/lexical-handler", handler);
            xmlReader.parse(new InputSource(inputStream)); // HERE custom resource input

            return handler.buildResult(); // HERE
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to parse XML resource: " + resourcePath, exception);
        }
    }

    private InputStream getRequiredResource(String resourcePath) throws IOException {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }
        return inputStream;
    }
}

