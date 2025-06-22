package model.orders;

public interface Order {
    void displayOrderDetails();

    int getCustomerId();
    double getTotalAmount();
    String getOrderDate();
    String getStatus();
}
