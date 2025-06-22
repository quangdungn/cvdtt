package model.products;

public interface Product {
    void displayProductDetails();
    String getName();
    double getPrice();
    String getDescription();
    int getStockQuantity();
}
