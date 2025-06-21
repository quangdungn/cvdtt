package service;

import dao.OrderDAO;
import model.orders.Order;
import java.util.*;

public class OrderService {

    private static OrderDAO orderDAO;

    public OrderService() {
        orderDAO = new OrderDAO();
    }

    // Thêm đơn hàng mới
    public void addOrder(Order order) {
        orderDAO.addOrder(order);
    }

    // Cập nhật thông tin đơn hàng
    public void updateOrder(Order order) {
        // Gọi phương thức updateOrder từ OrderDAO, không cần orderId vì cơ sở dữ liệu tự tạo
        orderDAO.updateOrder(order, order.getCustomerId()); // Cập nhật theo orderId
    }

    // Lấy đơn hàng theo ID
    public Order getOrderById(int id) {
        return orderDAO.getOrderById(id);
    }

    // Lấy tất cả đơn hàng
    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }

    // Xóa đơn hàng
    public void deleteOrder(int orderId) {
        orderDAO.deleteOrder(orderId);
    }
}
