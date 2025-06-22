package model.orders;

// Concrete class ExpressOrder implement Order
public class ExpressOrder implements Order {
    private int customerId;
    private double totalAmount;
    private String orderDate;
    private String status;

    public ExpressOrder(int customerId, double totalAmount, String orderDate, String status) {
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.status = status;
    }

    @Override
    public void displayOrderDetails() {
        System.out.println("Express Order - Customer ID: " + customerId + ", Total Amount: " + totalAmount);
    }

    @Override
    public int getCustomerId() {
        return customerId;
    }

    @Override
    public double getTotalAmount() {
        return totalAmount;
    }

    @Override
    public String getOrderDate() {
        return orderDate;
    }

    @Override
    public String getStatus() {
        return status;
    }
}
