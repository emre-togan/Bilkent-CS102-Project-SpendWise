package com.spendwise.scrapers;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.spendwise.models.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HepsiburadaScraper {

    public ArrayList<Product> searchAndSearch(String searchTerm) {
        ArrayList<Product> productsList = new ArrayList<>();

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(true)
                    .setArgs(Arrays.asList("--disable-blink-features=AutomationControlled")));

            BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                    .setUserAgent(
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36"));

            Page page = context.newPage();

            String searchUrl = "https://www.hepsiburada.com/ara?q=" + searchTerm.replace(" ", "+");
            page.navigate(searchUrl);

            // Sayfanın yüklenmesini bekle
            // Sayfanın yüklenmesini bekle
            try {
                page.waitForLoadState(LoadState.DOMCONTENTLOADED);
                Thread.sleep(2000);
            } catch (Exception e) {
                // ignore
            }

            List<Locator> items = page.locator("li[class*='productListContent']").all();
            if (items.isEmpty()) {
                items = page.locator("li[id^='i']").all();
            }
            int limit = Math.min(items.size(), 10);

            for (int i = 0; i < limit; i++) {
                Locator item = items.get(i);
                try {
                    // İsim
                    Locator nameLoc = item.locator("[class*='title-module_titleText']"); // New selector
                    if (nameLoc.count() == 0) {
                        nameLoc = item.locator("h3[data-test-id='product-card-name']"); // Old selector
                    }
                    if (nameLoc.count() == 0) {
                        nameLoc = item.locator("h3"); // General fallback
                    }

                    String name = nameLoc.count() > 0 ? nameLoc.first().innerText() : "";

                    // Fiyat
                    Locator priceLoc = item.locator("[data-test-id='price-current-price']");
                    if (priceLoc.count() == 0) {
                        priceLoc = item.locator("[class*='price-module_price']"); // New specific selector part
                    }
                    if (priceLoc.count() == 0) {
                        priceLoc = item.locator("[class*='price-module_finalPrice']"); // Another new one
                    }

                    String priceText = priceLoc.count() > 0 ? priceLoc.first().innerText() : "0";
                    double price = parsePrice(priceText);

                    // Resim
                    Locator imgLoc = item.locator("img[data-test-id='product-image']");
                    if (imgLoc.count() == 0) {
                        imgLoc = item.locator("img[class*='hbImageView']"); // New selector
                    }

                    String imageUrl = imgLoc.count() > 0 ? imgLoc.first().getAttribute("src") : "";

                    if (price > 0 && !name.equals("No Name")) {
                        productsList.add(new Product(name, price, "General", imageUrl, "Hepsiburada"));
                    }

                } catch (Exception e) {
                    // Item parse hatası
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return productsList;
    }

    private double parsePrice(String price) {
        try {
            String clean = price.replaceAll("[^0-9.,]", "").trim();
            if (clean.contains(",")) {
                clean = clean.replace(".", "").replace(",", ".");
            }
            return Double.parseDouble(clean);
        } catch (Exception e) {
            return 0.0;
        }
    }
}