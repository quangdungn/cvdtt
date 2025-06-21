package factory.orders;

import model.orders.ExpressOrder;
import model.orders.Order;

public class ExpressOrderCreator extends OrderCreator {
    @Override
    public Order createOrder(int customerId, double totalAmount, String orderDate, String status) {
        return new ExpressOrder(customerId, totalAmount, orderDate, status);
    }
}
