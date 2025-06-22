package factory.products;

import model.products.Product;

import java.util.Map;
import java.util.HashMap;

public class ProductCreatorRegistry {
    private static Map<String, ProductCreator> creatorMap = new HashMap<>();

    static {
        creatorMap.put("Electronics", new ElectronicsCreator());
        creatorMap.put("Clothing", new ClothingCreator());
    }

    // Method to create product using the registry
    public static Product createProduct(String category, String name, double price, String description, int stockQuantity, String attribute) {
        ProductCreator creator = creatorMap.get(category);
        if (creator != null) {
            return creator.createProduct(name, price, description, stockQuantity, attribute);
        }
        return null;
    }

    public static void registerCreator(String category, ProductCreator creator) {
        creatorMap.put(category, creator);
    }
}
