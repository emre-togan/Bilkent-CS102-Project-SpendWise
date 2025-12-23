package com.spendwise.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import com.spendwise.models.Product;
import com.spendwise.models.Purchase;
import com.spendwise.models.User;
import com.spendwise.services.ProfileService;
import com.spendwise.services.productService;

public class ProfilePanel extends JPanel {

    private MainFrame mainFrame;
    private User currentUser;

    // UI Components
    // UI Components
    private JLabel sidebarNameLabel;
    private JLabel sidebarEmailLabel;
    private JLabel sidebarAvatarLabel; // Sidebar Avatar
    private JLabel avatarLabel; // Main Content Avatar
    private JLabel mainNameLabel; // Main Content Name
    private JLabel mainEmailLabel; // Main Content Email
    private JLabel purchaseCountLabel;
    private JLabel savedAmountLabel;
    private JLabel dealsFoundLabel;
    private JPanel friendsContainer;
    private JPanel savedProductsContainer;

    // Services
    private ProfileService profileService;
    private productService productServiceInstance;

    // Data
    private List<User> friends;
    private List<Product> savedProducts;
    private List<Purchase> purchases;

    public ProfilePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.currentUser = UserSession.getCurrentUser();
        this.profileService = new ProfileService();
        this.productServiceInstance = new productService();
        this.friends = new ArrayList<>();
        this.savedProducts = new ArrayList<>();
        this.purchases = new ArrayList<>();

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
        addMenuItem(sideMenu, "🏠", "Dashboard", "DASHBOARD", startY, false);
        addMenuItem(sideMenu, "💳", "Budget", "BUDGET", startY + 60, false);
        addMenuItem(sideMenu, "🧾", "Expenses", "EXPENSES", startY + 120, false);
        addMenuItem(sideMenu, "🛍️", "Shop", "SHOP", startY + 180, false);
        addMenuItem(sideMenu, "💬", "Chat", "CHAT", startY + 240, false);
        addMenuItem(sideMenu, "👤", "Profile", "PROFILE", startY + 300, true);
        addMenuItem(sideMenu, "⚙️", "Settings", "SETTINGS", startY + 360, false);

        // Profile card
        JPanel profileCard = createProfileCard();
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

    private JPanel createProfileCard() {
        JPanel profileCard = new JPanel();
        profileCard.setBounds(15, 650, 230, 70);
        profileCard.setBackground(new Color(248, 249, 250));
        profileCard.setLayout(null);
        profileCard.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        String initials = getInitials(currentUser.getUserName());
        sidebarAvatarLabel = new JLabel(initials);
        sidebarAvatarLabel.setBounds(15, 15, 40, 40);
        sidebarAvatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebarAvatarLabel.setOpaque(true);
        sidebarAvatarLabel.setBackground(UIConstants.PRIMARY_GREEN);
        sidebarAvatarLabel.setForeground(Color.WHITE);
        sidebarAvatarLabel.setFont(new Font("Arial", Font.BOLD, 16));
        profileCard.add(sidebarAvatarLabel);

        sidebarNameLabel = new JLabel(currentUser.getUserName());
        sidebarNameLabel.setBounds(65, 18, 150, 18);
        sidebarNameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        profileCard.add(sidebarNameLabel);

        sidebarEmailLabel = new JLabel(currentUser.geteMail());
        sidebarEmailLabel.setBounds(65, 37, 150, 15);
        sidebarEmailLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        sidebarEmailLabel.setForeground(new Color(120, 120, 120));
        profileCard.add(sidebarEmailLabel);

        return profileCard;
    }

    private String getInitials(String name) {
        if (name == null || name.isEmpty())
            return "??";
        String[] parts = name.trim().split("\\s+");
        if (parts.length == 1) {
            return parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase();
        }
        return (parts[0].charAt(0) + "" + parts[parts.length - 1].charAt(0)).toUpperCase();
    }

    private void addMenuItem(JPanel parent, String emoji, String text, String targetPanelName, int y, boolean active) {
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

        btn.addActionListener(e -> mainFrame.showPanel(targetPanelName));
        parent.add(btn);
    }

