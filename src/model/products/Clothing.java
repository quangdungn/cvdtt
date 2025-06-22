package model.products;

public class Clothing implements Product {
    private String name;
    private double price;
    private String description;
    private int stockQuantity;
    private String size;

    public Clothing(String name, double price, String description, int stockQuantity, String size) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.stockQuantity = stockQuantity;
        this.size = size;
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
    public String getSize() {
        return size;
    }

    public void displayProductDetails() {
        System.out.print("Sản phẩm thời trang - ");
        System.out.println("Tên: " + name + ", Giá: " + price + ", Size: " + size + ", Mô tả: " + description + ", Số lượng: " + stockQuantity);
    }
}
