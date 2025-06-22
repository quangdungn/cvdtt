package dao;

import model.orders.*;
import factory.orders.*;
import java.sql.*;
import java.util.*;

public class OrderDAO {

    private Connection connection;

    public OrderDAO() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public void addOrder(Order order) {
        String checkCustomerQuery = "SELECT COUNT(*) FROM customers WHERE customer_id = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkCustomerQuery)) {
            checkStmt.setInt(1, order.getCustomerId());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                String query = "INSERT INTO Orders (customer_id, total_amount, order_date, status) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setInt(1, order.getCustomerId());
                    stmt.setDouble(2, order.getTotalAmount());
                    stmt.setString(3, order.getOrderDate());
                    stmt.setString(4, order.getStatus());
                    stmt.executeUpdate();

                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int orderId = generatedKeys.getInt(1);
                        System.out.println("Order created with ID: " + orderId);
                    }
                }
            } else {
                System.out.println("Customer with ID " + order.getCustomerId() + " does not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateOrder(Order order, int orderId) {
        String query = "UPDATE Orders SET customer_id = ?, total_amount = ?, order_date = ?, status = ? WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, order.getCustomerId());
            stmt.setDouble(2, order.getTotalAmount());
            stmt.setString(3, order.getOrderDate());
            stmt.setString(4, order.getStatus());
            stmt.setInt(5, orderId);
            stmt.executeUpdate();
            System.out.println("Đơn hàng với ID " + orderId + " đã được cập nhật.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Order getOrderById(int id) {
        String query = "SELECT * FROM Orders WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int customerId = rs.getInt("customer_id");
                double totalAmount = rs.getDouble("total_amount");
                String orderDate = rs.getString("order_date");
                String status = rs.getString("status");
                return OrderCreatorRegistry.createOrder(status, customerId, totalAmount, orderDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM Orders";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                int customerId = rs.getInt("customer_id");
                double totalAmount = rs.getDouble("total_amount");
                String orderDate = rs.getString("order_date");
                String status = rs.getString("status");

                Order order = OrderCreatorRegistry.createOrder(status, customerId, totalAmount, orderDate);
                if (order != null) {
                    orders.add(order);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public void deleteOrder(int orderId) {
        String deleteOrderQuery = "DELETE FROM Orders WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteOrderQuery)) {
            stmt.setInt(1, orderId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Đơn hàng với ID " + orderId + " đã được xóa.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
