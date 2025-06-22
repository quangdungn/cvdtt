package service;

import dao.UserDAO;
import model.users.User;
import factory.users.*;
import java.util.Scanner;

public class LoginSystem {
    private static UserDAO userDAO = new UserDAO();

    public static User login() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nhập tên tài khoản: ");
        String username = scanner.nextLine();

        System.out.print("Nhập mật khẩu: ");
        String password = scanner.nextLine();

        User user = userDAO.getUserByUsernameAndPassword(username, password);

        if (user != null) {
            System.out.println("Đăng nhập thành công với " + user.getRole());

            // Sử dụng Factory Registry để tạo người dùng dựa trên vai trò
            User createdUser = UserFactoryRegistry.createUser(user.getRole(), username, password, user.getEmail(), user.getPhoneNumber());

            if (createdUser != null) {
                // Quản lý người dùng sau khi đăng nhập thông qua factory
                System.out.println("Quản lý người dùng: " + createdUser.getUsername());
                createdUser.login();
            }

            return createdUser;
        } else {
            System.out.println("Đăng nhập không thành công.");
            return null;
        }
    }
}

