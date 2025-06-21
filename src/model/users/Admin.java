package model.users;

public class Admin extends User {
    public Admin(String username, String password, String email, String phoneNumber) {
        super(username, password, email, phoneNumber, "Admin");
    }

    @Override
    public String toString() {
        return "Admin: " + super.toString();
    }
}
