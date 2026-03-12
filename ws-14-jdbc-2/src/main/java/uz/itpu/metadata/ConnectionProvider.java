package uz.itpu.metadata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Provides a JDBC {@link Connection} using driver manager.
 * Connection parameters are read from system properties or fall back to defaults.
 */
public class ConnectionProvider {

    private static final String DEFAULT_URL      = "jdbc:postgresql://localhost:5432/jdbc_demo";
    private static final String DEFAULT_USER     = "jdbc_user";
    private static final String DEFAULT_PASSWORD = "jdbc_pass";

    private ConnectionProvider() {}

    /**
     * Opens and returns a new {@link Connection}.
     * Override defaults via system properties:
     * {@code -Ddb.url=...  -Ddb.user=...  -Ddb.password=...}
     *
     * @return a new JDBC connection
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        String url      = System.getProperty("db.url",      DEFAULT_URL);
        String user     = System.getProperty("db.user",     DEFAULT_USER);
        String password = System.getProperty("db.password", DEFAULT_PASSWORD);
        return DriverManager.getConnection(url, user, password);
    }
}

