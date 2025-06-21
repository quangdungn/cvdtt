package factory.users;

import model.users.*;

public class StaffFactory extends UserFactory {
    @Override
    public User createUser(String username, String password) {
        return new Staff(username, password);
    }
}
