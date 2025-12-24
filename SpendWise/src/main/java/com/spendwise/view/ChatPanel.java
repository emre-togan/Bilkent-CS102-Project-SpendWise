package com.spendwise.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.spendwise.controllers.ChatController;
import com.spendwise.models.Message;
import com.spendwise.models.Product;
import com.spendwise.models.User;
import com.spendwise.services.ChatService;
import com.spendwise.services.productService;
import com.spendwise.view.components.RoundedPanel;
import com.spendwise.view.components.SidebarPanel;

<<<<<<< HEAD
// import Models.Message;
// import Models.User;
// import Models.Product;
// import Services.ChatService; 
=======
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
>>>>>>> origin/main

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
    private SidebarPanel sidebarPanel;

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

        // Sidebar
        sidebarPanel = new SidebarPanel("CHAT", key -> mainFrame.showPanel(key), mainFrame::logout);
        add(sidebarPanel, BorderLayout.WEST);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createLeftPanel(), createRightPanel());
        splitPane.setDividerLocation(300);
        splitPane.setDividerSize(1);
        splitPane.setBorder(null);

        add(splitPane, BorderLayout.CENTER);

        loadFriends();
    }

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
        addMenuItem(sideMenu, "🛍️", "Shop", startY + 180, false);
        addMenuItem(sideMenu, "💬", "Chat", startY + 240, true);
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
=======
    public void refreshData() {
        if (sidebarPanel != null) {
            sidebarPanel.updateUser();
        }
        loadFriends();
        if (!currentFriendName.isEmpty()) {
            loadMessages();
        }
    }

    private String getInitials(String name) {
        if (name == null || name.isEmpty())
            return "??";
        String[] parts = name.trim().split("\\s+");
        if (parts.length == 1) {
            return parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase();
>>>>>>> origin/main
        }
        return (parts[0].charAt(0) + "" + parts[parts.length - 1].charAt(0)).toUpperCase();
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setPreferredSize(new Dimension(300, 800));

        JPanel headerWrapper = new JPanel(new BorderLayout());
        headerWrapper.setBackground(Color.WHITE);

        // Title
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(25, 20, 15, 20));

        JLabel title = new JLabel("Chat with Friends");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(title, BorderLayout.WEST);

        // Search
        JPanel searchWrapper = new JPanel(new BorderLayout());
        searchWrapper.setBackground(Color.WHITE);
        searchWrapper.setBorder(new EmptyBorder(0, 20, 15, 20));

        RoundedPanel searchContainer = new RoundedPanel(20, new Color(245, 245, 245));
        searchContainer.setPreferredSize(new Dimension(260, 40));
        searchContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        searchContainer.setLayout(new BorderLayout());
        searchContainer.setBorder(new EmptyBorder(5, 15, 5, 10));

        JLabel searchIcon = new JLabel("🔍"); // Or use icon
        searchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        searchIcon.setBorder(new EmptyBorder(0, 0, 0, 10));

        JTextField searchField = new JTextField("Search friends...");
        searchField.setFont(new Font("Arial", Font.PLAIN, 13));
        searchField.setForeground(Color.GRAY);
        searchField.setBackground(new Color(245, 245, 245));
        searchField.setBorder(null);

        // Search Logic
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals("Search friends...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

<<<<<<< HEAD
            public void mouseExited(MouseEvent e) {
                if (!active) {
                    btn.setBackground(Color.WHITE);
=======
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search friends...");
                    searchField.setForeground(Color.GRAY);
>>>>>>> origin/main
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

        searchContainer.add(searchIcon, BorderLayout.WEST);
        searchContainer.add(searchField, BorderLayout.CENTER);
        searchWrapper.add(searchContainer, BorderLayout.CENTER);

        headerWrapper.add(headerPanel, BorderLayout.NORTH);
        headerWrapper.add(searchWrapper, BorderLayout.CENTER);

        leftPanel.add(headerWrapper, BorderLayout.NORTH);

        friendListPanel = new JPanel();
        friendListPanel.setLayout(new BoxLayout(friendListPanel, BoxLayout.Y_AXIS));
        friendListPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(friendListPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        return leftPanel;
    }

    // Added field for chat header avatar
    private JLabel currentChatAvatarLabel;

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);

        // Header
        JPanel chatHeader = new JPanel(new BorderLayout());
        chatHeader.setBackground(Color.WHITE);
        chatHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                new EmptyBorder(15, 25, 15, 25)));

        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        userInfoPanel.setBackground(Color.WHITE);

        // Avatar
        currentChatAvatarLabel = new JLabel("??");
        currentChatAvatarLabel.setPreferredSize(new Dimension(50, 50));
        currentChatAvatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        currentChatAvatarLabel.setOpaque(true);
        currentChatAvatarLabel.setBackground(new Color(230, 230, 230)); // Placeholder gray
        currentChatAvatarLabel.setForeground(Color.WHITE);
        currentChatAvatarLabel.setFont(new Font("Arial", Font.BOLD, 18));
        // Rounding would be nice, but JLable is square.

        JPanel textInfoPanel = new JPanel();
        textInfoPanel.setLayout(new BoxLayout(textInfoPanel, BoxLayout.Y_AXIS));
        textInfoPanel.setBackground(Color.WHITE);

        currentChatUserLabel = new JLabel("Select a friend");
        currentChatUserLabel.setFont(new Font("Arial", Font.BOLD, 18));
        currentChatUserLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        currentChatStatusLabel = new JLabel("");
        currentChatStatusLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        currentChatStatusLabel.setForeground(new Color(120, 120, 120));
        currentChatStatusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textInfoPanel.add(currentChatUserLabel);
        textInfoPanel.add(Box.createVerticalStrut(4));
        textInfoPanel.add(currentChatStatusLabel);

        userInfoPanel.add(currentChatAvatarLabel);
        userInfoPanel.add(textInfoPanel);

        chatHeader.add(userInfoPanel, BorderLayout.WEST);
        rightPanel.add(chatHeader, BorderLayout.NORTH);

        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setBackground(Color.WHITE); // Or very light gray
        messagesPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        showEmptyState();

        messagesScrollPane = new JScrollPane(messagesPanel);
        messagesScrollPane.setBorder(null);
        messagesScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        messagesScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        rightPanel.add(messagesScrollPane, BorderLayout.CENTER);

        // Input Area
        JPanel inputPanel = new JPanel(new BorderLayout(15, 0));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        recommendProductButton = new JButton("🛍 Recommend");
        recommendProductButton.setFont(new Font("Arial", Font.BOLD, 12));
        recommendProductButton.setPreferredSize(new Dimension(130, 45));
        recommendProductButton.setBackground(new Color(240, 240, 240));
        recommendProductButton.setForeground(new Color(80, 80, 80));
        recommendProductButton.setBorderPainted(false);
        recommendProductButton.setFocusPainted(false);
        recommendProductButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        recommendProductButton.setEnabled(false);
        recommendProductButton.addActionListener(e -> showRecommendProductDialog());

        // Rounded Text Field Logic
        // We can use a RoundedPanel wrapper for the text field
        RoundedPanel inputFieldWrapper = new RoundedPanel(25, new Color(245, 245, 245));
        inputFieldWrapper.setLayout(new BorderLayout());
        inputFieldWrapper.setBorder(new EmptyBorder(5, 15, 5, 15));

        messageField = new JTextField();
        messageField.setFont(new Font("Arial", Font.PLAIN, 14));
        messageField.setBackground(new Color(245, 245, 245));
        messageField.setBorder(null);
        messageField.setEnabled(false);
        messageField.addActionListener(e -> sendMessage());

