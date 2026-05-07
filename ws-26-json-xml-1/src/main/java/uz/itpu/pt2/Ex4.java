package uz.itpu.pt2;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

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
 * <p>The domain model is an <em>electronic business card</em> ({@code <businessCards>})
 * demonstrating how XSD provides a flexible and powerful mechanism for maintaining
 * high-quality business data.
 *
 * <p>Resources used (src/main/resources/ex4/):
 * <ul>
 *   <li>business-card.xsd                    – XSD schema (Step 1)</li>
 *   <li>business-card-valid.xml              – well-formed &amp; valid content (Step 2)</li>
 *   <li>business-card-invalid-dept.xml       – invalid department enumeration</li>
 *   <li>business-card-missing-element.xml    – missing required element</li>
 *   <li>business-card-not-well-formed.xml    – not well-formed XML</li>
 * </ul>
 */
public class Ex4 {

    private static final String XSD = "ex4/business-card.xsd";

    public static void main(String[] args) {
        System.out.println("=== Slide 4: Best Practices and Validation ===\n");

        System.out.println("Workflow:");
        System.out.println("  1. Define the structure  → ex4/business-card.xsd");
        System.out.println("  2. Create the content    → ex4/business-card-*.xml");
        System.out.println("  3. Validate XML vs XSD   → results printed\n");

        validate("ex4/business-card-valid.xml",           "well-formed + schema-valid business cards");
        validate("ex4/business-card-invalid-dept.xml",    "invalid department (enumeration violated)");
        validate("ex4/business-card-missing-element.xml", "missing required element <department>");
        validate("ex4/business-card-not-well-formed.xml", "not well-formed XML (unclosed tag)");
    }

    /**
     * Validates the XML classpath resource against {@value XSD}.
     * <p>
     * Requirement from the slide: the XML must be <em>well-formed first</em>;
     * schema validation catches structural/business-rule violations.
     *
     * @param xmlPath classpath-relative path to the XML resource
     * @param label   human-readable description of the test case
     */
    private static void validate(String xmlPath, String label) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(Ex1.openResource(XSD)));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(Ex1.openResource(xmlPath)));
            System.out.printf("  [%-55s]  VALID ✔  — high-quality business data confirmed%n", label);
        } catch (Exception e) {
            System.out.printf("  [%-55s]  INVALID ✘  %s%n", label, e.getMessage());
        }
    }
}

