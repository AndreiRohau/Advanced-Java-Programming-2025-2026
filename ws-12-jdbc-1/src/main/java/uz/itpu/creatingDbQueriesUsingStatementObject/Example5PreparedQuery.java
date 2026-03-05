package uz.itpu.creatingDbQueriesUsingStatementObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Example5PreparedQuery {

    public static void main(String[] args) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = null; // todo Get the connection (previous steps to establish the connection)
            statement = connection.createStatement();

            String sqlQuery = "SELECT * FROM my_table";
            resultSet = statement.executeQuery(sqlQuery);

            while (resultSet.next()) {
                // Process data from the current row using getter methods
                String columnValue1 = resultSet.getString("column_name1");
                int columnValue2 = resultSet.getInt("column_name2");
                // ... and so on
            }

        } catch (SQLException e) {
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
}
