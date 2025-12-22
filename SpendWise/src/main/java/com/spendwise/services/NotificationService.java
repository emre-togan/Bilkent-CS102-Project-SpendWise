package com.spendwise.services;

import java.sql.Timestamp;
import java.util.ArrayList;

// DB'de notifications tablosu garanti değil.
// Şimdilik basit in-memory notification listesi.
public class NotificationService {

    public static class NotificationDTO {
        public int id;
        public int userId;
        public String type;
        public String message;
        public boolean isRead;
        public Timestamp createdAt;

        public NotificationDTO(int id, int userId, String type, String message, boolean isRead, Timestamp createdAt) {
            this.id = id;
            this.userId = userId;
            this.type = type;
            this.message = message;
            this.isRead = isRead;
            this.createdAt = createdAt;
        }
    }

    private static int seq = 1;
    private static final ArrayList<NotificationDTO> notifications = new ArrayList<>();

    public boolean sendBudgetAlert(int userId, String message) {
        return add(userId, "BUDGET", message);
    }

    public boolean sendDiscountAlert(int userId, String message) {
        return add(userId, "DISCOUNT", message);
    }

    public boolean sendFriendRequest(int toUserId, String message) {
        return add(toUserId, "FRIEND_REQUEST", message);
    }

    public ArrayList<NotificationDTO> getNotifications(int userId) {
        ArrayList<NotificationDTO> list = new ArrayList<>();
        for (NotificationDTO n : notifications) {
            if (n.userId == userId)
                list.add(n);
        }
        return list;
    }

    public boolean markAsRead(int notificationId) {
        for (NotificationDTO n : notifications) {
            if (n.id == notificationId) {
                n.isRead = true;
                return true;
            }
        }
        return false;
    }

    private boolean add(int userId, String type, String message) {
        NotificationDTO n = new NotificationDTO(seq++, userId, type, message, false,
                new Timestamp(System.currentTimeMillis()));
        notifications.add(n);
        return true;
    }
}
