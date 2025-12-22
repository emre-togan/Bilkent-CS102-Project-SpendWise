package com.spendwise.controllers;

import java.util.List;

import com.spendwise.models.User;
import com.spendwise.models.Address;
import com.spendwise.models.Product;
import com.spendwise.models.Purchase;
import com.spendwise.services.ProfileService;

public class ProfileController {

    public User getCurrentUser() {
        return ProfileService.getCurrentUser();
    }

    public List<User> getFriends() {
        return ProfileService.getFriends();
    }

    public List<Product> getSavedProducts() {
        return ProfileService.getSavedProducts();
    }

    public List<Purchase> getPurchaseHistory(String filter) {
        return ProfileService.getOrders(filter);
    }

    public List<Address> getAddresses() {
        return ProfileService.getAddresses();
    }

    public void addAddress(Address address) {
        ProfileService.addAddress(address);
    }

    public void deleteAddress(int addressId) {
        ProfileService.deleteAddress(addressId);
    }

    public void setDefaultAddress(int addressId) {
        ProfileService.setDefaultAddress(addressId);
    }
}
