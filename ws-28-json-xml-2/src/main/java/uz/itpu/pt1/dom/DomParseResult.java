package uz.itpu.pt1.dom;

import uz.itpu.model.Employee;

import java.util.List;

/**
 * Result of DOM parsing with mapped employees and DOM-level statistics.
 */
public final class DomParseResult {

    private final List<Employee> employees;
    private final int cdataSections;
    private final int elementNodes;
    private final int textNodes;

    /**
     * Creates immutable DOM parse result.
     *
     * @param employees parsed employees
     * @param cdataSections number of CDATA section nodes
     * @param elementNodes number of element nodes in the document
     * @param textNodes number of non-empty text nodes in the document
     */
    public DomParseResult(List<Employee> employees, int cdataSections, int elementNodes, int textNodes) {
        this.employees = List.copyOf(employees);
        this.cdataSections = cdataSections;
        this.elementNodes = elementNodes;
        this.textNodes = textNodes;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public int getCdataSections() {
        return cdataSections;
    }

    public int getElementNodes() {
        return elementNodes;
    }

    public int getTextNodes() {
        return textNodes;
    }
}

