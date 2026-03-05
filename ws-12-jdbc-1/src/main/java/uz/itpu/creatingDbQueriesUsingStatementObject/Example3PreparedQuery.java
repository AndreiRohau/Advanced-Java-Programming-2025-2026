package uz.itpu.creatingDbQueriesUsingStatementObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Example3PreparedQuery {

    public static void main(String[] args) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = null; // todo Get the connection

            String sqlQuery = "SELECT * FROM my_table WHERE column1 = ? AND column2 = ?";
            preparedStatement = connection.prepareStatement(sqlQuery);

            // Set parameter values for the placeholders
            preparedStatement.setString(1, "value1");
            preparedStatement.setInt(2, 42);

            // Execute the query and get the result set
            ResultSet resultSet = preparedStatement.executeQuery();
            // Process the ResultSet to retrieve the query results
        } catch (SQLException e) {
            // Handle any errors.
            throw e; // as an example
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
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
