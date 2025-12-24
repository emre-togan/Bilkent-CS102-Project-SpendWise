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

public boolean toggleWishlist(int userId, int productId) {
        // Check if exists
        String checkSql = "SELECT * FROM wishlist WHERE user_id = ? AND product_id = ?";
        try {
            ResultSet rs = DBconnection.executeQuery(checkSql, userId, productId);
            if (rs != null && rs.next()) {
                // It exists, so remove it (Unlike)
                String deleteSql = "DELETE FROM wishlist WHERE user_id = ? AND product_id = ?";
                DBconnection.executeUpdate(deleteSql, userId, productId);
                return false; // Returns false indicating it is now NOT wishlisted
            } else {
                // It doesn't exist, so add it (Like)
                String insertSql = "INSERT INTO wishlist (user_id, product_id) VALUES (?, ?)";
                DBconnection.executeUpdate(insertSql, userId, productId);
                return true; // Returns true indicating it IS wishlisted
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Product> getWishlist(int userId) {
        ArrayList<Product> products = new ArrayList<>();
        // Join products with wishlist table
        String sql = "SELECT p.*, 1 as is_wishlisted FROM products p " +
                     "JOIN wishlist w ON p.product_id = w.product_id " +
                     "WHERE w.user_id = ?";
        try {
            ResultSet resultSet = DBconnection.executeQuery(sql, userId);
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

        try {
            int requesterId = resultSet.getInt("requested_by_user_id");
            if (!resultSet.wasNull()) {
                p.setRequestByUserId(requesterId);
            }
        } catch (SQLException e) {}

        try {
  
            int wish = resultSet.getInt("is_wishlisted");
            p.setWishlisted(wish > 0);
        } catch (SQLException e) {

        }

        return p;
    }
    

    public void updateWishlistStatusForList(ArrayList<Product> products, int userId) {
        for(Product p : products) {
            String sql = "SELECT 1 FROM wishlist WHERE user_id = ? AND product_id = ?";
            try {
                ResultSet rs = DBconnection.executeQuery(sql, userId, p.getProductId());
                if(rs != null && rs.next()) {
                    p.setWishlisted(true);
                }
            } catch (Exception e) {}
        }
    }
}