package factory.orders;

import model.orders.Order;

import java.util.Map;
import java.util.HashMap;

public class OrderCreatorRegistry {
    private static Map<String, OrderCreator> creatorMap = new HashMap<>();

    // Static block to register creators for different order types
    static {
        creatorMap.put("Express", new ExpressOrderCreator());
        creatorMap.put("Standard", new StandardOrderCreator());
        // Đăng ký thêm các creator mới ở đây khi thêm kiểu Order mới
    }

    // Phương thức tạo đơn hàng từ creator map
    public static Order createOrder(String status, int customerId, double totalAmount, String orderDate) {
        OrderCreator creator = creatorMap.get(status);
        if (creator != null) {
            return creator.createOrder(customerId, totalAmount, orderDate, status);
        }
        return null; // Trả về null nếu không tìm thấy creator cho status
    }

    // Phương thức để đăng ký thêm creator mới khi thêm kiểu Order mới
    public static void registerCreator(String status, OrderCreator creator) {
        creatorMap.put(status, creator);
    }
}
