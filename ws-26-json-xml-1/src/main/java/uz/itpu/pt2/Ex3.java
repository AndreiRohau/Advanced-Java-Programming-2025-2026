package uz.itpu.pt2;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

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
 * <p>Resources used (src/main/resources/ex3/):
 * <ul>
 *   <li>vcard.xsd                  – XSD schema for vCard documents</li>
 *   <li>vcard-valid.xml            – valid vCard document</li>
 *   <li>vcard-invalid-email.xml    – invalid email pattern</li>
 *   <li>vcard-missing-attr.xml     – missing required attribute 'id'</li>
 * </ul>
 */
public class Ex3 {

    public static void main(String[] args) {
        System.out.println("=== Slide 3: Declaring Elements and Attributes ===\n");

        System.out.println("XSD snippet highlights:");
        System.out.println("  • Element declarations   – <xs:element name=\"..\" type=\"..\"/>");
        System.out.println("  • Attribute declarations – <xs:attribute name=\"..\" use=\"required|optional\"/>");
        System.out.println("  • Placement rule         – attributes always AFTER xs:sequence\n");

        validate("ex3/vcard-valid.xml",         "valid vCards (elements + attributes correct)");
        validate("ex3/vcard-invalid-email.xml",  "invalid email pattern (element data violated)");
        validate("ex3/vcard-missing-attr.xml",   "missing required attribute 'id'");
    }

    /**
     * Validates the given XML classpath resource against the vCard XSD schema.
     *
     * @param xmlPath classpath-relative path to the XML resource
     * @param label   human-readable description
     */
    private static void validate(String xmlPath, String label) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(Ex1.openResource("ex3/vcard.xsd")));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(Ex1.openResource(xmlPath)));
            System.out.printf("  [%-55s]  VALID ✔%n", label);
        } catch (Exception e) {
            System.out.printf("  [%-55s]  INVALID ✘  %s%n", label, e.getMessage());
        }
    }
}

