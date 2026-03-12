package uz.itpu.metadata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Demonstrates the usage of {@link DatabaseMetaData} to inspect
 * database-level information: product info, tables, columns, primary keys,
 * foreign keys, and supported SQL types.
 */
public class DatabaseMetaDataExplorer {

    private final Connection connection;

    public DatabaseMetaDataExplorer(Connection connection) {
        this.connection = connection;
    }

    /**
     * Prints general database and driver information.
     *
     * @throws SQLException if a database access error occurs
     */
    public void printDatabaseInfo() throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();

        System.out.println("=== DATABASE INFO ===");
        System.out.printf("  Product Name    : %s%n", meta.getDatabaseProductName());
        System.out.printf("  Product Version : %s%n", meta.getDatabaseProductVersion());
        System.out.printf("  Driver Name     : %s%n", meta.getDriverName());
        System.out.printf("  Driver Version  : %s%n", meta.getDriverVersion());
        System.out.printf("  JDBC Version    : %d.%d%n",
                meta.getJDBCMajorVersion(), meta.getJDBCMinorVersion());
        System.out.printf("  URL             : %s%n", meta.getURL());
        System.out.printf("  Username        : %s%n", meta.getUserName());
        System.out.printf("  Max Connections : %d%n", meta.getMaxConnections());
        System.out.printf("  ReadOnly        : %b%n", connection.isReadOnly());
        System.out.println();
    }

    /**
     * Prints all user-defined tables in the {@code public} schema.
     *
     * @throws SQLException if a database access error occurs
     */
    public void printTables() throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();

        System.out.println("=== TABLES ===");
        try (ResultSet tables = meta.getTables(null, "public", "%", new String[]{"TABLE"})) {
            while (tables.next()) {
                String tableName   = tables.getString("TABLE_NAME");
                String tableType   = tables.getString("TABLE_TYPE");
                String tableSchema = tables.getString("TABLE_SCHEM");
                System.out.printf("  [%s] %s.%s%n", tableType, tableSchema, tableName);
            }
        }
        System.out.println();
    }

    /**
     * Prints all columns for a given table, including type and nullability info.
     *
     * @param tableName the name of the table to inspect
     * @throws SQLException if a database access error occurs
     */
    public void printColumns(String tableName) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();

        System.out.printf("=== COLUMNS of '%s' ===%n", tableName);
        try (ResultSet columns = meta.getColumns(null, "public", tableName, "%")) {
            System.out.printf("  %-5s %-20s %-15s %-6s %-10s %-8s%n",
                    "POS", "COLUMN_NAME", "TYPE_NAME", "SIZE", "NULLABLE", "DEFAULT");
            System.out.println("  " + "-".repeat(70));

            while (columns.next()) {
                int    ordinal    = columns.getInt("ORDINAL_POSITION");
                String columnName = columns.getString("COLUMN_NAME");
                String typeName   = columns.getString("TYPE_NAME");
                int    columnSize = columns.getInt("COLUMN_SIZE");
                String isNullable = columns.getString("IS_NULLABLE");
                String columnDef  = columns.getString("COLUMN_DEF");

                System.out.printf("  %-5d %-20s %-15s %-6d %-10s %-8s%n",
                        ordinal, columnName, typeName, columnSize,
                        isNullable, columnDef != null ? columnDef : "");
            }
        }
        System.out.println();
    }

    /**
     * Prints primary key information for a given table.
     *
     * @param tableName the name of the table to inspect
     * @throws SQLException if a database access error occurs
     */
    public void printPrimaryKeys(String tableName) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();

        System.out.printf("=== PRIMARY KEYS of '%s' ===%n", tableName);
        try (ResultSet pks = meta.getPrimaryKeys(null, "public", tableName)) {
            while (pks.next()) {
                String columnName = pks.getString("COLUMN_NAME");
                String pkName     = pks.getString("PK_NAME");
                short  keySeq     = pks.getShort("KEY_SEQ");
                System.out.printf("  Constraint: %-30s  Column: %-15s  Seq: %d%n",
                        pkName, columnName, keySeq);
            }
        }
        System.out.println();
    }

    /**
     * Prints foreign key (imported keys) information for a given table.
     *
     * @param tableName the name of the table to inspect
     * @throws SQLException if a database access error occurs
     */
    public void printForeignKeys(String tableName) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();

        System.out.printf("=== FOREIGN KEYS of '%s' ===%n", tableName);
        try (ResultSet fks = meta.getImportedKeys(null, "public", tableName)) {
            while (fks.next()) {
                String fkName       = fks.getString("FK_NAME");
                String fkColumnName = fks.getString("FKCOLUMN_NAME");
                String pkTableName  = fks.getString("PKTABLE_NAME");
                String pkColumnName = fks.getString("PKCOLUMN_NAME");
                System.out.printf("  FK [%s]: %s.%s --> %s.%s%n",
                        fkName, tableName, fkColumnName, pkTableName, pkColumnName);
            }
        }
        System.out.println();
    }

    /**
     * Prints all SQL data types supported by the connected database.
     *
     * @throws SQLException if a database access error occurs
     */
    public void printSupportedTypes() throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();

        System.out.println("=== SUPPORTED SQL TYPES ===");
        try (ResultSet types = meta.getTypeInfo()) {
            System.out.printf("  %-20s %-8s %-10s%n", "TYPE_NAME", "DATA_TYPE", "PRECISION");
            System.out.println("  " + "-".repeat(42));
            while (types.next()) {
                System.out.printf("  %-20s %-8d %-10d%n",
                        types.getString("TYPE_NAME"),
                        types.getInt("DATA_TYPE"),
                        types.getInt("PRECISION"));
            }
        }
        System.out.println();
    }
}

