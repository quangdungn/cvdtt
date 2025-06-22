package factory.users;

import model.users.User;

public abstract class UserFactory {
    public abstract User createUser(String username, String password, String email, String phoneNumber);

    public void manageRoloUser(User user) {
        System.out.println("Managing user: " + user.getUsername());
        user.login();
    }
}
