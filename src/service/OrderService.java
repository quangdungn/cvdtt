package service;

import dao.OrderDAO;
import model.orders.*;
import factory.orders.*;

import java.util.*;

public class OrderService {

    private static OrderDAO orderDAO;

    public OrderService() {
        orderDAO = new OrderDAO();
    }

    // Thêm đơn hàng mới - Tạo đơn hàng qua FactoryRegistry
    public void addOrder(String status, int customerId, double totalAmount, String orderDate) {
        // Sử dụng FactoryRegistry để tạo đơn hàng
        Order order = OrderCreatorRegistry.createOrder(status, customerId, totalAmount, orderDate);

        if (order != null) {
            // Thêm đơn hàng thông qua OrderDAO
            orderDAO.addOrder(order);
        } else {
            System.out.println("Không thể tạo đơn hàng với status: " + status);
        }
    }

    // Cập nhật thông tin đơn hàng
    public void updateOrder(Order order) {
        orderDAO.updateOrder(order, order.getCustomerId());
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
