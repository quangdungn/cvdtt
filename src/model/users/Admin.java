package model.users;

public class Admin extends User {
    public Admin(String username, String password) {
        super(username, password);
        this.role = "Admin";
    }

    @Override
    public void displayRole() {
        System.out.println("Đây là một quản trị viên.");
    }
}