<<<<<<< HEAD
        RoundedButton sendButton = new RoundedButton("Send", 15);
        sendButton.setBackground(UIConstants.PRIMARY_BLUE);
=======
        inputFieldWrapper.add(messageField, BorderLayout.CENTER);

        sendButton = new JButton("➤");
        sendButton.setFont(new Font("Arial", Font.BOLD, 22));
        sendButton.setPreferredSize(new Dimension(50, 45));
        sendButton.setBackground(UIConstants.PRIMARY_GREEN); // Green send button
>>>>>>> origin/main
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setBorderPainted(false);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendButton.setEnabled(false);

<<<<<<< HEAD
        RoundedButton recommendBtn = new RoundedButton("+", 15);
        recommendBtn.setToolTipText("Recommend a Product");
        recommendBtn.setBackground(UIConstants.PRIMARY_GREEN);
        recommendBtn.setForeground(Color.WHITE);
        recommendBtn.setPreferredSize(new Dimension(45, 40));
        recommendBtn.setFont(new Font("Arial", Font.BOLD, 20));
=======
        // Make send button rounded? JButton default is square-ish.
        // It's fine for now, or we use a custom RoundedButton.
>>>>>>> origin/main

        sendButton.addActionListener(e -> sendMessage());

        // Layout Input
        JPanel leftInputControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftInputControls.setBackground(Color.WHITE);
        leftInputControls.add(recommendProductButton);
        // Add spacing
        leftInputControls.setBorder(new EmptyBorder(0, 0, 0, 15));

        inputPanel.add(leftInputControls, BorderLayout.WEST);
        inputPanel.add(inputFieldWrapper, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        rightPanel.add(inputPanel, BorderLayout.SOUTH);

        return rightPanel;
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
        } else {
            // Push items to top
            friendListPanel.add(Box.createVerticalGlue());
        }

        friendListPanel.revalidate();
        friendListPanel.repaint();
    }

    private JPanel createFriendItem(User friend) {
        boolean isSelected = friend.getUserName().equals(currentFriendName);
        Color bgColor = isSelected ? new Color(232, 245, 233) : Color.WHITE; // Light green if selected

        JPanel itemWrapper = new JPanel(new BorderLayout());
        itemWrapper.setBackground(Color.WHITE);
        itemWrapper.setBorder(new EmptyBorder(5, 15, 5, 15));
        // Important: Set Max Height on Wrapper to prevent BoxLayout stretching
        itemWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        itemWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedPanel item = new RoundedPanel(15, bgColor);
        item.setLayout(new BorderLayout(15, 0));
        item.setBorder(new EmptyBorder(10, 15, 10, 15));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));

        String initials = getInitials(friend.getUserName());
        JLabel avatar = new JLabel(initials);
        avatar.setPreferredSize(new Dimension(45, 45));
        avatar.setMaximumSize(new Dimension(45, 45));
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setOpaque(true);
        avatar.setBackground(UIConstants.PRIMARY_GREEN); // Always green as per design, or random
        avatar.setForeground(Color.WHITE);
        avatar.setFont(new Font("Arial", Font.BOLD, 16));
        // Make avatar circular? We can simulate with RoundedPanel logic if we wrap it,
        // but JLabel opaque is square.
        // For now keep square or simple.

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false); // Transparent to show RoundedPanel bg

        JLabel nameLabel = new JLabel(friend.getUserName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 15));
        nameLabel.setForeground(UIConstants.TEXT_DARK);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        boolean isOnline = ChatService.isUserOnline(friend.getUserName());
        JLabel statusLabel = new JLabel(isOnline ? "Online" : "Offline");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(isOnline ? UIConstants.SUCCESS_GREEN : Color.GRAY);
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(statusLabel);

        item.add(avatar, BorderLayout.WEST);
        item.add(infoPanel, BorderLayout.CENTER);

        // Notify/Unread Badge (Optional Placeholder)
        // item.add(badge, BorderLayout.EAST);

        item.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!isSelected) {
                    // Hover effect (requires repaint or changing bg color directly)
                    // Since RoundedPanel takes bg in constructor, we might need a setter or repaint
                    // logic.
                    // Simplified: just ignore hover color change for now to handle complexity,
                    // or ideally we update logic.
                }
            }

            public void mouseClicked(MouseEvent e) {
                openChat(friend);
            }
        });

        // Pass clicks from wrapper to item logic if needed, or just add listener to
        // item.

        itemWrapper.add(item, BorderLayout.CENTER);
        return itemWrapper;
    }

    private void openChat(User friend) {
        currentFriendName = friend.getUserName();

        // Refresh friends list to update selection
        filterFriendsList("");

        currentChatUserLabel.setText(friend.getUserName());
        String initials = getInitials(friend.getUserName());
        currentChatAvatarLabel.setText(initials);
        currentChatAvatarLabel.setBackground(UIConstants.PRIMARY_GREEN); // Ensure it's green or logic based

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
                    messagesPanel.add(Box.createVerticalStrut(15));
                }
                // Push messages to top
                messagesPanel.add(Box.createVerticalGlue());

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
        emptyIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        emptyIcon.setForeground(new Color(200, 200, 200));
        emptyIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emptyLabel = new JLabel("Select a friend to start chatting");
        emptyLabel.setFont(new Font("Arial", Font.BOLD, 16));
        emptyLabel.setForeground(Color.GRAY);
        emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

