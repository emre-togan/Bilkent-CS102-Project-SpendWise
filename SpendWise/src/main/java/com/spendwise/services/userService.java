package com.spendwise.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.spendwise.database.DBconnection;
import com.spendwise.models.User;

public class userService {

    public boolean createUser(User user) {
        boolean isCreated = false;

        String sql = "INSERT INTO users (username, password, email, registration_date) VALUES (?, ?, ?, ?)";
        int affectedRows = DBconnection.executeUpdate(sql,
                user.getUserName(),
                user.getPassword(),
                user.geteMail(),
                user.getAccountCreationTime());

        if (affectedRows > 0) {
            isCreated = true;
        }

        return isCreated;
    }

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

        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return targetUser;
    }
}
