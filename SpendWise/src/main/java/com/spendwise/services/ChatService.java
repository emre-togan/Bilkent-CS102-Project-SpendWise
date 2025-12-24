package com.spendwise.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.spendwise.database.DBconnection;
import com.spendwise.models.Message;
import com.spendwise.models.Product;
import com.spendwise.models.User;

public class ChatService {

    /**
     * Get ONLY added friends for the user.
     */
    public static List<User> getFriends(int currentUserId) {
        List<User> friends = new ArrayList<>();
        // Query joins users and friends table to find matches
        String sql = "SELECT u.* FROM users u " +
                     "JOIN friends f ON u.user_id = f.friend_id " +
                     "WHERE f.user_id = ?";

        try {
            ResultSet resultSet = DBconnection.executeQuery(sql, currentUserId);
            while (resultSet != null && resultSet.next()) {
                friends.add(mapUser(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }

    /**
     * Search for any user in the system by username.
     */
    public static List<User> searchUsers(String query) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE username LIKE ?";
        try {
            ResultSet resultSet = DBconnection.executeQuery(sql, "%" + query + "%");
            while (resultSet != null && resultSet.next()) {
                users.add(mapUser(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Add a new friend relationship.
     */
    public static boolean addFriend(int currentUserId, int friendId) {
        // Check if already friends
        if (isFriend(currentUserId, friendId)) {
            return false;
        }

        String sql = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
        // Add both ways so they appear in each other's lists
        int row1 = DBconnection.executeUpdate(sql, currentUserId, friendId);
        int row2 = DBconnection.executeUpdate(sql, friendId, currentUserId);
        
        return row1 > 0 && row2 > 0;
    }

    public static boolean isFriend(int userId, int friendId) {
        String sql = "SELECT COUNT(*) as count FROM friends WHERE user_id = ? AND friend_id = ?";
        try {
            ResultSet rs = DBconnection.executeQuery(sql, userId, friendId);
            if (rs != null && rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isUserOnline(String username) {
        // Simple mock or check last_active
        return Math.random() > 0.5; 
    }

    public static void sendMessage(int senderId, String receiverName, String content) {
        int receiverId = getUserIdByName(receiverName);
        if (receiverId == -1) return;

        String sql = "INSERT INTO messages (sender_id, receiver_id, content, timestamp, is_read, shared_product_id) VALUES (?, ?, ?, ?, ?, ?)";
        DBconnection.executeUpdate(sql, senderId, receiverId, content, new Timestamp(System.currentTimeMillis()), false, null);
    }

    public static void sendProductRecommendation(int senderId, String receiverName, Product product) {
        int receiverId = getUserIdByName(receiverName);
        if (receiverId == -1) return;

        String content = "Check out: " + product.getName() + " - $" + product.getPriceAfterDiscount();
        String sql = "INSERT INTO messages (sender_id, receiver_id, content, timestamp, is_read, shared_product_id) VALUES (?, ?, ?, ?, ?, ?)";
        DBconnection.executeUpdate(sql, senderId, receiverId, content, new Timestamp(System.currentTimeMillis()), false, null);
    }

    public static List<Message> getMessages(int userId, String friendName) {
        List<Message> messages = new ArrayList<>();
        int friendId = getUserIdByName(friendName);
        if (friendId == -1) return messages;

        String sql = "SELECT * FROM messages WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) ORDER BY timestamp ASC";
        
        try {
            ResultSet rs = DBconnection.executeQuery(sql, userId, friendId, friendId, userId);
            while (rs != null && rs.next()) {
                messages.add(new Message(
                    rs.getInt("message_id"),
                    rs.getInt("sender_id"),
                    rs.getInt("receiver_id"),
                    rs.getString("content"),
                    rs.getTimestamp("timestamp"),
                    rs.getBoolean("is_read"),
                    (Integer) rs.getObject("shared_product_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    private static int getUserIdByName(String userName) {
        String sql = "SELECT user_id FROM users WHERE username = ?";
        try {
            ResultSet resultSet = DBconnection.executeQuery(sql, userName);
            if (resultSet != null && resultSet.next()) {
                return resultSet.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static User mapUser(ResultSet resultSet) throws SQLException {
        return new User(
            resultSet.getString("username"),
            "***", 
            resultSet.getString("email"),
            resultSet.getInt("user_id"),
            resultSet.getTimestamp("registration_date")
        );
    }
}