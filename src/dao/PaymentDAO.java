package dao;

import java.sql.*;

import model.payments.Payment;
import model.payments.PaymentMethod;

public class PaymentDAO {

    private Connection connection;

    public PaymentDAO() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public void addPayment(Payment payment) {
        String query = "INSERT INTO Payments (order_id, payment_method, amount, payment_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, payment.getOrderId());
            stmt.setString(2, payment.getPaymentMethod().name().replace("_", " "));
            stmt.setDouble(3, payment.getAmount());
            stmt.setString(4, payment.getPaymentDate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Payment getPaymentByOrderId(int orderId) {
        String query = "SELECT * FROM Payments WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String method = rs.getString("payment_method");
                double amount = rs.getDouble("amount");
                String paymentDate = rs.getString("payment_date");
                return new Payment(orderId, PaymentMethod.valueOf(method.replace(" ", "_")), amount, paymentDate);  // Chuyển đổi ngược lại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
