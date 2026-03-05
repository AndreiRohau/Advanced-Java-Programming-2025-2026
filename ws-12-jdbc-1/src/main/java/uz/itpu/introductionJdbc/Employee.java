package uz.itpu.introductionJdbc;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Immutable value object representing a row in the {@code employees} table.
 * Uses a Java 17 {@code record} for conciseness.
 */
public record Employee(
        int id,
        String firstName,
        String lastName,
        String email,
        BigDecimal salary,
        LocalDate hireDate,
        Integer departmentId
) {
    /** Full name convenience method. */
    public String fullName() {
        return firstName + " " + lastName;
    }
}

