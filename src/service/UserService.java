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
        userDAO.addUser(user);
        System.out.println("Người dùng đã được thêm với userId: " + user.getUserId());
    }

    private static void viewUsers() {
        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.getAllUsers();


        if (users.isEmpty()) {
            System.out.println("Không có người dùng nào.");
        } else {
            for (User user : users) {
                System.out.println(user);
            }
        }
    }

    public void addOrUpdateUser(User user) {
        User existingUser = userDAO.getUserByUsername(user.getUsername());
        if (existingUser != null) {
            userDAO.updateUser(existingUser.getUserId(), user);
            System.out.println("Người dùng đã được cập nhật.");
        } else {
            addUser(user);
            System.out.println("Người dùng đã được thêm mới.");
        }
    }

    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    public void deleteUser(int userId) {
        userDAO.deleteUser(userId);
        System.out.println("Người dùng với ID " + userId + " đã được xóa.");
    }

    public void displayRole(User user) {
        System.out.println("Role: " + user.getRole());  // In ra vai trò người dùng
    }
}
