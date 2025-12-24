package com.spendwise.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.spendwise.models.Expense;
import com.spendwise.models.Product;
import com.spendwise.models.User;
import com.spendwise.scrapers.AmazonScraper;
import com.spendwise.scrapers.HepsiburadaScraper;
import com.spendwise.scrapers.N11Scraper;
import com.spendwise.scrapers.TrendyolScraper;
import com.spendwise.services.ChatService;
import com.spendwise.services.expenseService;
import com.spendwise.services.productService;
import com.spendwise.view.components.RoundedButton;
import com.spendwise.view.components.RoundedPanel;
import com.spendwise.view.components.RoundedTextField;
import com.spendwise.view.components.SidebarPanel;

public class ShopPanel extends JPanel {

    private MainFrame mainFrame;
    private SidebarPanel sidebarPanel;

    // UI Components
    private JPanel productsContainer;
    private RoundedTextField searchField;
    private RoundedTextField minPriceField;
    private RoundedTextField maxPriceField;
    private JComboBox<String> categoryBox;
    private JComboBox<String> sortBox;
    private JButton onlineTabBtn;
    private JButton secondHandTabBtn;

    private JLayeredPane layeredPane;
    private JButton fabBtn; // Sell Item FAB

    // Services
    private productService productServiceInstance;
    private expenseService expenseServiceInstance; // Added for buying logic
    private TrendyolScraper trendyolScraper;
    private AmazonScraper amazonScraper;
    private N11Scraper n11Scraper;
    private HepsiburadaScraper hepsiburadaScraper;

    // State
    private List<Product> currentProducts;
    private boolean showOnlineDeals = true;

    // Caching
    private String lastSearchQuery = "";
    private String cachedQuery = null;
    private Boolean cachedOnlineState = null;

    public ShopPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.productServiceInstance = new productService();
        this.expenseServiceInstance = new expenseService(); // Initialize expense service

        // Initialize Scrapers
        this.trendyolScraper = new TrendyolScraper();
        this.amazonScraper = new AmazonScraper();
        this.n11Scraper = new N11Scraper();
        this.hepsiburadaScraper = new HepsiburadaScraper();

        this.currentProducts = new ArrayList<>();

        setLayout(new BorderLayout());
        setBackground(UIConstants.BACKGROUND_LIGHT);

        // Sidebar
        sidebarPanel = new SidebarPanel("SHOP",
                panelName -> mainFrame.showPanel(panelName),
                () -> mainFrame.logout());
        add(sidebarPanel, BorderLayout.WEST);

        // Content
        createContent();

        // Initial Load
        refreshData();

<<<<<<< HEAD
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

        JLabel appName = new JLabel("Finance Assistant");
        appName.setBounds(80, 35, 150, 30);
        appName.setFont(new Font("Arial", Font.PLAIN, 15));
        appName.setForeground(new Color(100, 100, 100));
        sideMenu.add(appName);

        int startY = 120;
        addMenuItem(sideMenu, "🏠", "Dashboard", startY, false);
        addMenuItem(sideMenu, "💳", "Budget", startY + 60, false);
        addMenuItem(sideMenu, "🧾", "Expenses", startY + 120, false);
        addMenuItem(sideMenu, "🛍️", "Shop", startY + 180, true);
        addMenuItem(sideMenu, "💬", "Chat", startY + 240, false);
        addMenuItem(sideMenu, "👤", "Profile", startY + 300, false);
        addMenuItem(sideMenu, "⚙️", "Settings", startY + 360, false);

        JPanel profileCard = new JPanel();
        profileCard.setBounds(15, 650, 230, 70);
        profileCard.setBackground(new Color(248, 249, 250));
        profileCard.setLayout(null);
        profileCard.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        JLabel avatar = new JLabel("SJ");
        avatar.setBounds(15, 15, 40, 40);
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setOpaque(true);
        avatar.setBackground(UIConstants.PRIMARY_GREEN);
        avatar.setForeground(Color.WHITE);
        avatar.setFont(new Font("Arial", Font.BOLD, 16));
        profileCard.add(avatar);

        JLabel userName = new JLabel("Sarah Johnson");
        userName.setBounds(65, 18, 150, 18);
        userName.setFont(new Font("Arial", Font.BOLD, 13));
        profileCard.add(userName);

