package com.spendwise.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.spendwise.database.DBconnection;
import com.spendwise.models.Product;

public class productService {

    public boolean saveProduct(Product product) {
        String sql = "INSERT INTO products (name, description, price, original_price, category, image_url, rating, review_count, seller, is_second_hand, product_condition, location, discount_percentage) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        int affectedRows = DBconnection.executeUpdate(sql,
                product.getName(),
                product.getDescription() != null ? product.getDescription() : "No description",
                product.getPriceAfterDiscount(),
                product.getOriginalPrice() > 0 ? product.getOriginalPrice() : product.getPriceAfterDiscount(),
                product.getCategory(),
                product.getImageUrl(),
                product.getRating(),
                product.getReviewCount(),
                product.getSellerName(),
                product.isSecondHand(),
                product.getCondition(), 
                product.getLocation(),
                product.getDiscountPercentage());

        return affectedRows > 0;
    }

    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM products WHERE product_id = ?";
        int affected = DBconnection.executeUpdate(sql, productId);
        return affected > 0;
    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try {
            ResultSet resultSet = DBconnection.executeQuery(sql);
            while (resultSet != null && resultSet.next()) {
                products.add(helperResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public ArrayList<Product> getProductByEnteringName(String productName) {
        ArrayList<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE name LIKE ?";
        try {
            ResultSet resultSet = DBconnection.executeQuery(sql, "%" + productName + "%");
            while (resultSet != null && resultSet.next()) {
                products.add(helperResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public Product helperResultSet(ResultSet resultSet) throws SQLException {

        Product p = new Product(
                resultSet.getInt("product_id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getDouble("price"),
                resultSet.getDouble("original_price"),
                resultSet.getString("category"),
                resultSet.getString("image_url"),
                resultSet.getDouble("rating"),
                resultSet.getInt("review_count"),
                resultSet.getString("seller"),
                resultSet.getBoolean("is_second_hand"),
                resultSet.getString("product_condition"),
                resultSet.getString("location"),
                resultSet.getInt("discount_percentage"));

        // 2. THIS IS THE MISSING PART: Read the request status!
        try {
            int requesterId = resultSet.getInt("requested_by_user_id");
            // If the database value is not null, set it on the object
            if (!resultSet.wasNull()) {
                p.setRequestByUserId(requesterId);
            }
        } catch (SQLException e) {
            // This catch block prevents a crash if you haven't added the column to the DB yet
            // System.out.println("Column requested_by_user_id not found in DB");
        }

        return p;
    }

    public boolean requestProduct(int productId, int userId) {
    String query = "UPDATE products SET requested_by_user_id = ? WHERE product_id = ?";
    
    try (java.sql.Connection conn = DBconnection.getConnection();
         java.sql.PreparedStatement stmt = conn.prepareStatement(query)) {
        
        stmt.setInt(1, userId);
        stmt.setInt(2, productId);
        
        int rowsUpdated = stmt.executeUpdate();
        return rowsUpdated > 0;
        
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
}