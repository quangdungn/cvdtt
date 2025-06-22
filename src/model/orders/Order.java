package model.orders;

// Interface Order
public interface Order {
    void displayOrderDetails();  // Phương thức trừu tượng cho các lớp con

    // Các phương thức getter
    int getCustomerId();
    double getTotalAmount();
    String getOrderDate();
    String getStatus();
}
