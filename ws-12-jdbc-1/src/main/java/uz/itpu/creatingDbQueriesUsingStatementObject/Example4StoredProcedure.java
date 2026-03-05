package uz.itpu.creatingDbQueriesUsingStatementObject;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class Example4StoredProcedure {

    public static void main(String[] args) throws SQLException {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = null; // todo Get the connection (previous steps to establish the connection)

            // SQL call statement for a stored procedure or function with placeholders for parameters
            String sqlCall = "{CALL my_stored_procedure(?, ?)}";
            callableStatement = connection.prepareCall(sqlCall);

            // Set input parameters using setXXX() methods (e.g., setString(), setInt(), etc.)
            callableStatement.setString(1, "input_value");

            // Register output parameters (if any) using registerOutParameter() method
            callableStatement.registerOutParameter(2, Types.INTEGER); // For example, the second parameter is an INTEGER output

            // Execute the stored procedure or function
            callableStatement.execute();

            // Retrieve the value of the output parameter, if needed
            int outputValue = callableStatement.getInt(2); // For example, the value of the second parameter is retrieved here
        } catch (SQLException e) {
            // Handle any errors.
            throw e; // as an example
        } finally {
            if (callableStatement != null) {
                try {
                    callableStatement.close();
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
