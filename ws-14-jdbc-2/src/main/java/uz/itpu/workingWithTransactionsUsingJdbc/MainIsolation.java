package uz.itpu.workingWithTransactionsUsingJdbc;

/**
 * Entry point for the transaction-isolation demo suite.
 *
 * <p>Toggle {@code IS_ISOLATION_APPLIED} to switch between observing an anomaly
 * and seeing it prevented by the appropriate isolation level:
 * <ul>
 *   <li>{@code false} – anomalies are visible (READ_UNCOMMITTED / READ_COMMITTED / REPEATABLE_READ)</li>
 *   <li>{@code true}  – anomalies are prevented (READ_COMMITTED / REPEATABLE_READ / SERIALIZABLE)</li>
 * </ul>
 *
 * @see DirtyReadDemo
 * @see NonRepeatableReadDemo
 * @see PhantomReadDemo
 */
public class MainIsolation {

    private static final boolean IS_ISOLATION_APPLIED = false;

    public static void main(String[] args) {
        // Solution: READ_COMMITTED prevents dirty reads
        DirtyReadDemo.run(IS_ISOLATION_APPLIED);

        // Solution: REPEATABLE_READ prevents non-repeatable reads
        NonRepeatableReadDemo.run(IS_ISOLATION_APPLIED);

        // Solution: SERIALIZABLE prevents phantom reads
        PhantomReadDemo.run(IS_ISOLATION_APPLIED);
    }
}
