package Services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Database.DBconnection;
import Models.RegularUser; 
import Models.Product;
import Models.Purchase;
import Services.AddressService.AddressDTO; 

public class ProfileService {

    public static RegularUser getCurrentUser(){
        int userId = 1; 
        
        String sql = "SELECT * FROM users WHERE user_id = ?";
        
        try {
            ResultSet resultSet = DBconnection.executeQuery(sql, userId);
            
            if (resultSet != null && resultSet.next()) {

                return new RegularUser(
                    
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("email"),
                    resultSet.getInt("user_id"),
                    resultSet.getTimestamp("registration_date")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new RegularUser("Misafir", "", "guest@email.com");
    }

    public static List<RegularUser> getFriends(){
        List<RegularUser> friends = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE user_id != 1 LIMIT 5"; 

        try {
            ResultSet resultSet = DBconnection.executeQuery(sql);

            while (resultSet != null && resultSet.next()){
                friends.add(new RegularUser(
                    resultSet.getString("username"),
                    "***", 
                    resultSet.getString("email"),
                    resultSet.getInt("user_id"),
                    resultSet.getTimestamp("registration_date")
                ));
            }
        } 
        
        catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }

    public static List<Product> getSavedProducts(){

        List<Product> products = new ArrayList<>();

        products.add(new Product("Sony WH-1000XM5", 299.99, "Electronics", "sony.png", "Sony Store"));
        products.add(new Product("Nike Air Max", 120.00, "Fashion", "nike.png", "Nike"));
        
        return products;
    }

    public static List<Purchase> getOrders(String filter){
        List<Purchase> orders = new ArrayList<>();
        return orders; 
    }

    public static List<AddressDTO> getAddresses(){
        return new AddressService().getAddresses(1);
    }

    public static void addAddress(AddressDTO addr){
        new AddressService().addAddress(
            1, 
            addr.label, addr.fullName, addr.street, addr.city, addr.state, addr.zip, addr.phone, addr.isDefault
        );
    }

    public static void deleteAddress(int addressId){
        new AddressService().deleteAddress(addressId);
    }

    public static void setDefaultAddress(int addressId){
        new AddressService().setDefaultAddress(1, addressId);
    }
}
