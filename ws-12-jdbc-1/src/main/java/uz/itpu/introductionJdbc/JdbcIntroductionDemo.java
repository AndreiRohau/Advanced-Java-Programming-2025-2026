package uz.itpu.introductionJdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcIntroductionDemo {

    public static void main(String[] args) {
        var demo = new JdbcIntroductionDemo();
        demo.showDatabaseMetadata();
        demo.listAllDepartments();
        demo.listAllEmployees();
    }

    /**
     * Prints database product name, version, and JDBC driver info.
     */
    public void showDatabaseMetadata() {
        System.out.println("=== DATABASE METADATA ===");
        try (Connection con = DbConfig.getInstance().getConnection()) {

            DatabaseMetaData meta = con.getMetaData();
            System.out.println("DB product  : " + meta.getDatabaseProductName());
            System.out.println("DB version  : " + meta.getDatabaseProductVersion());
            System.out.println("Driver name : " + meta.getDriverName());
            System.out.println("Driver ver  : " + meta.getDriverVersion());
            System.out.println("JDBC URL    : " + meta.getURL());

        } catch (SQLException e) {
            System.err.println("Could not retrieve metadata: " + e.getMessage());
        }
    }

    /**
     * Lists all departments using a plain {@link Statement}.
     * Use this only for static SQL with <em>no user input</em>.
     */
    public void listAllDepartments() {
        System.out.println("\n=== ALL DEPARTMENTS ===");
        String sql = "SELECT id, name, location FROM departments ORDER BY id";

        try (Connection con = DbConfig.getInstance().getConnection();
             Statement  stmt = con.createStatement();
             ResultSet  rs   = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.printf("  [%d] %-15s %s%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("location"));
            }

        } catch (SQLException e) {
            System.err.println("Failed to list departments: " + e.getMessage());
        }
    }

    /**
     * Fetches all employees and maps each row to an {@link Employee} record.
     *
     * @return list of all employees; empty list on error
     */
    public List<Employee> findAllEmployees() {
        String sql = "SELECT id, first_name, last_name, email, salary, hire_date, department_id " +
                     "FROM employees ORDER BY id";
        var employees = new ArrayList<Employee>();

        try (Connection con = DbConfig.getInstance().getConnection();
             Statement  stmt = con.createStatement();
             ResultSet  rs   = stmt.executeQuery(sql)) {

            while (rs.next()) {
                employees.add(mapEmployee(rs));
            }

        } catch (SQLException e) {
            System.err.println("Failed to find employees: " + e.getMessage());
        }
        return employees;
    }

    /** Prints all employees to stdout. */
    public void listAllEmployees() {
        System.out.println("\n=== ALL EMPLOYEES ===");
        findAllEmployees().forEach(e ->
                System.out.printf("  [%d] %-20s | %-30s | salary=%-10s | hired=%s | deptId=%s%n",
                        e.id(), e.fullName(), e.email(),
                        e.salary(), e.hireDate(), e.departmentId()));
    }

    // ------------------------------------------------------------------
    // Helper – map a ResultSet row to an Employee record
    // ------------------------------------------------------------------

    private Employee mapEmployee(ResultSet rs) throws SQLException {
        var deptId = rs.getObject("department_id");
        return new Employee(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getBigDecimal("salary"),
                rs.getDate("hire_date").toLocalDate(),
                deptId != null ? (Integer) deptId : null
        );
    }
}

