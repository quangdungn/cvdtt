package service;

import dao.*;
import model.payments.Payment;

public class PaymentService {

    private PaymentDAO paymentDAO;

    public PaymentService() {
        paymentDAO = new PaymentDAO();
    }

    // Thêm thanh toán mới
    public void addPayment(Payment payment) {
        paymentDAO.addPayment(payment);
    }

    // Lấy thanh toán theo Order ID
    public Payment getPaymentByOrderId(int orderId) {
        return paymentDAO.getPaymentByOrderId(orderId);
    }

    // Hiển thị thông tin thanh toán
    public void displayPaymentInfo(Payment payment) {
        payment.displayPaymentInfo();
    }
}
