package uz.itpu.introductionJdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Reads {@code db.properties} from the classpath and provides a
 * {@link #getConnection()} factory backed by {@link DriverManager}.
 *
 * <p>Always close the returned connection with try-with-resources.</p>
 */
public final class DbConfig {

    private static final String PROPERTIES_FILE = "db.properties";

    private final String url;
    private final String user;
    private final String password;

    private static final DbConfig INSTANCE = new DbConfig();

    private DbConfig() {
        var props = new Properties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (in == null) {
                throw new IllegalStateException("Cannot find " + PROPERTIES_FILE + " on classpath");
            }
            props.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load " + PROPERTIES_FILE, e);
        }
        this.url      = props.getProperty("db.url");
        this.user     = props.getProperty("db.user");
        this.password = props.getProperty("db.password");
    }

    /** Returns the singleton config instance. */
    public static DbConfig getInstance() {
        return INSTANCE;
    }

    /**
     * Opens and returns a new JDBC {@link Connection}.
     * The caller must close it – use try-with-resources.
     *
     * @return a new {@link Connection}
     * @throws SQLException if the connection cannot be established
     */
    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
    }
}
