package service;

import dao.UserDAO;
import model.users.User;

import java.util.List;

public class UserService {

    private UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO();
    }

    public void addUser(User user) {
        userDAO.addUser(user); // Gọi phương thức addUser trong UserDAO
        System.out.println("Người dùng đã được thêm với userId: " + user.getUserId());  // In ra userId sau khi thêm người dùng
    }

    private static void viewUsers() {
        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.getAllUsers();  // Lấy danh sách người dùng từ CSDL

        // Kiểm tra nếu không có người dùng
        if (users.isEmpty()) {
            System.out.println("Không có người dùng nào.");
        } else {
            // Hiển thị thông tin người dùng
            for (User user : users) {
                System.out.println(user);  // Gọi phương thức toString() để in ra thông tin người dùng
            }
        }
    }


    public void addOrUpdateUser(User user) {
        // Kiểm tra người dùng đã tồn tại chưa, nếu có thì update, nếu không thì thêm mới
        User existingUser = userDAO.getUserByUsername(user.getUsername());
        if (existingUser != null) {
            // Cập nhật nếu người dùng đã tồn tại
            userDAO.updateUser(existingUser.getUserId(), user);  // Sử dụng userId kiểu int
            System.out.println("Người dùng đã được cập nhật.");
        } else {
            // Thêm mới nếu chưa tồn tại
            addUser(user);  // Gọi addUser để thêm người dùng
            System.out.println("Người dùng đã được thêm mới.");
        }
    }


    // Lấy người dùng theo username
    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username); // Truy vấn người dùng theo username
    }

    public void deleteUser(int userId) {
        userDAO.deleteUser(userId);  // Gọi phương thức deleteUser trong UserDAO để xóa người dùng theo userId
        System.out.println("Người dùng với ID " + userId + " đã được xóa.");
    }


    // Hiển thị thông tin người dùng
    public void displayRole(User user) {
        System.out.println("Role: " + user.getRole());  // In ra vai trò người dùng
    }
}
