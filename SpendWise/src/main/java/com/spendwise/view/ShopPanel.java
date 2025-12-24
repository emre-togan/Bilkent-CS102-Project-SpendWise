package com.spendwise.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import com.spendwise.models.Product;
import com.spendwise.services.productService;
// Scraper imports
import com.spendwise.scrapers.AmazonScraper;
import com.spendwise.scrapers.HepsiburadaScraper;
import com.spendwise.scrapers.N11Scraper;
import com.spendwise.scrapers.TrendyolScraper;

public class ShopPanel extends JPanel {

    private MainFrame mainFrame;
    private JPanel productsContainer;
    private JTextField searchField;
    private JComboBox<String> sortByBox;
    private JToggleButton onlineDealsBtn;
    private JToggleButton secondHandBtn;
    private JButton sellBtn;

    private productService productServiceInstance;
    
    // Scraper Instances
    private TrendyolScraper trendyolScraper;
    private AmazonScraper amazonScraper;
    private N11Scraper n11Scraper;
    private HepsiburadaScraper hepsiburadaScraper;

    private List<Product> currentProducts;
    private boolean showOnlineDeals = true;
    private boolean showSecondHand = false;
    
    // CACHING VARIABLES
    private String lastSearchQuery = ""; // Remembers text when switching tabs
    private String cachedQuery = null;   // Remembers what was last SCRAPED
    private Boolean cachedOnlineState = null;
    private Boolean cachedSecondHandState = null;

    public ShopPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.productServiceInstance = new productService();
        
        // Initialize Scrapers
        this.trendyolScraper = new TrendyolScraper();
        this.amazonScraper = new AmazonScraper();
        this.n11Scraper = new N11Scraper();
        this.hepsiburadaScraper = new HepsiburadaScraper();

        this.currentProducts = new ArrayList<>();

        setLayout(new BorderLayout());
        setBackground(UIConstants.WHITE_BG);

        add(createSideMenu(), BorderLayout.WEST);
        add(createContent(), BorderLayout.CENTER);

