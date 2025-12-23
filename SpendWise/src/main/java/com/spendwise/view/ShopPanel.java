package com.spendwise.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import com.spendwise.models.Product;
import com.spendwise.scrapers.AmazonScraper;
import com.spendwise.scrapers.HepsiburadaScraper;
import com.spendwise.scrapers.N11Scraper;
import com.spendwise.scrapers.TrendyolScraper;
import com.spendwise.services.productService;

public class ShopPanel extends JPanel {

    private MainFrame mainFrame;
    private JPanel productsContainer;
    private JTextField searchField;
    private JComboBox<String> sortByBox;
    private JToggleButton onlineDealsBtn;
    private JToggleButton secondHandBtn;

    private productService productServiceInstance;
    private TrendyolScraper trendyolScraper;
    private AmazonScraper amazonScraper;
    private N11Scraper n11Scraper;
    private HepsiburadaScraper hepsiburadaScraper;
    private List<Product> currentProducts;
    private boolean showOnlineDeals = true;
    private boolean showSecondHand = false;

    public ShopPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.productServiceInstance = new productService();
        this.trendyolScraper = new TrendyolScraper();
        this.amazonScraper = new AmazonScraper();
        this.n11Scraper = new N11Scraper();
        this.hepsiburadaScraper = new HepsiburadaScraper();
        this.currentProducts = new ArrayList<>();

        setLayout(new BorderLayout());
        setBackground(new Color(250, 250, 250));

        add(createSideMenu(), BorderLayout.WEST);
        add(createContent(), BorderLayout.CENTER);

        // Initial data load
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

