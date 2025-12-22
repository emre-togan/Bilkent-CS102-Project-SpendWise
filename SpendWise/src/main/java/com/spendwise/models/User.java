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

}
