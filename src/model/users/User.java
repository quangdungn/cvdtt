package model.users;

public interface User {
    void login();
    void logout();
    String getRole();
    String getUsername();
    String getEmail();
    String getPhoneNumber();
    String getPassword();
    void setUserId(int userId);
    int getUserId();
    String toString();
}
