package factory.orders;

import model.orders.*;

public class StandardOrderCreator extends OrderCreator {
    @Override
    public Order createOrder(int customerId, double totalAmount, String orderDate, String status) {
        return new StandardOrder(customerId, totalAmount, orderDate, status);
    }
}
