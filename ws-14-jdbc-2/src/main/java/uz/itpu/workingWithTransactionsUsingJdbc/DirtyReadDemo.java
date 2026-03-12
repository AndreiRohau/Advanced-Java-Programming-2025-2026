package uz.itpu.workingWithTransactionsUsingJdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.CountDownLatch;

/**
 * Demonstrates the <b>Dirty Read</b> transaction anomaly.
 *
 * <p>Scenario:
 * <ol>
 *   <li>Transaction B (writer) updates Alice's salary to
 *       {@value TransactionHelper#DIRTY_SALARY_VALUE} but does <em>NOT</em> commit.</li>
 *   <li>Transaction A (reader) reads Alice's salary.</li>
 *   <li>Transaction B rolls back its change.</li>
 * </ol>
 *
 * <p>Without isolation ({@code applyIsolation = false}):
 * Reader uses {@code READ_UNCOMMITTED} → sees the dirty value.<br>
 * With isolation ({@code applyIsolation = true}):
 * Reader uses {@code READ_COMMITTED} → sees the original committed salary.
 *
 * <p><b>Note:</b> PostgreSQL does not implement {@code READ_UNCOMMITTED}; it silently
 * upgrades it to {@code READ_COMMITTED}, so the dirty-read anomaly will not be observable
 * on PostgreSQL regardless of the flag.
 */
public class DirtyReadDemo {

    private DirtyReadDemo() {}

    /**
     * Runs the dirty-read demo.
     *
     * @param applyIsolation {@code true} to use READ_COMMITTED (prevents the anomaly);
     *                       {@code false} to use READ_UNCOMMITTED (allows dirty reads)
     */
    public static void run(boolean applyIsolation) {
        System.out.println("\n======================================================");
        System.out.println("  DIRTY READ  (isolation applied = " + applyIsolation + ")");
        System.out.println("======================================================");

        CountDownLatch writerHasUpdated = new CountDownLatch(1);
        CountDownLatch readerHasDone    = new CountDownLatch(1);

        // --- Transaction B: writer thread ---
        Thread writer = new Thread(() -> {
            try (Connection conn = TransactionHelper.openConnection()) {
                conn.setAutoCommit(false);
                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE employees SET salary = ? WHERE id = ?")) {
                    ps.setDouble(1, TransactionHelper.DIRTY_SALARY_VALUE);
                    ps.setInt(2, TransactionHelper.TARGET_EMPLOYEE_ID);
                    ps.executeUpdate();
                }
                System.out.printf("  [Writer] Updated salary to %.2f – NOT committed yet.%n",
                        TransactionHelper.DIRTY_SALARY_VALUE);

                writerHasUpdated.countDown(); // signal reader: dirty data is "live"
                readerHasDone.await();        // wait for reader to finish

                conn.rollback();
                System.out.println("  [Writer] Rolled back the update.");
            } catch (Exception e) {
                System.err.println("  [Writer] Error: " + e.getMessage());
            }
        }, "WriterThread");

        // --- Transaction A: reader thread ---
        Thread reader = new Thread(() -> {
            try (Connection conn = TransactionHelper.openConnection()) {
                conn.setAutoCommit(false);
                conn.setTransactionIsolation(applyIsolation
                        ? Connection.TRANSACTION_READ_COMMITTED
                        : Connection.TRANSACTION_READ_UNCOMMITTED);

                writerHasUpdated.await(); // wait until dirty data exists

                double salary = TransactionHelper.readSalary(conn, TransactionHelper.TARGET_EMPLOYEE_ID);
                System.out.printf("  [Reader] Salary read = %.2f  ← %s%n",
                        salary,
                        salary == TransactionHelper.DIRTY_SALARY_VALUE
                                ? "DIRTY READ occurred! (uncommitted value)"
                                : "Clean read (committed value)");

                readerHasDone.countDown();
                conn.commit();
            } catch (Exception e) {
                System.err.println("  [Reader] Error: " + e.getMessage());
            }
        }, "ReaderThread");

        writer.start();
        reader.start();
        TransactionHelper.joinQuietly(writer);
        TransactionHelper.joinQuietly(reader);
    }
}

