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
        System.out.println("Clothing Product: ");
        System.out.println("Name: " + name + ", Price: " + price + ", Size: " + size + ", Description: " + description + ", Stock Quantity: " + stockQuantity);
    }
}