        JLabel userEmail = new JLabel("sarah@email.com");
        userEmail.setBounds(65, 37, 150, 15);
        userEmail.setFont(new Font("Arial", Font.PLAIN, 11));
        userEmail.setForeground(new Color(120, 120, 120));
        profileCard.add(userEmail);

        sideMenu.add(profileCard);

        RoundedButton logoutBtn = new RoundedButton("↩︎ Logout", 15);
        logoutBtn.setBounds(15, 735, 230, 40);
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 14));
        logoutBtn.setForeground(new Color(220, 53, 69));
        logoutBtn.setBackground(Color.WHITE);
        logoutBtn.setBorder(BorderFactory.createLineBorder(new Color(220, 53, 69)));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> mainFrame.logout());
        sideMenu.add(logoutBtn);

        return sideMenu;
    }

    private void addMenuItem(JPanel parent, String emoji, String text, int y, boolean active) {
        RoundedButton btn = new RoundedButton(emoji + "  " + text, 15);
        btn.setBounds(10, y, 240, 50);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(0, 15, 0, 0));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (active) {
            btn.setBackground(UIConstants.PRIMARY_GREEN);
            btn.setForeground(Color.WHITE);
            btn.setOpaque(true);
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(new Color(80, 80, 80));
        }

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!active) {
                    btn.setBackground(new Color(245, 245, 245));
                    btn.setOpaque(true);
                }
=======
        // Handle resizing for FAB
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateFABPosition();
>>>>>>> origin/main
            }
        });
    }

<<<<<<< HEAD
            public void mouseExited(MouseEvent e) {
                if (!active) {
                    btn.setBackground(Color.WHITE);
                }
=======
    private void createContent() {
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        add(layeredPane, BorderLayout.CENTER);

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BorderLayout());
        mainContent.setBackground(UIConstants.BACKGROUND_LIGHT);
        mainContent.setBounds(0, 0, 1200, 900); // Initial

        // Resize listener for inner content
        layeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                mainContent.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
                updateFABPosition();
>>>>>>> origin/main
            }
        });

        // --- UPPER SECTION (Header + Filters) ---
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.Y_AXIS));
        upperPanel.setBackground(UIConstants.BACKGROUND_LIGHT);
        upperPanel.setBorder(new EmptyBorder(30, 30, 20, 30));

        // 1. Header Text
        JLabel title = new JLabel("Shop Deals");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Find the best deals and save money");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        upperPanel.add(title);
        upperPanel.add(Box.createVerticalStrut(5));
        upperPanel.add(subtitle);
        upperPanel.add(Box.createVerticalStrut(20));

        // 2. Search Row
        JPanel searchRow = new JPanel(new BorderLayout(15, 0));
        searchRow.setBackground(UIConstants.BACKGROUND_LIGHT);
        searchRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        searchRow.setAlignmentX(Component.LEFT_ALIGNMENT);

