package model.products;

public class Clothing extends Product {
    private String size;

    public Clothing(String name, double price, String description, int stockQuantity, String size) {
        super(name, price, description, stockQuantity);
        this.size = size;
    }

    public void setSize(String size) {
        this.size = size;
    }
    public String getSize() {
        return size;
    }

    @Override
    public void displayProductDetails() {
        System.out.println("Sản phẩm quần áo");
        System.out.println("Tên: " + name + ", Giá: " + price + ", Kích cỡ: " + size + ", Mô tả: " + description + ", Số lượng tồn kho: " + stockQuantity);
    }
}
