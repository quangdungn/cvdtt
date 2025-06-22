package factory.users;

import model.users.User;

public abstract class UserFactory {
    public abstract User createUser(String username, String password, String email, String phoneNumber);

    public void manageUser(User user) {
        System.out.println("Đang quản lý với người dùng: " + user.getUsername());
        user.login();
    }
}
