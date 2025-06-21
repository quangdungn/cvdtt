package service;

import dao.UserDAO;
import model.users.User;

public class UserService {

    private UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO();
    }

    // Thêm người dùng mới
    public void addUser(User user) {
        userDAO.addUser(user); // Gọi phương thức addUser trong UserDAO
    }

    // Cập nhật hoặc thêm người dùng (addOrUpdateUser)
    public void addOrUpdateUser(User user) {
        // Kiểm tra người dùng đã tồn tại chưa, nếu có thì update, nếu không thì thêm mới
        if (userDAO.getUserByUsername(user.getUsername()) != null) {
            // Cập nhật nếu người dùng đã tồn tại
            userDAO.updateUser(user.getUsername(), user.getPassword(), user.getRole());
        } else {
            // Thêm mới nếu chưa tồn tại
            userDAO.addUser(user);
        }
    }

    // Lấy người dùng theo username
    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username); // Truy vấn người dùng theo username
    }

    // Xóa người dùng
    public void deleteUser(String username) {
        userDAO.deleteUser(username); // Xóa người dùng theo username
    }

    // Hiển thị thông tin người dùng
    public void displayRole(User user) {
        user.displayRole(); // Gọi phương thức displayInfo để in ra thông tin người dùng
    }
}
