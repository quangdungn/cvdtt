package dao;

import model.customers.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private Connection connection;

    public CustomerDAO() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    // Thêm khách hàng
    public void addCustomer(Customer customer) {
        String query = "INSERT INTO Customers (full_name, phone_number, email, address) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, customer.getFullName());
            stmt.setString(2, customer.getPhoneNumber());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getAddress());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Lấy customer_id (do cơ sở dữ liệu tự động sinh ra)
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    customer.setCustomerId(generatedKeys.getInt(1));  // Gán customer_id
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Cập nhật thông tin khách hàng
    public void updateCustomer(int customerId, Customer customer) {
        String query = "UPDATE Customers SET full_name = ?, phone_number = ?, email = ?, address = ? WHERE customer_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, customer.getFullName());
            stmt.setString(2, customer.getPhoneNumber());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getAddress());
            stmt.setInt(5, customerId);  // Cập nhật theo customer_id

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy khách hàng theo customer_id
    public Customer getCustomerById(int customerId) {
        String query = "SELECT * FROM Customers WHERE customer_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String fullName = rs.getString("full_name");
                String phoneNumber = rs.getString("phone_number");
                String email = rs.getString("email");
                String address = rs.getString("address");

                return new Customer(fullName, phoneNumber, email, address);  // Tạo đối tượng Customer
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Nếu không tìm thấy khách hàng
    }

    // Lấy tất cả khách hàng
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM Customers";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int customerId = rs.getInt("customer_id");
                String fullName = rs.getString("full_name");
                String phoneNumber = rs.getString("phone_number");
                String email = rs.getString("email");
                String address = rs.getString("address");

                Customer customer = new Customer(fullName, phoneNumber, email, address);
                customer.setCustomerId(customerId);  // Gán customer_id
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;  // Trả về danh sách tất cả khách hàng
    }

    // Xóa khách hàng
    public void deleteCustomer(int customerId) {
        String query = "DELETE FROM Customers WHERE customer_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
