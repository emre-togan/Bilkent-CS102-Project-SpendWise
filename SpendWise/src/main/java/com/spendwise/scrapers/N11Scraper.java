package com.spendwise.scrapers;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.spendwise.models.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class N11Scraper {

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

            String searchUrl = "https://www.n11.com/arama?q=" + searchTerm.replace(" ", "+");
            page.navigate(searchUrl);

            try {
                page.waitForLoadState(LoadState.DOMCONTENTLOADED);
                Thread.sleep(2000);
            } catch (Exception e) {

            }

            List<Locator> items = page.locator(".product-item").all();
            if (items.isEmpty()) {
                items = page.locator(".list-ul > li").all();
            }
            if (items.isEmpty()) {
                items = page.locator("li.column").all();
            }
            int limit = Math.min(items.size(), 10);

            for (int i = 0; i < limit; i++) {
                Locator item = items.get(i);
                try {

                    Locator nameLoc = item.locator(".product-item-title");
                    if (nameLoc.count() == 0) {
                        nameLoc = item.locator(".proName");
                    }
                    String name = nameLoc.count() > 0 ? nameLoc.first().innerText().trim() : "No Name";

                    Locator priceLoc = item.locator(".price-currency"); // New selector
                    if (priceLoc.count() == 0) {
                        priceLoc = item.locator(".newPrice ins");
                    }
                    if (priceLoc.count() == 0) {

                        priceLoc = item.locator(".newPrice");
                    }
                    String priceText = priceLoc.count() > 0 ? priceLoc.first().innerText() : "0";
                    double price = parsePrice(priceText);

                    Locator imgLoc = item.locator(".listing-items-image"); // New selector
                    if (imgLoc.count() == 0) {
                        imgLoc = item.locator(".cardImage img");
                    }

                    String imageUrl = imgLoc.count() > 0 ? imgLoc.first().getAttribute("data-original") : "";
                    if (imageUrl == null || imageUrl.isEmpty()) {
                        imageUrl = imgLoc.count() > 0 ? imgLoc.first().getAttribute("src") : "";
                    }

                    if (price > 0) {
                        productsList.add(new Product(name, price, "General", imageUrl, "N11"));
                    }

                } catch (Exception e) {

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