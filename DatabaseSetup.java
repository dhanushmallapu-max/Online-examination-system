import java.sql.*;

public class DatabaseSetup {
    public static void main(String[] args) {
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement()) {

            st.executeUpdate("CREATE TABLE IF NOT EXISTS Users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT, role TEXT)");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS Questions (id INTEGER PRIMARY KEY AUTOINCREMENT, question TEXT, a TEXT, b TEXT, c TEXT, d TEXT, answer TEXT)");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS Results (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, score INTEGER, total INTEGER)");

            st.executeUpdate("INSERT OR IGNORE INTO Users(username,password,role) VALUES('admin','admin123','admin')");

            System.out.println("Database created successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
