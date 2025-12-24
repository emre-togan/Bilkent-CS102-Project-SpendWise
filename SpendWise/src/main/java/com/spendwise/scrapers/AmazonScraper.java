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
                    .setHeadless(true)
                    .setArgs(Arrays.asList(
                            "--disable-blink-features=AutomationControlled",
                            "--disable-dev-shm-usage",
                            "--no-sandbox")));

            BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                    .setViewportSize(1920, 1080)
                    .setUserAgent(
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36"));

            Page page = context.newPage();

            String searchUrl = "https://www.amazon.com.tr/s?k=" + searchTerm.replace(" ", "+");
            page.navigate(searchUrl);

            try {
                page.waitForSelector("div[data-component-type='s-search-result']",
                        new Page.WaitForSelectorOptions().setTimeout(5000));
            } catch (Exception e) {
                System.out.println("Amazon: Ürün bulunamadı veya sayfa yüklenemedi.");
                return productsList;
            }

            List<Locator> items = page.locator("div[data-component-type='s-search-result']").all();

            int limit = Math.min(items.size(), 10);

            for (int i = 0; i < limit; i++) {
                Locator item = items.get(i);
                try {
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

                    // expense
                    Locator priceLoc = item.locator(".a-price .a-offscreen");
                    String priceText = priceLoc.count() > 0 ? priceLoc.first().innerText() : "0";
                    double price = parsePrice(priceText);

                    // picture
                    Locator imgLoc = item.locator("img.s-image");
                    String imageUrl = imgLoc.count() > 0 ? imgLoc.first().getAttribute("src") : "";

                    if (price > 0) {
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