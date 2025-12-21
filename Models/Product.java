package Models;

public class Product {
    private String name;
    private int productId;
    private String description;
    private double originalPrice;
    private double priceAfterDiscount;
    private String category;
    private String imageUrl;
    private double rating;
    private int reviewCount;
    private String sellerName;
    private boolean isSecondHand;
    private String condition;
    private String location;
    private int discountPercentage;

    // this constructor for scraping
    public Product(String name, double priceAfterDiscount, String category, String imageUrl, String sellerName) {
        this.name = name;
        this.priceAfterDiscount = priceAfterDiscount;
        this.originalPrice = priceAfterDiscount; 
        this.category = category;
        this.imageUrl = imageUrl;
        this.sellerName = sellerName;
        this.isSecondHand = false;
        this.rating = 0.0;
        this.reviewCount = 0;
    }

    // this constructor for get the data from database
    public Product(int productId, String name, String description, double priceAfterDiscount, double originalPrice, 
                   String category, String imageUrl, double rating, int reviewCount, 
                   String sellerName, boolean isSecondHand, String condition, String location, int discountPercentage) {

        this.productId = productId;
        this.name = name;
        this.description = description;
        this.priceAfterDiscount = priceAfterDiscount;
        this.originalPrice = originalPrice;
        this.category = category;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.sellerName = sellerName;
        this.isSecondHand = isSecondHand;
        this.condition = condition;
        this.location = location;
        this.discountPercentage = discountPercentage;
    }

    public String getName() {
        return name;
    }

    public int getProductId() {
        return productId;
    }

    public double getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    public String getCategory() {
        return category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSellerName() {
        return sellerName;
    }

    public boolean isSecondHand() {
        return isSecondHand;
    }

    public double priceWithDiscount(){
        double priceAfterDiscount = originalPrice;
        if(discountPercentage > 0){
            priceAfterDiscount = originalPrice - ((originalPrice * discountPercentage) / 100);
        }

        return priceAfterDiscount;
    }

}

