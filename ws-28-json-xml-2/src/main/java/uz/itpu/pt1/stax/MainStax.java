package uz.itpu.pt1.stax;

import uz.itpu.model.Address;
import uz.itpu.model.Employee;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;

public class MainStax {

	public static void main(String[] args) {
		EmployeeStaxParser parser = new EmployeeStaxParser();
		StaxParseResult result = parser.parseFromResource("employee-example.xml");

		Employee newEmployee = new Employee(
				184,
				"Dilshod",
				"Tursunov",
				true,
				new BigDecimal("9100.00"),
				"Platform",
				List.of("Java", "Kafka", "PostgreSQL"),
				new Address("Nukus", "Uzbekistan", "230100")
		);

		Path outputPath = parser.createUpdatedXmlFromResource(
				"employee-example.xml",
				"employee-example-stax-updated.xml",
				newEmployee
		);

		System.out.println("StAX parsed employees: " + result.getEmployees().size());
		System.out.println("StAX CDATA sections: " + result.getCdataSections());
		System.out.println("StAX start elements: " + result.getStartElements());
		System.out.println("StAX end elements: " + result.getEndElements());
		System.out.println("StAX character events: " + result.getCharacterEvents());
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
