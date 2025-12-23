package com.spendwise.models;

import java.sql.Timestamp;

public class User {

    private int id;
    private String userName;
    private String password;
    private String eMail;
    private Timestamp accountCreationTime;

    // default
    public User(String userName, String password, String eMail) {
        this.userName = userName;
        this.password = password;
        this.eMail = eMail;
    }

    // this constructor for get the data from database
    public User(String userName, String password, String eMail, int id, Timestamp accountCreationTime) {
        this.userName = userName;
        this.password = password;
        this.eMail = eMail;
        this.id = id;
        this.accountCreationTime = accountCreationTime;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String geteMail() {
        return eMail;
    }

    public Timestamp getAccountCreationTime() {
        return accountCreationTime;
    }

    // Mock statistics for ProfilePanel
    public int getPurchaseCount() {
        return 0; // Placeholder
    }

    public double getTotalSaved() {
        return 0.0; // Placeholder
    }

    public int getDealsFound() {
        return 0; // Placeholder
    }

    // Chat related mocks
    public String getLastMessage() {
        return "Hello!";
    }

    public String getLastMessageTime() {
        return "10:30 AM";
    }

    public int getUnreadCount() {
        return 0;
    }

    public boolean isOnline() {
        return true;
    }

}
