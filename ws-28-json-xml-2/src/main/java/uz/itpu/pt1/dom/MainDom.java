package uz.itpu.pt1.dom;

import uz.itpu.model.Address;
import uz.itpu.model.Employee;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;

public class MainDom {

	public static void main(String[] args) {
		EmployeeDomParser parser = new EmployeeDomParser();
		DomParseResult result = parser.parseFromResource("employee-example-ns.xml");

		Employee newEmployee = new Employee(
				134,
				"Dilshod",
				"Tursunov",
				true,
				new BigDecimal("9100.00"),
				"Platform",
				List.of("Java", "Kafka", "PostgreSQL"),
				new Address("Nukus", "Uzbekistan", "230100")
		);

		Path outputPath = parser.createUpdatedXmlFromResource(
				"employee-example-ns.xml",
				"employee-example-dom-updated.xml",
				newEmployee
		);

		System.out.println("DOM parsed employees: " + result.getEmployees().size());
		System.out.println("DOM CDATA sections: " + result.getCdataSections());
		System.out.println("DOM element nodes: " + result.getElementNodes());
		System.out.println("DOM text nodes: " + result.getTextNodes());
		System.out.println("Updated XML saved to: " + outputPath);

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