    private JPanel createContent() {
        JPanel content = new JPanel();
        content.setLayout(null);
        content.setBackground(new Color(250, 250, 250));

        // Header
        JLabel header = new JLabel("Profile");
        header.setBounds(30, 30, 400, 40);
        header.setFont(new Font("Arial", Font.BOLD, 28));
        content.add(header);

        JLabel subHeader = new JLabel("Manage your account and preferences");
        subHeader.setBounds(30, 75, 400, 20);
        subHeader.setFont(new Font("Arial", Font.PLAIN, 14));
        subHeader.setForeground(new Color(120, 120, 120));
        content.add(subHeader);

        // Edit profile button
        JButton editBtn = new JButton("✏");
        editBtn.setBounds(1030, 35, 40, 40);
        editBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        editBtn.setBackground(Color.WHITE);
        editBtn.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        editBtn.setFocusPainted(false);
        editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editBtn.setToolTipText("Edit Profile");
        editBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Edit profile feature coming soon!"));
        content.add(editBtn);

        // Profile card
        createProfileInfoCard(content);

        // Stats
        createStatsCards(content);

        // Friends section
        createFriendsSection(content);

        // Saved Products section
        createSavedProductsSection(content);

        // Action buttons
        createActionButtons(content);

        return content;
    }

    private void createProfileInfoCard(JPanel parent) {
        JPanel card = new JPanel();
        card.setBounds(30, 130, 1070, 100);
        card.setBackground(Color.WHITE);
        card.setLayout(null);
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        // Large avatar
        String initials = getInitials(currentUser.getUserName());
        avatarLabel = new JLabel(initials);
        avatarLabel.setBounds(30, 20, 60, 60);
        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        avatarLabel.setOpaque(true);
        avatarLabel.setBackground(UIConstants.PRIMARY_GREEN);
        avatarLabel.setForeground(Color.WHITE);
        avatarLabel.setFont(new Font("Arial", Font.BOLD, 24));
        card.add(avatarLabel);

        // User info
        mainNameLabel = new JLabel(currentUser.getUserName());
        mainNameLabel.setBounds(110, 25, 300, 25);
        mainNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        card.add(mainNameLabel);

        mainEmailLabel = new JLabel(currentUser.geteMail());
        mainEmailLabel.setBounds(110, 55, 300, 20);
        mainEmailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        mainEmailLabel.setForeground(new Color(120, 120, 120));
        card.add(mainEmailLabel);

        parent.add(card);
    }

    private void createStatsCards(JPanel parent) {
        int cardY = 250;
        int cardWidth = 340;
        int spacing = 25;

        // Purchases card
        JPanel purchasesCard = createStatCard("156", "Purchases", cardWidth);
        purchasesCard.setBounds(30, cardY, cardWidth, 80);
        purchaseCountLabel = (JLabel) purchasesCard.getComponent(0);
        parent.add(purchasesCard);

        // Saved card
        JPanel savedCard = createStatCard("$2.4k", "Saved", cardWidth);
        savedCard.setBounds(30 + cardWidth + spacing, cardY, cardWidth, 80);
        savedAmountLabel = (JLabel) savedCard.getComponent(0);
        parent.add(savedCard);

        // Deals Found card
        JPanel dealsCard = createStatCard("12", "Deals Found", cardWidth);
        dealsCard.setBounds(30 + 2 * (cardWidth + spacing), cardY, cardWidth, 80);
        dealsFoundLabel = (JLabel) dealsCard.getComponent(0);
        parent.add(dealsCard);
    }

    private JPanel createStatCard(String value, String label, int width) {
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setBounds(20, 15, width - 40, 30);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        card.add(valueLabel);

        JLabel textLabel = new JLabel(label);
        textLabel.setBounds(20, 50, width - 40, 20);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        textLabel.setForeground(new Color(120, 120, 120));
        card.add(textLabel);

        return card;
    }

    private void createFriendsSection(JPanel parent) {
        JPanel sectionHeader = new JPanel(new BorderLayout());
        sectionHeader.setBounds(30, 360, 1070, 40);
        sectionHeader.setBackground(new Color(250, 250, 250));

        JLabel titleLabel = new JLabel("👥  Friends");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        sectionHeader.add(titleLabel, BorderLayout.WEST);

        JButton addFriendBtn = new JButton("➕ Add Friend");
        addFriendBtn.setFont(new Font("Arial", Font.BOLD, 12));
        addFriendBtn.setForeground(UIConstants.PRIMARY_GREEN);
        addFriendBtn.setBackground(Color.WHITE);
        addFriendBtn.setBorder(BorderFactory.createLineBorder(UIConstants.PRIMARY_GREEN));
        addFriendBtn.setFocusPainted(false);
        addFriendBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addFriendBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Add friend feature coming soon!"));
        sectionHeader.add(addFriendBtn, BorderLayout.EAST);

        parent.add(sectionHeader);

        // Friends container
        friendsContainer = new JPanel();
        friendsContainer.setBounds(30, 410, 1070, 120);
        friendsContainer.setBackground(Color.WHITE);
        friendsContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        friendsContainer.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        parent.add(friendsContainer);
    }

