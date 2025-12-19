package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBconnection {

    private static final String URL = "jdbc:mysql://localhost:3306/spendwise_db"; 
    private static final String USER = "root";
    private static final String PASSWORD = "42Aeb562..06"; 
    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Successfully accessed!!");
            }
        } 
        
        catch (ClassNotFoundException e) {
            System.out.println("JDBC driver cannot found!");
            e.printStackTrace();
        } 
        
        catch (SQLException e) {
            System.out.println(" Connection Error!");
            e.printStackTrace();
        }
        return connection;
    }

    public static int executeUpdate(String sql, Object... params) {
        getConnection(); 
        
        try (PreparedStatement currentStatement = connection.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                currentStatement.setObject(i + 1, params[i]);
            }

            return currentStatement.executeUpdate();
        } 
        
        catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static ResultSet executeQuery(String sql, Object... params) {
        getConnection();
        
        try {
            PreparedStatement currentStatement = connection.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                currentStatement.setObject(i + 1, params[i]);
            }

            return currentStatement.executeQuery();
        } 
        
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void FinishConnection() {
        try {

            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database Connection Closed");
            }
        } 
        
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        getConnection();
    }
}