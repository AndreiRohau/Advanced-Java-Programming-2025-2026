package uz.itpu.workingWithTransactionsUsingJdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.CountDownLatch;

/**
 * Demonstrates the <b>Phantom Read</b> transaction anomaly.
 *
 * <p>Scenario:
 * <ol>
 *   <li>Transaction A counts employees with salary &gt;
 *       {@value TransactionHelper#PHANTOM_SALARY_THRESHOLD} (first count).</li>
 *   <li>Transaction B inserts a new high-salary employee and commits.</li>
 *   <li>Transaction A counts again (second count).</li>
 * </ol>
 *
 * <p>Without isolation ({@code applyIsolation = false}):
 * Reader uses {@code REPEATABLE_READ} → the new row appears in the second count (phantom).<br>
 * With isolation ({@code applyIsolation = true}):
 * Reader uses {@code SERIALIZABLE} → both counts return the same value.
 */
public class PhantomReadDemo {

    private PhantomReadDemo() {}

    /**
     * Runs the phantom-read demo.
     *
     * @param applyIsolation {@code true} to use SERIALIZABLE (prevents the anomaly);
     *                       {@code false} to use REPEATABLE_READ (allows phantom reads)
     */
    public static void run(boolean applyIsolation) {
        System.out.println("\n======================================================");
        System.out.println("  PHANTOM READ  (isolation applied = " + applyIsolation + ")");
        System.out.println("======================================================");

        // Ensure the phantom row does not exist before the demo
        TransactionHelper.deletePhantomEmployee();

        CountDownLatch readerFirstCount = new CountDownLatch(1);
        CountDownLatch writerInserted   = new CountDownLatch(1);

        // --- Transaction A: reader thread ---
        Thread reader = new Thread(() -> {
            try (Connection conn = TransactionHelper.openConnection()) {
                conn.setAutoCommit(false);
                conn.setTransactionIsolation(applyIsolation
                        ? Connection.TRANSACTION_SERIALIZABLE
                        : Connection.TRANSACTION_REPEATABLE_READ);

                long first = TransactionHelper.countHighSalaryEmployees(
                        conn, TransactionHelper.PHANTOM_SALARY_THRESHOLD);
                System.out.printf("  [Reader] First  count (salary > %.0f): %d row(s)%n",
                        TransactionHelper.PHANTOM_SALARY_THRESHOLD, first);
                readerFirstCount.countDown();

                writerInserted.await();

                long second = TransactionHelper.countHighSalaryEmployees(
                        conn, TransactionHelper.PHANTOM_SALARY_THRESHOLD);
                System.out.printf("  [Reader] Second count (salary > %.0f): %d row(s)  ← %s%n",
                        TransactionHelper.PHANTOM_SALARY_THRESHOLD, second,
                        first != second
                                ? "PHANTOM READ occurred! (new row appeared)"
                                : "Consistent count (no phantoms)");

                conn.commit();
            } catch (Exception e) {
                System.err.println("  [Reader] Error: " + e.getMessage());
            }
        }, "ReaderThread");

        // --- Transaction B: writer thread ---
        Thread writer = new Thread(() -> {
            try (Connection conn = TransactionHelper.openConnection()) {
                conn.setAutoCommit(false);
                readerFirstCount.await();

                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO employees (first_name, last_name, email, salary, hire_date) " +
                        "VALUES ('Phantom', 'Employee', 'phantom.employee@itpu.uz', 120000.00, CURRENT_DATE)")) {
                    ps.executeUpdate();
                }
                conn.commit();
                System.out.println("  [Writer] Inserted and committed phantom employee (salary=120000).");
                writerInserted.countDown();
            } catch (Exception e) {
                System.err.println("  [Writer] Error: " + e.getMessage());
            }
        }, "WriterThread");

        reader.start();
        writer.start();
        TransactionHelper.joinQuietly(reader);
        TransactionHelper.joinQuietly(writer);

        // Clean up phantom row after the demo
        TransactionHelper.deletePhantomEmployee();
    }
}