    private void createSavedProductsSection(JPanel parent) {
        JPanel sectionHeader = new JPanel(new BorderLayout());
        sectionHeader.setBounds(30, 550, 1070, 40);
        sectionHeader.setBackground(new Color(250, 250, 250));

        JLabel titleLabel = new JLabel("🛍️  Saved Products");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        sectionHeader.add(titleLabel, BorderLayout.WEST);

        JButton seeAllBtn = new JButton("See All");
        seeAllBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        seeAllBtn.setForeground(new Color(120, 120, 120));
        seeAllBtn.setBackground(new Color(250, 250, 250));
        seeAllBtn.setBorderPainted(false);
        seeAllBtn.setFocusPainted(false);
        seeAllBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        seeAllBtn.addActionListener(e -> showAllSavedProducts());
        sectionHeader.add(seeAllBtn, BorderLayout.EAST);

        parent.add(sectionHeader);

        // Saved products container (3 columns grid)
        savedProductsContainer = new JPanel();
        savedProductsContainer.setBounds(30, 600, 1070, 150);
        savedProductsContainer.setBackground(new Color(250, 250, 250));
        savedProductsContainer.setLayout(new GridLayout(1, 3, 20, 0));
        parent.add(savedProductsContainer);
    }

    private void createActionButtons(JPanel parent) {
        // Purchase History
        JPanel purchaseBtn = createActionButton("📦", "Purchase History", "View your order history");
        purchaseBtn.setBounds(30, 770, 1070, 60);
        purchaseBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showPurchaseHistory();
            }
        });
        parent.add(purchaseBtn);

        // Wishlist
        JPanel wishlistBtn = createActionButton("❤️", "Wishlist", "View saved items");
        wishlistBtn.setBounds(30, 770 + 70, 1070, 60);
        wishlistBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showWishlist();
            }
        });
        parent.add(wishlistBtn);

        // Addresses (removed, moved above if needed)
    }

    private JPanel createActionButton(String emoji, String title, String subtitle) {
        JPanel btn = new JPanel(new BorderLayout(15, 0));
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(15, 20, 15, 20)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Arial", Font.PLAIN, 28));
        btn.add(emojiLabel, BorderLayout.WEST);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(120, 120, 120));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(3));
        textPanel.add(subtitleLabel);

        btn.add(textPanel, BorderLayout.CENTER);

        JLabel arrowLabel = new JLabel("›");
        arrowLabel.setFont(new Font("Arial", Font.BOLD, 24));
        arrowLabel.setForeground(new Color(180, 180, 180));
        btn.add(arrowLabel, BorderLayout.EAST);

        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(248, 248, 248));
                textPanel.setBackground(new Color(248, 248, 248));
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(Color.WHITE);
                textPanel.setBackground(Color.WHITE);
            }
        });

        return btn;
    }

    private void updateFriendsList() {
        friendsContainer.removeAll();

        if (friends.isEmpty()) {
            JLabel emptyLabel = new JLabel("No friends yet");
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 13));
            emptyLabel.setForeground(Color.GRAY);
            friendsContainer.add(emptyLabel);
        } else {
            int maxDisplay = Math.min(4, friends.size());
            for (int i = 0; i < maxDisplay; i++) {
                User friend = friends.get(i);
                friendsContainer.add(createFriendCard(friend));
            }
        }

        friendsContainer.revalidate();
        friendsContainer.repaint();
    }

    private JPanel createFriendCard(User friend) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(180, 80));
        card.setBorder(new EmptyBorder(10, 10, 10, 10));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        String initials = getInitials(friend.getUserName());
        JLabel avatar = new JLabel(initials);
        avatar.setPreferredSize(new Dimension(45, 45));
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setOpaque(true);
        avatar.setBackground(getRandomColor(friend.getUserName()));
        avatar.setForeground(Color.WHITE);
        avatar.setFont(new Font("Arial", Font.BOLD, 16));
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(friend.getUserName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel statusLabel = new JLabel("Friend since 2024");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        statusLabel.setForeground(new Color(120, 120, 120));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(avatar);
        card.add(Box.createVerticalStrut(8));
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(3));
        card.add(statusLabel);

        return card;
    }

    private Color getRandomColor(String seed) {
        int hash = seed.hashCode();
        Color[] colors = {
                new Color(76, 175, 80),
                new Color(33, 150, 243),
                new Color(156, 39, 176),
                new Color(255, 152, 0),
                new Color(233, 30, 99),
                new Color(0, 150, 136)
        };
        return colors[Math.abs(hash) % colors.length];
    }

    private void updateSavedProducts() {
        savedProductsContainer.removeAll();

        if (savedProducts.isEmpty()) {
            JLabel emptyLabel = new JLabel("No saved products");
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 13));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            savedProductsContainer.add(emptyLabel);
        } else {
            int maxDisplay = Math.min(3, savedProducts.size());
            for (int i = 0; i < maxDisplay; i++) {
                Product product = savedProducts.get(i);
                savedProductsContainer.add(createProductCard(product));
            }
        }

        savedProductsContainer.revalidate();
        savedProductsContainer.repaint();
    }

    private JPanel createProductCard(Product product) {
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Image placeholder
        JPanel imagePlaceholder = new JPanel(new BorderLayout());
        imagePlaceholder.setBounds(10, 10, 320, 90);
        imagePlaceholder.setBackground(new Color(245, 245, 245));

        JLabel imageLabel = new JLabel("📦", SwingConstants.CENTER);
        imageLabel.setFont(new Font("Arial", Font.PLAIN, 36));
        imageLabel.setForeground(new Color(180, 180, 180));
        imagePlaceholder.add(imageLabel, BorderLayout.CENTER);
        card.add(imagePlaceholder);

        // Product name
        JLabel nameLabel = new JLabel(truncateText(product.getName(), 25));
        nameLabel.setBounds(10, 110, 320, 20);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        card.add(nameLabel);

        // Price
        JLabel priceLabel = new JLabel(String.format("$%.2f", product.getPriceAfterDiscount()));
        priceLabel.setBounds(10, 132, 150, 18);
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        priceLabel.setForeground(UIConstants.PRIMARY_GREEN);
        card.add(priceLabel);

        return card;
    }

    private String truncateText(String text, int maxLength) {
        if (text == null)
            return "";
        if (text.length() <= maxLength)
            return text;
        return text.substring(0, maxLength) + "...";
    }

    private void showPurchaseHistory() {
        PurchaseHistoryDialog dialog = new PurchaseHistoryDialog((JFrame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
    }

    private void showWishlist() {
        WishlistDialog dialog = new WishlistDialog((JFrame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
    }

    private void showAllSavedProducts() {
        JOptionPane.showMessageDialog(this, "Showing all " + savedProducts.size() + " saved products!",
                "Saved Products", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Refresh all profile data from backend
     */
    public void refreshData() {
        try {
            currentUser = UserSession.getCurrentUser();
            int userId = currentUser.getId();

            // Update Sidebar Profile
            if (sidebarNameLabel != null) {
                sidebarNameLabel.setText(currentUser.getUserName());
                sidebarEmailLabel.setText(currentUser.geteMail());
                sidebarAvatarLabel.setText(getInitials(currentUser.getUserName()));
            }

            // Update Main Content Profile
            if (mainNameLabel != null) {
                mainNameLabel.setText(currentUser.getUserName());
                mainEmailLabel.setText(currentUser.geteMail());
                avatarLabel.setText(getInitials(currentUser.getUserName()));
            }

            // Load friends
            friends = profileService.getFriends();
            if (friends == null)
                friends = new ArrayList<>();
            updateFriendsList();

            // Load saved products
            savedProducts = productServiceInstance.getAllProducts();
            if (savedProducts == null)
                savedProducts = new ArrayList<>();
            updateSavedProducts();

            // Load purchases
            purchases = profileService.getOrders("" + userId);
            if (purchases == null)
                purchases = new ArrayList<>();

            // Update stats
            purchaseCountLabel.setText(String.valueOf(purchases.size()));
            dealsFoundLabel.setText(String.valueOf(savedProducts.size()));

            // Calculate saved amount (mock for now)
            double totalSaved = 0;
            for (Product product : savedProducts) {
                totalSaved += (product.getPrice() - product.getPriceAfterDiscount());
            }
            savedAmountLabel.setText(String.format("$%.1fk", totalSaved / 1000));

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error refreshing profile data: " + e.getMessage());
        }
    }

    /**
     * Clear all data - called on logout
     */
    public void clearData() {
        friends.clear();
        savedProducts.clear();
        purchases.clear();

        updateFriendsList();
        updateSavedProducts();

        purchaseCountLabel.setText("0");
        savedAmountLabel.setText("$0");
        dealsFoundLabel.setText("0");
    }
}
