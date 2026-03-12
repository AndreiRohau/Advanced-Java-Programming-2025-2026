package uz.itpu.workingWithTransactionsUsingJdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Shared constants, connection factory, and query helpers used by all
 * transaction-anomaly demo classes.
 */
public final class TransactionHelper {

    // -------------------------------------------------------------------------
    // DB connection constants – match the docker-compose / k8s setup
    // -------------------------------------------------------------------------
    static final String DB_URL      = "jdbc:postgresql://localhost:5432/jdbc_demo";
    static final String DB_USER     = "jdbc_user";
    static final String DB_PASSWORD = "jdbc_pass";

    // Employee used as the target row in dirty-read / non-repeatable-read demos
    static final int    TARGET_EMPLOYEE_ID   = 1;          // Alice Smith
    static final double DIRTY_SALARY_VALUE   = 999_999.00; // written but never committed
    static final double UPDATED_SALARY_VALUE = 88_000.00;  // committed update

    // Salary threshold used in the phantom-read range query
    static final double PHANTOM_SALARY_THRESHOLD = 50_000.00;

    private TransactionHelper() {
        // utility class – no instances
    }

    // -------------------------------------------------------------------------
    // Connection factory
    // -------------------------------------------------------------------------

    /**
     * Opens a new JDBC connection to the demo database.
     *
     * @return a new {@link Connection}
     * @throws SQLException if the connection cannot be established
     */
    public static Connection openConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // -------------------------------------------------------------------------
    // Query helpers
    // -------------------------------------------------------------------------

    /**
     * Reads the salary of the given employee within an already-open transaction.
     *
     * @param conn       an active connection (auto-commit may be off)
     * @param employeeId the employee's primary key
     * @return the current salary value visible to {@code conn}
     * @throws SQLException if the employee is not found or a DB error occurs
     */
    public static double readSalary(Connection conn, int employeeId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT salary FROM employees WHERE id = ?")) {
            ps.setInt(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("salary");
                }
            }
        }
        throw new SQLException("Employee with id=" + employeeId + " not found.");
    }

    /**
     * Counts employees whose salary exceeds the given threshold.
     *
     * @param conn      an active connection (auto-commit may be off)
     * @param threshold lower-bound salary (exclusive)
     * @return number of matching rows visible to {@code conn}
     * @throws SQLException if a DB error occurs
     */
    public static long countHighSalaryEmployees(Connection conn, double threshold) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) AS cnt FROM employees WHERE salary > ?")) {
            ps.setDouble(1, threshold);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("cnt");
                }
            }
        }
        return 0L;
    }

    // -------------------------------------------------------------------------
    // Setup / teardown helpers
    // -------------------------------------------------------------------------

    /**
     * Resets an employee's salary to a known value outside of any demo transaction.
     *
     * @param employeeId the employee's primary key
     * @param salary     the salary value to set
     */
    public static void resetSalary(int employeeId, double salary) {
        try (Connection conn = openConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE employees SET salary = ? WHERE id = ?")) {
            ps.setDouble(1, salary);
            ps.setInt(2, employeeId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[Setup] Failed to reset salary: " + e.getMessage());
        }
    }

    /**
     * Removes the phantom employee row inserted during the phantom-read demo.
     */
    public static void deletePhantomEmployee() {
        try (Connection conn = openConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM employees WHERE email = 'phantom.employee@itpu.uz'")) {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[Cleanup] Failed to delete phantom employee: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Thread utility
    // -------------------------------------------------------------------------

    /**
     * Joins a thread, swallowing {@link InterruptedException} and re-interrupting
     * the current thread if necessary.
     *
     * @param t the thread to join
     */
    public static void joinQuietly(Thread t) {
        try {
            t.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

