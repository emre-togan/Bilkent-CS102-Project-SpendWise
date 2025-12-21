import java.util.ArrayList;
import java.util.List;

public class ProfileService {

    // PDF GÖREVİ: loadUserProfile() için veri sağlar
    public static User getCurrentUser() {
        // Backend entegrasyonunda burası veritabanından çekecek
        // Şimdilik simüle ediyoruz
        User user = new User("Sarah Johnson", "sarah.johnson@email.com", "SJ", 156, "$2.4k", 12);
        
        // Arkadaşları ekleyelim
        user.addFriend("Emily R.");
        user.addFriend("Michael T.");
        user.addFriend("David K.");
        
        return user;
    }

    // PDF GÖREVİ: loadSavedProducts() için veri sağlar
    public static List<Product> getSavedProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("Nike Air Zoom", "$89.99", "$120.00"));
        products.add(new Product("Sony WH-1000XM5", "$299.00", "$348.00"));
        products.add(new Product("Apple iPad Air", "$559.00", "$599.00"));
        return products;
    }
    
    // PDF GÖREVİ: handleEditProfile() için
    public static void updateProfile(String newName, String newEmail) {
        System.out.println("[Backend Call] Profil güncellendi: " + newName);
    }
}