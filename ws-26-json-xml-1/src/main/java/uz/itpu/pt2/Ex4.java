package uz.itpu.pt2;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.StringReader;

/**
 * Slide 4: Best Practices and Validation.
 * <p>
 * Demonstrates the recommended workflow end-to-end:
 * <ol>
 *   <li><strong>Define the structure</strong> – author the XSD schema.</li>
 *   <li><strong>Create the content</strong> – produce the XML document (must be well-formed first).</li>
 *   <li><strong>Validate the XML against the XSD</strong> – confirm both well-formedness and schema conformance.</li>
 * </ol>
 *
 * <p>The domain model is an <em>electronic business card</em> ({@code <businessCard>}),
 * demonstrating how XSD provides a flexible and powerful mechanism for maintaining
 * high-quality business data.
 */
public class Ex4 {

    // -----------------------------------------------------------------------
    //  STEP 1 – Define the structure (XSD)
    // -----------------------------------------------------------------------

    /**
     * XSD for a business-card exchange format.
     * <p>
     * Best-practice elements shown here:
     * <ul>
     *   <li>Reusable named simple types.</li>
     *   <li>Required vs optional elements (minOccurs).</li>
     *   <li>Required and optional attributes placed after the sequence.</li>
     *   <li>Enumeration constraint for the {@code department} element.</li>
     * </ul>
     */
    private static final String BUSINESS_CARD_XSD =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "\n" +
            "  <!-- Reusable email type -->\n" +
            "  <xs:simpleType name=\"emailType\">\n" +
            "    <xs:restriction base=\"xs:string\">\n" +
            "      <xs:pattern value=\"[^@]+@[^@]+\\.[^@]+\"/>\n" +
            "    </xs:restriction>\n" +
            "  </xs:simpleType>\n" +
            "\n" +
            "  <!-- Reusable phone type -->\n" +
            "  <xs:simpleType name=\"phoneType\">\n" +
            "    <xs:restriction base=\"xs:string\">\n" +
            "      <xs:pattern value=\"\\+?[0-9 \\-]{7,15}\"/>\n" +
            "    </xs:restriction>\n" +
            "  </xs:simpleType>\n" +
            "\n" +
            "  <!-- Allowed departments (enumeration) -->\n" +
            "  <xs:simpleType name=\"departmentType\">\n" +
            "    <xs:restriction base=\"xs:string\">\n" +
            "      <xs:enumeration value=\"Engineering\"/>\n" +
            "      <xs:enumeration value=\"Sales\"/>\n" +
            "      <xs:enumeration value=\"Marketing\"/>\n" +
            "      <xs:enumeration value=\"Finance\"/>\n" +
            "      <xs:enumeration value=\"HR\"/>\n" +
            "    </xs:restriction>\n" +
            "  </xs:simpleType>\n" +
            "\n" +
            "  <xs:element name=\"businessCards\">\n" +
            "    <xs:complexType>\n" +
            "      <xs:sequence>\n" +
            "        <xs:element name=\"card\" maxOccurs=\"unbounded\">\n" +
            "          <xs:complexType>\n" +
            "            <xs:sequence>\n" +
            "              <!-- Required elements -->\n" +
            "              <xs:element name=\"fullName\"   type=\"xs:string\"/>\n" +
            "              <xs:element name=\"title\"      type=\"xs:string\"/>\n" +
            "              <xs:element name=\"department\" type=\"departmentType\"/>\n" +
            "              <xs:element name=\"email\"      type=\"emailType\"/>\n" +
            "              <!-- Optional elements -->\n" +
            "              <xs:element name=\"phone\"    type=\"phoneType\" minOccurs=\"0\" maxOccurs=\"3\"/>\n" +
            "              <xs:element name=\"website\"  type=\"xs:anyURI\" minOccurs=\"0\"/>\n" +
            "              <xs:element name=\"linkedIn\" type=\"xs:anyURI\" minOccurs=\"0\"/>\n" +
            "            </xs:sequence>\n" +
            "            <!-- Attributes AFTER the sequence (placement best practice) -->\n" +
            "            <xs:attribute name=\"id\"      type=\"xs:positiveInteger\" use=\"required\"/>\n" +
            "            <xs:attribute name=\"version\" type=\"xs:string\"         default=\"1.0\"/>\n" +
            "          </xs:complexType>\n" +
            "        </xs:element>\n" +
            "      </xs:sequence>\n" +
            "      <!-- Root-level attribute -->\n" +
            "      <xs:attribute name=\"organization\" type=\"xs:string\" use=\"required\"/>\n" +
            "    </xs:complexType>\n" +
            "  </xs:element>\n" +
            "\n" +
            "</xs:schema>";

