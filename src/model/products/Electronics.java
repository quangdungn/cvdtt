package model.products;

public class Electronics extends Product {
    private String brand;

    public Electronics(String name, double price, String description, int stockQuantity, String brand) {
        super(name, price, description, stockQuantity);
        this.brand = brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    @Override
    public void displayProductDetails() {
        System.out.println("Sản phẩm điện tử: ");
        System.out.println("Tên: " + name + ", Giá: " + price + ", Nhãn hàng: " + brand + ", Mô tả: " + description + ", Số lượng tồn kho: " + stockQuantity);

    }
}
