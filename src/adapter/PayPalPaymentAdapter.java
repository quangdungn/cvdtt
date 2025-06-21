package adapter;
import strategy.*;

public class PayPalPaymentAdapter implements PaymentStrategy {
    private PayPalPayment payPalPayment;

    public PayPalPaymentAdapter(PayPalPayment payPalPayment) {
        this.payPalPayment = payPalPayment;
    }
    @Override
    public void pay(double amount) {
        payPalPayment.makePayPalPayment(amount);
    }
}