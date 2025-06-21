package dao;

import model.users.User;
import model.users.Admin;
import model.users.Staff;

import java.sql.*;

public class UserDAO {

    private Connection connection;

    // Constructor - Khởi tạo kết nối cơ sở dữ liệu
    public UserDAO() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    // Thêm người dùng mới
    public void addUser(User user) {
        String query = "INSERT INTO Users (username, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            stmt.executeUpdate();
            System.out.println("User Created: " + user.getUsername());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Cập nhật thông tin người dùng
    public void updateUser(String username, String password, String role) {
        String query = "UPDATE Users SET password = ?, role = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, password);
            stmt.setString(2, role);
            stmt.setString(3, username);
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy người dùng theo username
    public User getUserByUsername(String username) {
        String query = "SELECT * FROM Users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String password = rs.getString("password");
                String role = rs.getString("role");

                User user = null;
                if ("Admin".equalsIgnoreCase(role)) {
                    user = new Admin(username, password);
                } else if ("Staff".equalsIgnoreCase(role)) {
                    user = new Staff(username, password);
                }

                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Trả về null nếu không tìm thấy người dùng
    }

    public void getAllUsers() {
        String query = "SELECT * FROM Users";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");

                User user = null;
                if ("Admin".equalsIgnoreCase(role)) {
                    user = new Admin(username, password);
                } else if ("Staff".equalsIgnoreCase(role)) {
                    user = new Staff(username, password);
                }

                System.out.println("Username: " + user.getUsername() + ", Role: " + user.getRole());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(String username) {
        String query = "DELETE FROM Users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);  // Kiểm tra số dòng bị ảnh hưởng
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        String query = "SELECT * FROM Users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                if ("Admin".equalsIgnoreCase(role)) {
                    return new Admin(username, password);
                } else if ("Staff".equalsIgnoreCase(role)) {
                    return new Staff(username, password);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
