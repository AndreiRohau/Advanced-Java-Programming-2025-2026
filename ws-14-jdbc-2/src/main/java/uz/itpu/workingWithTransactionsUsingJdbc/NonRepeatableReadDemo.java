package uz.itpu.workingWithTransactionsUsingJdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.CountDownLatch;

/**
 * Demonstrates the <b>Non-Repeatable Read</b> transaction anomaly.
 *
 * <p>Scenario:
 * <ol>
 *   <li>Transaction A reads Alice's salary (first read).</li>
 *   <li>Transaction B updates and commits Alice's salary to
 *       {@value TransactionHelper#UPDATED_SALARY_VALUE}.</li>
 *   <li>Transaction A reads Alice's salary again (second read).</li>
 * </ol>
 *
 * <p>Without isolation ({@code applyIsolation = false}):
 * Reader uses {@code READ_COMMITTED} → second read returns the new committed value (anomaly).<br>
 * With isolation ({@code applyIsolation = true}):
 * Reader uses {@code REPEATABLE_READ} → both reads return the same value.
 */
public class NonRepeatableReadDemo {

    private NonRepeatableReadDemo() {}

    /**
     * Runs the non-repeatable-read demo.
     *
     * @param applyIsolation {@code true} to use REPEATABLE_READ (prevents the anomaly);
     *                       {@code false} to use READ_COMMITTED (allows non-repeatable reads)
     */
    public static void run(boolean applyIsolation) {
        System.out.println("\n======================================================");
        System.out.println("  NON-REPEATABLE READ  (isolation applied = " + applyIsolation + ")");
        System.out.println("======================================================");

        // Reset to a known salary before the demo
        TransactionHelper.resetSalary(TransactionHelper.TARGET_EMPLOYEE_ID, 75_000.00);

        CountDownLatch readerFirstRead = new CountDownLatch(1);
        CountDownLatch writerCommitted = new CountDownLatch(1);

        // --- Transaction A: reader thread ---
        Thread reader = new Thread(() -> {
            try (Connection conn = TransactionHelper.openConnection()) {
                conn.setAutoCommit(false);
                conn.setTransactionIsolation(applyIsolation
                        ? Connection.TRANSACTION_REPEATABLE_READ
                        : Connection.TRANSACTION_READ_COMMITTED);

                double first = TransactionHelper.readSalary(conn, TransactionHelper.TARGET_EMPLOYEE_ID);
                System.out.printf("  [Reader] First  read: salary = %.2f%n", first);
                readerFirstRead.countDown(); // let writer proceed

                writerCommitted.await();     // wait for writer to commit

                double second = TransactionHelper.readSalary(conn, TransactionHelper.TARGET_EMPLOYEE_ID);
                System.out.printf("  [Reader] Second read: salary = %.2f  ← %s%n",
                        second,
                        first != second
                                ? "NON-REPEATABLE READ occurred! (value changed)"
                                : "Consistent read (same value both times)");

                conn.commit();
            } catch (Exception e) {
                System.err.println("  [Reader] Error: " + e.getMessage());
            }
        }, "ReaderThread");

        // --- Transaction B: writer thread ---
        Thread writer = new Thread(() -> {
            try (Connection conn = TransactionHelper.openConnection()) {
                conn.setAutoCommit(false);
                readerFirstRead.await(); // wait until reader has done its first read

                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE employees SET salary = ? WHERE id = ?")) {
                    ps.setDouble(1, TransactionHelper.UPDATED_SALARY_VALUE);
                    ps.setInt(2, TransactionHelper.TARGET_EMPLOYEE_ID);
                    ps.executeUpdate();
                }
                conn.commit();
                System.out.printf("  [Writer] Committed salary update to %.2f.%n",
                        TransactionHelper.UPDATED_SALARY_VALUE);
                writerCommitted.countDown();
            } catch (Exception e) {
                System.err.println("  [Writer] Error: " + e.getMessage());
            }
        }, "WriterThread");

        reader.start();
        writer.start();
        TransactionHelper.joinQuietly(reader);
        TransactionHelper.joinQuietly(writer);

        // Restore original salary so subsequent demos start from a clean state
        TransactionHelper.resetSalary(TransactionHelper.TARGET_EMPLOYEE_ID, 75_000.00);
    }
}

