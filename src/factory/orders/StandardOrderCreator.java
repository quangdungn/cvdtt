package factory.orders;

import model.orders.*;

public class StandardOrderCreator extends OrderCreator {
    @Override
    public Order createOrder(int customerId, double totalAmount, String orderDate, String status) {
        return new StandardOrder(customerId, totalAmount, orderDate, status);
    }

    // Optional: Có thể override manageOrder nếu cần hành động khác cho StandardOrder
    @Override
    public void manageOrder(Order order) {
        super.manageOrder(order);  // Gọi phương thức của lớp cha để thực hiện các hành động chung
    }
}
