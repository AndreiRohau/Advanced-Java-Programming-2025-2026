package uz.itpu.creatingDbQueriesUsingStatementObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Example1FetchOperation {
    public static void main(String[] args) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = null; // todo Get the connection
            statement = connection.createStatement();

            String sqlQuery = "SELECT * FROM my_table";
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            // Process the ResultSet to retrieve the query results
        } catch (SQLException e) {
            // Handle any errors.
            throw e; // as an example
        } finally {
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
}
