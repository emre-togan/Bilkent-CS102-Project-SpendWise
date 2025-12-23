package com.spendwise.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.spendwise.database.DBconnection;
import com.spendwise.models.User;

public class userService {

    /**
     * Check if a username already exists in the database
     * @param username Username to check
     * @return true if username exists, false otherwise
     */
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) as count FROM users WHERE username = ?";
        
        try {
            ResultSet resultSet = DBconnection.executeQuery(sql, username);
            if (resultSet != null && resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Check if an email already exists in the database
     * @param email Email to check
     * @return true if email exists, false otherwise
     */
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) as count FROM users WHERE email = ?";
        
        try {
            ResultSet resultSet = DBconnection.executeQuery(sql, email);
            if (resultSet != null && resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Create a new user in the database
     * @param user User object to create
     * @return true if user created successfully, false otherwise
     */
    public boolean createUser(User user) {
        // Check if username already exists
        if (usernameExists(user.getUserName())) {
            System.out.println("Username already exists: " + user.getUserName());
            return false;
        }
        
        // Check if email already exists
        if (emailExists(user.geteMail())) {
            System.out.println("Email already exists: " + user.geteMail());
            return false;
        }
        
        boolean isCreated = false;

        String sql = "INSERT INTO users (username, password, email, registration_date) VALUES (?, ?, ?, ?)";
        
        try {
            int affectedRows = DBconnection.executeUpdate(sql,
                    user.getUserName(),
                    user.getPassword(),
                    user.geteMail(),
                    user.getAccountCreationTime());

            if (affectedRows > 0) {
                isCreated = true;
                System.out.println("User created successfully: " + user.getUserName());
            }
        } catch (Exception e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        return isCreated;
    }

    /**
     * Get user by user ID
     * @param id User ID
     * @return User object or mock user if not found
     */
    public static User getUserById(int id) {
        User targetUser = null;

        String sql = "SELECT * FROM users WHERE user_id = ?";
        ResultSet resultSet = DBconnection.executeQuery(sql, id);

        try {

            if (resultSet != null && resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                String permenantUserName = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                Timestamp creationTime = resultSet.getTimestamp("registration_date");

                targetUser = new User(permenantUserName, password, email, userId, creationTime);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (targetUser == null) {
            System.out.println("User not found with ID: " + id + ", returning mock user.");
            targetUser = new User("MockUser", "Display", "mock@email.com", id,
                    new Timestamp(System.currentTimeMillis()));
        }

        return targetUser;
    }

    /**
     * Get user by username
     * @param userName Username to search for
     * @return User object or null if not found
     */
    public User getUserByUserName(String userName) {

        User targetUser = null;

        String sql = "SELECT * FROM users WHERE username = ?";
        ResultSet resultSet = DBconnection.executeQuery(sql, userName);

        try {

            if (resultSet != null && resultSet.next()) {
                int id = resultSet.getInt("user_id");
                String permenantUserName = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                Timestamp creationTime = resultSet.getTimestamp("registration_date");

                targetUser = new User(permenantUserName, password, email, id, creationTime);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return targetUser;
    }
    
    /**
     * Get user by email
     * @param email Email to search for
     * @return User object or null if not found
     */
    public User getUserByEmail(String email) {
        User targetUser = null;

        String sql = "SELECT * FROM users WHERE email = ?";
        ResultSet resultSet = DBconnection.executeQuery(sql, email);

        try {
            if (resultSet != null && resultSet.next()) {
                int id = resultSet.getInt("user_id");
                String userName = resultSet.getString("username");
                String password = resultSet.getString("password");
                String userEmail = resultSet.getString("email");
                Timestamp creationTime = resultSet.getTimestamp("registration_date");

                targetUser = new User(userName, password, userEmail, id, creationTime);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return targetUser;
    }
}
