package factory.users;

import model.users.User;

public abstract class UserFactory {
    public abstract User createUser(String username, String password);
}



