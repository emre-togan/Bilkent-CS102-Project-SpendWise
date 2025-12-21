package Services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Database.DBconnection;
import Models.Product;

public class productService {
    
    public boolean saveProduct(Product product){

        String sql = "INSERT INTO products (name, description, price, original_price, category, image_url, rating, review_count, seller, is_second_hand, product_condition, location, discount_percentage) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        boolean isAffected = false;
        int affectedRows = DBconnection.executeUpdate(sql, 
            product.getName(),
            "No description",       
            product.getPriceAfterDiscount(), 
            product.getPriceAfterDiscount(), 
            product.getCategory(),
            product.getImageUrl(),
            0.0,                      
            0,                          
            product.getSellerName(),    
            product.isSecondHand(),
            "New",                      
            "Online",                   
            0
        );

        if(affectedRows > 0){
            isAffected = true;
        }

        return isAffected;
    }

    public ArrayList<Product> getAllProducts(){

        ArrayList<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try{
            ResultSet resultSet = DBconnection.executeQuery(sql);
            while(resultSet != null && resultSet.next()){
                products.add(helperResultSet(resultSet));
            }
        }

        catch(SQLException e){
            e.printStackTrace();
        }

        return products;
    }

    public ArrayList<Product> getProductByEnteringName(String productName){
        
        ArrayList<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE name LIKE ?";

        try{
            ResultSet resultSet = DBconnection.executeQuery(sql, "%" + productName + "%");
            
            while(resultSet != null && resultSet.next()){
                products.add(helperResultSet(resultSet));
            }
        }

        catch (SQLException e){
            e.printStackTrace();
        }

        return products;
    }

    public Product helperResultSet(ResultSet resultSet) throws SQLException{

        return new Product(
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
            resultSet.getInt("discount_percentage")
        );
    }

}
