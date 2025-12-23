package com.spendwise.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class ShopPanel extends JPanel {

    private MainFrame mainFrame;
    private JTextField searchField;
    private JButton onlineTab;
    private JButton secondHandTab;
    private JPanel productGridPanel;
    private boolean isSecondHandMode = false;
    private JPanel filterPanel;
    private boolean filtersVisible = false;

    public ShopPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(250, 250, 250));

        add(createSideMenu(), BorderLayout.WEST);
        add(createContent(), BorderLayout.CENTER);
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
        addMenuItem(sideMenu, "💰", "Budget", startY + 60, false);
        addMenuItem(sideMenu, "📋", "Expenses", startY + 120, false);
        addMenuItem(sideMenu, "🛒", "Shop", startY + 180, true);
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

        JButton logoutBtn = new JButton("🚪 Logout");
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

        JLabel header = new JLabel("Shop Deals");
        header.setBounds(30, 30, 400, 40);
        header.setFont(new Font("Arial", Font.BOLD, 28));
        content.add(header);

        JLabel subHeader = new JLabel("Find the best deals and save money");
        subHeader.setBounds(30, 75, 400, 20);
        subHeader.setFont(new Font("Arial", Font.PLAIN, 14));
        subHeader.setForeground(new Color(120, 120, 120));
        content.add(subHeader);

        createSearchBar(content);

        createFilterPanel(content);

        createProductGrid(content);

        createTabs(content);

        return content;
    }

    private void createSearchBar(JPanel parent) {
        JPanel searchPanel = new JPanel();
        searchPanel.setBounds(30, 130, 1070, 55);
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setLayout(null);
        searchPanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setBounds(20, 15, 30, 25);
        searchIcon.setFont(new Font("Arial", Font.PLAIN, 20));
        searchPanel.add(searchIcon);

        searchField = new JTextField("Search for products, deals, or brands...");
        searchField.setBounds(60, 12, 880, 30);
        searchField.setFont(new Font("Arial", Font.PLAIN, 15));
        searchField.setForeground(new Color(160, 160, 160));
        searchField.setBorder(BorderFactory.createEmptyBorder());

        searchField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search for products, deals, or brands...")) {
                    searchField.setText("");
                    searchField.setForeground(new Color(50, 50, 50));
                }
            }

            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search for products, deals, or brands...");
                    searchField.setForeground(new Color(160, 160, 160));
                }
            }
        });

        searchPanel.add(searchField);

        JButton filterToggle = new JButton("☰");
        filterToggle.setBounds(950, 12, 40, 30);
        filterToggle.setFont(new Font("Arial", Font.PLAIN, 20));
        filterToggle.setFocusPainted(false);
        filterToggle.setBorderPainted(false);
        filterToggle.setContentAreaFilled(false);
        filterToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        filterToggle.addActionListener(e -> toggleFilters());
        searchPanel.add(filterToggle);

        JButton searchBtn = new JButton("Search");
        searchBtn.setBounds(1000, 12, 55, 30);
        searchBtn.setFont(new Font("Arial", Font.BOLD, 13));
        searchBtn.setBackground(UIConstants.PRIMARY_GREEN);
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.setBorderPainted(false);
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchBtn.addActionListener(e -> handleSearch());
        searchPanel.add(searchBtn);

        parent.add(searchPanel);
    }

    private void createFilterPanel(JPanel parent) {
        filterPanel = new JPanel();
        filterPanel.setBounds(30, 195, 1070, 70);
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setLayout(null);
        filterPanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        filterPanel.setVisible(false);

        JLabel priceLabel = new JLabel("Price Range");
        priceLabel.setBounds(20, 10, 100, 20);
        priceLabel.setFont(new Font("Arial", Font.BOLD, 12));
        priceLabel.setForeground(new Color(100, 100, 100));
        filterPanel.add(priceLabel);

        JTextField minField = new JTextField("Min");
        minField.setBounds(20, 32, 80, 28);
        minField.setFont(new Font("Arial", Font.PLAIN, 13));
        filterPanel.add(minField);

        JTextField maxField = new JTextField("Max");
        maxField.setBounds(110, 32, 80, 28);
        maxField.setFont(new Font("Arial", Font.PLAIN, 13));
        filterPanel.add(maxField);

        JLabel categoryLabel = new JLabel("Category");
        categoryLabel.setBounds(230, 10, 100, 20);
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 12));
        categoryLabel.setForeground(new Color(100, 100, 100));
        filterPanel.add(categoryLabel);

        String[] categories = { "All Categories", "Electronics", "Fashion", "Home", "Sports" };
        JComboBox<String> categoryBox = new JComboBox<>(categories);
        categoryBox.setBounds(230, 32, 200, 28);
        categoryBox.setFont(new Font("Arial", Font.PLAIN, 13));
        filterPanel.add(categoryBox);

        JLabel sortLabel = new JLabel("Sort By");
        sortLabel.setBounds(470, 10, 100, 20);
        sortLabel.setFont(new Font("Arial", Font.BOLD, 12));
        sortLabel.setForeground(new Color(100, 100, 100));
        filterPanel.add(sortLabel);

        String[] sortOptions = { "Relevance", "Price: Low to High", "Price: High to Low", "Rating" };
        JComboBox<String> sortBox = new JComboBox<>(sortOptions);
        sortBox.setBounds(470, 32, 200, 28);
        sortBox.setFont(new Font("Arial", Font.PLAIN, 13));
        filterPanel.add(sortBox);

        parent.add(filterPanel);
    }

    private void toggleFilters() {
        filtersVisible = !filtersVisible;
        filterPanel.setVisible(filtersVisible);

        if (filtersVisible) {
            onlineTab.setBounds(30, 285, 520, 50);
            secondHandTab.setBounds(570, 285, 530, 50);
            productGridPanel.setBounds(30, 355, 1070, 465);
        } else {
            onlineTab.setBounds(30, 215, 520, 50);
            secondHandTab.setBounds(570, 215, 530, 50);
            productGridPanel.setBounds(30, 285, 1070, 535);
        }
    }

    private void createTabs(JPanel parent) {
        onlineTab = new JButton("Online Deals");
        onlineTab.setBounds(30, 215, 520, 50);
        onlineTab.setFont(new Font("Arial", Font.BOLD, 15));
        onlineTab.setBackground(Color.WHITE);
        onlineTab.setForeground(new Color(100, 100, 100));
        onlineTab.setFocusPainted(false);
        onlineTab.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.WHITE));
        onlineTab.setCursor(new Cursor(Cursor.HAND_CURSOR));
        onlineTab.addActionListener(e -> switchTab(false));
        parent.add(onlineTab);

        secondHandTab = new JButton("Second-hand");
        secondHandTab.setBounds(570, 215, 530, 50);
        secondHandTab.setFont(new Font("Arial", Font.BOLD, 15));
        secondHandTab.setBackground(Color.WHITE);
        secondHandTab.setForeground(new Color(100, 100, 100));
        secondHandTab.setFocusPainted(false);
        secondHandTab.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.WHITE));
        secondHandTab.setCursor(new Cursor(Cursor.HAND_CURSOR));
        secondHandTab.addActionListener(e -> switchTab(true));
        parent.add(secondHandTab);

        switchTab(false);
    }

    private void switchTab(boolean secondHand) {
        isSecondHandMode = secondHand;

        if (secondHand) {
            secondHandTab.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, UIConstants.PRIMARY_GREEN));
            secondHandTab.setForeground(UIConstants.PRIMARY_GREEN);
            onlineTab.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.WHITE));
            onlineTab.setForeground(new Color(100, 100, 100));
            loadSecondHandProducts();
        } else {
            onlineTab.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, UIConstants.PRIMARY_GREEN));
            onlineTab.setForeground(UIConstants.PRIMARY_GREEN);
            secondHandTab.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.WHITE));
            secondHandTab.setForeground(new Color(100, 100, 100));
            loadOnlineProducts();
        }
    }

    private void createProductGrid(JPanel parent) {
        productGridPanel = new JPanel();
        productGridPanel.setBounds(30, 285, 1070, 535);
        productGridPanel.setBackground(new Color(250, 250, 250));
        productGridPanel.setLayout(new GridLayout(0, 3, 20, 20));

        JScrollPane scrollPane = new JScrollPane(productGridPanel);
        scrollPane.setBounds(30, 285, 1070, 535);
        scrollPane.setBorder(null);
        scrollPane.setBackground(new Color(250, 250, 250));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        loadOnlineProducts();

        parent.add(scrollPane);
    }

    private void loadOnlineProducts() {
        productGridPanel.removeAll();

        String[][] products = {
                { "Wireless Headphones Pro", "$89.99", "$149.99", "-40%", "⭐ 4.5", "(1234)", "TechStore" },
                { "Running Shoes Ultra", "$65", "$120", "-46%", "⭐ 4.8", "(856)", "SportMax" },
                { "Smart Watch Series 5", "$199.99", "$299.99", "-33%", "⭐ 4.6", "(2145)", "GadgetHub" }
        };

        for (String[] prod : products) {
            productGridPanel.add(createOnlineProductCard(prod));
        }

        productGridPanel.revalidate();
        productGridPanel.repaint();
    }

    private void loadSecondHandProducts() {
        productGridPanel.removeAll();

        String[][] products = {
                { "Vintage Camera", "$145", "Excellent", "⭐ 4.7", "PhotoCollector", "San Francisco, CA" },
                { "Designer Backpack", "$85", "Like New", "⭐ 4.9", "FashionResale", "New York, NY" }
        };

        for (String[] prod : products) {
            productGridPanel.add(createSecondHandProductCard(prod));
        }

        productGridPanel.revalidate();
        productGridPanel.repaint();
    }

    private JPanel createOnlineProductCard(String[] data) {
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(340, 400));
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel imagePlaceholder = new JPanel();
        imagePlaceholder.setBounds(0, 0, 340, 240);
        imagePlaceholder.setBackground(new Color(245, 245, 245));
        imagePlaceholder.setLayout(null);

        JLabel discountBadge = new JLabel(data[3]);
        discountBadge.setBounds(15, 15, 60, 28);
        discountBadge.setHorizontalAlignment(SwingConstants.CENTER);
        discountBadge.setOpaque(true);
        discountBadge.setBackground(new Color(255, 152, 0));
        discountBadge.setForeground(Color.WHITE);
        discountBadge.setFont(new Font("Arial", Font.BOLD, 13));
        imagePlaceholder.add(discountBadge);

        card.add(imagePlaceholder);

        JLabel nameLabel = new JLabel("<html>" + data[0] + "</html>");
        nameLabel.setBounds(15, 255, 310, 40);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 15));
        card.add(nameLabel);

        JLabel ratingLabel = new JLabel(data[4] + " " + data[5]);
        ratingLabel.setBounds(15, 300, 200, 20);
        ratingLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        ratingLabel.setForeground(new Color(120, 120, 120));
        card.add(ratingLabel);

        JLabel priceLabel = new JLabel(data[1]);
        priceLabel.setBounds(15, 325, 150, 30);
        priceLabel.setFont(new Font("Arial", Font.BOLD, 22));
        priceLabel.setForeground(UIConstants.PRIMARY_GREEN);
        card.add(priceLabel);

        JLabel originalPrice = new JLabel("<html><strike>" + data[2] + "</strike></html>");
        originalPrice.setBounds(170, 330, 100, 20);
        originalPrice.setFont(new Font("Arial", Font.PLAIN, 14));
        originalPrice.setForeground(new Color(150, 150, 150));
        card.add(originalPrice);

        JLabel sellerLabel = new JLabel(data[6]);
        sellerLabel.setBounds(15, 360, 250, 20);
        sellerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        sellerLabel.setForeground(new Color(120, 120, 120));
        card.add(sellerLabel);

        JButton wishlistBtn = new JButton("♡");
        wishlistBtn.setBounds(290, 360, 35, 25);
        wishlistBtn.setFont(new Font("Arial", Font.PLAIN, 22));
        wishlistBtn.setForeground(new Color(150, 150, 150));
        wishlistBtn.setFocusPainted(false);
        wishlistBtn.setBorderPainted(false);
        wishlistBtn.setContentAreaFilled(false);
        wishlistBtn.addActionListener(e -> {
            wishlistBtn.setText("♥");
            wishlistBtn.setForeground(new Color(220, 53, 69));
        });
        card.add(wishlistBtn);

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

    private JPanel createSecondHandProductCard(String[] data) {
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(340, 400));
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel imagePlaceholder = new JPanel();
        imagePlaceholder.setBounds(0, 0, 340, 240);
        imagePlaceholder.setBackground(new Color(245, 245, 245));
        imagePlaceholder.setLayout(null);

        JLabel conditionBadge = new JLabel(data[2]);
        conditionBadge.setBounds(15, 15, 90, 28);
        conditionBadge.setHorizontalAlignment(SwingConstants.CENTER);
        conditionBadge.setOpaque(true);
        conditionBadge.setBackground(UIConstants.PRIMARY_GREEN);
        conditionBadge.setForeground(Color.WHITE);
        conditionBadge.setFont(new Font("Arial", Font.BOLD, 12));
        imagePlaceholder.add(conditionBadge);

        card.add(imagePlaceholder);

        JLabel nameLabel = new JLabel(data[0]);
        nameLabel.setBounds(15, 255, 310, 25);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        card.add(nameLabel);

        JLabel ratingLabel = new JLabel(data[3] + " " + data[4]);
        ratingLabel.setBounds(15, 285, 200, 20);
        ratingLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        ratingLabel.setForeground(new Color(120, 120, 120));
        card.add(ratingLabel);

        JLabel priceLabel = new JLabel(data[1]);
        priceLabel.setBounds(15, 315, 150, 30);
        priceLabel.setFont(new Font("Arial", Font.BOLD, 24));
        priceLabel.setForeground(UIConstants.PRIMARY_GREEN);
        card.add(priceLabel);

        JLabel locationLabel = new JLabel("📍 " + data[5]);
        locationLabel.setBounds(15, 350, 310, 20);
        locationLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        locationLabel.setForeground(new Color(120, 120, 120));
        card.add(locationLabel);

        JButton wishlistBtn = new JButton("♡");
        wishlistBtn.setBounds(290, 350, 35, 25);
        wishlistBtn.setFont(new Font("Arial", Font.PLAIN, 22));
        wishlistBtn.setForeground(new Color(150, 150, 150));
        wishlistBtn.setFocusPainted(false);
        wishlistBtn.setBorderPainted(false);
        wishlistBtn.setContentAreaFilled(false);
        wishlistBtn.addActionListener(e -> {
            wishlistBtn.setText("♥");
            wishlistBtn.setForeground(new Color(220, 53, 69));
        });
        card.add(wishlistBtn);

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

    private void handleSearch() {
        String query = searchField.getText();
        if (query.isEmpty() || query.equals("Search for products, deals, or brands...")) {
            JOptionPane.showMessageDialog(this, "Please enter a search keyword!");
            return;
        }

        // TODO: Backend integration
        JOptionPane.showMessageDialog(this, "Searching for: " + query + "\n(Backend integration pending)");
    }

    public void refreshData() {
        if (isSecondHandMode) {
            loadSecondHandProducts();
        } else {
            loadOnlineProducts();
        }
    }

    public void clearData() {
        productGridPanel.removeAll();
        productGridPanel.revalidate();
        productGridPanel.repaint();
    }
}