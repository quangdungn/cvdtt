package model.payments;

public class Payment {
    private int orderId;
    private PaymentMethod paymentMethod;
    private double amount;
    private String paymentDate;  // Payment date is required.

    // Constructor
    public Payment(int orderId, PaymentMethod paymentMethod, double amount, String paymentDate) {
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentDate = paymentDate;
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    // Display payment information
    public void displayPaymentInfo() {
        System.out.println("Payment Info:");
        System.out.println("Order ID: " + orderId);
        System.out.println("Payment Method: " + paymentMethod);
        System.out.println("Amount: " + amount);
        System.out.println("Payment Date: " + paymentDate);
    }
}
