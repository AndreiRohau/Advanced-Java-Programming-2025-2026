package uz.itpu.pt1.stax;

import uz.itpu.model.Employee;

import java.util.List;

/**
 * Result of StAX parsing with extracted data and event statistics.
 */
public final class StaxParseResult {

    private final List<Employee> employees;
    private final int cdataSections;
    private final int startElements;
    private final int endElements;
    private final int characterEvents;

    /**
     * Creates immutable StAX parse result.
     *
     * @param employees parsed employees
     * @param cdataSections number of CDATA sections
     * @param startElements number of start element events
     * @param endElements number of end element events
     * @param characterEvents number of character events
     */
    public StaxParseResult(
            List<Employee> employees,
            int cdataSections,
            int startElements,
            int endElements,
            int characterEvents
    ) {
        this.employees = List.copyOf(employees);
        this.cdataSections = cdataSections;
        this.startElements = startElements;
        this.endElements = endElements;
        this.characterEvents = characterEvents;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public int getCdataSections() {
        return cdataSections;
    }

    public int getStartElements() {
        return startElements;
    }

    public int getEndElements() {
        return endElements;
    }

    public int getCharacterEvents() {
        return characterEvents;
    }
}

