public class Message {
    private String content; // Yazı mesajı
    private boolean isSentByMe;
    private String time;
    private Product sharedProduct; // Ürün mesajı (Varsa burası dolu olur)

    // Normal Yazı Mesajı İçin Kurucu
    public Message(String content, boolean isSentByMe, String time) {
        this.content = content;
        this.isSentByMe = isSentByMe;
        this.time = time;
        this.sharedProduct = null;
    }

    // Ürün Paylaşımı İçin Kurucu (YENİ)
    public Message(Product product, boolean isSentByMe, String time) {
        this.content = null;
        this.sharedProduct = product;
        this.isSentByMe = isSentByMe;
        this.time = time;
    }

    public String getContent() { return content; }
    public boolean isSentByMe() { return isSentByMe; }
    public String getTime() { return time; }
    
    // Bu mesaj bir ürün mü?
    public boolean isProduct() { return sharedProduct != null; }
    public Product getSharedProduct() { return sharedProduct; }
}