package Controllers;

import java.util.List;

import Models.RegularUser;
import Models.Product;
import Models.Purchase; 
import Services.ProfileService;
import Services.AddressService; 

public class ProfileController {

    public RegularUser getCurrentUser(){
        return (RegularUser) ProfileService.getCurrentUser();
    }

    public List<RegularUser> getFriends(){
        return ProfileService.getFriends();
    }

    public List<Product> getSavedProducts(){
        return ProfileService.getSavedProducts();
    }

    public List<Purchase> getPurchaseHistory(String filter){
        return ProfileService.getOrders(filter);
    }
 
    public List<AddressService.AddressDTO> getAddresses(){
        return ProfileService.getAddresses();
    }

    public void addAddress(AddressService.AddressDTO address){
        ProfileService.addAddress(address);
    }

    public void deleteAddress(int addressId){
        ProfileService.deleteAddress(addressId);
    }

    public void setDefaultAddress(int addressId){
        ProfileService.setDefaultAddress(addressId);
    }
}

