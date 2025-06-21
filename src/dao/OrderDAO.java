package dao;

import model.orders.*;
import factory.orders.*;
import java.util.*;
import java.sql.*;

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


    // Cập nhật đơn hàng
    public void updateOrder(Order order, int orderId) {
        String query = "UPDATE Orders SET customer_id = ?, total_amount = ?, order_date = ?, status = ? WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, order.getCustomerId());
            stmt.setDouble(2, order.getTotalAmount());
            stmt.setString(3, order.getOrderDate());
            stmt.setString(4, order.getStatus());
            stmt.setInt(5, orderId);  // Cập nhật theo orderId
            stmt.executeUpdate();
            System.out.println("Order updated: " + orderId);
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

                // Dùng Factory Method để tạo đối tượng sản phẩm tương ứng
                OrderCreator creator = null;
                if ("Express".equalsIgnoreCase(status)) {
                    creator = new ExpressOrderCreator();
                } else if ("Standard".equalsIgnoreCase(status)) {
                    creator = new StandardOrderCreator();
                }

                if (creator != null) {
                    return creator.createOrder(customerId, totalAmount, orderDate, status);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Nếu không tìm thấy đơn hàng
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM Orders";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            // Kiểm tra dữ liệu trả về từ ResultSet
            int rowCount = 0;  // Đếm số lượng đơn hàng
            while (rs.next()) {
                rowCount++;  // Tăng đếm số lượng đơn hàng

                int orderId = rs.getInt("order_id");
                int customerId = rs.getInt("customer_id");
                double totalAmount = rs.getDouble("total_amount");
                String orderDate = rs.getString("order_date");
                String status = rs.getString("status");

                // Tạo đối tượng Order và thêm vào danh sách
                OrderCreator creator = null;
                if ("Express".equalsIgnoreCase(status)) {
                    creator = new ExpressOrderCreator();

                } else if ("Standard".equalsIgnoreCase(status)) {
                    creator = new StandardOrderCreator();

                }

                if (creator != null) {
                    Order order = creator.createOrder(customerId, totalAmount, orderDate, status);
                    orders.add(order);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public void deleteOrder(int orderId) {
        // Xóa các bản ghi trong bảng payments có liên quan đến orderId
        String deletePaymentsQuery = "DELETE FROM payments WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deletePaymentsQuery)) {
            stmt.setInt(1, orderId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Deleted payment records for orderId: " + orderId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Xóa các mục trong bảng order_items có liên quan đến orderId
        String deleteItemsQuery = "DELETE FROM order_items WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteItemsQuery)) {
            stmt.setInt(1, orderId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Deleted order items for orderId: " + orderId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Xóa đơn hàng trong bảng orders
        String query = "DELETE FROM Orders WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Order deleted with ID: " + orderId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
