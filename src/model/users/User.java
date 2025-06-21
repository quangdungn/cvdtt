package model.users;

public abstract class User {
    protected String username;
    protected String password;
    protected String role;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public abstract void displayRole();

    // Getter and setter methods
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public boolean login(String enteredUsername, String enteredPassword) {
        return enteredUsername.equals(username) && enteredPassword.equals(password);
    }
}