        // Initial load
        refreshData();
    }

    private JPanel createSideMenu() {
        JPanel sideMenu = new JPanel();
        sideMenu.setPreferredSize(new Dimension(260, 800));
        sideMenu.setBackground(Color.WHITE);
        sideMenu.setLayout(null);
        sideMenu.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(240, 240, 240)));

        JLabel logo = new JLabel("W$");
        logo.setBounds(20, 25, 50, 50);
        logo.setFont(new Font("Arial", Font.BOLD, 40));
        logo.setForeground(UIConstants.PRIMARY_GREEN);
        sideMenu.add(logo);

        int startY = 120;
        addMenuButton(sideMenu, "🏠", "Dashboard", "DASHBOARD", startY, false);
        addMenuButton(sideMenu, "💳", "Budget", "BUDGET", startY + 60, false);
        addMenuButton(sideMenu, "🧾", "Expenses", "EXPENSES", startY + 120, false);
        addMenuButton(sideMenu, "🛍️", "Shop", "SHOP", startY + 180, true);
        addMenuButton(sideMenu, "💬", "Chat", "CHAT", startY + 240, false);
        addMenuButton(sideMenu, "👤", "Profile", "PROFILE", startY + 300, false);
        addMenuButton(sideMenu, "⚙️", "Settings", "SETTINGS", startY + 360, false);

        return sideMenu;
    }

    private void addMenuButton(JPanel panel, String emoji, String text, String target, int y, boolean active) {
        JButton btn = new JButton(emoji + "  " + text);
        btn.setBounds(10, y, 240, 50);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(0, 15, 0, 0));
        btn.setFocusPainted(false);
        if (active) {
            btn.setBackground(UIConstants.PRIMARY_GREEN);
            btn.setForeground(Color.WHITE);
            btn.setOpaque(true);
        } else {
            btn.setContentAreaFilled(false);
        }
        btn.addActionListener(e -> mainFrame.showPanel(target));
        panel.add(btn);
    }

    private JPanel createContent() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UIConstants.BACKGROUND_LIGHT);

        // Header Section
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(UIConstants.WHITE_BG);
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("Shop & Deals");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Search and Sort Row
        JPanel searchRow = new JPanel(new BorderLayout(15, 0));
        searchRow.setBackground(UIConstants.WHITE_BG);
        searchRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        searchRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        searchField = new JTextField();
        searchField.putClientProperty("JTextField.placeholderText", "Search for products (e.g., iPhone 13)...");
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
            new EmptyBorder(5, 10, 5, 10)));
        
        // Populate with last search if exists
        if (!lastSearchQuery.isEmpty()) {
            searchField.setText(lastSearchQuery);
        }
        
        JButton searchBtn = new JButton("🔍");
        searchBtn.setBackground(UIConstants.PRIMARY_BLUE);
        searchBtn.setForeground(Color.WHITE);
        
        // FORCE REFRESH when clicking search button manually
        searchBtn.addActionListener(e -> {
            cachedQuery = null; // Reset cache to force reload
            refreshData();
        });

        JPanel searchContainer = new JPanel(new BorderLayout());
        searchContainer.add(searchField, BorderLayout.CENTER);
        searchContainer.add(searchBtn, BorderLayout.EAST);

        String[] sorts = {"Sort by: Featured", "Price: Low to High", "Price: High to Low"};
        sortByBox = new JComboBox<>(sorts);
        sortByBox.setBackground(Color.WHITE);
        sortByBox.addActionListener(e -> sortProducts());

        searchRow.add(searchContainer, BorderLayout.CENTER);
        searchRow.add(sortByBox, BorderLayout.EAST);

        // Filters and Actions Row
        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        actionRow.setBackground(UIConstants.WHITE_BG);
        actionRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        onlineDealsBtn = new JToggleButton("Online Deals", true);
        styleToggle(onlineDealsBtn);
        
        secondHandBtn = new JToggleButton("Second Hand", false);
        styleToggle(secondHandBtn);

        // Button Group logic
        onlineDealsBtn.addActionListener(e -> {
            secondHandBtn.setSelected(!onlineDealsBtn.isSelected());
            updateFilterState();
        });
        secondHandBtn.addActionListener(e -> {
            onlineDealsBtn.setSelected(!secondHandBtn.isSelected());
            updateFilterState();
        });

        // Sell Button
        sellBtn = new JButton("➕ Sell Item");
        sellBtn.setFont(new Font("Arial", Font.BOLD, 14));
        sellBtn.setBackground(UIConstants.PRIMARY_BLUE);
        sellBtn.setForeground(Color.WHITE);
        sellBtn.setFocusPainted(false);
        sellBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sellBtn.setBorder(new EmptyBorder(8, 20, 8, 20));
        
        sellBtn.addActionListener(e -> {
            new SellProductDialog((Frame) SwingUtilities.getWindowAncestor(this)).setVisible(true);
            // Force refresh because we added a new item
            cachedQuery = null; 
            secondHandBtn.setSelected(true);
            onlineDealsBtn.setSelected(false);
            updateFilterState();
        });

        actionRow.add(onlineDealsBtn);
        actionRow.add(Box.createHorizontalStrut(10));
        actionRow.add(secondHandBtn);
        actionRow.add(Box.createHorizontalStrut(20));
        actionRow.add(sellBtn);

        headerPanel.add(title);
        headerPanel.add(Box.createVerticalStrut(15));
        headerPanel.add(searchRow);
        headerPanel.add(Box.createVerticalStrut(15));
        headerPanel.add(actionRow);

        content.add(headerPanel, BorderLayout.NORTH);

        // Products Grid
        productsContainer = new JPanel(new GridLayout(0, 3, 20, 20)); // 3 columns
        productsContainer.setBackground(UIConstants.BACKGROUND_LIGHT);
        
        JScrollPane scrollPane = new JScrollPane(productsContainer);
        scrollPane.setBorder(new EmptyBorder(20, 30, 20, 30));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        content.add(scrollPane, BorderLayout.CENTER);

        return content;
    }

    private void styleToggle(JToggleButton btn) {
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.DARK_GRAY);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        btn.setPreferredSize(new Dimension(150, 35));
        
        btn.addChangeListener(e -> {
            if (btn.isSelected()) {
                btn.setBackground(UIConstants.PRIMARY_GREEN);
                btn.setForeground(Color.WHITE);
            } else {
                btn.setBackground(Color.WHITE);
                btn.setForeground(Color.DARK_GRAY);
            }
        });
    }

    private void updateFilterState() {
        showOnlineDeals = onlineDealsBtn.isSelected();
        showSecondHand = secondHandBtn.isSelected();
        refreshData();
    }

    public void refreshData() {
        // --- SEARCH LOGIC ---
        String currentInput = searchField.getText().trim();
        
        // 1. If input is empty, try to use lastSearchQuery (History)
        if (currentInput.isEmpty()) {
            if (!lastSearchQuery.isEmpty()) {
                currentInput = lastSearchQuery;
                searchField.setText(currentInput); 
            }
        } else {
            // Update history
            lastSearchQuery = currentInput;
        }

        final String effectiveQuery = currentInput;

        // 2. CHECK CACHE - Prevent unnecessary scraping
        // If query matches AND filter matches AND we have data -> STOP.
        if (currentProducts != null && !currentProducts.isEmpty()) {
            if (effectiveQuery.equals(cachedQuery) &&
                showOnlineDeals == (cachedOnlineState != null && cachedOnlineState) &&
                showSecondHand == (cachedSecondHandState != null && cachedSecondHandState)) {
                
                // Data is fresh enough, just ensure it's displayed
                displayProducts(currentProducts);
                return; // EXIT HERE
            }
        }

        // 3. Setup UI for Loading
        productsContainer.removeAll();
        productsContainer.setLayout(new BorderLayout());

        // If "Online Deals" is selected BUT we still have no query, don't scrape.
        if (showOnlineDeals && effectiveQuery.isEmpty()) {
            JLabel emptyLabel = new JLabel("Enter a product name to start searching...", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            emptyLabel.setForeground(Color.GRAY);
            productsContainer.add(emptyLabel, BorderLayout.CENTER);
            productsContainer.revalidate();
            productsContainer.repaint();
            
            // Update cache to avoid looping
            cachedQuery = "";
            cachedOnlineState = showOnlineDeals;
            cachedSecondHandState = showSecondHand;
            return;
        }

        // Show loading
        JLabel loading = new JLabel("Searching for '" + (effectiveQuery.isEmpty() ? "All Items" : effectiveQuery) + "'...", SwingConstants.CENTER);
        loading.setFont(new Font("Arial", Font.PLAIN, 16));
        productsContainer.add(loading, BorderLayout.CENTER);
        productsContainer.revalidate();
        productsContainer.repaint();

        // 4. Perform Scraping / DB Fetch
        SwingWorker<List<Product>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Product> doInBackground() {
                List<Product> results = new ArrayList<>();
                try {
                    // A. FETCH DATABASE PRODUCTS (Second Hand)
                    if (showSecondHand) {
                        List<Product> dbProducts = productServiceInstance.getAllProducts();
                        if (dbProducts != null) {
                            for (Product p : dbProducts) {
                                if (p.isSecondHand()) {
                                    if (effectiveQuery.isEmpty() || p.getName().toLowerCase().contains(effectiveQuery.toLowerCase())) {
                                        results.add(p);
                                    }
                                }
                            }
                        }
                    }

                    // B. FETCH ONLINE SCRAPER PRODUCTS
                    if (showOnlineDeals && !effectiveQuery.isEmpty()) {
                        try { results.addAll(trendyolScraper.searchAndSearch(effectiveQuery)); } catch (Exception e) {}
                        try { results.addAll(amazonScraper.searchAndSearch(effectiveQuery)); } catch (Exception e) {}
                        try { results.addAll(n11Scraper.searchAndSearch(effectiveQuery)); } catch (Exception e) {}
                        try { results.addAll(hepsiburadaScraper.searchAndSearch(effectiveQuery)); } catch (Exception e) {}
                    }
                } catch (Exception e) {
                    System.err.println("Error fetching products: " + e.getMessage());
                }
                return results;
            }

            @Override
            protected void done() {
                try {
                    currentProducts = get();
                    
                    // UPDATE CACHE
                    cachedQuery = effectiveQuery;
                    cachedOnlineState = showOnlineDeals;
                    cachedSecondHandState = showSecondHand;
                    
                    displayProducts(currentProducts);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void sortProducts() {
        String sort = (String) sortByBox.getSelectedItem();
        if (currentProducts == null || currentProducts.isEmpty()) return;

        if (sort.contains("Low to High")) {
            currentProducts.sort((p1, p2) -> Double.compare(p1.getPriceAfterDiscount(), p2.getPriceAfterDiscount()));
        } else if (sort.contains("High to Low")) {
            currentProducts.sort((p1, p2) -> Double.compare(p2.getPriceAfterDiscount(), p1.getPriceAfterDiscount()));
        }

        displayProducts(currentProducts);
    }

    private void displayProducts(List<Product> products) {
        productsContainer.removeAll();
        if (products.isEmpty()) {
            productsContainer.setLayout(new BorderLayout());
            JLabel empty = new JLabel("No products found.", SwingConstants.CENTER);
            empty.setFont(new Font("Arial", Font.PLAIN, 18));
            productsContainer.add(empty, BorderLayout.CENTER);
        } else {
            productsContainer.setLayout(new GridLayout(0, 3, 20, 20)); 
            for (Product p : products) {
                productsContainer.add(createProductCard(p));
            }
        }
        productsContainer.revalidate();
        productsContainer.repaint();
    }

    private JPanel createProductCard(Product p) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        card.setPreferredSize(new Dimension(200, 320));

        // IMAGE HANDLING
        JLabel imgLabel = new JLabel();
        imgLabel.setPreferredSize(new Dimension(180, 150));
        imgLabel.setMaximumSize(new Dimension(180, 150));
        imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imgLabel.setBackground(new Color(245, 245, 245));
        imgLabel.setOpaque(true);
        imgLabel.setText("Loading...");

        // Load image in background
        if (p.getImageUrl() != null && !p.getImageUrl().isEmpty()) {
            loadImage(p.getImageUrl(), imgLabel);
        } else {
            imgLabel.setText("No Image");
        }

        // Name
        JLabel nameLabel = new JLabel(p.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setToolTipText(p.getName());

        // Price
        JLabel priceLabel = new JLabel("$" + p.getPriceAfterDiscount());
        priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        priceLabel.setForeground(UIConstants.PRIMARY_GREEN);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Condition / Seller
        JLabel metaLabel = new JLabel(p.isSecondHand() ? "Used - " + p.getSellerName() : "New - Online");
        metaLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        metaLabel.setForeground(Color.GRAY);
        metaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Buy Button
        JButton buyBtn = new JButton(p.isSecondHand() ? "Contact Seller" : "Buy Now");
        buyBtn.setBackground(p.isSecondHand() ? UIConstants.PRIMARY_BLUE : new Color(255, 153, 0));
        buyBtn.setForeground(Color.WHITE);
        buyBtn.setFocusPainted(false);
        buyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        buyBtn.setMaximumSize(new Dimension(140, 30));
        buyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buyBtn.addActionListener(e -> {
            if (p.isSecondHand()) {
                // Open Chat
                JOptionPane.showMessageDialog(this, "Opening chat with " + p.getSellerName() + "...");
            } else {
                // Open URL
                try {
                    String urlStr = p.getLocation();
                    if (urlStr != null && !urlStr.isEmpty()) {
                        if (!urlStr.startsWith("http")) {
                            urlStr = "https://" + urlStr;
                        }
                        Desktop.getDesktop().browse(new URI(urlStr));
                    } else {
                        JOptionPane.showMessageDialog(this, "Link not available.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Could not open browser: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        card.add(imgLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(priceLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(metaLabel);
        card.add(Box.createVerticalGlue());
        card.add(buyBtn);
        card.add(Box.createVerticalStrut(5));

        return card;
    }

    private void loadImage(String urlStr, JLabel targetLabel) {
        SwingWorker<ImageIcon, Void> worker = new SwingWorker<>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                try {
                    URL url = new URL(urlStr);
                    BufferedImage image = ImageIO.read(url);
                    if (image == null) return null;
                    Image scaled = image.getScaledInstance(180, 150, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaled);
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void done() {
                try {
                    ImageIcon icon = get();
                    if (icon != null) {
                        targetLabel.setIcon(icon);
                        targetLabel.setText("");
                    } else {
                        targetLabel.setText("Img Error");
                    }
                } catch (Exception e) {
                    targetLabel.setText("Img Error");
                }
            }
        };
        worker.execute();
    }

    public void clearData() {
        currentProducts.clear();
        cachedQuery = null; // Reset cache on logout
        productsContainer.removeAll();
        productsContainer.repaint();
    }
}