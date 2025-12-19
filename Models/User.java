package Models;

import java.security.Timestamp;

public class User {
    
    private int id;
    private String userName;
    private String password;
    private String eMail;
    private Timestamp accountCreationTime;
    
    public User(String userName, String password, String eMail, int id, Timestamp accountCreationTime){
        this.userName = userName;
        this.password = password;
        this.eMail = eMail;
        this.id = id;
        this.accountCreationTime = accountCreationTime;
    }

    public User(String userName, String password, String eMail){
        this.userName = userName;
        this.password = password;
        this.eMail = eMail;
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
