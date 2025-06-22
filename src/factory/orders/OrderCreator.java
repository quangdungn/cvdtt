package factory.orders;

import model.orders.*;

public abstract class OrderCreator {
    // Phương thức trừu tượng tạo đơn hàng
    public abstract Order createOrder(int customerId, double totalAmount, String orderDate, String status);

    // Phương thức quản lý đơn hàng sau khi tạo
    public void manageOrder(Order order) {
        // Ví dụ: In ra thông tin đơn hàng
        System.out.println("Managing order for customer ID: " + order.getCustomerId());

        // Kiểm tra trạng thái đơn hàng
        if ("Express".equalsIgnoreCase(order.getStatus())) {
            System.out.println("Express shipping selected.");
        } else if ("Standard".equalsIgnoreCase(order.getStatus())) {
            System.out.println("Standard shipping selected.");
        }

        // Các hành động khác, ví dụ ghi log, tính toán lại tổng giá trị, kiểm tra trạng thái đơn hàng
        order.displayOrderDetails();
    }
}
