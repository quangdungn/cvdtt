package factory.products;

import model.products.Product;

public abstract class ProductCreator {
    public abstract Product createProduct(String name, double price, String description, int stockQuantity, String attribute);

    public void productInfo(String name, double price, String description, int stockQuantity, String attribute) {
        Product product = createProduct(name, price, description, stockQuantity, attribute);
        product.displayProductDetails();
    }
}
