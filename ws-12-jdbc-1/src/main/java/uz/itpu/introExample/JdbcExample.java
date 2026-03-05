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

//        firstOption();
        secondOption();
    }

    private static void firstOption() throws SQLException, ClassNotFoundException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Registering a Driver (optional, for old versions of JDBC).
            Class.forName("org.postgresql.Driver");
            // or
//            DriverManager.registerDriver(new org.postgresql.Driver());

            // Open a connection.
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/jdbc_demo", "jdbc_user", "jdbc_pass");
            // Create an object to send requests
            statement = connection.createStatement();
            // Execute a query.
            resultSet = statement.executeQuery("SELECT * FROM employees");

            // Extract data from result set.
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1) + " - " + resultSet.getString(2));
            }
        } catch (SQLException e) {
            // Handle any errors.
            throw e; // as an example
        } catch (ClassNotFoundException e) {
            // Handle any errors.
            throw e; // as an example
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    // Handle any errors.
                    throw e; // as an example
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    // Handle any errors.
                    throw e; // as an example
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    // Handle any errors.
                    throw e; // as an example
                }
            }
        }
    }

    private static void secondOption() throws SQLException, ClassNotFoundException {
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/jdbc_demo", "jdbc_user", "jdbc_pass");
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM employees")) {

            while (rs.next()) {
                System.out.println(rs.getInt(1) + " - " + rs.getString(2));
            }
        } catch (SQLException e) {
            // Handle any errors.
            throw e; // as an example
        }
    }
}
