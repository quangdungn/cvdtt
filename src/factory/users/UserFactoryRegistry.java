package factory.users;

import model.users.User;

import java.util.Map;
import java.util.HashMap;

public class UserFactoryRegistry {
    private static Map<String, UserFactory> factoryMap = new HashMap<>();

    static {
        factoryMap.put("Admin", new AdminFactory());
        factoryMap.put("Staff", new StaffFactory());
    }

    public static User createUser(String role, String username, String password, String email, String phoneNumber) {
        UserFactory factory = factoryMap.get(role);
        if (factory != null) {
            return factory.createUser(username, password, email, phoneNumber);
        }
        return null;
    }
}
