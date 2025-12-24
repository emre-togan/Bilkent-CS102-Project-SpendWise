package com.spendwise.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.spendwise.controllers.ChatController;
import com.spendwise.models.Message;
import com.spendwise.models.Product;
import com.spendwise.models.User;
import com.spendwise.services.ChatService;
import com.spendwise.services.productService;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ChatPanel extends JPanel {

    private MainFrame mainFrame;
    private User currentUser;
    private JPanel friendListPanel;
    private JPanel messagesPanel;
    private JScrollPane messagesScrollPane;
    private JLabel currentChatUserLabel;
    private JLabel currentChatStatusLabel;
    private JTextField messageField;
    private JButton sendButton;
    private JButton recommendProductButton;

    // Sidebar Live References
    private JLabel sidebarNameLabel;
    private JLabel sidebarEmailLabel;
    private JLabel sidebarAvatarLabel;

    private ChatController chatController;
    private productService productServiceInstance;

    private String currentFriendName = "";
    private List<User> allFriends; // Store full list for filtering
    private List<Message> currentMessages;

    public ChatPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.currentUser = UserSession.getCurrentUser();
        this.chatController = new ChatController();
        this.productServiceInstance = new productService();
        this.allFriends = new ArrayList<>();
        this.currentMessages = new ArrayList<>();

        setLayout(new BorderLayout());
        setBackground(UIConstants.WHITE_BG);

        add(createSideMenu(), BorderLayout.WEST);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createLeftPanel(), createRightPanel());
        splitPane.setDividerLocation(300);
        splitPane.setDividerSize(1);
        splitPane.setBorder(null);

        add(splitPane, BorderLayout.CENTER);

        loadFriends();
    }

    private JPanel createSideMenu() {
        JPanel sideMenu = new JPanel();
        sideMenu.setPreferredSize(new Dimension(260, 800));
        sideMenu.setBackground(Color.WHITE);
        sideMenu.setLayout(null);
        sideMenu.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(240, 240, 240)));

        JLabel logo = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/Resim1.png"));
            Image image = logoIcon.getImage();
            Image newimg = image.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
            logoIcon = new ImageIcon(newimg);
            logo.setIcon(logoIcon);
        } catch (Exception e) {
            logo.setText("W$");
        }
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
        addMenuItem(sideMenu, "💬", "Chat", "CHAT", startY + 240, true);
        addMenuItem(sideMenu, "👤", "Profile", "PROFILE", startY + 300, false);
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

        User u = UserSession.getCurrentUser();
        String name = (u != null) ? u.getUserName() : "Guest";
        String email = (u != null) ? u.geteMail() : "";

        sidebarAvatarLabel = new JLabel(getInitials(name));
        sidebarAvatarLabel.setBounds(15, 15, 40, 40);
        sidebarAvatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebarAvatarLabel.setOpaque(true);
        sidebarAvatarLabel.setBackground(UIConstants.PRIMARY_GREEN);
        sidebarAvatarLabel.setForeground(Color.WHITE);
        sidebarAvatarLabel.setFont(new Font("Arial", Font.BOLD, 16));
        profileCard.add(sidebarAvatarLabel);

        sidebarNameLabel = new JLabel(name);
        sidebarNameLabel.setBounds(65, 18, 150, 18);
        sidebarNameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        profileCard.add(sidebarNameLabel);

        sidebarEmailLabel = new JLabel(email);
        sidebarEmailLabel.setBounds(65, 37, 150, 15);
        sidebarEmailLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        sidebarEmailLabel.setForeground(new Color(120, 120, 120));
        profileCard.add(sidebarEmailLabel);

        return profileCard;
    }

    private void updateSidebarUser() {
        User u = UserSession.getCurrentUser();
        if (u != null) {
            if (sidebarNameLabel != null)
                sidebarNameLabel.setText(u.getUserName());
            if (sidebarEmailLabel != null)
                sidebarEmailLabel.setText(u.geteMail());
            if (sidebarAvatarLabel != null)
                sidebarAvatarLabel.setText(getInitials(u.getUserName()));
        }
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

    private void addMenuItem(JPanel parent, String emoji, String text, String panelKey, int y, boolean active) {
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

        btn.addActionListener(e -> mainFrame.showPanel(panelKey));
        parent.add(btn);
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setPreferredSize(new Dimension(300, 800));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(20, 20, 15, 20));

        JLabel title = new JLabel("Chat with Friends");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(title, BorderLayout.WEST);
        leftPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(new EmptyBorder(0, 20, 15, 20));

        JTextField searchField = new JTextField("Search friends...");
        searchField.setFont(new Font("Arial", Font.PLAIN, 13));
        searchField.setForeground(Color.GRAY);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(8, 12, 8, 12)));

        // IMPLEMENTED SEARCH LOGIC
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals("Search friends...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search friends...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });

        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String query = searchField.getText().trim();
                if (query.equals("Search friends..."))
                    query = "";
                filterFriendsList(query);
            }
        });

        searchPanel.add(searchField, BorderLayout.CENTER);
        leftPanel.add(searchPanel, BorderLayout.NORTH);

        friendListPanel = new JPanel();
        friendListPanel.setLayout(new BoxLayout(friendListPanel, BoxLayout.Y_AXIS));
        friendListPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(friendListPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        return leftPanel;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);

        JPanel chatHeader = new JPanel(new BorderLayout());
        chatHeader.setBackground(Color.WHITE);
        chatHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                new EmptyBorder(20, 25, 20, 25)));

        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBackground(Color.WHITE);

        currentChatUserLabel = new JLabel("Select a friend");
        currentChatUserLabel.setFont(new Font("Arial", Font.BOLD, 18));
        currentChatUserLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        currentChatStatusLabel = new JLabel("");
        currentChatStatusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        currentChatStatusLabel.setForeground(new Color(120, 120, 120));
        currentChatStatusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        userInfoPanel.add(currentChatUserLabel);
        userInfoPanel.add(Box.createVerticalStrut(3));
        userInfoPanel.add(currentChatStatusLabel);

        chatHeader.add(userInfoPanel, BorderLayout.WEST);
        rightPanel.add(chatHeader, BorderLayout.NORTH);

        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setBackground(new Color(245, 245, 245));
        messagesPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        showEmptyState();

        messagesScrollPane = new JScrollPane(messagesPanel);
        messagesScrollPane.setBorder(null);
        messagesScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        rightPanel.add(messagesScrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        recommendProductButton = new JButton("🛍");
        recommendProductButton.setFont(new Font("Arial", Font.PLAIN, 20));
        recommendProductButton.setPreferredSize(new Dimension(45, 45));
        recommendProductButton.setBackground(Color.WHITE);
        recommendProductButton.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        recommendProductButton.setFocusPainted(false);
        recommendProductButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        recommendProductButton.setToolTipText("Recommend a Product");
        recommendProductButton.setEnabled(false);
        recommendProductButton.addActionListener(e -> showRecommendProductDialog());

        messageField = new JTextField();
        messageField.setFont(new Font("Arial", Font.PLAIN, 14));
        messageField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(10, 15, 10, 15)));
        messageField.setEnabled(false);
        messageField.addActionListener(e -> sendMessage());

        sendButton = new JButton("➤");
        sendButton.setFont(new Font("Arial", Font.BOLD, 20));
        sendButton.setPreferredSize(new Dimension(60, 45));
        sendButton.setBackground(UIConstants.PRIMARY_GREEN);
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setBorderPainted(false);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendButton.setEnabled(false);
        sendButton.addActionListener(e -> sendMessage());

        inputPanel.add(recommendProductButton, BorderLayout.WEST);
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        rightPanel.add(inputPanel, BorderLayout.SOUTH);

        return rightPanel;
    }

    public void refreshData() {
        updateSidebarUser();
        loadFriends();
        if (!currentFriendName.isEmpty()) {
            loadMessages();
        }
    }

    public void clearData() {
        currentFriendName = "";
        allFriends.clear();
        currentMessages.clear();
        friendListPanel.removeAll();
        showEmptyState();
        messageField.setEnabled(false);
        sendButton.setEnabled(false);
        recommendProductButton.setEnabled(false);
    }

    private void loadFriends() {
        try {
            int currentUserId = UserSession.getCurrentUserId();
            // Fetch once and store in allFriends
            allFriends = ChatService.getFriends(currentUserId);
            if (allFriends == null)
                allFriends = new ArrayList<>();

            // Show all initially
            filterFriendsList("");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // NEW FILTER METHOD
    private void filterFriendsList(String query) {
        friendListPanel.removeAll();
        String q = query.toLowerCase();

        boolean foundAny = false;

        for (User friend : allFriends) {
            if (friend.getUserName().toLowerCase().contains(q)) {
                friendListPanel.add(createFriendItem(friend));
                foundAny = true;
            }
        }

        if (!foundAny) {
            JLabel emptyLabel = new JLabel("No friends found");
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyLabel.setBorder(new EmptyBorder(50, 0, 0, 0));
            friendListPanel.add(emptyLabel);
        }

        friendListPanel.revalidate();
        friendListPanel.repaint();
    }

    private JPanel createFriendItem(User friend) {
        JPanel item = new JPanel(new BorderLayout(10, 0));
        item.setBackground(Color.WHITE);
        item.setBorder(new EmptyBorder(12, 20, 12, 20));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        String initials = getInitials(friend.getUserName());
        JLabel avatar = new JLabel(initials);
        avatar.setPreferredSize(new Dimension(45, 45));
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setOpaque(true);
        avatar.setBackground(getRandomColor(friend.getUserName()));
        avatar.setForeground(Color.WHITE);
        avatar.setFont(new Font("Arial", Font.BOLD, 14));
        avatar.setBorder(BorderFactory.createEmptyBorder());

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(friend.getUserName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        boolean isOnline = ChatService.isUserOnline(friend.getUserName());
        JLabel statusLabel = new JLabel(isOnline ? "Online" : "Offline");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        statusLabel.setForeground(isOnline ? UIConstants.SUCCESS_GREEN : Color.GRAY);
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(statusLabel);

        item.add(avatar, BorderLayout.WEST);
        item.add(infoPanel, BorderLayout.CENTER);

        item.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                item.setBackground(new Color(248, 248, 248));
                infoPanel.setBackground(new Color(248, 248, 248));
            }

            public void mouseExited(MouseEvent e) {
                item.setBackground(Color.WHITE);
                infoPanel.setBackground(Color.WHITE);
            }

            public void mouseClicked(MouseEvent e) {
                openChat(friend);
            }
        });
        return item;
    }

    private Color getRandomColor(String seed) {
        int hash = seed.hashCode();
        Color[] colors = { new Color(76, 175, 80), new Color(33, 150, 243), new Color(156, 39, 176),
                new Color(255, 152, 0), new Color(233, 30, 99), new Color(0, 150, 136) };
        return colors[Math.abs(hash) % colors.length];
    }

    private void openChat(User friend) {
        currentFriendName = friend.getUserName();
        currentChatUserLabel.setText(friend.getUserName());
        boolean isOnline = ChatService.isUserOnline(friend.getUserName());
        currentChatStatusLabel.setText(isOnline ? "Online" : "Offline");
        currentChatStatusLabel.setForeground(isOnline ? UIConstants.SUCCESS_GREEN : Color.GRAY);
        messageField.setEnabled(true);
        sendButton.setEnabled(true);
        recommendProductButton.setEnabled(true);
        loadMessages();
    }

    private void loadMessages() {
        messagesPanel.removeAll();
        try {
            int currentUserId = UserSession.getCurrentUserId();
            currentMessages = chatController.getMessages(currentUserId, currentFriendName);
            if (currentMessages == null || currentMessages.isEmpty()) {
                showEmptyChatState();
            } else {
                for (Message message : currentMessages) {
                    boolean isSentByMe = message.getSenderId() == currentUserId;
                    if (message.getSharedProductId() != null) {
                        messagesPanel.add(createProductMessage(message, isSentByMe));
                    } else {
                        messagesPanel.add(createTextMessage(message.getContent(), isSentByMe, message.getTimestamp()));
                    }
                    messagesPanel.add(Box.createVerticalStrut(10));
                }
                SwingUtilities.invokeLater(() -> {
                    JScrollBar vertical = messagesScrollPane.getVerticalScrollBar();
                    vertical.setValue(vertical.getMaximum());
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        messagesPanel.revalidate();
        messagesPanel.repaint();
    }

    private void showEmptyState() {
        messagesPanel.removeAll();
        JLabel emptyIcon = new JLabel("💬", SwingConstants.CENTER);
        emptyIcon.setFont(new Font("Arial", Font.PLAIN, 72));
        emptyIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel emptyLabel = new JLabel("Select a friend to start chatting");
        emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        emptyLabel.setForeground(Color.GRAY);
        emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messagesPanel.add(Box.createVerticalGlue());
        messagesPanel.add(emptyIcon);
        messagesPanel.add(Box.createVerticalStrut(15));
        messagesPanel.add(emptyLabel);
        messagesPanel.add(Box.createVerticalGlue());
    }

    private void showEmptyChatState() {
        JLabel emptyLabel = new JLabel("No messages yet. Start the conversation!");
        emptyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emptyLabel.setForeground(Color.GRAY);
        emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messagesPanel.add(Box.createVerticalGlue());
        messagesPanel.add(emptyLabel);
        messagesPanel.add(Box.createVerticalGlue());
    }

    private JPanel createTextMessage(String content, boolean isSentByMe, java.sql.Timestamp timestamp) {
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.X_AXIS));
        messagePanel.setBackground(new Color(245, 245, 245));
        messagePanel.setAlignmentX(isSentByMe ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT);
        if (!isSentByMe)
            messagePanel.add(Box.createHorizontalGlue());

        JPanel bubble = new JPanel();
        bubble.setLayout(new BoxLayout(bubble, BoxLayout.Y_AXIS));
        bubble.setBackground(isSentByMe ? UIConstants.PRIMARY_GREEN : Color.WHITE);
        bubble.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(isSentByMe ? UIConstants.PRIMARY_GREEN : new Color(220, 220, 220)),
                new EmptyBorder(10, 15, 10, 15)));

        JLabel textLabel = new JLabel("<html><div style='width: 250px;'>" + content + "</div></html>");
        textLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        textLabel.setForeground(isSentByMe ? Color.WHITE : Color.BLACK);
        textLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel timeLabel = new JLabel(formatTime(timestamp));
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        timeLabel.setForeground(isSentByMe ? new Color(230, 255, 230) : Color.GRAY);
        timeLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        bubble.add(textLabel);
        bubble.add(Box.createVerticalStrut(5));
        bubble.add(timeLabel);
        messagePanel.add(bubble);

        if (isSentByMe)
            messagePanel.add(Box.createHorizontalGlue());
        return messagePanel;
    }

    private JPanel createProductMessage(Message message, boolean isSentByMe) {
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.X_AXIS));
        messagePanel.setBackground(new Color(245, 245, 245));
        if (!isSentByMe)
            messagePanel.add(Box.createHorizontalGlue());

        JPanel productCard = new JPanel();
        productCard.setLayout(new BoxLayout(productCard, BoxLayout.Y_AXIS));
        productCard.setBackground(Color.WHITE);
        productCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)), new EmptyBorder(15, 15, 15, 15)));
        productCard.setMaximumSize(new Dimension(280, 200));

        JLabel productIcon = new JLabel("🛍️", SwingConstants.CENTER);
        productIcon.setFont(new Font("Arial", Font.PLAIN, 48));
        productIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel productLabel = new JLabel("Product Recommendation");
        productLabel.setFont(new Font("Arial", Font.BOLD, 13));
        productLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton viewButton = new JButton("View Deal");
        viewButton.setBackground(UIConstants.PRIMARY_BLUE);
        viewButton.setForeground(Color.WHITE);
        viewButton.setFocusPainted(false);
        viewButton.setBorderPainted(false);
        viewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        productCard.add(productIcon);
        productCard.add(Box.createVerticalStrut(10));
        productCard.add(productLabel);
        productCard.add(Box.createVerticalStrut(10));
        productCard.add(viewButton);
        messagePanel.add(productCard);

        if (isSentByMe)
            messagePanel.add(Box.createHorizontalGlue());
        return messagePanel;
    }

    private String formatTime(java.sql.Timestamp timestamp) {
        if (timestamp == null)
            return "";
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(timestamp);
    }

    private void sendMessage() {
        String content = messageField.getText().trim();
        if (content.isEmpty() || currentFriendName.isEmpty())
            return;
        try {
            int senderId = UserSession.getCurrentUserId();
            chatController.sendMessage(senderId, currentFriendName, content);
            messageField.setText("");
            loadMessages();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to send message!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showRecommendProductDialog() {
        List<Product> products = productServiceInstance.getAllProducts();
        if (products == null || products.isEmpty()) {
            JOptionPane.showMessageDialog(this, "You don't have any saved products to recommend!", "No Products",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Recommend a Product", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Select a product to recommend:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(new EmptyBorder(20, 20, 10, 20));
        dialog.add(titleLabel, BorderLayout.NORTH);

        JPanel productsPanel = new JPanel();
        productsPanel.setLayout(new BoxLayout(productsPanel, BoxLayout.Y_AXIS));
        productsPanel.setBackground(Color.WHITE);
        productsPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        for (Product product : products) {
            JButton productBtn = new JButton(
                    product.getName() + " - $" + String.format("%.2f", product.getPriceAfterDiscount()));
            productBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            productBtn.setFont(new Font("Arial", Font.PLAIN, 13));
            productBtn.setHorizontalAlignment(SwingConstants.LEFT);
            productBtn.setBackground(Color.WHITE);
            productBtn.setFocusPainted(false);
            productBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            productBtn.addActionListener(e -> {
                int senderId = UserSession.getCurrentUserId();
                chatController.sendProductRecommendation(senderId, currentFriendName, product);
                dialog.dispose();
                loadMessages();
            });
            productsPanel.add(productBtn);
            productsPanel.add(Box.createVerticalStrut(5));
        }
        JScrollPane scrollPane = new JScrollPane(productsPanel);
        scrollPane.setBorder(null);
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
}