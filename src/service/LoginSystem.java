package service;

import dao.UserDAO;
import model.users.User;

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
            return user;
        } else {
            System.out.println("Đăng nhập không thành công.");
            return null;
        }
    }
}
