import java.sql.*;

public class DBConnection {
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:exam.db");
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }
}
