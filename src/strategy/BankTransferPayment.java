package strategy;

public class BankTransferPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Thanh toán qua chuyển khoản ngân hàng: " + amount);
    }
}
