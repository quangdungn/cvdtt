package model.orders;

public abstract class Order {
    protected int customerId;
    protected double totalAmount;
    protected String orderDate;
    protected String status;

    public Order(int customerId, double totalAmount, String orderDate, String status) {
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.status = status;
    }

    // Phương thức abstract cho các lớp con thực hiện
    public abstract void displayOrderDetails();

    // Getter methods
    public int getCustomerId() {
        return customerId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
