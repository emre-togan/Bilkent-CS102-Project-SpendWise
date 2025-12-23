package com.spendwise.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.spendwise.models.User;
import com.spendwise.services.ProfileService;

public class ProfilePanel extends JPanel {

    private MainFrame mainFrame;
    private JLabel userNameLabel;
    private JLabel userEmailLabel;
    private JLabel purchaseCountLabel;
    private JLabel savedAmountLabel;
    private JLabel dealsFoundLabel;
    private JPanel friendsContainer;

    public ProfilePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
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
        addMenuItem(sideMenu, "🛍️", "Shop", startY + 180, false);
        addMenuItem(sideMenu, "💬", "Chat", startY + 240, false);
        addMenuItem(sideMenu, "👤", "Profile", startY + 300, true);
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

        userNameLabel = new JLabel("Sarah Johnson");
        userNameLabel.setBounds(65, 18, 150, 18);
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        profileCard.add(userNameLabel);

        userEmailLabel = new JLabel("sarah@email.com");
        userEmailLabel.setBounds(65, 37, 150, 15);
        userEmailLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        userEmailLabel.setForeground(new Color(120, 120, 120));
        profileCard.add(userEmailLabel);

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
            }

            public void mouseExited(MouseEvent e) {
                if (!active) {
                    btn.setBackground(Color.WHITE);
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

        // Profile Header Card
        JPanel headerCard = new JPanel();
        headerCard.setBounds(30, 30, 1070, 120);
        headerCard.setBackground(Color.WHITE);
        headerCard.setLayout(null);
        headerCard.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        // Avatar
        JLabel bigAvatar = new JLabel("SJ");
        bigAvatar.setBounds(30, 25, 70, 70);
        bigAvatar.setHorizontalAlignment(SwingConstants.CENTER);
        bigAvatar.setOpaque(true);
        bigAvatar.setBackground(UIConstants.PRIMARY_GREEN);
        bigAvatar.setForeground(Color.WHITE);
        bigAvatar.setFont(new Font("Arial", Font.BOLD, 32));
        headerCard.add(bigAvatar);

        // User Info
        JLabel nameLabel = new JLabel("Sarah Johnson");
        nameLabel.setBounds(120, 30, 300, 30);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerCard.add(nameLabel);

        JLabel emailLabel = new JLabel("sarah.johnson@email.com");
        emailLabel.setBounds(120, 65, 300, 20);
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setForeground(new Color(120, 120, 120));
        headerCard.add(emailLabel);

        // Edit Profile Button
        RoundedButton editBtn = new RoundedButton("✏️ Edit Profile", 15);
        editBtn.setBounds(920, 40, 130, 40);
        editBtn.setFont(new Font("Arial", Font.BOLD, 13));
        editBtn.setBackground(UIConstants.PRIMARY_BLUE);
        editBtn.setForeground(Color.WHITE);
        editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editBtn.addActionListener(e -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            new PersonalInfoDialog(parent).setVisible(true);
        });
        headerCard.add(editBtn);

        content.add(headerCard);

        // Stats Cards
        createStatsCard(content, "156", "Purchases", 30, 170);
        createStatsCard(content, "$2.4k", "Saved", 380, 170);
        createStatsCard(content, "12", "Deals Found", 730, 170);

        // Friends Section
        JLabel friendsTitle = new JLabel("Friends");
        friendsTitle.setBounds(30, 290, 200, 30);
        friendsTitle.setFont(new Font("Arial", Font.BOLD, 18));
        content.add(friendsTitle);

        RoundedButton addFriendBtn = new RoundedButton("➕ Add Friend", 15);
        addFriendBtn.setBounds(950, 290, 150, 35);
        addFriendBtn.setFont(new Font("Arial", Font.BOLD, 13));
        addFriendBtn.setBackground(UIConstants.PRIMARY_GREEN);
        addFriendBtn.setForeground(Color.WHITE);
        addFriendBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        content.add(addFriendBtn);

        friendsContainer = new JPanel();
        friendsContainer.setLayout(new BoxLayout(friendsContainer, BoxLayout.Y_AXIS));
        friendsContainer.setBackground(new Color(250, 250, 250));

        JScrollPane friendsScroll = new JScrollPane(friendsContainer);
        friendsScroll.setBounds(30, 340, 1070, 180);
        friendsScroll.setBorder(null);
        friendsScroll.getVerticalScrollBar().setUnitIncrement(16);
        content.add(friendsScroll);

        // Quick Actions
        JLabel actionsTitle = new JLabel("Quick Actions");
        actionsTitle.setBounds(30, 540, 200, 30);
        actionsTitle.setFont(new Font("Arial", Font.BOLD, 18));
        content.add(actionsTitle);

        createActionButton(content, "📦 Purchase History", 30, 580, e -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            new PurchaseHistoryDialog(parent).setVisible(true);
        });

        createActionButton(content, "❤️ Wishlist", 380, 580, e -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            new WishlistDialog(parent).setVisible(true);
        });

        createActionButton(content, "📍 Addresses", 730, 580, e -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            new AddressesDialog(parent).setVisible(true);
        });

        return content;
    }

    private void createStatsCard(JPanel parent, String value, String label, int x, int y) {
        JPanel card = new JPanel();
        card.setBounds(x, y, 330, 100);
        card.setBackground(Color.WHITE);
        card.setLayout(null);
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setBounds(20, 20, 290, 35);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 32));
        valueLabel.setForeground(UIConstants.PRIMARY_GREEN);
        card.add(valueLabel);

        JLabel descLabel = new JLabel(label);
        descLabel.setBounds(20, 60, 290, 20);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descLabel.setForeground(new Color(120, 120, 120));
        card.add(descLabel);

        parent.add(card);
        
        // Store references for updating
        if (label.equals("Purchases")) purchaseCountLabel = valueLabel;
        if (label.equals("Saved")) savedAmountLabel = valueLabel;
        if (label.equals("Deals Found")) dealsFoundLabel = valueLabel;
    }

    private void createActionButton(JPanel parent, String text, int x, int y, java.awt.event.ActionListener action) {
        RoundedButton btn = new RoundedButton(text, 15);
        btn.setBounds(x, y, 330, 60);
        btn.setFont(new Font("Arial", Font.BOLD, 15));
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(50, 50, 50));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(0, 20, 0, 0)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(248, 248, 248));
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(Color.WHITE);
            }
        });
        
        parent.add(btn);
    }

    private void loadFriends() {
        friendsContainer.removeAll();

        try {
            List<User> friends = ProfileService.getFriends();
            
            if (friends == null || friends.isEmpty()) {
                JLabel emptyLabel = new JLabel("No friends yet. Add some friends to get started!");
                emptyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                emptyLabel.setForeground(Color.GRAY);
                emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                friendsContainer.add(Box.createVerticalStrut(30));
                friendsContainer.add(emptyLabel);
            } else {
                for (User friend : friends) {
                    friendsContainer.add(createFriendItem(friend));
                    friendsContainer.add(Box.createVerticalStrut(10));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Error loading friends");
            errorLabel.setForeground(Color.RED);
            friendsContainer.add(errorLabel);
        }

        friendsContainer.revalidate();
        friendsContainer.repaint();
    }

    private JPanel createFriendItem(User friend) {
        JPanel item = new JPanel(new BorderLayout(15, 0));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(15, 15, 15, 15)
        ));

        // Avatar
        JLabel avatar = new JLabel(getInitials(friend.getUserName()));
        avatar.setPreferredSize(new Dimension(45, 45));
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setOpaque(true);
        avatar.setBackground(UIConstants.PRIMARY_BLUE);
        avatar.setForeground(Color.WHITE);
        avatar.setFont(new Font("Arial", Font.BOLD, 16));

        // Info
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(friend.getUserName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel sinceLabel = new JLabel("Friend since " + friend.getAccountCreationTime().toString().substring(0, 10));
        sinceLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        sinceLabel.setForeground(new Color(120, 120, 120));

        infoPanel.add(nameLabel);
        infoPanel.add(sinceLabel);

        item.add(avatar, BorderLayout.WEST);
        item.add(infoPanel, BorderLayout.CENTER);

        return item;
    }

    private String getInitials(String name) {
        if (name == null || name.isEmpty()) return "?";
        String[] parts = name.split(" ");
        if (parts.length >= 2) {
            return ("" + parts[0].charAt(0) + parts[1].charAt(0)).toUpperCase();
        }
        return ("" + name.charAt(0)).toUpperCase();
    }

    /**
     * Refresh data from backend - called by MainFrame
     */
    public void refreshData() {
        try {
            User currentUser = ProfileService.getCurrentUser();
            
            if (currentUser != null) {
                // Update UI with user data
                if (userNameLabel != null) {
                    userNameLabel.setText(currentUser.getUserName());
                }
                if (userEmailLabel != null) {
                    userEmailLabel.setText(currentUser.geteMail());
                }
                
                // Update stats (placeholder values for now)
                if (purchaseCountLabel != null) {
                    purchaseCountLabel.setText(String.valueOf(currentUser.getPurchaseCount()));
                }
                if (savedAmountLabel != null) {
                    savedAmountLabel.setText(String.format("$%.1fk", currentUser.getTotalSaved() / 1000.0));
                }
                if (dealsFoundLabel != null) {
                    dealsFoundLabel.setText(String.valueOf(currentUser.getDealsFound()));
                }
            }
            
            // Load friends
            loadFriends();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error loading profile: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Clear all data - called on logout
     */
    public void clearData() {
        if (userNameLabel != null) {
            userNameLabel.setText("Guest");
        }
        if (userEmailLabel != null) {
            userEmailLabel.setText("guest@email.com");
        }
        if (purchaseCountLabel != null) {
            purchaseCountLabel.setText("0");
        }
        if (savedAmountLabel != null) {
            savedAmountLabel.setText("$0");
        }
        if (dealsFoundLabel != null) {
            dealsFoundLabel.setText("0");
        }
        
        friendsContainer.removeAll();
        JLabel emptyLabel = new JLabel("Please login to view profile");
        emptyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emptyLabel.setForeground(Color.GRAY);
        emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        friendsContainer.add(Box.createVerticalStrut(30));
        friendsContainer.add(emptyLabel);
        friendsContainer.revalidate();
        friendsContainer.repaint();
    }
}
