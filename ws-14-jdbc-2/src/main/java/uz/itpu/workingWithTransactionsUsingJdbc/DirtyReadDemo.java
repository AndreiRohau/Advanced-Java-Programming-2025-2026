package uz.itpu.workingWithTransactionsUsingJdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Savepoint;

/**
 * Demonstrates the <b>Dirty Read</b> transaction anomaly.
 *
 * <p><b>PostgreSQL note:</b> PostgreSQL does not implement {@code READ_UNCOMMITTED} —
 * it silently upgrades it to {@code READ_COMMITTED}, making a true two-connection dirty
 * read impossible on this engine. Instead, the anomaly is simulated within a <em>single
 * connection</em> using a {@link Savepoint}:
 * <ol>
 *   <li>Write a dirty salary value (not yet committed).</li>
 *   <li>Read it back — this is what another session would see on a DB that allows dirty reads.</li>
 *   <li>Roll back to the savepoint — the dirty value disappears.</li>
 *   <li>Read again — now showing the clean, committed value.</li>
 * </ol>
 *
 * <p>Without isolation ({@code applyIsolation = false}):
 * The dirty value is printed before the rollback, showing what a dirty read would expose.<br>
 * With isolation ({@code applyIsolation = true}):
 * The write is committed before reading, so the reader always sees a consistent value.
 */
public class DirtyReadDemo {

    private DirtyReadDemo() {}

    /**
     * Runs the dirty-read demo.
     *
     * @param applyIsolation {@code true} – commit before reading (clean);
     *                       {@code false} – read before commit then rollback (dirty simulation)
     */
    public static void run(boolean applyIsolation) {
        System.out.println("\n======================================================");
        System.out.println("  DIRTY READ  (isolation applied = " + applyIsolation + ")");
        System.out.println("  (simulated via Savepoint – PostgreSQL ignores READ_UNCOMMITTED)");
        System.out.println("======================================================");

        try (Connection conn = TransactionHelper.openConnection()) {
            conn.setAutoCommit(false);

            double originalSalary = TransactionHelper.readSalary(conn, TransactionHelper.TARGET_EMPLOYEE_ID);
            System.out.printf("  [Before] Alice's committed salary = %.2f%n", originalSalary);

            if (applyIsolation) {
                // CLEAN path: update → commit → read  (no dirty data ever visible)
                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE employees SET salary = ? WHERE id = ?")) {
                    ps.setDouble(1, TransactionHelper.DIRTY_SALARY_VALUE);
                    ps.setInt(2, TransactionHelper.TARGET_EMPLOYEE_ID);
                    ps.executeUpdate();
                }
                conn.commit(); // committed first, then read
                double salary = TransactionHelper.readSalary(conn, TransactionHelper.TARGET_EMPLOYEE_ID);
                System.out.printf("  [Reader] Salary after commit = %.2f  ← Clean read (committed value)%n", salary);

                // restore original value
                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE employees SET salary = ? WHERE id = ?")) {
                    ps.setDouble(1, originalSalary);
                    ps.setInt(2, TransactionHelper.TARGET_EMPLOYEE_ID);
                    ps.executeUpdate();
                }
                conn.commit();

            } else {
                // DIRTY path: update → savepoint → read (sees dirty) → rollback to savepoint → read again (clean)
                Savepoint beforeDirtyWrite = conn.setSavepoint("beforeDirtyWrite");

                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE employees SET salary = ? WHERE id = ?")) {
                    ps.setDouble(1, TransactionHelper.DIRTY_SALARY_VALUE);
                    ps.setInt(2, TransactionHelper.TARGET_EMPLOYEE_ID);
                    ps.executeUpdate();
                }
                System.out.printf("  [Writer] Updated salary to %.2f – NOT committed yet.%n",
                        TransactionHelper.DIRTY_SALARY_VALUE);

                double dirtySalary = TransactionHelper.readSalary(conn, TransactionHelper.TARGET_EMPLOYEE_ID);
                System.out.printf("  [Reader] Salary read = %.2f  ← DIRTY READ! (uncommitted value)%n",
                        dirtySalary);

                conn.rollback(beforeDirtyWrite); // undo the dirty write
                System.out.println("  [Writer] Rolled back to savepoint – dirty value is gone.");

                double cleanSalary = TransactionHelper.readSalary(conn, TransactionHelper.TARGET_EMPLOYEE_ID);
                System.out.printf("  [Reader] Salary after rollback = %.2f  ← Back to committed value%n",
                        cleanSalary);

                conn.commit();
            }

        } catch (Exception e) {
            System.err.println("  Error: " + e.getMessage());
        }
    }
}

