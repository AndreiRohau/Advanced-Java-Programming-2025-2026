package uz.itpu.metadata;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Entry point demonstrating both {@link java.sql.DatabaseMetaData}
 * and {@link java.sql.ResultSetMetaData} APIs against the jdbc-demo schema
 * (departments / employees tables).
 *
 * <p>Run with optional system properties to override the default connection:
 * <pre>
 *   -Ddb.url=jdbc:postgresql://localhost:5432/postgres
 *   -Ddb.user=postgres
 *   -Ddb.password=postgres
 * </pre>
 */
public class MainMetadata {

    public static void main(String[] args) {
        try (Connection connection = ConnectionProvider.getConnection()) {

            runDatabaseMetaDataExamples(connection);
            runResultSetMetaDataExamples(connection);

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------
    // DatabaseMetaData examples
    // -------------------------------------------------------------------------

    private static void runDatabaseMetaDataExamples(Connection connection) throws SQLException {
        var dbExplorer = new DatabaseMetaDataExplorer(connection);

        dbExplorer.printDatabaseInfo();
        dbExplorer.printTables();

        for (String table : new String[]{"departments", "employees"}) {
            dbExplorer.printColumns(table);
            dbExplorer.printPrimaryKeys(table);
        }

        dbExplorer.printForeignKeys("employees");
        dbExplorer.printSupportedTypes();
    }

    // -------------------------------------------------------------------------
    // ResultSetMetaData examples
    // -------------------------------------------------------------------------

    private static void runResultSetMetaDataExamples(Connection connection) throws SQLException {
        var rsExplorer = new ResultSetMetaDataExplorer(connection);

        // 1. Simple single-table query – shows raw column types from SELECT *
        rsExplorer.printResultSetMetaData(
                "SELECT * FROM employees",
                "All employees (SELECT *)"
        );

        // 2. JOIN with aliases – shows how AS aliases affect getColumnLabel()
        rsExplorer.printAliasDemo();

        // 3. Aggregate functions – COUNT / MIN / MAX / AVG type mapping
        rsExplorer.printAggregateDemo();

        // 4. Generic column-name extraction – database-agnostic utility
        var columns = rsExplorer.extractColumnNames("SELECT * FROM departments");
        System.out.println("=== EXTRACTED COLUMN NAMES (departments) ===");
        columns.forEach(col -> System.out.println("  - " + col));
        System.out.println();
    }
}
