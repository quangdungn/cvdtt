package dao;

import model.products.Product;
import model.products.Electronics;
import model.products.Clothing;
import factory.products.*;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class ProductDAO {
    private Connection connection;

    public ProductDAO() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    // Thêm sản phẩm
    public void addProduct(Product product, int categoryId) {
        String attributes = null;

        // Lấy thuộc tính tương ứng với loại sản phẩm
        if (product instanceof Electronics) {
            attributes = ((Electronics) product).getBrand();  // brand cho Electronics
        } else if (product instanceof Clothing) {
            attributes = ((Clothing) product).getSize();  // size cho Clothing
        }

        String query = "INSERT INTO Products (product_name, price, stock_quantity, description, attributes) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setInt(3, product.getStockQuantity());
            stmt.setString(4, product.getDescription());
            stmt.setString(5, attributes);  // Lưu vào cột attributes
            stmt.executeUpdate();

            // Lấy product_id của sản phẩm mới thêm vào
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int productId = generatedKeys.getInt(1);  // Lấy ID được tự động sinh ra

                // Thêm vào bảng Product_Categories để liên kết sản phẩm với danh mục
                String categoryQuery = "INSERT INTO Product_Categories (product_id, category_id) VALUES (?, ?)";
                try (PreparedStatement categoryStmt = connection.prepareStatement(categoryQuery)) {
                    categoryStmt.setInt(1, productId);
                    categoryStmt.setInt(2, categoryId);
                    categoryStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Cập nhật thông tin sản phẩm (Không dùng getId())
    public void updateProduct(int productId, Product product, int categoryId) {
        String attributes = null;

        // Lấy thuộc tính tương ứng với loại sản phẩm
        if (product instanceof Electronics) {
            attributes = ((Electronics) product).getBrand();  // brand cho Electronics
        } else if (product instanceof Clothing) {
            attributes = ((Clothing) product).getSize();  // size cho Clothing
        }

        // Sửa phương thức update để không sử dụng getId(), chỉ dùng product_id trong CSDL
        String query = "UPDATE Products SET product_name = ?, price = ?, stock_quantity = ?, description = ?, attributes = ? WHERE product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setInt(3, product.getStockQuantity());
            stmt.setString(4, product.getDescription());
            stmt.setString(5, attributes);  // Cập nhật attributes
            stmt.setInt(6, productId);  // Dùng productId truyền vào, không cần getId()
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);  // Kiểm tra số dòng bị ảnh hưởng

            // Cập nhật liên kết sản phẩm với danh mục trong bảng Product_Categories
            String categoryQuery = "UPDATE Product_Categories SET category_id = ? WHERE product_id = ?";
            try (PreparedStatement categoryStmt = connection.prepareStatement(categoryQuery)) {
                categoryStmt.setInt(1, categoryId);
                categoryStmt.setInt(2, productId);  // Dùng productId truyền vào
                categoryStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy sản phẩm theo ID
    public Product getProductById(int id) {
        String query = "SELECT * FROM Products WHERE product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("product_name");
                double price = rs.getDouble("price");
                String description = rs.getString("description");
                int stockQuantity = rs.getInt("stock_quantity");
                String attributes = rs.getString("attributes");  // Lấy thông tin attributes

                // Lấy category_name từ bảng Product_Categories
                String categoryQuery = "SELECT c.category_name FROM Categories c " +
                        "JOIN Product_Categories pc ON c.category_id = pc.category_id " +
                        "WHERE pc.product_id = ?";
                try (PreparedStatement categoryStmt = connection.prepareStatement(categoryQuery)) {
                    categoryStmt.setInt(1, id);  // Set product_id vào query
                    ResultSet categoryRs = categoryStmt.executeQuery();
                    if (categoryRs.next()) {
                        String category = categoryRs.getString("category_name");

                        // Dùng Factory Method để tạo đối tượng sản phẩm tương ứng
                        ProductCreator creator = null;
                        if ("Electronics".equalsIgnoreCase(category)) {
                            creator = new ElectronicsCreator();  // Tạo ElectronicsCreator
                        } else if ("Clothing".equalsIgnoreCase(category)) {
                            creator = new ClothingCreator();  // Tạo ClothingCreator
                        }

                        // Tạo đối tượng sản phẩm và trả về
                        if (creator != null) {
                            return creator.createProduct(name, price, description, stockQuantity, attributes);  // Gọi createProduct với tham số
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Nếu không tìm thấy sản phẩm, trả về null
    }

    // Lấy tất cả sản phẩm
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM Products";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString("product_name");
                double price = rs.getDouble("price");
                String description = rs.getString("description");
                int stockQuantity = rs.getInt("stock_quantity");
                String attributes = rs.getString("attributes");  // Lấy thông tin attributes

                // Lấy category_name từ bảng Product_Categories
                String categoryQuery = "SELECT c.category_name FROM Categories c " +
                        "JOIN Product_Categories pc ON c.category_id = pc.category_id " +
                        "WHERE pc.product_id = ?";
                try (PreparedStatement categoryStmt = connection.prepareStatement(categoryQuery)) {
                    categoryStmt.setInt(1, rs.getInt("product_id"));
                    ResultSet categoryRs = categoryStmt.executeQuery();
                    if (categoryRs.next()) {
                        String category = categoryRs.getString("category_name");

                        // Sử dụng Factory Method để tạo đối tượng sản phẩm tương ứng
                        ProductCreator creator = null;
                        if ("Electronics".equalsIgnoreCase(category)) {
                            creator = new ElectronicsCreator();  // Tạo ElectronicsCreator
                        } else if ("Clothing".equalsIgnoreCase(category)) {
                            creator = new ClothingCreator();  // Tạo ClothingCreator
                        }

                        if (creator != null) {
                            products.add(creator.createProduct( name, price, description, stockQuantity, attributes));  // Gọi createProduct với tham số
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    // Xóa sản phẩm
    public void deleteProduct(int id) {
        String query = "DELETE FROM Products WHERE product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);  // Kiểm tra số dòng bị ảnh hưởng
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
