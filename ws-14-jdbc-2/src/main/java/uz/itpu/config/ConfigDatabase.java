package uz.itpu.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database reset utility.
 *
 * <p>Run {@link #main(String[])} any time the database has drifted from its expected
 * initial state (e.g. after a demo that modifies salaries, inserts phantom rows, etc.).
 * It will:
 * <ol>
 *   <li>Drop existing {@code employees} and {@code departments} tables (cascade).</li>
 *   <li>Re-create the schema.</li>
 *   <li>Re-insert the original seed data.</li>
 * </ol>
 */
public class ConfigDatabase {

    private static final String URL      = "jdbc:postgresql://localhost:5432/jdbc_demo";
    private static final String USER     = "jdbc_user";
    private static final String PASSWORD = "jdbc_pass";

    public static void main(String[] args) {
        reset();
    }

    /**
     * Drops, recreates and re-seeds the demo schema.
     * Safe to call multiple times – each call produces a clean, predictable state.
     */
    public static void reset() {
        System.out.println("[ConfigDatabase] Resetting database...");

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = conn.createStatement()) {

            conn.setAutoCommit(false);

            dropTables(st);
            createTables(st);
            seedDepartments(st);
            seedEmployees(st);

            conn.commit();
            System.out.println("[ConfigDatabase] Reset complete – schema and seed data restored.");

        } catch (SQLException e) {
            System.err.println("[ConfigDatabase] Reset FAILED: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Private steps
    // -------------------------------------------------------------------------

    private static void dropTables(Statement st) throws SQLException {
        // employees first – it holds the FK reference to departments
        st.execute("DROP TABLE IF EXISTS employees");
        st.execute("DROP TABLE IF EXISTS departments");
        System.out.println("[ConfigDatabase] Dropped existing tables.");
    }

    private static void createTables(Statement st) throws SQLException {
        st.execute("""
                CREATE TABLE departments (
                    id       SERIAL PRIMARY KEY,
                    name     VARCHAR(100) NOT NULL UNIQUE,
                    location VARCHAR(100)
                )
                """);

        st.execute("""
                CREATE TABLE employees (
                    id            SERIAL PRIMARY KEY,
                    first_name    VARCHAR(50)  NOT NULL,
                    last_name     VARCHAR(50)  NOT NULL,
                    email         VARCHAR(120) NOT NULL UNIQUE,
                    salary        NUMERIC(10,2),
                    hire_date     DATE         NOT NULL DEFAULT CURRENT_DATE,
                    department_id INTEGER REFERENCES departments(id) ON DELETE SET NULL
                )
                """);

        System.out.println("[ConfigDatabase] Tables created.");
    }

    private static void seedDepartments(Statement st) throws SQLException {
        st.execute("""
                INSERT INTO departments (name, location) VALUES
                    ('Engineering', 'Tashkent'),
                    ('Marketing',   'Samarkand'),
                    ('HR',          'Tashkent'),
                    ('Finance',     'Bukhara')
                """);
        System.out.println("[ConfigDatabase] Departments seeded.");
    }

    private static void seedEmployees(Statement st) throws SQLException {
        st.execute("""
                INSERT INTO employees (first_name, last_name, email, salary, hire_date, department_id) VALUES
                    ('Alice', 'Smith',   'alice.smith@itpu.uz',  75000.00, '2022-03-15', 1),
                    ('Bob',   'Johnson', 'bob.johnson@itpu.uz',  62000.00, '2021-07-01', 1),
                    ('Carol', 'White',   'carol.white@itpu.uz',  55000.00, '2023-01-10', 2),
                    ('David', 'Brown',   'david.brown@itpu.uz',  48000.00, '2020-11-20', 3),
                    ('Eva',   'Davis',   'eva.davis@itpu.uz',    91000.00, '2019-05-05', 1),
                    ('Frank', 'Wilson',  'frank.wilson@itpu.uz', 67000.00, '2022-09-30', 4),
                    ('Grace', 'Lee',     'grace.lee@itpu.uz',    53000.00, '2023-06-14', 2),
                    ('Henry', 'Taylor',  'henry.taylor@itpu.uz', 44000.00, '2024-01-02', NULL)
                """);
        System.out.println("[ConfigDatabase] Employees seeded.");
    }
}
