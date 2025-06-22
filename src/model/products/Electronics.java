package model.products;

public class Electronics implements Product {
    private String name;
    private double price;
    private String description;
    private int stockQuantity;
    private String brand;

    public Electronics(String name, double price, String description, int stockQuantity, String brand) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.stockQuantity = stockQuantity;
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public String getBrand() {
        return brand;
    }

    public void displayProductDetails() {
        System.out.print("Sản phẩm điện tử - ");
        System.out.println("Tên : " + name + ", Giá: " + price + ", Nhãn hàng: " + brand + ", Mô tả: " + description + ", Số lượng: " + stockQuantity);
    }
}
