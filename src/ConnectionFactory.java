import java.sql.*;
public class ConnectionFactory {
    public static Connection getConnection(String url) throws SQLException {
        return DriverManager.getConnection(url);
    }
}
