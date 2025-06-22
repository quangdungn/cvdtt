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
        // Kiểm tra xem customer_id có tồn tại trong bảng customers không
        String checkCustomerQuery = "SELECT COUNT(*) FROM customers WHERE customer_id = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkCustomerQuery)) {
            checkStmt.setInt(1, order.getCustomerId());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // Nếu customer_id tồn tại trong bảng customers, thực hiện thêm đơn hàng
                String query = "INSERT INTO Orders (customer_id, total_amount, order_date, status) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setInt(1, order.getCustomerId());
                    stmt.setDouble(2, order.getTotalAmount());
                    stmt.setString(3, order.getOrderDate());
                    stmt.setString(4, order.getStatus());
                    stmt.executeUpdate();

                    // Lấy order_id của đơn hàng mới thêm vào (do CSDL tự sinh)
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int orderId = generatedKeys.getInt(1);  // Lấy ID được tự động sinh ra
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
            stmt.setInt(1, order.getCustomerId());  // Cập nhật customer_id
            stmt.setDouble(2, order.getTotalAmount());  // Cập nhật tổng tiền
            stmt.setString(3, order.getOrderDate());  // Cập nhật ngày đặt hàng
            stmt.setString(4, order.getStatus());  // Cập nhật trạng thái
            stmt.setInt(5, orderId);  // Cập nhật theo orderId

            stmt.executeUpdate();  // Thực hiện cập nhật
            System.out.println("Đơn hàng với ID " + orderId + " đã được cập nhật.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy đơn hàng theo ID
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

                // Sử dụng FactoryRegistry để tạo đơn hàng tương ứng
                return OrderCreatorRegistry.createOrder(status, customerId, totalAmount, orderDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Nếu không tìm thấy đơn hàng
    }

    // Lấy tất cả đơn hàng
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

                // Sử dụng FactoryRegistry để tạo đơn hàng tương ứng
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

    // Xóa đơn hàng
    public void deleteOrder(int orderId) {
        String deleteOrderQuery = "DELETE FROM Orders WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteOrderQuery)) {
            stmt.setInt(1, orderId);  // Gán orderId vào câu lệnh SQL
            int rowsAffected = stmt.executeUpdate();  // Thực hiện xóa
            if (rowsAffected > 0) {
                System.out.println("Đơn hàng với ID " + orderId + " đã được xóa.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
