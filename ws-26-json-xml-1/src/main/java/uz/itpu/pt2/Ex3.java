package uz.itpu.pt2;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.StringReader;

/**
 * Slide 3: Declaring Elements and Attributes.
 * <p>
 * Demonstrates:
 * <ul>
 *   <li>Element declarations – core building blocks defining what tags can exist
 *       and what data they hold (xs:string, xs:integer, xs:date, custom restrictions).</li>
 *   <li>Attribute declarations – metadata attached to elements (id, type, optional/required).</li>
 *   <li>Rule of placement – attributes must be declared <em>after</em> any
 *       {@code <xs:sequence>} or {@code <xs:choice>} block.</li>
 * </ul>
 *
 * <p>The example models an electronic business-card ({@code <vcard>}) because it
 * naturally requires a mix of element content and attribute metadata.
 */
public class Ex3 {

    // -----------------------------------------------------------------------
    //  Schema: vCard with element declarations + attribute placement rule
    // -----------------------------------------------------------------------

    /**
     * XSD that declares elements (name, email, phone, birthday) and attributes
     * (version, preferred) following the required placement rule:
     * attributes come AFTER the xs:sequence block.
     */
    private static final String VCARD_XSD =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "\n" +
            "  <!-- ── Simple type for e-mail ───────────────────────────── -->\n" +
            "  <xs:simpleType name=\"emailType\">\n" +
            "    <xs:restriction base=\"xs:string\">\n" +
            "      <xs:pattern value=\"[^@]+@[^@]+\\.[^@]+\"/>\n" +
            "    </xs:restriction>\n" +
            "  </xs:simpleType>\n" +
            "\n" +
            "  <!-- ── Simple type for phone (E.164-ish) ────────────────── -->\n" +
            "  <xs:simpleType name=\"phoneType\">\n" +
            "    <xs:restriction base=\"xs:string\">\n" +
            "      <xs:pattern value=\"\\+?[0-9 \\-]{7,15}\"/>\n" +
            "    </xs:restriction>\n" +
            "  </xs:simpleType>\n" +
            "\n" +
            "  <!-- ── Root element ─────────────────────────────────────── -->\n" +
            "  <xs:element name=\"vcards\">\n" +
            "    <xs:complexType>\n" +
            "      <xs:sequence>\n" +
            "        <xs:element name=\"vcard\" maxOccurs=\"unbounded\">\n" +
            "          <xs:complexType>\n" +
            "\n" +
            "            <!-- ELEMENT declarations (building blocks) -->\n" +
            "            <xs:sequence>\n" +
            "              <xs:element name=\"fullName\"  type=\"xs:string\"/>\n" +
            "              <xs:element name=\"email\"     type=\"emailType\"/>\n" +
            "              <!-- phone is optional; preferred attribute marks primary -->\n" +
            "              <xs:element name=\"phone\" minOccurs=\"0\" maxOccurs=\"5\">\n" +
            "                <xs:complexType>\n" +
            "                  <xs:simpleContent>\n" +
            "                    <xs:extension base=\"phoneType\">\n" +
            "                      <!-- ATTRIBUTE declared inside its own element type -->\n" +
            "                      <xs:attribute name=\"preferred\" type=\"xs:boolean\"\n" +
            "                                    default=\"false\"/>\n" +
            "                    </xs:extension>\n" +
            "                  </xs:simpleContent>\n" +
            "                </xs:complexType>\n" +
            "              </xs:element>\n" +
            "              <!-- xs:date element -->\n" +
            "              <xs:element name=\"birthday\" type=\"xs:date\" minOccurs=\"0\"/>\n" +
            "            </xs:sequence>\n" +
            "\n" +
            "            <!-- ATTRIBUTES declared AFTER the sequence (placement rule) -->\n" +
            "            <xs:attribute name=\"id\"      type=\"xs:positiveInteger\" use=\"required\"/>\n" +
            "            <xs:attribute name=\"version\" type=\"xs:string\"         default=\"3.0\"/>\n" +
            "\n" +
            "          </xs:complexType>\n" +
            "        </xs:element>\n" +
            "      </xs:sequence>\n" +
            "    </xs:complexType>\n" +
            "  </xs:element>\n" +
            "\n" +
            "</xs:schema>";

    // -----------------------------------------------------------------------
    //  XML instances
    // -----------------------------------------------------------------------

    private static final String VALID_XML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<vcards>\n" +
            "  <!-- vcard with all elements and both attributes -->\n" +
            "  <vcard id=\"1\" version=\"4.0\">\n" +
            "    <fullName>Alice Wonderland</fullName>\n" +
            "    <email>alice@example.com</email>\n" +
            "    <phone preferred=\"true\">+1-555-0100</phone>\n" +
            "    <phone>+1-555-0199</phone>\n" +
            "    <birthday>1990-06-15</birthday>\n" +
            "  </vcard>\n" +
            "  <!-- vcard with only required elements -->\n" +
            "  <vcard id=\"2\">\n" +
            "    <fullName>Bob Builder</fullName>\n" +
            "    <email>bob@example.com</email>\n" +
            "  </vcard>\n" +
            "</vcards>";

    /** Invalid: email does not match the pattern (no domain). */
    private static final String INVALID_EMAIL_XML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<vcards>\n" +
            "  <vcard id=\"3\">\n" +
            "    <fullName>Charlie</fullName>\n" +
            "    <email>charlie-at-nowhere</email>\n" +
            "  </vcard>\n" +
            "</vcards>";

    /** Invalid: required attribute 'id' is missing. */
    private static final String MISSING_ATTR_XML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<vcards>\n" +
            "  <vcard>\n" +
            "    <fullName>Dana</fullName>\n" +
            "    <email>dana@example.com</email>\n" +
            "  </vcard>\n" +
            "</vcards>";

    // -----------------------------------------------------------------------
    //  main
    // -----------------------------------------------------------------------

    public static void main(String[] args) {
        System.out.println("=== Slide 3: Declaring Elements and Attributes ===\n");

        System.out.println("XSD snippet highlights:");
        System.out.println("  • Element declarations  – <xs:element name=\"..\" type=\"..\"/>");
        System.out.println("  • Attribute declarations – <xs:attribute name=\"..\" use=\"required|optional\"/>");
        System.out.println("  • Placement rule         – attributes always AFTER xs:sequence\n");

        validate(VALID_XML,         "valid vCards (elements + attributes correct)");
        validate(INVALID_EMAIL_XML, "invalid email pattern (element data violated)");
        validate(MISSING_ATTR_XML,  "missing required attribute 'id'");
    }

    /**
     * Validates the given XML against the vCard XSD schema.
     *
     * @param xml   XML content to validate
     * @param label human-readable description
     */
    private static void validate(String xml, String label) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(new StringReader(VCARD_XSD)));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xml)));
            System.out.printf("  [%-55s]  VALID ✔%n", label);
        } catch (Exception e) {
            System.out.printf("  [%-55s]  INVALID ✘  %s%n", label, e.getMessage());
        }
    }
}

