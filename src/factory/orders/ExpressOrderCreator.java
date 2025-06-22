package factory.orders;

import model.orders.*;

public class ExpressOrderCreator extends OrderCreator {
    @Override
    public Order createOrder(int customerId, double totalAmount, String orderDate, String status) {
        return new ExpressOrder(customerId, totalAmount, orderDate, status);
    }
}
