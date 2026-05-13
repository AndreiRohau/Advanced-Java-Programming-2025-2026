package uz.itpu.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Immutable employee model parsed by SAX.
 */
public final class Employee {

    private final int id;
    private final String firstName;
    private final String lastName;
    private final boolean active;
    private final BigDecimal salary;
    private final String department;
    private final List<String> skills;
    private final Address address;

    /**
     * Creates immutable employee instance.
     */
    public Employee(
            int id,
            String firstName,
            String lastName,
            boolean active,
            BigDecimal salary,
            String department,
            List<String> skills,
            Address address
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = active;
        this.salary = salary;
        this.department = department;
        this.skills = List.copyOf(skills);
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isActive() {
        return active;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public String getDepartment() {
        return department;
    }

    public List<String> getSkills() {
        return skills;
    }

    public Address getAddress() {
        return address;
    }
}


