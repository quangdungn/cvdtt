package model.orders;

public class StandardOrder extends Order {
    public StandardOrder(int customerId, double totalAmount, String orderDate, String status) {
        super(customerId, totalAmount, orderDate, status);
    }

    @Override
    public void displayOrderDetails() {
        System.out.println("Standard Order - Customer ID: " + customerId + ", Total Amount: " + totalAmount);
    }
}
