package Services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Database.DBconnection;
import Models.RegularUser;
import Models.Address;
import Models.Product;
import Models.Purchase;

public class ProfileService {

    public static RegularUser getCurrentUser(){
        int userId = 1; 
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try {
            ResultSet rs = DBconnection.executeQuery(sql, userId);
            if (rs != null && rs.next()) {
                return new RegularUser(
                    rs.getString("username"),
                    rs.getString("password"), 
                    rs.getString("email"),
                    rs.getInt("user_id"),
                    rs.getTimestamp("registration_date")
                );
            }
        } 
        catch (SQLException e){
            e.printStackTrace();
        }
        return new RegularUser("Guest", "", "guest@email.com");
    }

    public static List<RegularUser> getFriends(){
        List<RegularUser> friends = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE user_id != 1 LIMIT 5"; 
        try {
            ResultSet rs = DBconnection.executeQuery(sql);
            while (rs != null && rs.next()) {
                friends.add(new RegularUser(
                    rs.getString("username"),
                    "***", 
                    rs.getString("email"),
                    rs.getInt("user_id"),
                    rs.getTimestamp("registration_date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }

    public static List<Product> getSavedProducts() {
        List<Product> products = new ArrayList<>();
        return products;
    }

    public static List<Purchase> getOrders(String filter) {
        List<Purchase> orders = new ArrayList<>();
        return orders; 
    }

    public static void deleteAddress(int addressId) {
        new AddressService().deleteAddress(addressId);
    }

    public static void setDefaultAddress(int addressId) {
        new AddressService().setDefaultAddress(1, addressId);
    }

    public static List<Address> getAddresses() {
        return new AddressService().getAddresses(1);
    }

    public static void addAddress(Address addr) {
        new AddressService().addAddress(addr);
    }
}