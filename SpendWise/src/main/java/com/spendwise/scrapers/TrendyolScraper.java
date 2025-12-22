package com.spendwise.scrapers;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.spendwise.models.Product;

public class TrendyolScraper {
    public ArrayList<Product> searchAndSearch(String searchTerm) {
        ArrayList<Product> productsList = new ArrayList<>();

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(false)
                    .setArgs(Arrays.asList(
                            "--disable-blink-features=AutomationControlled",
                            "--disable-dev-shm-usage",
                            "--no-sandbox")));

            BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                    .setViewportSize(1920, 1080)
                    .setUserAgent(
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36"));

            Page page = context.newPage();

            String searchUrl = "https://www.trendyol.com/sr?q=" + searchTerm.replace(" ", "%20");
            page.navigate(searchUrl);
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            Locator products = null;
            String[] possibleSelectors = {
                    "div.p-card-wrppr",
                    "div.p-card-chldrn-cntnr",
                    "a[href*='-p-']",
                    "div[class*='product']",
                    "div[class*='prdct']",
                    "article",
                    ".product-down"
            };

            for (String selector : possibleSelectors) {
                try {
                    Locator testLocator = page.locator(selector);
                    int foundCount = testLocator.count();

                    if (foundCount > 0) {
                        products = testLocator;
                        break;
                    }
                } catch (Exception e) {
                    continue;
                }
            }

            if (products == null || products.count() == 0) {

                page.screenshot(new Page.ScreenshotOptions().setPath(java.nio.file.Paths.get("debug_screenshot.png")));

                throw new Exception("No products found with any selector.");
            }

            int count = Math.min(products.count(), 10);

            String category = extractCategory(page);

            List<String> junkWords = Arrays.asList(
                    "Kupon", "Kargo", "Bedava", "Fırsatı", "Sepette",
                    "Popüler", "Yorum", "Özel", "Plus", "İndirim",
                    "Son", "Günün", "En Düşük", "Süper", "Puan",
                    "Hızlı teslimat", "teslimat yapılıyor", "Çok Al Az Öde",
                    "Ücretsiz Kargo", "Aynı Gün Kargo", "Favorilere Ekle",
                    "Sepete Ekle", "TL Kupon", "kampanya",
                    "öneriyor", "inceledi", "kişi", "saatte",
                    "Benim Haklarım", "değerlendirme",
                    "En Çok Satan", "Satan", "Beğenilen");

            for (int i = 0; i < count; i++) {
                try {
                    Locator item = products.nth(i);

                    String brandAndName = extractProductName(item, junkWords);

                    String priceText = extractPrice(item);

                    String imageUrl = extractImageUrl(item);

                    double numericPrice = parsePrice(priceText);

                    if (brandAndName.equals("Product Details Unreachable") || numericPrice == 0.0) {
                        continue;
                    }

                    productsList.add(new Product(brandAndName, numericPrice, category, imageUrl, "trendyol"));

                } catch (Exception e) {
                    continue;
                }
            }
            browser.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return productsList;
    }

    private String extractCategory(Page page) {
        try {
            Locator breadcrumb = page.locator(".breadcrumb");
            if (breadcrumb.count() > 0) {
                return breadcrumb.innerText().replace("\n", " > ");
            }
        } catch (Exception e) {

        }
        return "General";
    }

    private String extractProductName(Locator item, List<String> junkWords) {
        try {
            Locator nameLocator = item.locator(".prdct-desc-cntnr-name, span.prdct-desc-cntnr-name");
            if (nameLocator.count() > 0) {
                String name = nameLocator.first().textContent().trim();
                if (name.length() > 5 && !isJunkText(name, junkWords)) {
                    return name;
                }
            }

            String allText = item.innerText();
            String[] lines = allText.split("\n");

            for (int j = 0; j < lines.length; j++) {
                String line = lines[j].trim();

                if (line.contains("TL") || line.contains("₺")) {
                    for (int k = j - 1; k >= 0; k--) {
                        String potName = lines[k].trim();

                        if (isValidProductName(potName, junkWords)) {
                            return potName;
                        }
                    }
                    break;
                }
            }

            for (String line : lines) {
                String trimmed = line.trim();
                if (trimmed.length() > 15 && isValidProductName(trimmed, junkWords)) {
                    return trimmed;
                }
            }

        } catch (Exception e) {
        }
        return "Product Details Unreachable";
    }

    private boolean isValidProductName(String text, List<String> junkWords) {
        if (text == null || text.isEmpty())
            return false;

        if (text.length() < 10)
            return false;

        if (text.matches("^[0-9.,]+$"))
            return false;

        if (text.matches("^\\([0-9]+\\)$"))
            return false;

        if (text.matches(".*\\d+.*saatte.*") ||
                text.matches(".*\\d+.*kişi.*inceledi.*")) {
            return false;
        }

        if (text.matches(".*öneriyor!?$")) {
            return false;
        }

        if (text.matches(".*\\d+\\..*Ürün.*")) {
            return false;
        }

        if (text.contains("Haklarım") || text.contains("Hakların")) {
            return false;
        }

        if (isJunkText(text, junkWords))
            return false;

        if (!text.matches(".*[a-zA-ZçÇğĞıİöÖşŞüÜ].*")) {
            return false;
        }

        return true;
    }

    private boolean isJunkText(String text, List<String> junkWords) {
        String lowerText = text.toLowerCase();
        return junkWords.stream()
                .anyMatch(junk -> lowerText.contains(junk.toLowerCase()));
    }

    private String extractPrice(Locator item) {
        try {
            Locator priceLocator = item.locator(".prc-box-dscntd, div.prc-box-dscntd, span.prc-dsc");
            if (priceLocator.count() > 0) {
                String price = priceLocator.first().textContent().trim();
                if ((price.contains("TL") || price.contains("₺")) &&
                        !price.toLowerCase().contains("kupon")) {
                    return price;
                }
            }

            String allText = item.innerText();
            String[] lines = allText.split("\n");

            for (String line : lines) {
                line = line.trim();
                if ((line.contains("TL") || line.contains("₺")) &&
                        line.matches(".*\\d+.*") &&
                        !line.toLowerCase().contains("kupon") &&
                        !line.toLowerCase().contains("indirim") &&
                        !line.toLowerCase().contains("kampanya")) {
                    return line;
                }
            }
        } catch (Exception e) {

        }
        return "0 TL";
    }

    private String extractImageUrl(Locator item) {
        try {
            Locator imgLocator = item.locator("img");
            if (imgLocator.count() > 0) {
                String src = imgLocator.first().getAttribute("src");
                if (src != null && !src.isEmpty()) {
                    return src;
                }
            }
        } catch (Exception e) {

        }
        return "No Image";
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