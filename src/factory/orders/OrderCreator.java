package factory.orders;

import model.orders.*;

public abstract class OrderCreator {
    public abstract Order createOrder(int customerId, double totalAmount, String orderDate, String status);
}
