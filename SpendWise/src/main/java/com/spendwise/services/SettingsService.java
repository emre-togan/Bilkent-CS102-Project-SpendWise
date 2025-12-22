package com.spendwise.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.spendwise.database.DBconnection;

public class SettingsService {

    private static final Map<Integer, Map<String, Boolean>> store = new HashMap<>();

    public boolean updateSettings(int userId, Map<String, Boolean> settings) {
        if (settings == null)
            return false;
        store.put(userId, new HashMap<>(settings));
        return true;
    }

    public Map<String, Boolean> getSettings(int userId) {
        Map<String, Boolean> s = store.get(userId);
        return (s == null) ? new HashMap<>() : new HashMap<>(s);
    }

    public boolean toggleNotification(int userId, String type, boolean enabled) {
        if (type == null || type.trim().isEmpty())
            return false;
        Map<String, Boolean> s = store.getOrDefault(userId, new HashMap<>());
        s.put(type.trim(), enabled);
        store.put(userId, s);
        return true;
    }

    public boolean updateProfile(int userId, String newName, String newEmail) {
        String sql = "UPDATE users SET username = ?, email = ? WHERE user_id = ?";

        try {
            DBconnection.executeUpdate(sql, newName, newEmail, userId);
            return true;
        }

        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean verifyPassword(int userId, String inputPassword) {

        String sql = "SELECT password FROM users WHERE user_id = ?";

        try {
            ResultSet ResultSet = DBconnection.executeQuery(sql, userId);

            if (ResultSet != null && ResultSet.next()) {

                String dbPassword = ResultSet.getString("password");
                return dbPassword.equals(inputPassword);
            }
        }

        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePassword(int userId, String newPassword) {

        String sql = "UPDATE users SET password = ? WHERE user_id = ?";
        try {
            DBconnection.executeUpdate(sql, newPassword, userId);
            return true;
        }

        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAccount(int userId) {

        String sql = "DELETE FROM users WHERE user_id = ?";

        try {
            DBconnection.executeUpdate(sql, userId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