        JButton logoutBtn = new JButton("↩︎ Logout");
        logoutBtn.setBounds(15, 735, 230, 40);
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 14));
        logoutBtn.setForeground(new Color(220, 53, 69));
        logoutBtn.setBackground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createLineBorder(new Color(220, 53, 69)));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> mainFrame.logout());
        sideMenu.add(logoutBtn);

        return sideMenu;
    }

    private void addMenuItem(JPanel parent, String emoji, String text, int y, boolean active) {
        JButton btn = new JButton(emoji + "  " + text);
        btn.setBounds(10, y, 240, 50);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(0, 15, 0, 0));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (active) {
            btn.setBackground(UIConstants.PRIMARY_GREEN);
            btn.setForeground(Color.WHITE);
            btn.setOpaque(true);
        } else {
            btn.setContentAreaFilled(false);
            btn.setForeground(new Color(80, 80, 80));
        }

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!active) {
                    btn.setBackground(new Color(245, 245, 245));
                    btn.setOpaque(true);
                }
            }

            public void mouseExited(MouseEvent e) {
                if (!active) {
                    btn.setContentAreaFilled(false);
                }
            }
        });

        btn.addActionListener(e -> mainFrame.showPanel(text.toUpperCase()));
        parent.add(btn);
    }

    private JPanel createContent() {
        JPanel content = new JPanel();
        content.setLayout(null);
        content.setBackground(new Color(250, 250, 250));

        // Header
        JLabel header = new JLabel("Shop Deals");
        header.setBounds(30, 30, 400, 40);
        header.setFont(new Font("Arial", Font.BOLD, 28));
        content.add(header);

        JLabel subHeader = new JLabel("Find the best deals and save money");
        subHeader.setBounds(30, 75, 400, 20);
        subHeader.setFont(new Font("Arial", Font.PLAIN, 14));
        subHeader.setForeground(new Color(120, 120, 120));
        content.add(subHeader);

        // Search Bar
        searchField = new JTextField("Search for products, deals, or brands...");
        searchField.setBounds(30, 120, 450, 40);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        content.add(searchField);

        JButton searchBtn = new JButton("🔍 Search");
        searchBtn.setBounds(500, 120, 100, 40);
        searchBtn.setFont(new Font("Arial", Font.BOLD, 14));
        searchBtn.setBackground(UIConstants.PRIMARY_GREEN);
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.setBorderPainted(false);
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
        });
        content.add(onlineDealsBtn);

        secondHandBtn = new JToggleButton("♻️ Second-hand");
        secondHandBtn.setBounds(180, 180, 140, 35);
        secondHandBtn.setFont(new Font("Arial", Font.BOLD, 12));
        secondHandBtn.setSelected(false);
        secondHandBtn.setBackground(Color.WHITE);
        secondHandBtn.setForeground(new Color(80, 80, 80));
        secondHandBtn.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        secondHandBtn.setFocusPainted(false);
        secondHandBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        secondHandBtn.addActionListener(e -> {
            showSecondHand = secondHandBtn.isSelected();
            filterProducts();
        });
        content.add(secondHandBtn);

        // Products Container (Grid)
        productsContainer = new JPanel(new GridLayout(0, 3, 20, 20));
        productsContainer.setBackground(new Color(250, 250, 250));
        productsContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(productsContainer);
        scrollPane.setBounds(30, 235, 1070, 450);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        content.add(scrollPane);

        return content;
    }

    private void performSearch() {
        String searchTerm = searchField.getText().trim();

        if (searchTerm.isEmpty() || searchTerm.equals("Search for products, deals, or brands...")) {
            JOptionPane.showMessageDialog(this, "Please enter a search term", "Search", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Show loading dialog
        JDialog loadingDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Searching...", true);
        JLabel loadingLabel = new JLabel("Searching for products, please wait...");
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loadingLabel.setBorder(new EmptyBorder(30, 30, 30, 30));
        loadingDialog.add(loadingLabel);
        loadingDialog.setSize(300, 150);
        loadingDialog.setLocationRelativeTo(this);

        // Perform search in background thread
        SwingWorker<List<Product>, Void> worker = new SwingWorker<List<Product>, Void>() {
            @Override
            protected List<Product> doInBackground() throws Exception {
                // Search using all scrapers
                List<Product> allResults = new ArrayList<>();

                // We can run these in parallel if needed, but for now sequential is
                // safer/simpler
                try {
                    allResults.addAll(trendyolScraper.searchAndSearch(searchTerm));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    allResults.addAll(amazonScraper.searchAndSearch(searchTerm));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    allResults.addAll(n11Scraper.searchAndSearch(searchTerm));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    allResults.addAll(hepsiburadaScraper.searchAndSearch(searchTerm));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return allResults;
            }

            @Override
            protected void done() {
                loadingDialog.dispose();
                try {
                    List<Product> results = get();
                    if (results != null && !results.isEmpty()) {
                        currentProducts = results;
                        displayProducts(currentProducts);
                        JOptionPane.showMessageDialog(ShopPanel.this,
                                "Found " + results.size() + " products!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(ShopPanel.this,
                                "No products found. Try different keywords.",
                                "No Results",
                                JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(ShopPanel.this,
                            "Error searching products: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
        loadingDialog.setVisible(true);
    }

    private void filterProducts() {
        List<Product> filtered = new ArrayList<>();

        for (Product p : currentProducts) {
            boolean matchesOnline = !showOnlineDeals || !p.isSecondHand();
            boolean matchesSecondHand = !showSecondHand || p.isSecondHand();

            if (matchesOnline && matchesSecondHand) {
                filtered.add(p);
            }
        }

        displayProducts(filtered);
    }

    private void displayProducts(List<Product> products) {
        productsContainer.removeAll();

        if (products == null || products.isEmpty()) {
            JLabel emptyLabel = new JLabel("No products to display. Try searching for something!");
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            productsContainer.setLayout(new BorderLayout());
            productsContainer.add(emptyLabel, BorderLayout.CENTER);
        } else {
            productsContainer.setLayout(new GridLayout(0, 3, 20, 20));
            for (Product product : products) {
                productsContainer.add(createProductCard(product));
            }
        }

        productsContainer.revalidate();
        productsContainer.repaint();
    }

    private JPanel createProductCard(Product product) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(15, 15, 15, 15)));
        card.setPreferredSize(new Dimension(330, 280));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Image Placeholder
        JPanel imagePlaceholder = new JPanel();
        imagePlaceholder.setBackground(new Color(245, 245, 245));
        imagePlaceholder.setPreferredSize(new Dimension(300, 150));
        imagePlaceholder.setLayout(new BorderLayout());

        JLabel imageLabel = new JLabel("🖼️", SwingConstants.CENTER);
        imageLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        imagePlaceholder.add(imageLabel, BorderLayout.CENTER);

        // Load image asynchronously
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            loadProductImage(product.getImageUrl(), imageLabel);
        }

        // Product Info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel nameLabel = new JLabel("<html>" + truncateText(product.getName(), 50) + "</html>");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sellerLabel = new JLabel(product.getSellerName());
        sellerLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        sellerLabel.setForeground(Color.GRAY);
        sellerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel priceLabel = new JLabel(String.format("₺%.2f", product.getPrice()));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        priceLabel.setForeground(UIConstants.SUCCESS_GREEN);
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton viewBtn = new JButton("View Deal");
        viewBtn.setFont(new Font("Arial", Font.BOLD, 12));
        viewBtn.setBackground(UIConstants.PRIMARY_BLUE);
        viewBtn.setForeground(Color.WHITE);
        viewBtn.setFocusPainted(false);
        viewBtn.setBorderPainted(false);
        viewBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        viewBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(sellerLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(viewBtn);

        card.add(imagePlaceholder, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);

        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(248, 248, 248));
            }

            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });

        return card;
    }

    private void loadProductImage(String imageUrl, JLabel imageLabel) {
        SwingWorker<ImageIcon, Void> worker = new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                try {
                    URL url = new URL(imageUrl);
                    BufferedImage image = ImageIO.read(url);
                    if (image != null) {
                        // Scale image to fit the 300x150 area nicely
                        // We can use a slightly smarter scaling to preserve aspect ratio within bounds
                        int maxWidth = 300;
                        int maxHeight = 150;

                        double widthRatio = (double) maxWidth / image.getWidth();
                        double heightRatio = (double) maxHeight / image.getHeight();
                        double ratio = Math.min(widthRatio, heightRatio);

                        int newWidth = (int) (image.getWidth() * ratio);
                        int newHeight = (int) (image.getHeight() * ratio);

                        Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                        return new ImageIcon(scaledImage);
                    }
                } catch (Exception e) {
                    // Fail silently
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    ImageIcon icon = get();
                    if (icon != null) {
                        imageLabel.setText("");
                        imageLabel.setIcon(icon);
                    }
                } catch (Exception e) {
                    // Ignore
                }
            }
        };
        worker.execute();
    }

    private String truncateText(String text, int maxLength) {
        if (text == null)
            return "";
        if (text.length() <= maxLength)
            return text;
        return text.substring(0, maxLength) + "...";
    }

    /**
     * Refresh data from backend - called by MainFrame
     */
    public void refreshData() {
        try {
            // Load products from database
            currentProducts = productServiceInstance.getAllProducts();

            if (currentProducts == null) {
                currentProducts = new ArrayList<>();
            }

            displayProducts(currentProducts);

        } catch (Exception e) {
            e.printStackTrace();
            currentProducts = new ArrayList<>();
            displayProducts(currentProducts);
        }
    }

    /**
     * Clear all data - called on logout
     */
    public void clearData() {
        currentProducts.clear();
        productsContainer.removeAll();

        productsContainer.setLayout(new BorderLayout());
        JLabel emptyLabel = new JLabel("Please login to view products");
        emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        emptyLabel.setForeground(Color.GRAY);
        emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        productsContainer.add(emptyLabel, BorderLayout.CENTER);

        productsContainer.revalidate();
        productsContainer.repaint();

        // Reset search and filters
        if (searchField != null) {
            searchField.setText("Search for products, deals, or brands...");
        }
        if (sortByBox != null) {
            sortByBox.setSelectedIndex(0);
        }
        if (onlineDealsBtn != null) {
            onlineDealsBtn.setSelected(true);
            showOnlineDeals = true;
        }
        if (secondHandBtn != null) {
            secondHandBtn.setSelected(false);
            showSecondHand = false;
        }
    }
}
