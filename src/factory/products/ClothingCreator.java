package factory.products;

import model.products.Clothing;
import model.products.Product;

public class ClothingCreator extends ProductCreator {
    @Override
    public Product createProduct(String name, double price, String description, int stockQuantity, String size) {
        return new Clothing(name, price, description, stockQuantity, size);
    }
}
