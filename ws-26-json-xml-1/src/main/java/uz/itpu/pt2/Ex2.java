package uz.itpu.pt2;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.StringReader;

/**
 * Slide 2: Anatomy of an XML Schema (XSD).
 * <p>
 * Demonstrates:
 * <ul>
 *   <li>Robustness: data-type control (xs:integer, xs:date, xs:string, patterns, restrictions).</li>
 *   <li>Separation of concerns: the XSD is kept separate from the XML data.</li>
 *   <li>Complexity scale: from a basic declaration up to sophisticated constraints
 *       (minOccurs, maxOccurs, minInclusive, pattern).</li>
 * </ul>
 */
public class Ex2 {

    // -----------------------------------------------------------------------
    //  BASIC schema – a plain product catalogue with only type declarations
    // -----------------------------------------------------------------------

    private static final String BASIC_XSD =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "  <!-- Basic: just element names and primitive types -->\n" +
            "  <xs:element name=\"product\">\n" +
            "    <xs:complexType>\n" +
            "      <xs:sequence>\n" +
            "        <xs:element name=\"name\"  type=\"xs:string\"/>\n" +
            "        <xs:element name=\"price\" type=\"xs:decimal\"/>\n" +
            "        <xs:element name=\"stock\" type=\"xs:integer\"/>\n" +
            "      </xs:sequence>\n" +
            "    </xs:complexType>\n" +
            "  </xs:element>\n" +
            "</xs:schema>";

    private static final String BASIC_XML_VALID =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<product>\n" +
            "  <name>Laptop</name>\n" +
            "  <price>999.99</price>\n" +
            "  <stock>42</stock>\n" +
            "</product>";

    private static final String BASIC_XML_INVALID =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<product>\n" +
            "  <name>Laptop</name>\n" +
            "  <price>not-a-number</price>\n" +   // xs:decimal violated
            "  <stock>42</stock>\n" +
            "</product>";

    // -----------------------------------------------------------------------
    //  SOPHISTICATED schema – employee record with restrictions & occurrences
    // -----------------------------------------------------------------------

    private static final String SOPHISTICATED_XSD =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "\n" +
            "  <!-- Reusable simple type: salary must be >= 0 -->\n" +
            "  <xs:simpleType name=\"salaryType\">\n" +
            "    <xs:restriction base=\"xs:decimal\">\n" +
            "      <xs:minInclusive value=\"0\"/>\n" +
            "    </xs:restriction>\n" +
            "  </xs:simpleType>\n" +
            "\n" +
            "  <!-- Reusable simple type: email pattern -->\n" +
            "  <xs:simpleType name=\"emailType\">\n" +
            "    <xs:restriction base=\"xs:string\">\n" +
            "      <xs:pattern value=\"[^@]+@[^@]+\\.[^@]+\"/>\n" +
            "    </xs:restriction>\n" +
            "  </xs:simpleType>\n" +
            "\n" +
            "  <xs:element name=\"employees\">\n" +
            "    <xs:complexType>\n" +
            "      <xs:sequence>\n" +
            "        <!-- minOccurs/maxOccurs control repetition -->\n" +
            "        <xs:element name=\"employee\" minOccurs=\"1\" maxOccurs=\"unbounded\">\n" +
            "          <xs:complexType>\n" +
            "            <xs:sequence>\n" +
            "              <xs:element name=\"firstName\" type=\"xs:string\"/>\n" +
            "              <xs:element name=\"lastName\"  type=\"xs:string\"/>\n" +
            "              <xs:element name=\"email\"     type=\"emailType\"/>\n" +
            "              <xs:element name=\"salary\"    type=\"salaryType\"/>\n" +
            "              <xs:element name=\"startDate\" type=\"xs:date\"/>\n" +
            "              <!-- Optional: 0 or more phone numbers -->\n" +
            "              <xs:element name=\"phone\" type=\"xs:string\"\n" +
            "                          minOccurs=\"0\" maxOccurs=\"3\"/>\n" +
            "            </xs:sequence>\n" +
            "            <!-- Attribute declared after the sequence -->\n" +
            "            <xs:attribute name=\"id\" type=\"xs:positiveInteger\" use=\"required\"/>\n" +
            "          </xs:complexType>\n" +
            "        </xs:element>\n" +
            "      </xs:sequence>\n" +
            "    </xs:complexType>\n" +
            "  </xs:element>\n" +
            "\n" +
            "</xs:schema>";

    private static final String SOPHISTICATED_XML_VALID =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<employees>\n" +
            "  <employee id=\"1\">\n" +
            "    <firstName>Alice</firstName>\n" +
            "    <lastName>Smith</lastName>\n" +
            "    <email>alice@example.com</email>\n" +
            "    <salary>75000.00</salary>\n" +
            "    <startDate>2020-03-15</startDate>\n" +
            "    <phone>+1-555-0100</phone>\n" +
            "  </employee>\n" +
            "  <employee id=\"2\">\n" +
            "    <firstName>Bob</firstName>\n" +
            "    <lastName>Jones</lastName>\n" +
            "    <email>bob@example.com</email>\n" +
            "    <salary>82000.50</salary>\n" +
            "    <startDate>2018-07-01</startDate>\n" +
            "  </employee>\n" +
            "</employees>";

    private static final String SOPHISTICATED_XML_INVALID =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<employees>\n" +
            "  <employee id=\"3\">\n" +
            "    <firstName>Charlie</firstName>\n" +
            "    <lastName>Brown</lastName>\n" +
            "    <email>not-an-email</email>\n" +    // pattern violated
            "    <salary>-500</salary>\n" +           // minInclusive violated
            "    <startDate>2022-01-10</startDate>\n" +
            "  </employee>\n" +
            "</employees>";

    // -----------------------------------------------------------------------
    //  main
    // -----------------------------------------------------------------------

    public static void main(String[] args) {
        System.out.println("=== Slide 2: Anatomy of an XML Schema (XSD) ===\n");

        System.out.println("--- Basic Schema (primitive type declarations) ---");
        validate(BASIC_XSD, BASIC_XML_VALID,   "basic – valid product");
        validate(BASIC_XSD, BASIC_XML_INVALID, "basic – invalid price (not a decimal)");

        System.out.println("\n--- Sophisticated Schema (restrictions, patterns, occurrences) ---");
        validate(SOPHISTICATED_XSD, SOPHISTICATED_XML_VALID,   "sophisticated – valid employees");
        validate(SOPHISTICATED_XSD, SOPHISTICATED_XML_INVALID, "sophisticated – bad email & negative salary");
    }

    /**
     * Validates {@code xml} against {@code xsd} and prints the outcome.
     *
     * @param xsd   the XSD schema as a string (stored separately from the XML data)
     * @param xml   the XML document to validate
     * @param label a human-readable description for the test case
     */
    private static void validate(String xsd, String xml, String label) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(new StringReader(xsd)));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xml)));
            System.out.printf("  [%-50s]  VALID ✔%n", label);
        } catch (Exception e) {
            System.out.printf("  [%-50s]  INVALID ✘  %s%n", label, e.getMessage());
        }
    }
}

