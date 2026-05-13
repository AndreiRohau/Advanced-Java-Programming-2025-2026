package uz.itpu.pt1.sax;

import uz.itpu.model.Employee;

public class MainSax {

	public static void main(String[] args) {
		EmployeeSaxParser parser = new EmployeeSaxParser();
		SaxParseResult result = parser.parseFromResource("employee-example.xml");

		System.out.println("SAX parsed employees: " + result.getEmployees().size());
		System.out.println("CDATA sections captured: " + result.getCdataSections());

		for (Employee employee : result.getEmployees()) {
			System.out.printf(
					"#%d %s %s | active=%s | salary=%s | dept=%s | city=%s%n",
					employee.getId(),
					employee.getFirstName(),
					employee.getLastName(),
					employee.isActive(),
					employee.getSalary(),
					employee.getDepartment(),
					employee.getAddress().getCity()
			);
		}
	}
}