    // -----------------------------------------------------------------------
    //  STEP 2 – Create the content (XML)
    // -----------------------------------------------------------------------

    /** Well-formed and schema-conformant business-card document. */
    private static final String WELL_FORMED_VALID_XML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<businessCards organization=\"ITPU\">\n" +
            "  <card id=\"1\" version=\"2.0\">\n" +
            "    <fullName>Alice Wonderland</fullName>\n" +
            "    <title>Senior Software Engineer</title>\n" +
            "    <department>Engineering</department>\n" +
            "    <email>alice@itpu.uz</email>\n" +
            "    <phone>+998-71-123-4567</phone>\n" +
            "    <website>https://alice.dev</website>\n" +
            "    <linkedIn>https://linkedin.com/in/alice</linkedIn>\n" +
            "  </card>\n" +
            "  <card id=\"2\">\n" +
            "    <fullName>Bob Builder</fullName>\n" +
            "    <title>Sales Manager</title>\n" +
            "    <department>Sales</department>\n" +
            "    <email>bob@itpu.uz</email>\n" +
            "  </card>\n" +
            "</businessCards>";

    /** Schema-invalid: unknown department value. */
    private static final String INVALID_DEPARTMENT_XML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<businessCards organization=\"ITPU\">\n" +
            "  <card id=\"3\">\n" +
            "    <fullName>Charlie</fullName>\n" +
            "    <title>Intern</title>\n" +
            "    <department>Unknown</department>\n" +   // not in enumeration
            "    <email>charlie@itpu.uz</email>\n" +
            "  </card>\n" +
            "</businessCards>";

    /** Schema-invalid: missing required element 'department'. */
    private static final String MISSING_ELEMENT_XML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<businessCards organization=\"ITPU\">\n" +
            "  <card id=\"4\">\n" +
            "    <fullName>Dana</fullName>\n" +
            "    <title>Designer</title>\n" +
            "    <email>dana@itpu.uz</email>\n" +
            "  </card>\n" +
            "</businessCards>";

    /** Not well-formed XML (unclosed tag). */
    private static final String NOT_WELL_FORMED_XML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<businessCards organization=\"ITPU\">\n" +
            "  <card id=\"5\">\n" +
            "    <fullName>Eve\n" +          // missing </fullName>
            "    <email>eve@itpu.uz</email>\n" +
            "  </card>\n" +
            "</businessCards>";

    // -----------------------------------------------------------------------
    //  STEP 3 – Validate
    // -----------------------------------------------------------------------

    public static void main(String[] args) {
        System.out.println("=== Slide 4: Best Practices and Validation ===\n");

        System.out.println("Workflow:");
        System.out.println("  1. Define the structure  → XSD authored above");
        System.out.println("  2. Create the content    → XML documents below");
        System.out.println("  3. Validate XML vs XSD   → results printed\n");

        // Step 3 – validate each XML document against the XSD
        validate(WELL_FORMED_VALID_XML,   "well-formed + schema-valid business cards");
        validate(INVALID_DEPARTMENT_XML,  "invalid department (enumeration violated)");
        validate(MISSING_ELEMENT_XML,     "missing required element <department>");
        validate(NOT_WELL_FORMED_XML,     "not well-formed XML (unclosed tag)");
    }

    /**
     * Validates {@code xml} against {@link #BUSINESS_CARD_XSD}.
     * <p>
     * Requirement from the slide: the XML must be <em>well-formed first</em>;
     * schema validation catches structural/business-rule violations.
     *
     * @param xml   the XML document to validate
     * @param label human-readable description of the test case
     */
    private static void validate(String xml, String label) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(new StringReader(BUSINESS_CARD_XSD)));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xml)));
            System.out.printf("  [%-55s]  VALID ✔  — high-quality business data confirmed%n", label);
        } catch (Exception e) {
            System.out.printf("  [%-55s]  INVALID ✘  %s%n", label, e.getMessage());
        }
    }
}

