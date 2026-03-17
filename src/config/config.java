package config;
import java.sql.*;

public class config {

        public static Connection connectDB() {
         Connection con = null;
         try {
             Class.forName("org.sqlite.JDBC");

             // ✅ Fixed path - uses database folder inside project
             String dbPath = System.getProperty("user.dir") + "/src/database/inks.db";
             new java.io.File(System.getProperty("user.dir") + "/src/database/").mkdirs();

             con = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             try (Statement st = con.createStatement()) {
                 st.execute("PRAGMA journal_mode=WAL;");
             }
             System.out.println("Connected: " + dbPath);
         } catch (Exception e) {
             System.out.println("Connection Failed: " + e);
         }
         return con;
     }

    public void createTable() {
        try (Connection conn = connectDB();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS tbl_accounts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "fn TEXT NOT NULL," +
                "ln TEXT NOT NULL," +
                "email TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL," +
                "type TEXT NOT NULL," +
                "status TEXT NOT NULL)"
            );
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS tbl_books (" +
                "b_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "b_name TEXT NOT NULL," +
                "b_author TEXT NOT NULL," +
                "b_date TEXT," +
                "b_stock INTEGER NOT NULL," +
                "b_price DOUBLE NOT NULL," +
                "b_image TEXT)"
            );
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS tbl_orders (" +
                "o_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "u_id INTEGER NOT NULL," +
                "o_total DOUBLE NOT NULL," +
                "o_date DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY(u_id) REFERENCES tbl_accounts(id))"
            );
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS tbl_order_items (" +
                "oi_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "o_id INTEGER NOT NULL," +
                "b_name TEXT NOT NULL," +
                "oi_qty INTEGER NOT NULL," +
                "oi_price DOUBLE NOT NULL," +
                "FOREIGN KEY(o_id) REFERENCES tbl_orders(o_id))"
            );
            stmt.executeUpdate(
                "INSERT OR IGNORE INTO tbl_accounts (id, fn, ln, email, password, type, status) " +
                "VALUES (1, 'Admin', 'Admin', 'admin@inkspire.com', 'admin123', 'admin', 'active')"
            );
            System.out.println("All tables are ready.");
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }
    public static int getUserId(String email, String password) {
    String sql = "SELECT id FROM tbl_accounts WHERE email = ? AND password = ?";
    try (Connection conn = connectDB();
         PreparedStatement pst = conn.prepareStatement(sql)) {
        pst.setString(1, email);
        pst.setString(2, password);
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) return rs.getInt("id");
        }
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
    return -1; // not found
}

    // ✅ Fixed - connection is now properly closed
    public void addRecord(String sql, Object... values) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }
            pstmt.executeUpdate();
            System.out.println("Operation successful!");
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    // ✅ Fixed - connection is now properly closed
    public String authenticate(String sql, Object... values) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("type");
                }
            }
        } catch (SQLException e) {
            System.out.println("Login Error: " + e.getMessage());
        }
        return null;
    }
}