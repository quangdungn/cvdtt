package factory.users;

import model.users.*;

public class AdminFactory extends UserFactory {
    @Override
    public User createUser(String username, String password, String email, String phoneNumber) {
        return new Admin(username, password, email, phoneNumber);
    }
}
