package Services;

import Database.DBconnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddressService {

    // DB schema (task list):
    // addresses (address_id, user_id, label, full_name, street, city, state, zip, phone, is_default)

    public static class AddressDTO {
        public int addressId;
        public int userId;
        public String label;
        public String fullName;
        public String street;
        public String city;
        public String state;
        public String zip;
        public String phone;
        public boolean isDefault;

        public AddressDTO(int addressId, int userId, String label, String fullName, String street,
                          String city, String state, String zip, String phone, boolean isDefault) {
            this.addressId = addressId;
            this.userId = userId;
            this.label = label;
            this.fullName = fullName;
            this.street = street;
            this.city = city;
            this.state = state;
            this.zip = zip;
            this.phone = phone;
            this.isDefault = isDefault;
        }
    }

    public boolean addAddress(int userId, String label, String fullName, String street,
                              String city, String state, String zip, String phone, boolean isDefault) {

        String sql = "INSERT INTO addresses (user_id, label, full_name, street, city, state, zip, phone, is_default) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int rows = DBconnection.executeUpdate(sql,
                userId, label, fullName, street, city, state, zip, phone, isDefault);

        if (rows > 0 && isDefault) {
            // default ise diğerlerini kapat
            setDefaultAddress(userId, getLastInsertedAddressId(userId));
        }
        return rows > 0;
    }

    public boolean updateAddress(int addressId, String label, String fullName, String street,
                                 String city, String state, String zip, String phone, boolean isDefault) {

        String sql = "UPDATE addresses SET label = ?, full_name = ?, street = ?, city = ?, state = ?, zip = ?, phone = ?, is_default = ? " +
                     "WHERE address_id = ?";

        int rows = DBconnection.executeUpdate(sql,
                label, fullName, street, city, state, zip, phone, isDefault, addressId);

        if (rows > 0 && isDefault) {
            Integer userId = getUserIdByAddressId(addressId);
            if (userId != null) setDefaultAddress(userId, addressId);
        }
        return rows > 0;
    }

    public boolean deleteAddress(int addressId) {
        String sql = "DELETE FROM addresses WHERE address_id = ?";
        int rows = DBconnection.executeUpdate(sql, addressId);
        return rows > 0;
    }

    public boolean setDefaultAddress(int userId, int addressId) {
        // 1) hepsini false yap
        String sql1 = "UPDATE addresses SET is_default = 0 WHERE user_id = ?";
        DBconnection.executeUpdate(sql1, userId);

        // 2) seçileni true yap
        String sql2 = "UPDATE addresses SET is_default = 1 WHERE user_id = ? AND address_id = ?";
        int rows = DBconnection.executeUpdate(sql2, userId, addressId);
        return rows > 0;
    }

    public ArrayList<AddressDTO> getAddresses(int userId) {
        ArrayList<AddressDTO> list = new ArrayList<>();

        String sql = "SELECT * FROM addresses WHERE user_id = ? ORDER BY is_default DESC, address_id DESC";
        ResultSet rs = DBconnection.executeQuery(sql, userId);

        try {
            while (rs != null && rs.next()) {
                AddressDTO dto = new AddressDTO(
                        rs.getInt("address_id"),
                        rs.getInt("user_id"),
                        rs.getString("label"),
                        rs.getString("full_name"),
                        rs.getString("street"),
                        rs.getString("city"),
                        rs.getString("state"),
                        rs.getString("zip"),
                        rs.getString("phone"),
                        rs.getBoolean("is_default")
                );
                list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    private Integer getUserIdByAddressId(int addressId) {
        String sql = "SELECT user_id FROM addresses WHERE address_id = ?";
        ResultSet rs = DBconnection.executeQuery(sql, addressId);
        try {
            if (rs != null && rs.next()) return rs.getInt("user_id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Basit yaklaşım: en son adresi id'ye göre çek
    private int getLastInsertedAddressId(int userId) {
        String sql = "SELECT address_id FROM addresses WHERE user_id = ? ORDER BY address_id DESC LIMIT 1";
        ResultSet rs = DBconnection.executeQuery(sql, userId);
        try {
            if (rs != null && rs.next()) return rs.getInt("address_id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
