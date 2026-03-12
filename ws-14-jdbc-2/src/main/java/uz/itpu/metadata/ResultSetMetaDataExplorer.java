package uz.itpu.metadata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates the usage of {@link ResultSetMetaData} to inspect
 * query result structure: column count, names, Java class mappings,
 * SQL type names, precision, scale, and nullability.
 */
public class ResultSetMetaDataExplorer {

    private final Connection connection;

    public ResultSetMetaDataExplorer(Connection connection) {
        this.connection = connection;
    }

    /**
     * Executes a query and prints detailed {@link ResultSetMetaData} for every column,
     * followed by a generic metadata-driven rendering of all rows.
     *
     * @param sql   the SQL query to execute
     * @param label a descriptive label for the output section
     * @throws SQLException if a database access error occurs
     */
    public void printResultSetMetaData(String sql, String label) throws SQLException {
        System.out.printf("=== RESULTSET METADATA: %s ===%n", label);
        System.out.printf("  Query: %s%n%n", sql.strip());

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            ResultSetMetaData rsMeta = rs.getMetaData();
            int columnCount = rsMeta.getColumnCount();

            System.out.printf("  Column count: %d%n%n", columnCount);
            System.out.printf("  %-5s %-25s %-20s %-15s %-6s %-6s %-10s %-10s%n",
                    "#", "LABEL", "CLASS_NAME", "TYPE_NAME",
                    "PREC", "SCALE", "NULLABLE", "AUTO_INC");
            System.out.println("  " + "-".repeat(100));

            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("  %-5d %-25s %-20s %-15s %-6d %-6d %-10s %-10s%n",
                        i,
                        rsMeta.getColumnLabel(i),
                        extractSimpleClassName(rsMeta.getColumnClassName(i)),
                        rsMeta.getColumnTypeName(i),
                        rsMeta.getPrecision(i),
                        rsMeta.getScale(i),
                        nullabilityLabel(rsMeta.isNullable(i)),
                        rsMeta.isAutoIncrement(i) ? "YES" : "NO"
                );
            }
            System.out.println();

            // Print actual data using a metadata-driven approach
            printDataGeneric(rs, rsMeta, columnCount);
        }
    }

    /**
     * Demonstrates column aliasing — how {@code AS} aliases appear in {@link ResultSetMetaData}.
     * Shows a JOIN query with computed columns and aliases.
     *
     * @throws SQLException if a database access error occurs
     */
    public void printAliasDemo() throws SQLException {
        String sql = """
                SELECT
                    e.id                                    AS employee_id,
                    e.first_name || ' ' || e.last_name      AS full_name,
                    e.salary,
                    d.name                                  AS department_name,
                    ROUND(e.salary / 12, 2)                 AS monthly_salary
                FROM employees e
                LEFT JOIN departments d ON e.department_id = d.id
                ORDER BY e.salary DESC
                """;
        printResultSetMetaData(sql, "JOIN with column aliases");
    }

    /**
     * Demonstrates aggregate functions and how they appear in {@link ResultSetMetaData}.
     *
     * @throws SQLException if a database access error occurs
     */
    public void printAggregateDemo() throws SQLException {
        String sql = """
                SELECT
                    d.name          AS department,
                    COUNT(e.id)     AS headcount,
                    MIN(e.salary)   AS min_salary,
                    MAX(e.salary)   AS max_salary,
                    AVG(e.salary)   AS avg_salary
                FROM departments d
                LEFT JOIN employees e ON e.department_id = d.id
                GROUP BY d.name
                ORDER BY avg_salary DESC NULLS LAST
                """;
        printResultSetMetaData(sql, "Aggregate query");
    }

    /**
     * Generically extracts all column labels from a query result using metadata.
     * Demonstrates how metadata enables writing database-agnostic code.
     *
     * @param sql the SQL query to inspect
     * @return list of column labels in select order
     * @throws SQLException if a database access error occurs
     */
    public List<String> extractColumnNames(String sql) throws SQLException {
        var columnNames = new ArrayList<String>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            ResultSetMetaData rsMeta = rs.getMetaData();
            for (int i = 1; i <= rsMeta.getColumnCount(); i++) {
                columnNames.add(rsMeta.getColumnLabel(i));
            }
        }
        return columnNames;
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /**
     * Generically prints all rows of a ResultSet, driven entirely by metadata.
     * This technique allows rendering any query without knowing the schema upfront.
     */
    private void printDataGeneric(ResultSet rs, ResultSetMetaData rsMeta, int columnCount)
            throws SQLException {

        System.out.println("  --- Data (metadata-driven rendering) ---");

        var header    = new StringBuilder("  |");
        var separator = new StringBuilder("  +");
        for (int i = 1; i <= columnCount; i++) {
            header.append(String.format(" %-18s |", rsMeta.getColumnLabel(i)));
            separator.append("-".repeat(20)).append("+");
        }

        System.out.println(separator);
        System.out.println(header);
        System.out.println(separator);

        int rowCount = 0;
        while (rs.next()) {
            var row = new StringBuilder("  |");
            for (int i = 1; i <= columnCount; i++) {
                Object value = rs.getObject(i);
                row.append(String.format(" %-18s |", value != null ? value.toString() : "NULL"));
            }
            System.out.println(row);
            rowCount++;
        }

        System.out.println(separator);
        System.out.printf("  Total rows: %d%n%n", rowCount);
    }

    private static String nullabilityLabel(int nullable) {
        return switch (nullable) {
            case ResultSetMetaData.columnNoNulls         -> "NOT NULL";
            case ResultSetMetaData.columnNullable        -> "NULLABLE";
            case ResultSetMetaData.columnNullableUnknown -> "UNKNOWN";
            default                                      -> "?";
        };
    }

    private static String extractSimpleClassName(String fqcn) {
        if (fqcn == null) return "";
        int lastDot = fqcn.lastIndexOf('.');
        return lastDot >= 0 ? fqcn.substring(lastDot + 1) : fqcn;
    }
}

