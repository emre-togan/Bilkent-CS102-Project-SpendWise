package Controllers;

import java.sql.Timestamp;
import Models.User;
import Services.userService;

public class AuthController {
    
    private userService userService;
    private User currentUser;
    
    public AuthController(){
        this.userService = new userService();
        this.currentUser = null;
    }

    public boolean login(String userName, String passWord){

        User userFromTheList = userService.getUserByUserName(userName);

        if(userFromTheList != null){
            if(userFromTheList.getPassword().equals(passWord)){
                this.currentUser = userFromTheList;
                return true;
            }
        }

        return false;
    }

    public boolean register(String username, String passWord, String eMail){

        Timestamp start = new Timestamp(System.currentTimeMillis());
        User newUser = new User(username, passWord, eMail,0,start);
        boolean isItAvailabletoCreate = userService.createUser(newUser);

        return isItAvailabletoCreate;
    }

    public void logout(){
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
