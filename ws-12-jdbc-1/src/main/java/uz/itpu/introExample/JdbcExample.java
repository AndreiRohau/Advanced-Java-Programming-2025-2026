package uz.itpu.introExample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Since the java.sql.Connection, java.sql.Statement, java.sql.PreparedStatement, java.sql.CallableStatement,
 * and java.sql.ResultSet types implement the AutoClosable interface,
 * in addition to requiring the close() method to be called after use,
 * they can be used in try-with-resources constructs.
 *
 * The code for closing connections is provided below.
 */
public class JdbcExample {
    public static void main(String[] args)
            throws SQLException, ClassNotFoundException {

        fisrtOption();
        secondOption();
    }

    private static void fisrtOption() throws SQLException, ClassNotFoundException {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            // Registering a Driver (optional, for old versions of JDBC).
            Class.forName("org.gjt.mm.mysql.Driver");

            // Open a connection.
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1/db-example-01?useSSL=false", "root", "pass123");
            // Create an object to send requests
            st = con.createStatement();

            // Execute a query.
            rs = st.executeQuery("SELECT * FROM users");

            // Extract data from result set.
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " - " + rs.getString(2));
            }
        } catch (SQLException e) {
            // Handle any errors.
            throw e; // as an example
        } catch (ClassNotFoundException e) {
            // Handle any errors.
            throw e; // as an example
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    // Handle any errors.
                    throw e; // as an example
                }
            }

            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    // Handle any errors.
                    throw e; // as an example
                }
            }

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    // Handle any errors.
                    throw e; // as an example
                }
            }
        }
    }

    private static void secondOption() throws SQLException, ClassNotFoundException {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1/db-example-01?useSSL=false",
                "root", "pass123");
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM users")) {

            while (rs.next()) {
                System.out.println(rs.getInt(1) + " - " + rs.getString(2));
            }
        } catch (SQLException e) {
            // Handle any errors.
            throw e; // as an example
        }
    }
}
