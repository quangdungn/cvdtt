package dao;

import model.products.Clothing;
import model.products.Electronics;
import model.products.Product;
import factory.products.ProductCreatorRegistry;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class ProductDAO {
    private Connection connection;

    public ProductDAO() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public void addProduct(Product product, int categoryId) {
        String attributes = null;

        if (product instanceof Electronics) {
            attributes = ((Electronics) product).getBrand();
        } else if (product instanceof Clothing) {
            attributes = ((Clothing) product).getSize();
        }

        String query = "INSERT INTO Products (product_name, price, stock_quantity, description, attributes) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setInt(3, product.getStockQuantity());
            stmt.setString(4, product.getDescription());
            stmt.setString(5, attributes);
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int productId = generatedKeys.getInt(1);

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
                String attributes = rs.getString("attributes");

                String categoryQuery = "SELECT c.category_name FROM Categories c " +
                        "JOIN Product_Categories pc ON c.category_id = pc.category_id " +
                        "WHERE pc.product_id = ?";
                try (PreparedStatement categoryStmt = connection.prepareStatement(categoryQuery)) {
                    categoryStmt.setInt(1, id);
                    ResultSet categoryRs = categoryStmt.executeQuery();
                    if (categoryRs.next()) {
                        String category = categoryRs.getString("category_name");
                        return ProductCreatorRegistry.createProduct(category, name, price, description, stockQuantity, attributes);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void updateProduct(int productId, Product product, int categoryId) {
        String attributes = null;
        if (product instanceof Electronics) {
            attributes = ((Electronics) product).getBrand();
        } else if (product instanceof Clothing) {
            attributes = ((Clothing) product).getSize();
        }

        String query = "UPDATE Products SET product_name = ?, price = ?, stock_quantity = ?, description = ?, attributes = ? WHERE product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setInt(3, product.getStockQuantity());
            stmt.setString(4, product.getDescription());
            stmt.setString(5, attributes);
            stmt.setInt(6, productId);
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);

            String categoryQuery = "UPDATE Product_Categories SET category_id = ? WHERE product_id = ?";
            try (PreparedStatement categoryStmt = connection.prepareStatement(categoryQuery)) {
                categoryStmt.setInt(1, categoryId);
                categoryStmt.setInt(2, productId);
                categoryStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
                String attributes = rs.getString("attributes");

                String categoryQuery = "SELECT c.category_name FROM Categories c " +
                        "JOIN Product_Categories pc ON c.category_id = pc.category_id " +
                        "WHERE pc.product_id = ?";
                try (PreparedStatement categoryStmt = connection.prepareStatement(categoryQuery)) {
                    categoryStmt.setInt(1, rs.getInt("product_id"));
                    ResultSet categoryRs = categoryStmt.executeQuery();
                    if (categoryRs.next()) {
                        String category = categoryRs.getString("category_name");
                        Product product = ProductCreatorRegistry.createProduct(category, name, price, description, stockQuantity, attributes);
                        if (product != null) {
                            products.add(product);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public void deleteProduct(int id) {
        String query = "DELETE FROM Products WHERE product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
