package com.spendwise.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.spendwise.models.Message;
import com.spendwise.models.Product;
import com.spendwise.models.User;
import com.spendwise.services.ChatService;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

// import Models.Message;
// import Models.User;
// import Models.Product;
// import Services.ChatService; 

public class ChatPanel extends JPanel {

    private MainFrame mainFrame;
    private JPanel friendListPanel;
    private JPanel chatAreaPanel;
    private JLabel currentChatUserLabel;
    private JLabel currentChatStatusLabel;
    private JTextField messageField;

    private String currentFriendName = "";

    public ChatPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
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

    // --- LEFT PANEL (Friend List) ---
    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        JPanel header = new JPanel(new BorderLayout(0, 10));
        header.setBackground(new Color(245, 245, 245));
        header.setBorder(new EmptyBorder(20, 15, 20, 15));

        JLabel title = new JLabel("Chat with Friends");
        title.setFont(UIConstants.HEADING_FONT);

        JTextField searchBar = new JTextField("Search friends...");
        searchBar.setPreferredSize(new Dimension(100, 35));

        header.add(title, BorderLayout.NORTH);
        header.add(searchBar, BorderLayout.CENTER);

        friendListPanel = new JPanel();
        friendListPanel.setLayout(new BoxLayout(friendListPanel, BoxLayout.Y_AXIS));
        friendListPanel.setBackground(new Color(245, 245, 245));

        JScrollPane scrollPane = new JScrollPane(friendListPanel);
        scrollPane.setBorder(null);

        panel.add(header, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    // --- RİGHT PANEL (Chat Area) ---
    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.WHITE_BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIConstants.WHITE_BG);
        header.setBorder(new EmptyBorder(10, 20, 10, 20));
        header.setPreferredSize(new Dimension(100, 70));

        JPanel titleBox = new JPanel(new GridLayout(2, 1));
        titleBox.setOpaque(false);

        currentChatUserLabel = new JLabel("Select a friend");
        currentChatUserLabel.setFont(new Font("Arial", Font.BOLD, 16));

        currentChatStatusLabel = new JLabel("");
        currentChatStatusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        currentChatStatusLabel.setForeground(UIConstants.PRIMARY_GREEN);

        titleBox.add(currentChatUserLabel);
        titleBox.add(currentChatStatusLabel);

        header.add(titleBox, BorderLayout.WEST);

        chatAreaPanel = new JPanel();
        chatAreaPanel.setLayout(new BoxLayout(chatAreaPanel, BoxLayout.Y_AXIS));
        chatAreaPanel.setBackground(UIConstants.WHITE_BG);

        JScrollPane chatScroll = new JScrollPane(chatAreaPanel);
        chatScroll.setBorder(null);

        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(UIConstants.WHITE_BG);
        inputPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        messageField = new JTextField();
        messageField.setPreferredSize(new Dimension(100, 40));

        JButton sendButton = new JButton("Send");
        sendButton.setBackground(UIConstants.PRIMARY_BLUE);
        sendButton.setForeground(Color.WHITE);

        JButton recommendBtn = new JButton("+");
        recommendBtn.setToolTipText("Recommend a Product");
        recommendBtn.setBackground(UIConstants.PRIMARY_GREEN);
        recommendBtn.setForeground(Color.WHITE);
        recommendBtn.setPreferredSize(new Dimension(45, 40));
        recommendBtn.setFont(new Font("Arial", Font.BOLD, 20));

        recommendBtn.addActionListener(e -> handleRecommendProduct());
        sendButton.addActionListener(e -> handleSendMessage());

