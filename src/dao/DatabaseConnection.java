package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            // Kiểm tra nếu bạn cần thay đổi tên database hoặc thông tin đăng nhập
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/salesmanagements?useSSL=false&serverTimezone=UTC", "root", "");
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            e.printStackTrace(); // In ra chi tiết lỗi
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
