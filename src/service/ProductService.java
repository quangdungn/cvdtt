package service;

import dao.*;
import model.products.*;

import java.util.*;

public class ProductService {

    private ProductDAO productDAO;

    public ProductService() {
        productDAO = new ProductDAO(); // Khởi tạo ProductDAO
    }

    // Thêm sản phẩm mới
    public void addProduct(Product product, int categoryId) {
        if (product != null && categoryId > 0) {  // Kiểm tra tham số hợp lệ
            productDAO.addProduct(product, categoryId);
        } else {
            System.out.println("Sản phẩm hoặc CategoryId không hợp lệ.");
        }
    }

    // Cập nhật thông tin sản phẩm
    public void updateProduct(int productId, Product product, int categoryId) {
        if (productId > 0 && product != null && categoryId > 0) {  // Kiểm tra tham số hợp lệ
            productDAO.updateProduct(productId, product, categoryId);
        } else {
            System.out.println("Sản phẩm, ProductId hoặc CategoryId không hợp lệ.");
        }
    }

    // Xóa sản phẩm
    public void deleteProduct(int id) {
        if (id > 0) {  // Kiểm tra id hợp lệ
            productDAO.deleteProduct(id);
        } else {
            System.out.println("ProductId không hợp lệ.");
        }
    }

    // Lấy sản phẩm theo ID
    public Product getProductById(int id) {
        if (id > 0) {  // Kiểm tra id hợp lệ
            return productDAO.getProductById(id);
        } else {
            System.out.println("ProductId không hợp lệ.");
            return null;
        }
    }

    // Lấy tất cả sản phẩm
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }
}
