package uz.itpu.pt2;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Slide 1: Introduction to XML Validation.
 * <p>
 * Demonstrates:
 * <ul>
 *   <li>Purpose of XML validation: enforce rules, ensure data integrity, minimize errors.</li>
 *   <li>Two primary methods: DTD (older/simpler) and XSD (modern/robust).</li>
 *   <li>Key advantage: separating rules (Schema) from data (XML).</li>
 * </ul>
 *
 * <p>Resources used (src/main/resources/ex1/):
 * <ul>
 *   <li>note.xsd               – XSD schema for a note document</li>
 *   <li>note-dtd.xml           – well-formed XML with an inline DTD</li>
 *   <li>note-valid.xml         – valid XML conforming to the XSD</li>
 *   <li>note-invalid.xml       – invalid XML missing the required &lt;message&gt; element</li>
 * </ul>
 */
public class Ex1 {

    public static void main(String[] args) throws IOException {
        System.out.println("=== Slide 1: Introduction to XML Validation ===\n");

        demonstrateDtdConcept();
        System.out.println();
        validateWithXsd("ex1/note-valid.xml",   "Valid XML");
        System.out.println();
        validateWithXsd("ex1/note-invalid.xml", "Invalid XML (missing <message>)");
    }

    /**
     * Demonstrates the DTD concept by printing the XML+DTD document loaded from resources.
     */
    private static void demonstrateDtdConcept() throws IOException {
        System.out.println("--- DTD (Document Type Definition) ---");
        System.out.println("DTD rules are embedded directly inside the XML or referenced externally.");
        System.out.println("Sample XML with inline DTD (note-dtd.xml):\n");
        System.out.println(readResource("ex1/note-dtd.xml"));
        System.out.println("DTD advantage: simple and human-readable.");
        System.out.println("DTD limitation: no data-type support (everything is text).");
    }

    /**
     * Validates the XML resource at {@code xmlPath} against the note XSD schema.
     *
     * @param xmlPath classpath-relative path to the XML resource
     * @param label   a label for display purposes
     */
    private static void validateWithXsd(String xmlPath, String label) {
        System.out.println("--- XSD Validation: " + label + " ---");
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(openResource("ex1/note.xsd")));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(openResource(xmlPath)));
            System.out.println("Result: VALID ✔  — Data integrity confirmed by XSD rules.");
        } catch (Exception e) {
            System.out.println("Result: INVALID ✘  — " + e.getMessage());
            System.out.println("Key advantage: structural violations are caught before data is processed.");
        }
    }

    // -----------------------------------------------------------------------
    //  Helpers
    // -----------------------------------------------------------------------

    /**
     * Opens a classpath resource as an {@link InputStream}.
     *
     * @param path resource path relative to the classpath root
     * @return the input stream
     * @throws IOException if the resource cannot be found
     */
    static InputStream openResource(String path) throws IOException {
        InputStream is = Ex1.class.getClassLoader().getResourceAsStream(path);
        if (is == null) {
            throw new IOException("Resource not found on classpath: " + path);
        }
        return is;
    }

    /**
     * Reads a classpath resource fully as a UTF-8 string.
     *
     * @param path resource path relative to the classpath root
     * @return the file content
     * @throws IOException if the resource cannot be found or read
     */
    static String readResource(String path) throws IOException {
        try (InputStream is = openResource(path)) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
