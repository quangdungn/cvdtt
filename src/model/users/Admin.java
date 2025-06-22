package model.users;

// Concrete class Admin
public class Admin implements User {
    private int userId;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String role;

    public Admin(String username, String password, String email, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = "Admin";
    }

    @Override
    public void login() {
        System.out.println(username + " logged in as Admin.");
    }

    @Override
    public void logout() {
        System.out.println(username + " logged out.");
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public int getUserId() {
        return userId;
    }

    @Override
    public String toString(){
        return "User ID: " + userId + ", Username: " + username + ", Email: " + email + ", Phone: " + phoneNumber + ", Role: " + role;
    }
}
