package uz.itpu.connectionPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A simple JDBC connection pool backed by:
 * <ul>
 *   <li>{@code available} – {@link ArrayBlockingQueue} of idle core connections.</li>
 *   <li>{@code taken}     – unbounded {@link Set} of every connection currently borrowed
 *                           (core or temporary), allowing forced cleanup on shutdown.</li>
 * </ul>
 *
 * <p>The pool is pre-filled with {@code coreSize} persistent connections.
 * When all core connections are busy, {@link #acquire()} opens an extra
 * temporary connection. On {@link #release(Connection)} that temporary
 * connection is physically closed instead of being returned to the pool.
 *
 * <p>Using a {@link ConcurrentHashMap}-backed {@link Set} for {@code taken}
 * means there is no upper bound on concurrent borrows, and the actual
 * {@link Connection} objects are always reachable for forced shutdown.
 */
public class SimpleConnectionPool {

    private final String url;
    private final String user;
    private final String password;
    private final int coreSize;

    private final BlockingQueue<Connection> available;
    // ConcurrentHashMap-backed Set: no capacity limit, thread-safe, holds real references
    private final Set<Connection> taken = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /**
     * Creates the pool and opens {@code coreSize} persistent connections.
     *
     * @param url      JDBC URL
     * @param user     database user
     * @param password database password
     * @param coreSize number of persistent connections to pre-create
     * @throws SQLException if any connection cannot be opened
     */
    public SimpleConnectionPool(String url, String user, String password, int coreSize)
            throws SQLException {
        this.url      = url;
        this.user     = user;
        this.password = password;
        this.coreSize = coreSize;

        this.available = new ArrayBlockingQueue<>(coreSize);

        for (int i = 0; i < coreSize; i++) {
            available.add(DriverManager.getConnection(url, user, password));
        }

        System.out.printf("[Pool] Initialized with %d core connections.%n", coreSize);
    }

    /**
     * Borrows a connection from the pool.
     *
     * <p>If a core connection is idle it is returned immediately.
     * Otherwise a new temporary connection is opened and will be physically
     * closed on {@link #release(Connection)}.
     *
     * @return a {@link Connection} ready for use
     * @throws SQLException if a temporary connection cannot be opened
     */
    public Connection acquire() throws SQLException {
        Connection conn = available.poll(); // non-blocking: null if no idle core conn

        if (conn == null) {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("[Pool] Core pool exhausted – opened a temporary connection.");
        }

        taken.add(conn);
        System.out.printf("[Pool] acquire()  – available: %d, taken: %d%n",
                available.size(), taken.size());
        return conn;
    }

    /**
     * Returns a borrowed connection to the pool.
     *
     * <p>Core connections are put back into {@code available}.
     * Temporary connections (borrowed when the core pool was full) are closed physically.
     *
     * @param conn the connection to release
     * @throws SQLException if closing a temporary connection fails
     */
    public void release(Connection conn) throws SQLException {
        taken.remove(conn);

        if (available.size() < coreSize) {
            available.add(conn);
        } else {
            conn.close();
            System.out.println("[Pool] Temporary connection closed.");
        }

        System.out.printf("[Pool] release()  – available: %d, taken: %d%n",
                available.size(), taken.size());
    }

    /**
     * Gracefully shuts down the pool:
     * <ol>
     *   <li>Closes all idle core connections in {@code available}.</li>
     *   <li>Force-closes every connection still in {@code taken} (leaked / unreturned).</li>
     * </ol>
     * All {@link SQLException}s during close are caught and printed; iteration always completes.
     */
    public void shutdown() {
        // 1. Close idle core connections
        Connection conn;
        while ((conn = available.poll()) != null) {
            closeQuietly(conn, "idle");
        }

        // 2. Force-close any connections still borrowed (not yet released)
        if (!taken.isEmpty()) {
            System.out.printf("[Pool] Forcing close of %d unreturned connection(s).%n", taken.size());
            for (Connection c : taken) {
                closeQuietly(c, "unreturned");
            }
            taken.clear();
        }

        System.out.println("[Pool] Shut down complete.");
    }

    /** @return number of idle core connections currently in the pool */
    public int availableCount() { return available.size(); }

    /** @return number of connections currently borrowed (core + temporary) */
    public int takenCount() { return taken.size(); }

    // -------------------------------------------------------------------------

    private void closeQuietly(Connection c, String label) {
        try {
            c.close();
            System.out.println("[Pool] Closed " + label + " connection.");
        } catch (SQLException e) {
            System.err.println("[Pool] Error closing " + label + " connection: " + e.getMessage());
        }
    }
}