        inputPanel.add(recommendBtn, BorderLayout.WEST);
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        panel.add(header, BorderLayout.NORTH);
        panel.add(chatScroll, BorderLayout.CENTER);
        panel.add(inputPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Methods

    // 1. loadFriends
    private void loadFriends() {
        friendListPanel.removeAll();

        // backend
        // Expected metod: public static List<User> getFriends(int currentUserId)
        List<User> friends = ChatService.getFriends(UserSession.getCurrentUserId());

        if (friends != null) {
            for (User friend : friends) {
                friendListPanel.add(createFriendItem(
                        friend.getUserName(),
                        friend.getLastMessage(), // Expected Methods from backend
                        friend.getLastMessageTime(), //
                        friend.getUnreadCount(), //
                        friend.isOnline() //
                ));
            }
        }

        friendListPanel.revalidate();
        friendListPanel.repaint();
    }

    // 2. loadMessages
    private void loadMessages(String friendName) {
        this.currentFriendName = friendName;
        currentChatUserLabel.setText(friendName);

        // Check if person online from backend
        boolean isOnline = ChatService.isUserOnline(friendName);
        currentChatStatusLabel.setText(isOnline ? "Online" : "Offline");

        chatAreaPanel.removeAll();

        // Take the message history from backend
        // Expected metod: public static List<Message> getMessages(int currentUserId,
        // String friendName)
        List<Message> messages = ChatService.getMessages(UserSession.getCurrentUserId(), friendName);

        if (messages != null) {
            for (Message msg : messages) {
                chatAreaPanel.add(createMessageBubble(msg));
                chatAreaPanel.add(Box.createVerticalStrut(5));
            }
        }

        chatAreaPanel.revalidate();
        chatAreaPanel.repaint();
        scrollToBottom();
    }

    // 3. handleSendMessage
    private void handleSendMessage() {
        String txt = messageField.getText();
        if (!txt.isEmpty() && !currentFriendName.isEmpty()) {

            // Backend: saving messages
            // Expected metod: public static void sendMessage(int senderId, String
            // receiverName, String content)
            ChatService.sendMessage(UserSession.getCurrentUserId(), currentFriendName, txt);

            // Backend'den "Başarılı" dönmesini beklemeden ekrana basıyoruz
            Message newMsg = new Message(txt, true, "Now");
            chatAreaPanel.add(createMessageBubble(newMsg));
            chatAreaPanel.add(Box.createVerticalStrut(5));

            chatAreaPanel.revalidate();
            chatAreaPanel.repaint();
            scrollToBottom();

            messageField.setText("");
        }
    }

    // 4. handleRecommendProduct
    private void handleRecommendProduct() {
        if (currentFriendName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a friend first!");
            return;
        }

        // Sample product selection (Normally, a product selection window would open
        // here)
        // For now, we are sending a fixed product
        Product recommendedProduct = new Product("Sony WH-1000XM5", "$299.99", "$349.99");

        // Expected metod: public static void sendProductRecommendation(int senderId,
        // String receiverName, Product p)
        ChatService.sendProductRecommendation(UserSession.getCurrentUserId(), currentFriendName, recommendedProduct);

        Message productMsg = new Message(recommendedProduct, true, "Now");
        chatAreaPanel.add(createMessageBubble(productMsg));
        chatAreaPanel.add(Box.createVerticalStrut(5));

        chatAreaPanel.revalidate();
        chatAreaPanel.repaint();
        scrollToBottom();
    }

    // Helper Methods

    private JPanel createFriendItem(String name, String lastMsg, String time, int unread, boolean isOnline) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));
        panel.setBackground(new Color(245, 245, 245));
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Avatar + Indicator
        JPanel avatarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillOval(0, 0, 45, 45);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 16));
                g2.drawString(name.substring(0, 1), 17, 28);
                if (isOnline) {
                    g2.setColor(UIConstants.PRIMARY_GREEN);
                    g2.fillOval(32, 32, 12, 12);
                    g2.setColor(Color.WHITE);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawOval(32, 32, 12, 12);
                }
            }
        };
        avatarPanel.setPreferredSize(new Dimension(50, 50));

        JPanel center = new JPanel(new GridLayout(2, 1));
        center.setOpaque(false);
        center.add(new JLabel(name));
        center.add(new JLabel(lastMsg));

        JPanel right = new JPanel(new GridLayout(2, 1));
        right.setOpaque(false);
        JLabel timeLabel = new JLabel(time, SwingConstants.RIGHT);
        timeLabel.setForeground(Color.GRAY);
        right.add(timeLabel);

        if (unread > 0) {
            JLabel badge = new JLabel(" " + unread + " ");
            badge.setOpaque(true);
            badge.setBackground(UIConstants.PRIMARY_GREEN);
            badge.setForeground(Color.WHITE);
            JPanel badgeWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            badgeWrap.setOpaque(false);
            badgeWrap.add(badge);
            right.add(badgeWrap);
        } else {
            right.add(new JLabel(""));
        }

        panel.add(avatarPanel, BorderLayout.WEST);
        panel.add(center, BorderLayout.CENTER);
        panel.add(right, BorderLayout.EAST);

        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                loadMessages(name);
            }
        });

        return panel;
    }

    private JPanel createMessageBubble(Message msg) {
        JPanel row = new JPanel(new FlowLayout(msg.isSentByMe() ? FlowLayout.RIGHT : FlowLayout.LEFT));
        row.setBackground(UIConstants.WHITE_BG);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, msg.isProduct() ? 140 : 60));

        if (msg.isProduct()) {
            // Use getProductObject() if available, otherwise we might need to fetch it (not
            // implemented here)
            // But since ChatPanel creates specific messages with Product objects, it should
            // work for those.
            // For loaded messages from DB, productObject might be null.
            // Ideally we'd fetch it. For now, we assume if it's a product message, we have
            // the object or we handle null.
            Product p = msg.getProductObject();
            if (p != null) {
                row.add(createProductCard(p));
            } else {
                // Fallback or todo: fetch product by ID
                row.add(new JLabel("Product info unavailable"));
            }
        } else {
            JPanel bubbleContainer = new JPanel(new BorderLayout());
            bubbleContainer.setOpaque(true);
            bubbleContainer.setBorder(new EmptyBorder(8, 12, 8, 12));

            if (msg.isSentByMe()) {
                bubbleContainer.setBackground(new Color(220, 248, 198));
            } else {
                bubbleContainer.setBackground(new Color(240, 240, 240));
            }

            JLabel textLabel = new JLabel("<html>" + msg.getContent() + "</html>");
            textLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            JLabel timeLabel = new JLabel(msg.getTime());
            timeLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            timeLabel.setForeground(Color.GRAY);
            timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            bubbleContainer.add(textLabel, BorderLayout.CENTER);
            bubbleContainer.add(timeLabel, BorderLayout.SOUTH);

            row.add(bubbleContainer);
        }
        return row;
    }

    private JPanel createProductCard(Product product) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(200, 130));

        JLabel header = new JLabel("Recommended Deal");
        header.setFont(new Font("Arial", Font.BOLD, 10));
        header.setForeground(UIConstants.PRIMARY_GREEN);
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel priceLabel = new JLabel("$" + product.getPrice());
        priceLabel.setForeground(Color.RED);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton viewBtn = new JButton("View Deal");
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
    }

    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            if (chatAreaPanel.getParent() instanceof JViewport) {
                JScrollPane sp = (JScrollPane) chatAreaPanel.getParent().getParent();
                sp.getVerticalScrollBar().setValue(sp.getVerticalScrollBar().getMaximum());
            }
        });
    }
}