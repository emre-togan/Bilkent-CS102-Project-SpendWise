package com.spendwise.models;

import java.sql.Timestamp;

public class Message {

    private int messageId;
    private int senderId;
    private int receiverId;
    private String content;
    private Timestamp timestamp;
    private boolean isRead;

    private Integer sharedProductId;
    private Product productObject;
    private boolean isSentByMe;
    private String timeStr;

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

    // Constructor for Chatpanel UI 
    public Message(String content, boolean isSentByMe, String timeStr) {
        this.content = content;
        this.isSentByMe = isSentByMe;
        this.timeStr = timeStr;
        this.senderId = isSentByMe ? 1 : 2;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.isRead = true;
    }

    // Constructor for ChatPanel UI 
    public Message(Product product, boolean isSentByMe, String timeStr) {
        this.productObject = product;
        this.sharedProductId = product.getProductId();
        this.isSentByMe = isSentByMe;
        this.timeStr = timeStr;
        this.content = "Shared Product: " + product.getName();
        this.senderId = isSentByMe ? 1 : 2;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.isRead = true;
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

    public boolean isSentByMe() {
        return isSentByMe || (senderId == 1);
    }

    public boolean isProduct() {
        return sharedProductId != null;
    }

    public Product getProductObject() {
        return productObject;
    }

    public Integer getSharedProduct() {
        return sharedProductId;
    }

    public String getTime() {
        if (timeStr != null)
            return timeStr;
        if (timestamp != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
            return sdf.format(timestamp);
        }
        return "";
    }
}
