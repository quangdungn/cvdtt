package model.orders;

public class StandardOrder implements Order {
    private int customerId;
    private double totalAmount;
    private String orderDate;
    private String status;

    public StandardOrder(int customerId, double totalAmount, String orderDate, String status) {
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.status = status;
    }

    @Override
    public void displayOrderDetails() {
        System.out.println("Đơn hàng tiêu chuẩn - Mã khách hàng: " + customerId + ", Tổng tiền: " + totalAmount);
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
