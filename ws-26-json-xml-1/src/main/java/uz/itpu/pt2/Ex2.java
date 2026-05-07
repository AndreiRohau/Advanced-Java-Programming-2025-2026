package uz.itpu.pt2;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

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
 *
 * <p>Resources used (src/main/resources/ex2/):
 * <ul>
 *   <li>product-basic.xsd           – basic schema with primitive types</li>
 *   <li>product-valid.xml           – valid product XML</li>
 *   <li>product-invalid.xml         – invalid product (non-decimal price)</li>
 *   <li>employees-sophisticated.xsd – schema with restrictions, patterns, occurrences</li>
 *   <li>employees-valid.xml         – valid employees XML</li>
 *   <li>employees-invalid.xml       – invalid employees (bad email &amp; negative salary)</li>
 * </ul>
 */
public class Ex2 {

    public static void main(String[] args) {
        System.out.println("=== Slide 2: Anatomy of an XML Schema (XSD) ===\n");

        System.out.println("--- Basic Schema (primitive type declarations) ---");
        validate("ex2/product-basic.xsd", "ex2/product-valid.xml",   "basic – valid product");
        validate("ex2/product-basic.xsd", "ex2/product-invalid.xml", "basic – invalid price (not a decimal)");

        System.out.println("\n--- Sophisticated Schema (restrictions, patterns, occurrences) ---");
        validate("ex2/employees-sophisticated.xsd", "ex2/employees-valid.xml",   "sophisticated – valid employees");
        validate("ex2/employees-sophisticated.xsd", "ex2/employees-invalid.xml", "sophisticated – bad email & negative salary");
    }

    /**
     * Validates the XML classpath resource against the XSD classpath resource and prints the outcome.
     *
     * @param xsdPath classpath-relative path to the XSD schema (stored separately from the XML data)
     * @param xmlPath classpath-relative path to the XML document
     * @param label   a human-readable description for the test case
     */
    private static void validate(String xsdPath, String xmlPath, String label) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(Ex1.openResource(xsdPath)));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(Ex1.openResource(xmlPath)));
            System.out.printf("  [%-50s]  VALID ✔%n", label);
        } catch (Exception e) {
            System.out.printf("  [%-50s]  INVALID ✘  %s%n", label, e.getMessage());
        }
    }
}
