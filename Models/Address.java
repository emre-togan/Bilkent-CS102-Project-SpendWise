package Models;

public class Address {
    
    private int id;
    private int userId;
    private String label;
    private String fullName;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String phone;
    private boolean isDefault;

    // Constructor for create new Adress
    public Address(String label, String fullName, String street, String city, String state, String zip, String phone, boolean isDefault) {
        this.label = label;
        this.fullName = fullName;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phone = phone;
        this.isDefault = isDefault;
    }

    // Constructor for database 
    public Address(int id, int userId, String label, String fullName, String street, String city, String state, String zip, String phone, boolean isDefault) {
        this.id = id;
        this.userId = userId;
        this.label = label;
        this.fullName = fullName;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phone = phone;
        this.isDefault = isDefault;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getLabel() {
        return label;
    }

    public String getFullName() {
        return fullName;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public String getFullAddress() { 
        return street; 
    }

    public String getCityStateZip() {

        String s = (state == null || state.isEmpty()) ? "" : ", " + state;
        String z = (zip == null || zip.isEmpty()) ? "" : " " + zip;
        return city + s + z;
    }

}
