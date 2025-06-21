package model.users;

public class Staff extends User {
    public Staff(String username, String password) {
        super(username, password);
        this.role = "Staff";
    }

    @Override
    public void displayRole() {
        System.out.println("Đây là một nhân viên.");
    }
}
