package com.spendwise.scrapers;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.spendwise.models.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AmazonScraper {

    public ArrayList<Product> searchAndSearch(String searchTerm) {
        ArrayList<Product> productsList = new ArrayList<>();

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(true) // Amazon için UI görmek iyidir, bazen captcha çıkabilir.
                    .setArgs(Arrays.asList(
                            "--disable-blink-features=AutomationControlled",
                            "--disable-dev-shm-usage",
                            "--no-sandbox")));

            BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                    .setViewportSize(1920, 1080)
                    .setUserAgent(
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36"));

            Page page = context.newPage();

            // Amazon Türkiye araması
            String searchUrl = "https://www.amazon.com.tr/s?k=" + searchTerm.replace(" ", "+");
            page.navigate(searchUrl);

            // Ürünlerin yüklenmesini bekle
            try {
                page.waitForSelector("div[data-component-type='s-search-result']",
                        new Page.WaitForSelectorOptions().setTimeout(5000));
            } catch (Exception e) {
                System.out.println("Amazon: Ürün bulunamadı veya sayfa yüklenemedi.");
                return productsList;
            }

            List<Locator> items = page.locator("div[data-component-type='s-search-result']").all();

            // İlk 5-10 ürünü alalım (Performans için sınır koymak iyi olabilir)
            int limit = Math.min(items.size(), 10);

            for (int i = 0; i < limit; i++) {
                Locator item = items.get(i);
                try {
                    // İsim
                    // İsim
                    Locator nameLoc = item.locator("h2 a span");
                    if (nameLoc.count() == 0) {
                        nameLoc = item.locator("span.a-size-medium");
                    }
                    if (nameLoc.count() == 0) {
                        nameLoc = item.locator("span.a-size-base-plus");
                    }
                    if (nameLoc.count() == 0) {
                        nameLoc = item.locator("h2");
                    }
                    String name = nameLoc.count() > 0 ? nameLoc.first().innerText() : "No Name";

                    // Fiyat
                    Locator priceLoc = item.locator(".a-price .a-offscreen");
                    String priceText = priceLoc.count() > 0 ? priceLoc.first().innerText() : "0";
                    double price = parsePrice(priceText);

                    // Resim
                    Locator imgLoc = item.locator("img.s-image");
                    String imageUrl = imgLoc.count() > 0 ? imgLoc.first().getAttribute("src") : "";

                    // Kategoriyi "Electronic" varsayalım veya genel bırakalım
                    if (price > 0) {
                        // TrendyolScraper'daki constructor yapısını kullanıyoruz
                        // Seller Name olarak "Amazon" veriyoruz.
                        productsList.add(new Product(name, price, "General", imageUrl, "Amazon"));
                    }

                } catch (Exception e) {
                    System.out.println("Amazon parsin error for an item: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return productsList;
    }

    private double parsePrice(String price) {
        try {
            // "1.234,50 TL" veya "TL 1.234,50" formatlarını temizle
            String clean = price.replaceAll("[^0-9.,]", "").trim();

            // Türkçe format: Binlik nokta, ondalık virgül ise -> Javaya uygun (Nokta
            // ondalık) çevir
            if (clean.contains(",")) {
                clean = clean.replace(".", "").replace(",", ".");
            }
            return Double.parseDouble(clean);
        } catch (Exception e) {
            return 0.0;
        }
    }
}