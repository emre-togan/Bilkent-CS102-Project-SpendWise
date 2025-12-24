package com.spendwise.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import com.spendwise.models.Product;
import com.spendwise.models.Purchase;
import com.spendwise.models.User;
import com.spendwise.services.ChatService;
import com.spendwise.services.ProfileService;
import com.spendwise.services.productService;
import com.spendwise.view.components.RoundedPanel;
import com.spendwise.view.components.SidebarPanel;

public class ProfilePanel extends JPanel {

    private User currentUser;

    // UI Components
    private SidebarPanel sidebarPanel;
    private JLabel mainNameLabel;
    private JLabel mainEmailLabel;
    private JLabel avatarLabel;
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
        this.currentUser = UserSession.getCurrentUser();
        this.profileService = new ProfileService();
        this.productServiceInstance = new productService();
        this.friends = new ArrayList<>();
        this.savedProducts = new ArrayList<>();
        this.purchases = new ArrayList<>();

        setLayout(new BorderLayout());
        setBackground(new Color(250, 250, 250));

        // Sidebar
        sidebarPanel = new SidebarPanel("PROFILE", key -> mainFrame.showPanel(key), mainFrame::logout);
        add(sidebarPanel, BorderLayout.WEST);

        // Scroll Pane for the main content
        JScrollPane scrollPane = new JScrollPane(createContent());
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        // Initial data load
        refreshData();
    }

    private JPanel createContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS)); // Center aligned vertical flow
        content.setBackground(new Color(250, 250, 250));
        content.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(250, 250, 250)); // Match bg
        headerPanel.setMaximumSize(new Dimension(1100, 60));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel titleBlock = new JPanel(new GridLayout(2, 1));
        titleBlock.setBackground(new Color(250, 250, 250));

        JLabel header = new JLabel("Profile");
        header.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel subHeader = new JLabel("Manage your account and preferences");
        subHeader.setFont(new Font("Arial", Font.PLAIN, 12));
        subHeader.setForeground(new Color(120, 120, 120));

        titleBlock.add(header);
        titleBlock.add(subHeader);

        JButton editBtn = new JButton("✏");
        editBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        editBtn.setBackground(new Color(250, 250, 250)); // Transparent-ish
        editBtn.setBorder(null);
        editBtn.setFocusPainted(false);
        editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editBtn.setForeground(new Color(150, 150, 150));
        editBtn.setToolTipText("Edit Profile");
        editBtn.addActionListener(e -> {
            new PersonalInfoDialog((JFrame) SwingUtilities.getWindowAncestor(this)).setVisible(true);
            refreshData();
        });

        headerPanel.add(titleBlock, BorderLayout.WEST);
        headerPanel.add(editBtn, BorderLayout.EAST);

        content.add(headerPanel);
        content.add(Box.createVerticalStrut(25));

        // Big Profile Card (Info + Stats)
        content.add(createBigProfileCard());
        content.add(Box.createVerticalStrut(30));

        // Friends Section Header
        content.add(createSectionHeader("👥 Friends", "Add Friend", e -> {
            new AddFriendDialog((Frame) SwingUtilities.getWindowAncestor(this)).setVisible(true);
            refreshData();
        }));
        content.add(Box.createVerticalStrut(15));

        // Friends Container
        friendsContainer = new JPanel();
        friendsContainer.setLayout(new BoxLayout(friendsContainer, BoxLayout.Y_AXIS));
        friendsContainer.setBackground(new Color(250, 250, 250));
        friendsContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(friendsContainer);

        content.add(Box.createVerticalStrut(30));

        // Saved Products Header
        content.add(createSectionHeader("🛍️ Saved Products", "See All", e -> showAllSavedProducts()));
        content.add(Box.createVerticalStrut(15));

        // Saved Products Container
        savedProductsContainer = new JPanel();
        savedProductsContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));
        savedProductsContainer.setBackground(new Color(250, 250, 250));
        savedProductsContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(savedProductsContainer);

        content.add(Box.createVerticalStrut(30));

        // Action Buttons (History, Addresses) - Wishlist Removed
        createActionButtons(content);

        // Fill remaining
        content.add(Box.createVerticalGlue());

        return content;
    }

    private JPanel createSectionHeader(String title, String actionText, java.awt.event.ActionListener action) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(250, 250, 250));
        panel.setMaximumSize(new Dimension(1100, 30));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.WEST);

        if (actionText != null) {
            JButton actionBtn = new JButton(actionText);
            actionBtn.setFont(new Font("Arial", Font.BOLD, 12));
            actionBtn.setForeground(UIConstants.PRIMARY_GREEN);
            actionBtn.setBackground(new Color(250, 250, 250));
            actionBtn.setBorder(null);
            if (actionText.contains("Add")) {
                actionBtn.setBorder(BorderFactory.createCompoundBorder(
                        new RoundedPanel(10, new Color(230, 245, 230)).getBorder(), // Fake border logic
                        new EmptyBorder(5, 10, 5, 10)));
                actionBtn.setBorder(BorderFactory.createLineBorder(UIConstants.PRIMARY_GREEN, 1, true));
                actionBtn.setBackground(Color.WHITE);
                actionBtn.setPreferredSize(new Dimension(100, 30));
            } else {
                actionBtn.setForeground(new Color(120, 120, 120));
            }

            actionBtn.setFocusPainted(false);
            actionBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            actionBtn.addActionListener(action);
            panel.add(actionBtn, BorderLayout.EAST);
        }
        return panel;
    }

    private JPanel createBigProfileCard() {
        RoundedPanel card = new RoundedPanel(20, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setMaximumSize(new Dimension(1100, 200));
        card.setPreferredSize(new Dimension(1100, 200));
        card.setBorder(new EmptyBorder(30, 40, 30, 40));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Top Info: Avatar, Name, Email
        JPanel topInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        topInfo.setOpaque(false);

        String initials = getInitials(currentUser.getUserName());
        avatarLabel = new JLabel(initials);
        avatarLabel.setPreferredSize(new Dimension(64, 64));
        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        avatarLabel.setOpaque(true);
        avatarLabel.setBackground(UIConstants.PRIMARY_GREEN); // Light green bg
        avatarLabel.setForeground(Color.WHITE);
        avatarLabel.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel nameEmailPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        nameEmailPanel.setOpaque(false);

        mainNameLabel = new JLabel(currentUser.getUserName());
        mainNameLabel.setFont(new Font("Arial", Font.BOLD, 18));

        mainEmailLabel = new JLabel(currentUser.geteMail());
        mainEmailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        mainEmailLabel.setForeground(Color.GRAY);

        nameEmailPanel.add(mainNameLabel);
        nameEmailPanel.add(mainEmailLabel);

        topInfo.add(avatarLabel);
        topInfo.add(nameEmailPanel);

        // Stats Row
        JPanel statsRow = new JPanel(new GridLayout(1, 3, 50, 0));
        statsRow.setOpaque(false);
        statsRow.setBorder(new EmptyBorder(30, 0, 0, 0)); // Spacing from top info

        statsRow.add(createStatItem("Purchases", "0", label -> purchaseCountLabel = label));
        statsRow.add(createStatItem("Saved", "₺0.00", label -> savedAmountLabel = label));
        statsRow.add(createStatItem("Deals Found", "0", label -> dealsFoundLabel = label));

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.add(topInfo, BorderLayout.NORTH);
        contentWrapper.add(statsRow, BorderLayout.CENTER);

        card.add(contentWrapper, BorderLayout.CENTER);
        return card;
    }

    private JPanel createStatItem(String title, String initialValue, java.util.function.Consumer<JLabel> labelSetter) {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setOpaque(false);

        JLabel valueLabel = new JLabel(initialValue);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 18));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        labelSetter.accept(valueLabel);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(Color.GRAY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(valueLabel);
        panel.add(titleLabel);
        return panel;
    }

    private void updateFriendsList() {
        friendsContainer.removeAll();

        if (friends.isEmpty()) {
            // Empty state
        } else {
            for (User friend : friends) {
                friendsContainer.add(createFriendRow(friend));
                friendsContainer.add(Box.createVerticalStrut(10));
            }
        }

        friendsContainer.revalidate();
        friendsContainer.repaint();
    }

    private JPanel createFriendRow(User friend) {
        RoundedPanel row = new RoundedPanel(15, Color.WHITE);
        row.setLayout(new BorderLayout());
        row.setMaximumSize(new Dimension(1100, 70));
        row.setPreferredSize(new Dimension(1100, 70));
        row.setBorder(new EmptyBorder(10, 20, 10, 20));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Left: Avatar + Name + Subtext
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        left.setOpaque(false);

        JLabel avatar = new JLabel(getInitials(friend.getUserName()));
        avatar.setPreferredSize(new Dimension(40, 40));
        avatar.setOpaque(true);
        avatar.setBackground(UIConstants.PRIMARY_GREEN); // or random color
        avatar.setForeground(Color.WHITE);
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel namePanel = new JPanel(new GridLayout(2, 1));
        namePanel.setOpaque(false);

        JLabel name = new JLabel(friend.getUserName());
        name.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel sub = new JLabel("Friend since 2024");
        sub.setFont(new Font("Arial", Font.PLAIN, 12));
        sub.setForeground(Color.GRAY);

        namePanel.add(name);
        namePanel.add(sub);

        left.add(avatar);
        left.add(namePanel);

        // Right: Chevron
        JLabel arrow = new JLabel("›");
        arrow.setFont(new Font("Arial", Font.BOLD, 20));
        arrow.setForeground(Color.LIGHT_GRAY);

        row.add(left, BorderLayout.WEST);
        row.add(arrow, BorderLayout.EAST);

        return row;
    }

    private void createActionButtons(JPanel parent) {
        // Container for the list of actions
        RoundedPanel actionsPanel = new RoundedPanel(20, Color.WHITE);
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
        // Adjusted height since we removed one item
        actionsPanel.setMaximumSize(new Dimension(1100, 120));
        actionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionsPanel.setBorder(new EmptyBorder(10, 0, 10, 0)); // Outer padding

        actionsPanel.add(createActionRow("📦", "Purchase History",
                e -> new PurchaseHistoryDialog((JFrame) SwingUtilities.getWindowAncestor(this)).setVisible(true)));

        // --- REMOVED WISHLIST BUTTON HERE ---

        actionsPanel.add(createDivider());
        actionsPanel.add(createActionRow("📍", "Addresses",
                e -> JOptionPane.showMessageDialog(this, "Addresses feature coming soon!")));

        parent.add(actionsPanel);
        parent.add(Box.createVerticalStrut(30)); // Bottom spacing
    }

    private JPanel createActionRow(String icon, String text, java.awt.event.ActionListener action) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(1100, 50));
        row.setPreferredSize(new Dimension(1100, 50));
        row.setBorder(new EmptyBorder(0, 25, 0, 25));
        row.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Use BoxLayout for better vertical centering and alignment control
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.X_AXIS));
        left.setBackground(Color.WHITE);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        // Fix icon width to ensure text aligns perfectly vertically
        iconLabel.setPreferredSize(new Dimension(40, 40));
        iconLabel.setMaximumSize(new Dimension(40, 40));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Arial", Font.BOLD, 14));
        textLabel.setForeground(new Color(50, 50, 50));
        textLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        left.add(iconLabel);
        left.add(Box.createHorizontalStrut(10)); // Gap between icon and text
        left.add(textLabel);

        // Right side: Chevron
        JLabel chevron = new JLabel("›");
        chevron.setFont(new Font("Arial", Font.BOLD, 18));
        chevron.setForeground(Color.LIGHT_GRAY);

        row.add(left, BorderLayout.WEST);
        row.add(chevron, BorderLayout.EAST);

        // Click interaction
        row.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (action != null)
                    action.actionPerformed(null);
            }

            public void mouseEntered(java.awt.event.MouseEvent e) {
                row.setBackground(new Color(248, 248, 248));
                left.setBackground(new Color(248, 248, 248));
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                row.setBackground(Color.WHITE);
                left.setBackground(Color.WHITE);
            }
        });

        return row;
    }

    private JPanel createDivider() {
        JPanel dividerContainer = new JPanel(new BorderLayout());
        dividerContainer.setBackground(Color.WHITE);
        dividerContainer.setMaximumSize(new Dimension(1100, 1));

        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(240, 240, 240));
        separator.setBackground(new Color(240, 240, 240));

        dividerContainer.add(separator, BorderLayout.CENTER);

        dividerContainer.setBorder(new EmptyBorder(0, 20, 0, 20));

        return dividerContainer;
    }

    private void updateSavedProducts() {
        savedProductsContainer.removeAll();

        if (savedProducts.isEmpty()) {
            // ...
        } else {
            int max = Math.min(3, savedProducts.size());
            for (int i = 0; i < max; i++) {
                savedProductsContainer.add(createSmallProductCard(savedProducts.get(i)));
            }
        }
        savedProductsContainer.revalidate();
        savedProductsContainer.repaint();
    }

    private JPanel createSmallProductCard(Product product) {

        RoundedPanel card = new RoundedPanel(15, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(300, 200)); // Fixed size
        card.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Image placeholder
        JLabel image = new JLabel("📦", SwingConstants.CENTER); // Placeholder
        image.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        image.setBackground(new Color(245, 245, 245));
        image.setOpaque(true);
        image.setPreferredSize(new Dimension(280, 120));

        // Info
        JPanel info = new JPanel(new BorderLayout());
        info.setOpaque(false);
        info.setBorder(new EmptyBorder(10, 5, 0, 5));

        JLabel name = new JLabel(truncateText(product.getName(), 25));
        name.setFont(new Font("Arial", Font.BOLD, 13));

        // Price or other icon?
        // Let's put Price
        JLabel price = new JLabel("₺" + String.format("%.2f", product.getPriceAfterDiscount()));
        price.setFont(new Font("Arial", Font.PLAIN, 12));
        price.setForeground(Color.GRAY);

        info.add(name, BorderLayout.NORTH);
        info.add(price, BorderLayout.SOUTH);

        card.add(image, BorderLayout.NORTH);
        card.add(info, BorderLayout.CENTER);

        return card;
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

    private void showAllSavedProducts() {
        // This can now open the dialog directly since the button was removed
        new WishlistDialog((JFrame) SwingUtilities.getWindowAncestor(this)).setVisible(true);
    }

    private String truncateText(String text, int maxLength) {
        if (text == null)
            return "";
        if (text.length() <= maxLength)
            return text;
        return text.substring(0, maxLength) + "...";
    }

    public void refreshData() {
        try {
            currentUser = UserSession.getCurrentUser();
            int userId = currentUser.getId();

            if (sidebarPanel != null) {
                sidebarPanel.updateUser();
            }

            if (mainNameLabel != null) {
                mainNameLabel.setText(currentUser.getUserName());
                mainEmailLabel.setText(currentUser.geteMail());
                avatarLabel.setText(getInitials(currentUser.getUserName()));
            }

            // USE CHAT SERVICE FOR FRIENDS TO ENSURE CONSISTENCY
            friends = ChatService.getFriends(userId);
            if (friends == null)
                friends = new ArrayList<>();
            updateFriendsList();

            savedProducts = productServiceInstance.getWishlist(userId);

            if (savedProducts == null)
                savedProducts = new ArrayList<>();
            updateSavedProducts();

            purchases = profileService.getOrders("" + userId);
            if (purchases == null)
                purchases = new ArrayList<>();

            if (purchaseCountLabel != null)
                purchaseCountLabel.setText(String.valueOf(purchases.size()));
            if (dealsFoundLabel != null)
                dealsFoundLabel.setText(String.valueOf(savedProducts.size()));

            double totalSaved = 0;
            for (Product product : savedProducts) {
                totalSaved += (product.getPrice() - product.getPriceAfterDiscount());
            }
            if (savedAmountLabel != null)
                savedAmountLabel.setText(String.format("₺%.1fk", totalSaved / 1000));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearData() {
        friends.clear();
        savedProducts.clear();
        purchases.clear();
        updateFriendsList();
        updateSavedProducts();
        purchaseCountLabel.setText("0");
        savedAmountLabel.setText("₺0");
        dealsFoundLabel.setText("0");
    }
}