package adapter;

import strategy.*;

public class StripePaymentAdapter implements PaymentStrategy {
    private StripePayment stripePayment;

    public StripePaymentAdapter(StripePayment stripePayment) {
        this.stripePayment = stripePayment;
    }

    @Override
    public void pay(double amount) {
        stripePayment.makeStripePayment(amount);
    }
}