package service;

import dao.*;
import model.products.*;

import java.util.*;

public class ProductService {

    private ProductDAO productDAO;

    public ProductService() {
        productDAO = new ProductDAO();
    }

    public void addProduct(Product product, int categoryId) {
        if (product != null && categoryId > 0) {
            productDAO.addProduct(product, categoryId);
        } else {
            System.out.println("Sản phẩm hoặc CategoryId không hợp lệ.");
        }
    }

    public void updateProduct(int productId, Product product, int categoryId) {
        if (productId > 0 && product != null && categoryId > 0) {
            productDAO.updateProduct(productId, product, categoryId);
        } else {
            System.out.println("Sản phẩm, ProductId hoặc CategoryId không hợp lệ.");
        }
    }

    public void deleteProduct(int id) {
        if (id > 0) {
            productDAO.deleteProduct(id);
        } else {
            System.out.println("ProductId không hợp lệ.");
        }
    }

    public Product getProductById(int id) {
        if (id > 0) {
            return productDAO.getProductById(id);
        } else {
            System.out.println("ProductId không hợp lệ.");
            return null;
        }
    }

    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }
}
