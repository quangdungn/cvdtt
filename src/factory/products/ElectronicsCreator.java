package factory.products;

import model.products.Electronics;
import model.products.Product;

public class ElectronicsCreator extends ProductCreator {
    @Override
    public Product createProduct(String name, double price, String description, int stockQuantity, String brand) {
        return new Electronics(name, price, description, stockQuantity, brand);
    }
}
