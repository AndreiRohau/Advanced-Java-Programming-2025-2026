package uz.itpu.workingWithTransactionsUsingJdbc;

/**
 * Entry point for the transaction-isolation demo suite.
 *
 * <p>Toggle {@code IS_ISOLATION_APPLIED} to switch between observing an anomaly
 * and seeing it prevented by the appropriate isolation level:
 * <ul>
 *   <li>{@code false} – anomalies are visible
 *       (Dirty read: Savepoint simulation | Non-repeatable: READ_COMMITTED | Phantom: READ_COMMITTED)</li>
 *   <li>{@code true}  – anomalies are prevented
 *       (Dirty read: commit-before-read | Non-repeatable: REPEATABLE_READ | Phantom: SERIALIZABLE)</li>
 * </ul>
 *
 * @see DirtyReadDemo
 * @see NonRepeatableReadDemo
 * @see PhantomReadDemo
 */
public class MainIsolation {

    private static final boolean IS_ISOLATION_APPLIED = false;

    public static void main(String[] args) {
        // Simulated via Savepoint (because PostgreSQL ignores READ_UNCOMMITTED)
        DirtyReadDemo.run(IS_ISOLATION_APPLIED);

        // Solution: REPEATABLE_READ prevents non-repeatable reads
        NonRepeatableReadDemo.run(IS_ISOLATION_APPLIED);

        // Solution: SERIALIZABLE prevents phantom reads (READ_COMMITTED used as unprotected level)
        PhantomReadDemo.run(IS_ISOLATION_APPLIED);
    }
}
