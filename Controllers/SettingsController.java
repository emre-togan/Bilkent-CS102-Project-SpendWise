package Controllers;

import Models.RegularUser;
import Services.SettingsService;

public class SettingsController {
    private SettingsService settingsService;

    public SettingsController(){
        this.settingsService = new SettingsService();
    }

    public boolean updateProfile(RegularUser user, String newName, String newEmail){
        if (newName == null || newName.trim().isEmpty() || newEmail == null || newEmail.trim().isEmpty()){
            return false;
        }
        return settingsService.updateProfile(user.getId(), newName, newEmail);
    }

    public boolean changePassword(RegularUser user, String currentPassword, String newPassword){

        if (newPassword == null || newPassword.length() < 4){
            return false; 
        }

        if (settingsService.verifyPassword(user.getId(), currentPassword)){
            return settingsService.updatePassword(user.getId(), newPassword);
        }
        
        return false;
    }

    public boolean deleteAccount(RegularUser user){
        return settingsService.deleteAccount(user.getId());
    }
}