<<<<<<< HEAD
        RoundedButton searchBtn = new RoundedButton("🔍 Search", 15);
        searchBtn.setBounds(500, 120, 100, 40);
        searchBtn.setFont(new Font("Arial", Font.BOLD, 14));
        searchBtn.setBackground(UIConstants.PRIMARY_GREEN);
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchBtn.addActionListener(e -> performSearch());
        content.add(searchBtn);

        // Filters
        JLabel sortLabel = new JLabel("Sort By:");
        sortLabel.setBounds(750, 125, 60, 30);
        sortLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        content.add(sortLabel);

        String[] sortOptions = { "Relevance", "Price: Low to High", "Price: High to Low", "Newest" };
        sortByBox = new JComboBox<>(sortOptions);
        sortByBox.setBounds(820, 120, 150, 40);
        sortByBox.setFont(new Font("Arial", Font.PLAIN, 13));
        content.add(sortByBox);

        // Toggle Buttons
        onlineDealsBtn = new JToggleButton("🌐 Online Deals");
        onlineDealsBtn.setBounds(30, 180, 140, 35);
        onlineDealsBtn.setFont(new Font("Arial", Font.BOLD, 12));
        onlineDealsBtn.setSelected(true);
        onlineDealsBtn.setBackground(UIConstants.PRIMARY_BLUE);
        onlineDealsBtn.setForeground(Color.WHITE);
        onlineDealsBtn.setFocusPainted(false);
        onlineDealsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        onlineDealsBtn.addActionListener(e -> {
            showOnlineDeals = onlineDealsBtn.isSelected();
            filterProducts();
=======
        searchField = new RoundedTextField(20);
        searchField.setPlaceholder("Search for products, deals, or brands...");

        JButton filterIconBtn = new JButton("⚙️"); // Placeholder icon
        filterIconBtn.setBackground(Color.WHITE);
        filterIconBtn.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        filterIconBtn.setFocusPainted(false);
        filterIconBtn.setPreferredSize(new Dimension(50, 45));

        RoundedButton searchBtn = new RoundedButton("Search", 20, UIConstants.SELECTION_GREEN,
                UIConstants.darker(UIConstants.SELECTION_GREEN));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFont(new Font("Arial", Font.BOLD, 14));
        searchBtn.setPreferredSize(new Dimension(100, 45));
        searchBtn.addActionListener(e -> {
            cachedQuery = null; // force refresh
            refreshData();
>>>>>>> origin/main
        });

        JPanel searchRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchRight.setBackground(UIConstants.BACKGROUND_LIGHT);
        searchRight.add(filterIconBtn);
        searchRight.add(searchBtn);

        searchRow.add(searchField, BorderLayout.CENTER);
        searchRow.add(searchRight, BorderLayout.EAST);

        upperPanel.add(searchRow);
        upperPanel.add(Box.createVerticalStrut(15));

        // 3. Filters Row (Price, Category, Sort)
        JPanel filtersRow = new JPanel(new GridBagLayout());
        filtersRow.setBackground(UIConstants.BACKGROUND_LIGHT);
        filtersRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        filtersRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 15);
        gbc.weightx = 0;

        // Price Range
        gbc.gridx = 0;
        filtersRow.add(createLabel("Price Range"), gbc);

        minPriceField = new RoundedTextField(10);
        minPriceField.setPlaceholder("Min");
        minPriceField.setPreferredSize(new Dimension(80, 40));
        gbc.gridx = 1;
        filtersRow.add(minPriceField, gbc);

        maxPriceField = new RoundedTextField(10);
        maxPriceField.setPlaceholder("Max");
        maxPriceField.setPreferredSize(new Dimension(80, 40));
        gbc.gridx = 2;
        filtersRow.add(maxPriceField, gbc);

        // Category
        gbc.gridx = 3;
        gbc.weightx = 0;
        filtersRow.add(createLabel("Category"), gbc);

        String[] cats = { "All Categories", "Electronics", "Fashion", "Home", "Sports" };
        categoryBox = new JComboBox<>(cats);
        categoryBox.setBackground(Color.WHITE);
        categoryBox.setPreferredSize(new Dimension(150, 40));
        gbc.gridx = 4;
        gbc.weightx = 1.0;
        filtersRow.add(categoryBox, gbc);

        // Sort By
        gbc.gridx = 5;
        gbc.weightx = 0;
        filtersRow.add(createLabel("Sort By"), gbc);

        String[] sorts = { "Relevance", "Price: Low to High", "Price: High to Low" };
        sortBox = new JComboBox<>(sorts);
        sortBox.setBackground(Color.WHITE);
        sortBox.setPreferredSize(new Dimension(150, 40));
        sortBox.addActionListener(e -> sortProducts());
        gbc.gridx = 6;
        filtersRow.add(sortBox, gbc);

        upperPanel.add(filtersRow);
        upperPanel.add(Box.createVerticalStrut(20));

        // 4. Tabs (Online vs Second Hand) - Segmented Control Look
        JPanel tabsPanel = new JPanel(new GridLayout(1, 2, 0, 0));
        tabsPanel.setBackground(Color.WHITE);
        tabsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        tabsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tabsPanel.setBorder(new LineBorder(new Color(230, 230, 230), 1, true));

        onlineTabBtn = createTabButton("Online Deals", true);
        secondHandTabBtn = createTabButton("Second-hand", false);

        onlineTabBtn.addActionListener(e -> switchTab(true));
        secondHandTabBtn.addActionListener(e -> switchTab(false));

        tabsPanel.add(onlineTabBtn);
        tabsPanel.add(secondHandTabBtn);

        upperPanel.add(tabsPanel);

        // --- SCROLLABLE PRODUCTS GRID ---
        productsContainer = new JPanel(new GridLayout(0, 3, 20, 20));
        productsContainer.setBackground(UIConstants.BACKGROUND_LIGHT);

        JScrollPane scrollPane = new JScrollPane(productsContainer);
        scrollPane.setBorder(new EmptyBorder(20, 30, 20, 30));
        scrollPane.setBackground(UIConstants.BACKGROUND_LIGHT);
        scrollPane.getViewport().setBackground(UIConstants.BACKGROUND_LIGHT);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Add to mainContent
        mainContent.add(upperPanel, BorderLayout.NORTH);
        mainContent.add(scrollPane, BorderLayout.CENTER);

        layeredPane.add(mainContent, JLayeredPane.DEFAULT_LAYER);

        // FAB for Selling Item
        fabBtn = new JButton("+") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillOval(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        fabBtn.setFont(new Font("Arial", Font.PLAIN, 30));
        fabBtn.setForeground(Color.WHITE);
        fabBtn.setBackground(UIConstants.SELECTION_GREEN);
        fabBtn.setBorderPainted(false);
        fabBtn.setFocusPainted(false);
        fabBtn.setContentAreaFilled(false);
        fabBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fabBtn.addActionListener(e -> {
            new SellProductDialog((Frame) SwingUtilities.getWindowAncestor(this)).setVisible(true);
            // If adding second hand item, switch to second hand tab and refresh
            cachedQuery = null;
            switchTab(false);
        });

        layeredPane.add(fabBtn, JLayeredPane.POPUP_LAYER);
    }

    private void updateFABPosition() {
        if (layeredPane != null && fabBtn != null) {
            int x = layeredPane.getWidth() - 90;
            int y = layeredPane.getHeight() - 90;
            fabBtn.setBounds(x, y, 60, 60);
        }
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.PLAIN, 12));
        l.setForeground(Color.GRAY);
        l.setBorder(new EmptyBorder(0, 0, 5, 0)); // space below label? actually this is gridbag, so maybe margin needed
        return l;
    }

    private JButton createTabButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        if (active) {
            btn.setBackground(Color.WHITE);
            btn.setForeground(UIConstants.PRIMARY_GREEN);
        } else {
            btn.setBackground(new Color(245, 245, 245));
            btn.setForeground(Color.GRAY);
        }
        return btn;
    }

    private void updateTabStyles() {
        if (showOnlineDeals) {
            onlineTabBtn.setBackground(Color.WHITE);
            onlineTabBtn.setForeground(UIConstants.PRIMARY_GREEN);
            secondHandTabBtn.setBackground(new Color(245, 245, 245));
            secondHandTabBtn.setForeground(Color.GRAY);
        } else {
            secondHandTabBtn.setBackground(Color.WHITE);
            secondHandTabBtn.setForeground(UIConstants.PRIMARY_GREEN);
            onlineTabBtn.setBackground(new Color(245, 245, 245));
            onlineTabBtn.setForeground(Color.GRAY);
        }
    }

    private void switchTab(boolean online) {
        if (showOnlineDeals != online) {
            showOnlineDeals = online;
            updateTabStyles();
            refreshData();
        }
    }

    public void refreshData() {
        sidebarPanel.updateUser();

        String input = searchField.getText().trim();
        if (input.isEmpty() && !lastSearchQuery.isEmpty()) {
            input = lastSearchQuery;
            searchField.setText(input);
        } else {
            lastSearchQuery = input;
        }

        final String query = input;

        // Cache Check
        if (currentProducts != null && !currentProducts.isEmpty()) {
            if (query.equals(cachedQuery) && (cachedOnlineState != null && cachedOnlineState == showOnlineDeals)) {
                displayProducts(currentProducts);
                return;
            }
        }

        productsContainer.removeAll();
        productsContainer.setLayout(new BorderLayout());

        // Initial state or Loading
        if (showOnlineDeals && query.isEmpty()) {
            JLabel empty = new JLabel("Enter a product name to search...", SwingConstants.CENTER);
            empty.setFont(new Font("Arial", Font.PLAIN, 16));
            productsContainer.add(empty, BorderLayout.CENTER);
            productsContainer.revalidate();
            productsContainer.repaint();

            cachedQuery = "";
            cachedOnlineState = showOnlineDeals;
            return;
        }

        JLabel loading = new JLabel("Searching...", SwingConstants.CENTER);
        loading.setFont(new Font("Arial", Font.PLAIN, 16));
        productsContainer.add(loading, BorderLayout.CENTER);
        productsContainer.revalidate();
        productsContainer.repaint();

        SwingWorker<List<Product>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Product> doInBackground() {
                List<Product> results = new ArrayList<>();
                try {
                    if (!showOnlineDeals) {
                        // Second Hand (DB)
                        List<Product> db = productServiceInstance.getAllProducts();
                        if (db != null) {
                            for (Product p : db) {
                                if (p.isSecondHand()) {
                                    if (query.isEmpty() || p.getName().toLowerCase().contains(query.toLowerCase())) {
                                        results.add(p);
                                    }
                                }
                            }
                        }
                    } else {
                        // Online Scrapers
                        if (!query.isEmpty()) {
                            try {
                                results.addAll(trendyolScraper.searchAndSearch(query));
                            } catch (Exception e) {
                            }
                            try {
                                results.addAll(amazonScraper.searchAndSearch(query));
                            } catch (Exception e) {
                            }
                            try {
                                results.addAll(n11Scraper.searchAndSearch(query));
                            } catch (Exception e) {
                            }
                            try {
                                results.addAll(hepsiburadaScraper.searchAndSearch(query));
                            } catch (Exception e) {
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Search error: " + e.getMessage());
                }
                return results;
            }

            @Override
            protected void done() {
                try {
                    currentProducts = get();
                    cachedQuery = query;
                    cachedOnlineState = showOnlineDeals;
                    displayProducts(currentProducts);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void sortProducts() {
        String sort = (String) sortBox.getSelectedItem();
        if (currentProducts == null || currentProducts.isEmpty())
            return;

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
            empty.setFont(new Font("Arial", Font.PLAIN, 16));
            productsContainer.add(empty, BorderLayout.CENTER);
        } else {
            // Check grid width to determine columns? For now fixed 3 cols
            productsContainer.setLayout(new GridLayout(0, 3, 20, 20)); // 3 columns
            for (Product p : products) {
                productsContainer.add(createProductCard(p));
            }
        }
        productsContainer.revalidate();
        productsContainer.repaint();
    }

    private JPanel createProductCard(Product p) {
        RoundedPanel card = new RoundedPanel(15, Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(10, 10, 10, 10));
        card.setPreferredSize(new Dimension(220, 300));

        // Image
        JLabel imgLabel = new JLabel("Loading...", SwingConstants.CENTER);
        imgLabel.setPreferredSize(new Dimension(200, 140));
        imgLabel.setMaximumSize(new Dimension(200, 140));
        imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imgLabel.setBackground(new Color(245, 245, 245));
        imgLabel.setOpaque(true);

        if (p.getImageUrl() != null && !p.getImageUrl().isEmpty()) {
            loadImage(p.getImageUrl(), imgLabel);
        } else {
            imgLabel.setText("No Image");
        }

        // Text Info
        JLabel nameLabel = new JLabel(p.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Tags / Condition
        JPanel tagsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tagsPanel.setOpaque(false);
        tagsPanel.setMaximumSize(new Dimension(200, 30));
        if (p.isSecondHand()) {
            JLabel badge = new JLabel("Like New"); // Placeholder logic
            badge.setFont(new Font("Arial", Font.BOLD, 10));
            badge.setForeground(new Color(39, 174, 96));
            badge.setBorder(BorderFactory.createLineBorder(new Color(39, 174, 96), 1, true));
            tagsPanel.add(badge);
        } else {
            JLabel badge = new JLabel("New");
            badge.setFont(new Font("Arial", Font.BOLD, 10));
            badge.setForeground(Color.BLUE);
            tagsPanel.add(badge);
        }

        JLabel priceLabel = new JLabel("$" + p.getPriceAfterDiscount());
        priceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        priceLabel.setForeground(UIConstants.SELECTION_GREEN);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sellerLabel = new JLabel(
                p.isSecondHand() ? "Seller: " + p.getSellerName() : "Store: " + p.getSellerName());
        sellerLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        sellerLabel.setForeground(Color.GRAY);
        sellerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(imgLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(nameLabel);
        card.add(tagsPanel);
        card.add(priceLabel);
        card.add(sellerLabel);
        card.add(Box.createVerticalGlue());

<<<<<<< HEAD
        RoundedButton viewBtn = new RoundedButton("View Deal", 12);
        viewBtn.setFont(new Font("Arial", Font.BOLD, 12));
        viewBtn.setBackground(UIConstants.PRIMARY_BLUE);
        viewBtn.setForeground(Color.WHITE);
        viewBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        viewBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
=======
        if (p.isSecondHand()) {
            // BUY BUTTON
            RoundedButton buyBtn = new RoundedButton("Buy Now", 15, UIConstants.SELECTION_GREEN,
                    UIConstants.darker(UIConstants.SELECTION_GREEN));
            buyBtn.setForeground(Color.WHITE);
            buyBtn.setMaximumSize(new Dimension(140, 35));
            buyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            buyBtn.addActionListener(e -> {
                int choice = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to buy '" + p.getName() + "' for $" + p.getPriceAfterDiscount() + "?",
                        "Confirm Purchase", JOptionPane.YES_NO_OPTION);
>>>>>>> origin/main

                if (choice == JOptionPane.YES_OPTION) {
                    int userId = UserSession.getCurrentUserId();
                    // 1. Add Expense
                    Expense expense = new Expense(userId, 0, "Shopping",
                            "Bought: " + p.getName(), p.getPriceAfterDiscount(),
                            new Date(System.currentTimeMillis()));
                    
                    if (expenseServiceInstance.addExpense(expense)) {
                        // 2. Delete Product
                        boolean deleted = productServiceInstance.deleteProduct(p.getProductId());
                        if (deleted) {
                            JOptionPane.showMessageDialog(this, "Purchase successful!");
                            refreshData(); // Reload list
                        } else {
                            JOptionPane.showMessageDialog(this, "Bought, but error removing listing.", "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Transaction failed.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            card.add(buyBtn);
            card.add(Box.createVerticalStrut(5));

            // CHAT BUTTON
            RoundedButton chatBtn = new RoundedButton("Start Chat", 15, UIConstants.PRIMARY_BLUE,
                    Color.DARK_GRAY);
            chatBtn.setForeground(Color.WHITE);
            chatBtn.setMaximumSize(new Dimension(140, 35));
            chatBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            chatBtn.addActionListener(e -> {
                String sellerName = p.getSellerName();
                int currentUserId = UserSession.getCurrentUserId();
                List<User> foundUsers = ChatService.searchUsers(sellerName);
                User sellerUser = null;
                for (User u : foundUsers) {
                    if (u.getUserName().equalsIgnoreCase(sellerName)) {
                        sellerUser = u;
                        break;
                    }
                }
                if (sellerUser != null) {
                    if (sellerUser.getId() == currentUserId) {
                        JOptionPane.showMessageDialog(this, "You cannot chat with yourself!", "Info", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    ChatService.addFriend(currentUserId, sellerUser.getId());
                    mainFrame.showPanel("CHAT");
                } else {
                    JOptionPane.showMessageDialog(this, "Seller not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            card.add(chatBtn);

        } else {
            // Online Deal Button
            RoundedButton actionBtn = new RoundedButton("Go to Store", 15, Color.ORANGE, Color.DARK_GRAY);
            actionBtn.setForeground(Color.WHITE);
            actionBtn.setMaximumSize(new Dimension(140, 35));
            actionBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            actionBtn.addActionListener(e -> {
                try {
                    Desktop.getDesktop().browse(new URI(p.getLocation()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            card.add(actionBtn);
        }

        card.add(Box.createVerticalStrut(5));
        return card;
    }

    private void loadImage(String urlStr, JLabel targetLabel) {
        SwingWorker<ImageIcon, Void> worker = new SwingWorker<>() {
            @Override
            protected ImageIcon doInBackground() {
                try {
                    URL url = new URL(urlStr);
                    BufferedImage image = ImageIO.read(url);
                    if (image == null)
                        return null;
                    Image scaled = image.getScaledInstance(180, 120, Image.SCALE_SMOOTH);
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
                    }
                } catch (Exception e) {
                }
            }
        };
        worker.execute();
    }

    public void clearData() {
        currentProducts.clear();
        cachedQuery = null;
        if (productsContainer != null)
            productsContainer.removeAll();
        repaint();
    }
}