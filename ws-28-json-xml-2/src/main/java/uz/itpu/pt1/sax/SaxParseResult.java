package uz.itpu.pt1.sax;

import uz.itpu.model.Employee;

import java.util.List;

/**
 * Result of SAX parsing with extracted domain data and parser event statistics.
 */
public final class SaxParseResult {

	private final List<Employee> employees;
	private final int cdataSections;

	/**
	 * Creates parse result snapshot.
	 *
	 * @param employees parsed employees from XML
	 * @param cdataSections number of CDATA sections observed by lexical handler
	 */
	public SaxParseResult(List<Employee> employees, int cdataSections) {
		this.employees = List.copyOf(employees);
		this.cdataSections = cdataSections;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public int getCdataSections() {
		return cdataSections;
	}
}

