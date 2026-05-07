package uz.itpu.pt2;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.StringReader;

/**
 * Slide 1: Introduction to XML Validation.
 * <p>
 * Demonstrates:
 * <ul>
 *   <li>Purpose of XML validation: enforce rules, ensure data integrity, minimize errors.</li>
 *   <li>Two primary methods: DTD (older/simpler) and XSD (modern/robust).</li>
 *   <li>Key advantage: separating rules (Schema) from data (XML).</li>
 * </ul>
 */
public class Ex1 {

    // --- Sample XML documents ---

    /** A well-formed XML document referencing an inline DTD. */
    private static final String XML_WITH_DTD =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<!DOCTYPE note [\n" +
            "  <!ELEMENT note (to,from,message)>\n" +
            "  <!ELEMENT to (#PCDATA)>\n" +
            "  <!ELEMENT from (#PCDATA)>\n" +
            "  <!ELEMENT message (#PCDATA)>\n" +
            "]>\n" +
            "<note>\n" +
            "  <to>Alice</to>\n" +
            "  <from>Bob</from>\n" +
            "  <message>Hello from Bob!</message>\n" +
            "</note>";

    /** A well-formed XML document to be validated against an XSD schema. */
    private static final String XML_FOR_XSD =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<note>\n" +
            "  <to>Alice</to>\n" +
            "  <from>Bob</from>\n" +
            "  <message>Hello from Bob!</message>\n" +
            "</note>";

    /** An invalid XML document (missing required 'message' element). */
    private static final String INVALID_XML_FOR_XSD =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<note>\n" +
            "  <to>Alice</to>\n" +
            "  <from>Bob</from>\n" +
            "</note>";

    /**
     * XSD schema string defining the structure of a &lt;note&gt; document.
     * Separation of concerns: rules are kept separate from data.
     */
    private static final String NOTE_XSD =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "  <xs:element name=\"note\">\n" +
            "    <xs:complexType>\n" +
            "      <xs:sequence>\n" +
            "        <xs:element name=\"to\"      type=\"xs:string\"/>\n" +
            "        <xs:element name=\"from\"    type=\"xs:string\"/>\n" +
            "        <xs:element name=\"message\" type=\"xs:string\"/>\n" +
            "      </xs:sequence>\n" +
            "    </xs:complexType>\n" +
            "  </xs:element>\n" +
            "</xs:schema>";

    public static void main(String[] args) {
        System.out.println("=== Slide 1: Introduction to XML Validation ===\n");

        demonstrateDtdConcept();
        System.out.println();
        validateWithXsd(XML_FOR_XSD, "Valid XML");
        System.out.println();
        validateWithXsd(INVALID_XML_FOR_XSD, "Invalid XML (missing <message>)");
    }

    /**
     * Demonstrates the DTD concept by printing the XML+DTD document.
     * Java's built-in parser can parse DTD-based XML, but full DTD validation
     * requires enabling features; here we simply highlight the structure.
     */
    private static void demonstrateDtdConcept() {
        System.out.println("--- DTD (Document Type Definition) ---");
        System.out.println("DTD rules are embedded directly inside the XML or referenced externally.");
        System.out.println("Sample XML with inline DTD:\n");
        System.out.println(XML_WITH_DTD);
        System.out.println("\nDTD advantage: simple and human-readable.");
        System.out.println("DTD limitation: no data-type support (everything is text).");
    }

    /**
     * Validates the given XML string against the NOTE_XSD schema.
     *
     * @param xml   the XML content to validate
     * @param label a label for display purposes
     */
    private static void validateWithXsd(String xml, String label) {
        System.out.println("--- XSD Validation: " + label + " ---");
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(new StringReader(NOTE_XSD)));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xml)));
            System.out.println("Result: VALID ✔  — Data integrity confirmed by XSD rules.");
        } catch (Exception e) {
            System.out.println("Result: INVALID ✘  — " + e.getMessage());
            System.out.println("Key advantage: structural violations are caught before data is processed.");
        }
    }
}
