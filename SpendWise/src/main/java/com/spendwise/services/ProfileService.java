package com.spendwise.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.spendwise.database.DBconnection;
import com.spendwise.models.User;
import com.spendwise.models.Address;
import com.spendwise.models.Product;
import com.spendwise.models.Purchase;

public class ProfileService {

    public static User getCurrentUser() {
        int userId = 1;
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try {
            ResultSet rs = DBconnection.executeQuery(sql, userId);
            if (rs != null && rs.next()) {
                return new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getInt("user_id"),
                        rs.getTimestamp("registration_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new User("Guest", "", "guest@email.com");
    }

    public static List<User> getFriends() {
        List<User> friends = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE user_id != 1 LIMIT 5";
        try {
            ResultSet rs = DBconnection.executeQuery(sql);
            while (rs != null && rs.next()) {
                friends.add(new User(
                        rs.getString("username"),
                        "***",
                        rs.getString("email"),
                        rs.getInt("user_id"),
                        rs.getTimestamp("registration_date")));
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

    public static boolean updateProfile(String name, String email) {
        return true;
    }

    public static List<Product> getWishlist() {

        return new ArrayList<>();
    }

    public static void removeFromWishlist(Product product) {

    }

    public static void trackOrder(int orderId) {
        System.out.println("Tracking order: " + orderId);
    }

    public static void buyAgain(int orderId) {
        System.out.println("Buy again order: " + orderId);
    }
}