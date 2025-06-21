package model.orders;

public class ExpressOrder extends Order {
    public ExpressOrder(int customerId, double totalAmount, String orderDate, String status) {
        super(customerId, totalAmount, orderDate, status);
    }

    @Override
    public void displayOrderDetails() {
        System.out.println("Express Order - Customer ID: " + customerId + ", Total Amount: " + totalAmount);
    }
}
