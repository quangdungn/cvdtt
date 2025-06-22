package service;

import dao.*;
import model.payments.Payment;

public class PaymentService {

    private PaymentDAO paymentDAO;

    public PaymentService() {
        paymentDAO = new PaymentDAO();
    }

    public void addPayment(Payment payment) {
        paymentDAO.addPayment(payment);
    }

    public Payment getPaymentByOrderId(int orderId) {
        return paymentDAO.getPaymentByOrderId(orderId);
    }

    public void displayPaymentInfo(Payment payment) {
        payment.displayPaymentInfo();
    }
}
