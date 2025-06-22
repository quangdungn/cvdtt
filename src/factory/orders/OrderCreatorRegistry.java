package factory.orders;

import model.orders.Order;

import java.util.Map;
import java.util.HashMap;

public class OrderCreatorRegistry {
    private static Map<String, OrderCreator> creatorMap = new HashMap<>();

    static {
        creatorMap.put("Express", new ExpressOrderCreator());
        creatorMap.put("Standard", new StandardOrderCreator());
    }

    public static Order createOrder(String status, int customerId, double totalAmount, String orderDate) {
        OrderCreator creator = creatorMap.get(status);
        if (creator != null) {
            return creator.createOrder(customerId, totalAmount, orderDate, status);
        }
        return null;
    }

    public static void registerCreator(String status, OrderCreator creator) {
        creatorMap.put(status, creator);
    }
}
