package com.spendwise.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.spendwise.database.DBconnection;
import com.spendwise.models.Address;

public class AddressService {

    public List<Address> getAddresses(int userId) {
        List<Address> list = new ArrayList<>();
        String sql = "SELECT * FROM addresses WHERE user_id = ?";

        try {
            ResultSet rs = DBconnection.executeQuery(sql, userId);
            while (rs != null && rs.next()) {
                list.add(new Address(
                        rs.getInt("address_id"),
                        rs.getInt("user_id"),
                        rs.getString("title"),
                        rs.getString("full_name"),
                        rs.getString("address_line"),
                        rs.getString("city"),
                        rs.getString("state"),
                        rs.getString("zip_code"),
                        rs.getString("phone_number"),
                        rs.getBoolean("is_default")));
            }
        }

        catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addAddress(Address addr) {
        String sql = "INSERT INTO addresses (user_id, title, full_name, address_line, city, state, zip_code, phone_number, is_default) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        DBconnection.executeUpdate(sql,
                1,
                addr.getLabel(),
                addr.getFullName(),
                addr.getStreet(),
                addr.getCity(),
                addr.getState(),
                addr.getZip(),
                addr.getPhone(),
                addr.isDefault());
    }

    public void deleteAddress(int addressId) {
        String sql = "DELETE FROM addresses WHERE address_id = ?";
        DBconnection.executeUpdate(sql, addressId);
    }

    public void setDefaultAddress(int userId, int addressId) {
        String resetSql = "UPDATE addresses SET is_default = false WHERE user_id = ?";
        DBconnection.executeUpdate(resetSql, userId);

        String setSql = "UPDATE addresses SET is_default = true WHERE address_id = ?";
        DBconnection.executeUpdate(setSql, addressId);
    }
}