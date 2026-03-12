package uz.itpu.connectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrates {@link SimpleConnectionPool}:
 * <ol>
 *   <li>Normal use – borrow, query, release.</li>
 *   <li>Pool exhaustion – more threads than core size forces temporary connections.</li>
 *   <li>Forced shutdown – a connection that was never released is closed by {@code shutdown()}.</li>
 * </ol>
 *
 * Override defaults with system properties:
 * <pre>
 *   -Ddb.url=jdbc:postgresql://localhost:5432/jdbc_demo
 *   -Ddb.user=jdbc_user
 *   -Ddb.password=jdbc_pass
 *   -Ddb.poolSize=3
 * </pre>
 */
public class MainConnectionPool {

    private static final String DEFAULT_URL       = "jdbc:postgresql://localhost:5432/jdbc_demo";
    private static final String DEFAULT_USER      = "jdbc_user";
    private static final String DEFAULT_PASSWORD  = "jdbc_pass";
    private static final int    DEFAULT_POOL_SIZE  = 3;

    public static void main(String[] args) throws Exception {
        String url      = System.getProperty("db.url",      DEFAULT_URL);
        String user     = System.getProperty("db.user",     DEFAULT_USER);
        String password = System.getProperty("db.password", DEFAULT_PASSWORD);
        int    poolSize = Integer.parseInt(
                            System.getProperty("db.poolSize", String.valueOf(DEFAULT_POOL_SIZE)));

        var pool = new SimpleConnectionPool(url, user, password, poolSize);

        // ----------------------------------------------------------------
        // 1. Normal use: borrow, query, release
        // ----------------------------------------------------------------
        System.out.println("\n=== 1. Normal use ===");
        Connection conn = pool.acquire();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT id, first_name, last_name, salary FROM employees ORDER BY id LIMIT 5");
             ResultSet rs = ps.executeQuery()) {

            System.out.printf("%-4s %-10s %-10s %s%n", "ID", "First", "Last", "Salary");
            System.out.println("-".repeat(38));
            while (rs.next()) {
                System.out.printf("%-4d %-10s %-10s %.2f%n",
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getBigDecimal("salary"));
            }
        } finally {
            pool.release(conn);
        }

        // ----------------------------------------------------------------
        // 2. Exhaustion: more threads than core size → temporary connections
        // ----------------------------------------------------------------
        System.out.println("\n=== 2. Pool exhaustion (threads=" + (poolSize + 2) + ", core=" + poolSize + ") ===");
        int taskCount = poolSize + 2;
        ExecutorService executor = Executors.newFixedThreadPool(taskCount);
        for (int i = 0; i < taskCount; i++) {
            final int taskId = i + 1;
            executor.submit(() -> {
                try {
                    Connection c = pool.acquire();
                    try (PreparedStatement ps = c.prepareStatement(
                            "SELECT COUNT(*) AS cnt FROM employees");
                         ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            System.out.printf("  Thread %2d – employee count: %d%n",
                                    taskId, rs.getLong("cnt"));
                        }
                    } finally {
                        pool.release(c); // core → back to pool; temporary → closed
                    }
                } catch (SQLException e) {
                    System.err.println("  Thread " + taskId + " error: " + e.getMessage());
                }
            });
        }
        executor.shutdown();
        boolean finished = executor.awaitTermination(30, TimeUnit.SECONDS);
        if (!finished) {
            System.err.println("  WARNING: not all threads finished within the timeout.");
        }

        // ----------------------------------------------------------------
        // 3. Leaked connection: acquired but never released.
        //    shutdown() must force-close it.
        // ----------------------------------------------------------------
        System.out.println("\n=== 3. Leaked connection + forced shutdown ===");

        Connection leaked = pool.acquire();
        System.out.println("  Acquired a connection and 'forgot' to release it.");
        System.out.printf("  taken before shutdown: %d%n", pool.takenCount());

        // Simulate app stopping without the caller returning the connection.
        // shutdown() iterates the 'taken' Set and force-closes every entry.
        pool.shutdown();

        System.out.printf("  taken after  shutdown: %d%n", pool.takenCount());
        System.out.println("  Verifying leaked connection is closed: " + leaked.isClosed());
    }
}



