    package model.payments;

    public class Payment {
        private int orderId;
        private PaymentMethod paymentMethod;
        private double amount;
        private String paymentDate;
        public Payment(int orderId, PaymentMethod paymentMethod, double amount, String paymentDate) {
            this.orderId = orderId;
            this.paymentMethod = paymentMethod;
            this.amount = amount;
            this.paymentDate = paymentDate;
        }

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

        public void displayPaymentInfo() {
            System.out.println("Thông tin thanh toán:");
            System.out.println("Mã đơn hàng: " + orderId);
            System.out.println("Phương thức thanh toán: " + paymentMethod);
            System.out.println("Tiền: " + amount);
            System.out.println("Thanh toán ngày: " + paymentDate);
        }
    }
