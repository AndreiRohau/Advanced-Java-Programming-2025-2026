package uz.itpu;

import org.junit.jupiter.api.*;
import uz.itpu.introductionJdbc.DbConfig;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests covering schema creation, CRUD and a FK join.
 * Requires a live PostgreSQL instance (start with docker compose).
 * All tables are dropped after the test class finishes.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SchemaAndCrudTest {

    private static Connection con;

    @BeforeAll
    static void connect() throws SQLException {
        con = DbConfig.getInstance().getConnection();
        con.setAutoCommit(true);
    }

    @AfterAll
    static void cleanup() throws SQLException {
        try (Statement st = con.createStatement()) {
            st.execute("DROP TABLE IF EXISTS contacts");
            st.execute("DROP TABLE IF EXISTS users");
        }
        con.close();
    }

    // 1 ─────────────────────────────────────────────────────────────────
    @Test @Order(1)
    void shouldConnectToDatabase() throws SQLException {
        assertFalse(con.isClosed());
        System.out.println("✔ connected to: " + con.getMetaData().getURL());
    }

    // 2 ─────────────────────────────────────────────────────────────────
    @Test @Order(2)
    void shouldCreateTableUsers() throws SQLException {
        try (Statement st = con.createStatement()) {
            st.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id    SERIAL PRIMARY KEY,
                    name  VARCHAR(100) NOT NULL,
                    email VARCHAR(150) NOT NULL UNIQUE
                )
                """);
        }
        assertTrue(tableExists("users"));
        System.out.println("✔ table 'users' created");
    }

    // 3 ─────────────────────────────────────────────────────────────────
    @Test @Order(3)
    void shouldCreateTableContacts() throws SQLException {
        try (Statement st = con.createStatement()) {
            st.execute("""
                CREATE TABLE IF NOT EXISTS contacts (
                    id    SERIAL PRIMARY KEY,
                    phone VARCHAR(30)
                )
                """);
        }
        assertTrue(tableExists("contacts"));
        System.out.println("✔ table 'contacts' created");
    }

    // 4 ─────────────────────────────────────────────────────────────────
    @Test @Order(4)
    void shouldAddForeignKeyFromContactsToUsers() throws SQLException {
        try (Statement st = con.createStatement()) {
            st.execute("ALTER TABLE contacts ADD COLUMN IF NOT EXISTS user_id INT");
            st.execute("""
                ALTER TABLE contacts
                    ADD CONSTRAINT fk_contacts_user
                    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
                """);
        }
        assertTrue(foreignKeyExists("fk_contacts_user"));
        System.out.println("✔ foreign key 'fk_contacts_user' added");
    }

    // 5 ─────────────────────────────────────────────────────────────────
    @Test @Order(5)
    void shouldInsertFiveUsers() throws SQLException {
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            String[][] data = {
                {"Alice", "alice@test.com"},
                {"Bob",   "bob@test.com"},
                {"Carol", "carol@test.com"},
                {"David", "david@test.com"},
                {"Eva",   "eva@test.com"}
            };
            for (var row : data) {
                ps.setString(1, row[0]);
                ps.setString(2, row[1]);
                ps.addBatch();
            }
            int[] counts = ps.executeBatch();
            assertEquals(5, counts.length);
        }
        System.out.println("✔ 5 users inserted");
    }

    // 6 ─────────────────────────────────────────────────────────────────
    @Test @Order(6)
    void shouldUpdateOneUser() throws SQLException {
        String sql = "UPDATE users SET name = 'Alice Updated' WHERE email = 'alice@test.com'";
        try (Statement st = con.createStatement()) {
            int rows = st.executeUpdate(sql);
            assertEquals(1, rows);
        }
        System.out.println("✔ user 'alice@test.com' updated");
    }

    // 7 ─────────────────────────────────────────────────────────────────
    @Test @Order(7)
    void shouldDeleteOneUser() throws SQLException {
        String sql = "DELETE FROM users WHERE email = 'eva@test.com'";
        try (Statement st = con.createStatement()) {
            int rows = st.executeUpdate(sql);
            assertEquals(1, rows);
        }
        System.out.println("✔ user 'eva@test.com' deleted");
    }

    // 8 ─────────────────────────────────────────────────────────────────
    @Test @Order(8)
    void shouldRetrieveAndPrintAllUsers() throws SQLException {
        String sql = "SELECT id, name, email FROM users ORDER BY id";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            int count = 0;
            System.out.println("─── users ───────────────────────────────");
            while (rs.next()) {
                System.out.printf("  [%d] %-20s %s%n",
                        rs.getInt("id"), rs.getString("name"), rs.getString("email"));
                count++;
            }
            System.out.println("─────────────────────────────────────────");
            assertEquals(4, count); // 5 inserted – 1 deleted = 4
        }
        System.out.println("✔ all users retrieved");
    }

    // ── helpers ──────────────────────────────────────────────────────────

    private boolean tableExists(String table) throws SQLException {
        var meta = con.getMetaData();
        try (ResultSet rs = meta.getTables(null, "public", table, new String[]{"TABLE"})) {
            return rs.next();
        }
    }

    private boolean foreignKeyExists(String constraintName) throws SQLException {
        String sql = """
                SELECT 1 FROM information_schema.table_constraints
                WHERE constraint_type = 'FOREIGN KEY'
                  AND constraint_name  = ?
                """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, constraintName);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}

