package factory.orders;

import model.orders.*;

public abstract class OrderCreator {
    public abstract Order createOrder(int customerId, double totalAmount, String orderDate, String status);

    public void manageOrder(Order order) {
        System.out.println("Managing order for customer ID: " + order.getCustomerId());

        if ("Express".equalsIgnoreCase(order.getStatus())) {
            System.out.println("Express shipping selected.");
        } else if ("Standard".equalsIgnoreCase(order.getStatus())) {
            System.out.println("Standard shipping selected.");
        }
        order.displayOrderDetails();
    }
}
