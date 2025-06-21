package service;

import dao.*;
import model.products.*;

import java.util.*;

public class ProductService {

    private ProductDAO productDAO;

    public ProductService() {
        productDAO = new ProductDAO();
    }

    // Thêm sản phẩm mới
    public void addProduct(Product product, int categoryId) {
        // Gọi dao để thêm sản phẩm cùng với categoryId
        productDAO.addProduct(product, categoryId);
    }

    // Cập nhật thông tin sản phẩm
    public void updateProduct(int productId, Product product, int categoryId) {
        // Gọi dao để cập nhật sản phẩm
        productDAO.updateProduct(productId, product, categoryId); // Truyền 3 tham số cần thiết
    }

    // Xóa sản phẩm
    public void deleteProduct(int id) {
        productDAO.deleteProduct(id);
    }

    // Lấy sản phẩm theo ID
    public Product getProductById(int id) {
        return productDAO.getProductById(id);
    }

    // Lấy tất cả sản phẩm
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }
}
