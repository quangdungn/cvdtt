package dao;

import model.users.*;
import java.sql.*;
import java.util.*;

import factory.users.*;


public class UserDAO {

    private Connection connection;

    // Constructor - Khởi tạo kết nối cơ sở dữ liệu
    public UserDAO() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public void addUser(User user) {
        String query = "INSERT INTO Users (username, password, email, phone_number, role) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhoneNumber());
            stmt.setString(5, user.getRole());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setUserId(generatedKeys.getInt(1));  // Gán userId mới vào đối tượng user
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateUser(int userId, User user) {
        String query = "UPDATE Users SET username = ?, password = ?, email = ?, phone_number = ?, role = ? WHERE user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhoneNumber());
            stmt.setString(5, user.getRole());
            stmt.setInt(6, userId);  // Cập nhật theo userId, không phải username

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
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");
                String role = rs.getString("role");

                UserFactory factory = null;

                // Sử dụng Factory để tạo đối tượng User dựa trên role
                if ("Admin".equalsIgnoreCase(role)) {
                    factory = new AdminFactory();  // Factory cho Admin
                } else if ("Staff".equalsIgnoreCase(role)) {
                    factory = new StaffFactory();  // Factory cho Staff
                }

                if (factory != null) {
                    return factory.createUser(username, password, email, phoneNumber);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Trả về null nếu không tìm thấy người dùng
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM Users";  // Lấy tất cả người dùng từ bảng Users

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                // Lấy thông tin người dùng từ cơ sở dữ liệu
                int userId = rs.getInt("user_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");
                String role = rs.getString("role");

                // Tạo đối tượng User theo vai trò (Admin hoặc Staff)
                User user = null;

                if ("Admin".equalsIgnoreCase(role)) {
                    user = new Admin(username, password, email, phoneNumber);  // Tạo đối tượng Admin
                } else if ("Staff".equalsIgnoreCase(role)) {
                    user = new Staff(username, password, email, phoneNumber);  // Tạo đối tượng Staff
                }

                if (user != null) {
                    user.setUserId(userId);  // Gán userId vào đối tượng user
                    users.add(user);  // Thêm vào danh sách người dùng
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;  // Trả về danh sách người dùng
    }



    public void deleteUser(int userId) {
        String query = "DELETE FROM Users WHERE user_id = ?";  // Câu lệnh xóa người dùng theo userId

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);  // Gán userId vào câu lệnh SQL

            int rowsAffected = stmt.executeUpdate();  // Thực hiện câu lệnh
            if (rowsAffected > 0) {
                System.out.println("Người dùng với ID " + userId + " đã được xóa.");
            } else {
                System.out.println("Không tìm thấy người dùng với ID " + userId + ".");
            }
        } catch (SQLException e) {
            e.printStackTrace();  // In ra lỗi nếu có
        }
    }


    // Lấy người dùng theo username và password
    public User getUserByUsernameAndPassword(String username, String password) {
        String query = "SELECT * FROM Users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");

                UserFactory factory = null;
                if ("Admin".equalsIgnoreCase(role)) {
                    factory = new AdminFactory();
                } else if ("Staff".equalsIgnoreCase(role)) {
                    factory = new StaffFactory();
                }

                if (factory != null) {
                    return factory.createUser(username, password, email, phoneNumber);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Trả về null nếu không tìm thấy người dùng
    }
}
