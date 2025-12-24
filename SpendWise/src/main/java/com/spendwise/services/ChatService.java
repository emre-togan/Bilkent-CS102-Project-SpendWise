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

    public static List<User> getFriends(int currentUserId) {
        List<User> friends = new ArrayList<>();

        // Ensure schema is updated
        ensureFriendStatusColumn();

        String sql = "SELECT u.* FROM users u " +
                "JOIN friends f ON u.user_id = f.friend_id " +
                "WHERE f.user_id = ? AND f.status = 'ACCEPTED'";

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
     * Send a friend request.
     */
    public static boolean sendFriendRequest(int currentUserId, int friendId) {
        ensureFriendStatusColumn();

        // Check if already friends or request pending
        if (isFriend(currentUserId, friendId) || hasPendingRequest(currentUserId, friendId)) {
            return false;
        }

        // Insert single row: Request Sender -> Receiver (PENDING)
        String sql = "INSERT INTO friends (user_id, friend_id, status) VALUES (?, ?, 'PENDING')";
        int row = DBconnection.executeUpdate(sql, currentUserId, friendId);

        return row > 0;
    }

    public static boolean acceptFriendRequest(int currentUserId, int requesterId) {
        // Update requester -> current to ACCEPTED
        String updateSql = "UPDATE friends SET status = 'ACCEPTED' WHERE user_id = ? AND friend_id = ?";
        // Insert current -> requester as ACCEPTED
        String insertSql = "INSERT INTO friends (user_id, friend_id, status) VALUES (?, ?, 'ACCEPTED')";

        int row1 = DBconnection.executeUpdate(updateSql, requesterId, currentUserId);
        int row2 = DBconnection.executeUpdate(insertSql, currentUserId, requesterId);

        return row1 > 0 && row2 > 0;
    }

    public static boolean rejectFriendRequest(int currentUserId, int requesterId) {
        // Delete the request
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        int row = DBconnection.executeUpdate(sql, requesterId, currentUserId);
        return row > 0;
    }

    public static List<User> getFriendRequests(int currentUserId) {
        ensureFriendStatusColumn();
        List<User> requests = new ArrayList<>();
        // Select users who added 'currentUserId' but status is PENDING
        String sql = "SELECT u.* FROM users u " +
                "JOIN friends f ON u.user_id = f.user_id " +
                "WHERE f.friend_id = ? AND f.status = 'PENDING'";

        try {
            ResultSet resultSet = DBconnection.executeQuery(sql, currentUserId);
            while (resultSet != null && resultSet.next()) {
                requests.add(mapUser(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public static boolean isFriend(int userId, int friendId) {
        ensureFriendStatusColumn();
        String sql = "SELECT COUNT(*) as count FROM friends WHERE user_id = ? AND friend_id = ? AND status = 'ACCEPTED'";
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

    private static boolean hasPendingRequest(int senderId, int receiverId) {
        String sql = "SELECT COUNT(*) as count FROM friends WHERE user_id = ? AND friend_id = ? AND status = 'PENDING'";
        try {
            ResultSet rs = DBconnection.executeQuery(sql, senderId, receiverId);
            if (rs != null && rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Helper to add 'status' column if missing
    private static void ensureFriendStatusColumn() {
        // Check if column exists using information_schema (MySQL specific but project
        // uses MySQL)
        String checkSql = "SELECT count(*) as cnt FROM information_schema.columns " +
                "WHERE table_name = 'friends' AND column_name = 'status' AND table_schema = 'spendwise'";
        try {
            ResultSet rs = DBconnection.executeQuery(checkSql);
            if (rs != null && rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    return; // Column exists
                }
            }

            // If we get here, column doesn't exist (or check failed, we assume it doesn't)
            System.out.println("Adding 'status' column to friends table...");
            String alterSql = "ALTER TABLE friends ADD COLUMN status VARCHAR(20) DEFAULT 'ACCEPTED'";
            DBconnection.executeUpdate(alterSql);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isUserOnline(String username) {
        return Math.random() > 0.5;
    }

    public static void sendMessage(int senderId, String receiverName, String content) {
        int receiverId = getUserIdByName(receiverName);
        if (receiverId == -1)
            return;

        String sql = "INSERT INTO messages (sender_id, receiver_id, content, timestamp, is_read, shared_product_id) VALUES (?, ?, ?, ?, ?, ?)";
        DBconnection.executeUpdate(sql, senderId, receiverId, content, new Timestamp(System.currentTimeMillis()), false,
                null);
    }

    public static void sendProductRecommendation(int senderId, String receiverName, Product product) {
        int receiverId = getUserIdByName(receiverName);
        if (receiverId == -1)
            return;

        String content = "Check out: " + product.getName() + " - $" + product.getPriceAfterDiscount();
        String sql = "INSERT INTO messages (sender_id, receiver_id, content, timestamp, is_read, shared_product_id) VALUES (?, ?, ?, ?, ?, ?)";
        DBconnection.executeUpdate(sql, senderId, receiverId, content, new Timestamp(System.currentTimeMillis()), false,
                null);
    }

    public static List<Message> getMessages(int userId, String friendName) {
        List<Message> messages = new ArrayList<>();
        int friendId = getUserIdByName(friendName);
        if (friendId == -1)
            return messages;

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
                        (Integer) rs.getObject("shared_product_id")));
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
                resultSet.getTimestamp("registration_date"));
    }
}