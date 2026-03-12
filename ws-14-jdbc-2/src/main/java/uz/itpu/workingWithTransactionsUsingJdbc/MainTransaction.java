package uz.itpu.workingWithTransactionsUsingJdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Minimal examples of JDBC transaction management:
 * <ol>
 *   <li>{@link #demonstrateCommit()}  – two salary updates that succeed → commit.</li>
 *   <li>{@link #demonstrateRollback()} – two salary updates where the second fails → rollback.</li>
 * </ol>
 */
public class MainTransaction {

    private static final String URL      = "jdbc:postgresql://localhost:5432/jdbc_demo";
    private static final String USER     = "jdbc_user";
    private static final String PASSWORD = "jdbc_pass";

    public static void main(String[] args) {
        demonstrateCommit();
        demonstrateRollback();
    }

    // -------------------------------------------------------------------------

    /**
     * Happy path: raises Alice's and Bob's salaries, then commits.
     */
    private static void demonstrateCommit() {
        System.out.println("\n--- COMMIT demo ---");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            conn.setAutoCommit(false); // start transaction

            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE employees SET salary = salary + 1000 WHERE id = ?")) {

                ps.setInt(1, 1); // Alice
                ps.executeUpdate();
                System.out.println("  Alice's salary raised by 1000.");

                ps.setInt(1, 2); // Bob
                ps.executeUpdate();
                System.out.println("  Bob's   salary raised by 1000.");

                conn.commit(); // both updates succeed → persist them
                System.out.println("  Committed successfully.");

            } catch (SQLException e) {
                conn.rollback();
                System.err.println("  Rolled back due to: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("  Connection error: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------

    /**
     * Error path: raises Alice's salary, then tries an invalid update (id = -1) → rollback.
     * Neither change is persisted.
     */
    private static void demonstrateRollback() {
        System.out.println("\n--- ROLLBACK demo ---");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            conn.setAutoCommit(false); // start transaction

            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE employees SET salary = salary + 5000 WHERE id = ?")) {

                ps.setInt(1, 1); // Alice – succeeds
                ps.executeUpdate();
                System.out.println("  Alice's salary raised by 5000 (not committed yet).");

                // Intentionally trigger a constraint violation: id column cannot be NULL
                try (PreparedStatement bad = conn.prepareStatement(
                        "UPDATE employees SET id = NULL WHERE id = 1")) {
                    bad.executeUpdate(); // throws → goes to catch
                }

                conn.commit(); // never reached
                System.out.println("  Committed.");

            } catch (SQLException e) {
                conn.rollback(); // undo Alice's update too
                System.out.println("  Error occurred: " + e.getMessage().strip());
                System.out.println("  Rolled back – Alice's salary is unchanged.");
            }

        } catch (SQLException e) {
            System.err.println("  Connection error: " + e.getMessage());
        }
    }
}
