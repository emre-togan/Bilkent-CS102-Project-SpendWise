package com.spendwise.models;

import java.sql.Timestamp;

public class Message {

    private int messageId;
    private int senderId;
    private int receiverId;
    private String content;
    private Timestamp timestamp;
    private boolean isRead;

    // optional: shared product id (nullable)
    private Integer sharedProductId;

    // create new message
    public Message(int senderId, int receiverId, String content, Timestamp timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
        this.isRead = false;
        this.sharedProductId = null;
    }

    // create new message with shared product
    public Message(int senderId, int receiverId, String content, Timestamp timestamp, Integer sharedProductId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
        this.isRead = false;
        this.sharedProductId = sharedProductId;
    }

    // load from DB
    public Message(int messageId, int senderId, int receiverId, String content, Timestamp timestamp, boolean isRead,
            Integer sharedProductId) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
        this.isRead = isRead;
        this.sharedProductId = sharedProductId;
    }

    public void markAsRead() {
        this.isRead = true;
    }

    public int getMessageId() {
        return messageId;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public Integer getSharedProductId() {
        return sharedProductId;
    }
}
