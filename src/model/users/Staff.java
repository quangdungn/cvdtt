package model.users;

public class Staff extends User {
    public Staff(String username, String password, String email, String phoneNumber) {
        super(username, password, email, phoneNumber, "Staff");
    }

    @Override
    public String toString() {
        return "Staff: " + super.toString();
    }
}
