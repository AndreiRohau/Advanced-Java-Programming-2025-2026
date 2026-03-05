package uz.itpu.creatingDbQueriesUsingStatementObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcExample {
    public static void main(String[] args) throws SQLException {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1/db-example-01?useSSL=false", "root", "pass123");
        } catch (SQLException e) {
            // Handle any errors.
            throw e; // as an example
        } finally {
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
}
