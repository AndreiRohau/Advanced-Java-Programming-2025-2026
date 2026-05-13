package uz.itpu.pt2.jaxb;

import uz.itpu.model.Employee;

public class MainJaxb {

	public static void main(String[] args) {
		EmployeeJaxbParser parser = new EmployeeJaxbParser();
		JaxbParseResult result = parser.parseFromResource("employee-example-ns.xml");

		System.out.println("JAXB parsed employees: " + result.getEmployees().size());
		System.out.println("JAXB root element: " + result.getRootElementName());
		System.out.println("JAXB namespace: " + result.getNamespaceUri());

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

		System.out.println("Marshalled XML preview:");
		System.out.println(result.getMarshalledXml());
	}
}
