package com.spendwise.view;

import com.spendwise.models.User;
import com.spendwise.services.userService;

public class UserSession {
    private static int currentUserId = 1; // Default/Mock ID

    public static int getCurrentUserId() {
        return currentUserId;
    }

    public static void setCurrentUserId(int id) {
        currentUserId = id;
    }

    public static User getCurrentUser() {
        return userService.getUserById(currentUserId);
    }
}