<<<<<<< HEAD
        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel priceLabel = new JLabel("$" + product.getPrice());
        priceLabel.setForeground(Color.RED);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedButton viewBtn = new RoundedButton("View Deal", 12);
        viewBtn.setBackground(UIConstants.PRIMARY_BLUE);
        viewBtn.setForeground(Color.WHITE);
        viewBtn.setFont(new Font("Arial", Font.PLAIN, 10));
        viewBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalStrut(5));
        card.add(header);
        card.add(Box.createVerticalStrut(5));
        card.add(nameLabel);
        card.add(priceLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(viewBtn);

        return card;
=======
        messagesPanel.add(Box.createVerticalGlue());
        messagesPanel.add(emptyIcon);
        messagesPanel.add(Box.createVerticalStrut(20));
        messagesPanel.add(emptyLabel);
        messagesPanel.add(Box.createVerticalGlue());
>>>>>>> origin/main
    }

    private void showEmptyChatState() {
        // No glue here except at ends to center it, or top if preferred.
        // Usually empty state is centered.
        messagesPanel.removeAll();
        JLabel emptyLabel = new JLabel("No messages yet. Say hi! 👋");
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
        messagePanel.setBackground(Color.WHITE); // Panel background matches chat area
        messagePanel.setAlignmentX(isSentByMe ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT);
        // Prevent vertical stretching
        messagePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1000)); // Height will be determined by content but
                                                                             // constrained
        // Actually better to not set fixed max height for text as it can be long,
        // but BoxLayout respects preferred height if we don't mess it up.
        // A safer bet for single line alignment is allowing height but ensuring wrapper
        // doesn't force it.

        // Check alignment
        if (!isSentByMe) {
            // Add avatar for friend
            JLabel avatar = new JLabel(getInitials(currentFriendName));
            avatar.setPreferredSize(new Dimension(30, 30));
            avatar.setMaximumSize(new Dimension(30, 30));
            avatar.setHorizontalAlignment(SwingConstants.CENTER);
            avatar.setOpaque(true);
            avatar.setBackground(UIConstants.PRIMARY_GREEN);
            avatar.setForeground(Color.WHITE);
            avatar.setFont(new Font("Arial", Font.BOLD, 10));
            // Ideally circular

            messagePanel.add(avatar);
            messagePanel.add(Box.createHorizontalStrut(10));
        } else {
            messagePanel.add(Box.createHorizontalGlue());
        }

        Color bubbleColor = isSentByMe ? UIConstants.PRIMARY_GREEN : new Color(240, 242, 245);
        Color textColor = isSentByMe ? Color.WHITE : Color.BLACK;

        RoundedPanel bubble = new RoundedPanel(18, bubbleColor);
        bubble.setLayout(new BoxLayout(bubble, BoxLayout.Y_AXIS));
        bubble.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel textLabel = new JLabel("<html><body style='width: 200px'>" + content + "</body></html>");
        textLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        textLabel.setForeground(textColor);
        textLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel timeLabel = new JLabel(formatTime(timestamp));
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        timeLabel.setForeground(isSentByMe ? new Color(230, 255, 230) : Color.GRAY);
        timeLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        bubble.add(textLabel);
        bubble.add(Box.createVerticalStrut(4));
        bubble.add(timeLabel);

        messagePanel.add(bubble);

        if (!isSentByMe) {
            messagePanel.add(Box.createHorizontalGlue());
        }

        return messagePanel;
    }

    private JPanel createProductMessage(Message message, boolean isSentByMe) {
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.X_AXIS));
        messagePanel.setBackground(Color.WHITE);
        // Prevent stretching
        messagePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        if (!isSentByMe) {
            JLabel avatar = new JLabel(getInitials(currentFriendName));
            avatar.setPreferredSize(new Dimension(30, 30));
            avatar.setMaximumSize(new Dimension(30, 30));
            avatar.setHorizontalAlignment(SwingConstants.CENTER);
            avatar.setOpaque(true);
            avatar.setBackground(UIConstants.PRIMARY_GREEN);
            avatar.setForeground(Color.WHITE);
            avatar.setFont(new Font("Arial", Font.BOLD, 10));

            messagePanel.add(avatar);
            messagePanel.add(Box.createHorizontalStrut(10));
        } else {
            messagePanel.add(Box.createHorizontalGlue());
        }

        RoundedPanel productCard = new RoundedPanel(15, Color.WHITE);
        productCard.setLayout(new BoxLayout(productCard, BoxLayout.Y_AXIS));
        productCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(15, 15, 15, 15)));
        productCard.setMaximumSize(new Dimension(250, 220));

        // Use a placeholder image or icon
        JLabel productIcon = new JLabel("🛍️", SwingConstants.CENTER);
        productIcon.setFont(new Font("Arial", Font.PLAIN, 48));
        productIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        productIcon.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel productLabel = new JLabel("Shared Deal");
        productLabel.setFont(new Font("Arial", Font.BOLD, 14));
        productLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton viewButton = new JButton("View Details");
        viewButton.setBackground(UIConstants.PRIMARY_BLUE);
        viewButton.setForeground(Color.WHITE);
        viewButton.setFocusPainted(false);
        viewButton.setBorderPainted(false);
        viewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // If we had product details in message, we'd show them.
        // Currently message logic relies on ID, assuming controller handles fetching or
        // it's embedded.
        // The original code passed `message.getSharedProductId()`.

        productCard.add(productIcon);
        productCard.add(Box.createVerticalStrut(15));
        productCard.add(productLabel);
        productCard.add(Box.createVerticalStrut(15));
        productCard.add(viewButton);

        messagePanel.add(productCard);

        if (!isSentByMe) {
            messagePanel.add(Box.createHorizontalGlue());
        }

        return messagePanel;
    }

    private String formatTime(java.sql.Timestamp timestamp) {
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