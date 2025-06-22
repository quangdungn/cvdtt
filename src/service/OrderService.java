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

    public void addOrder(String status, int customerId, double totalAmount, String orderDate) {
        Order order = OrderCreatorRegistry.createOrder(status, customerId, totalAmount, orderDate);

        if (order != null) {
            orderDAO.addOrder(order);
        } else {
            System.out.println("Không thể tạo đơn hàng với status: " + status);
        }
    }

    public void updateOrder(Order order) {
        orderDAO.updateOrder(order, order.getCustomerId());
    }

    public Order getOrderById(int id) {
        return orderDAO.getOrderById(id);
    }

    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }

    public void deleteOrder(int orderId) {
        orderDAO.deleteOrder(orderId);
    }
}
