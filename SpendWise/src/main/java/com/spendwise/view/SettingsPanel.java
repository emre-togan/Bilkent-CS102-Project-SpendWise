package com.spendwise.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// import Services.SettingsService; 
// import Services.UserSession;     

public class SettingsPanel extends JPanel {

    private MainFrame mainFrame;
    // Callback to notify MainFrame when logout occurs
    private Runnable onLogoutClicked;

    public SettingsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(UIConstants.WHITE_BG);

        add(createSideMenu(), BorderLayout.WEST);

        // Load UI and Settings
        loadSettings();
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
        addMenuItem(sideMenu, "👤", "Profile", startY + 300, false);
        addMenuItem(sideMenu, "⚙️", "Settings", startY + 360, true);

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

    private void loadSettings() {
        removeAll(); // Clear panel to refresh if needed

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(UIConstants.WHITE_BG);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. Header
        JLabel headerLabel = new JLabel("Settings");
        headerLabel.setFont(UIConstants.HEADING_FONT);
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(headerLabel);
        contentPanel.add(Box.createVerticalStrut(20));

        // 2. Notifications Section
        contentPanel.add(createSectionHeader("Notifications"));
        contentPanel.add(Box.createVerticalStrut(10));

        // INTEGRATION: Fetch current status from backend
        // boolean budgetAlerts = SettingsService.getSetting("Budget Alerts");
        // boolean discountAlerts = SettingsService.getSetting("Discount Alerts");

        // Using 'true' as placeholder until backend service is connected
        contentPanel.add(createToggleItem("Budget Alerts", true));
        contentPanel.add(createToggleItem("Discount Alerts", true));
        contentPanel.add(Box.createVerticalStrut(25));

        // 3. Account Section
        contentPanel.add(createSectionHeader("Account"));
        contentPanel.add(Box.createVerticalStrut(10));

        // Navigation items now open the real Dialogs we created
        contentPanel.add(createNavigationItem("Personal Information", () -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            new PersonalInfoDialog(parent).setVisible(true);
        }));

        contentPanel.add(createNavigationItem("Change Password", () -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            new ChangePasswordDialog(parent).setVisible(true);
        }));

        contentPanel.add(createNavigationItem("Privacy Settings", () -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            new PrivacySettingsDialog(parent).setVisible(true);
        }));

        contentPanel.add(Box.createVerticalStrut(25));

        // 4. Support Section
        contentPanel.add(createSectionHeader("Support"));
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(createNavigationItem("Help Center",
                () -> JOptionPane.showMessageDialog(this, "Opening Help Center...")));
        contentPanel.add(createNavigationItem("Terms of Service",
                () -> JOptionPane.showMessageDialog(this, "Opening Terms of Service...")));
        contentPanel.add(createNavigationItem("Privacy Policy",
                () -> JOptionPane.showMessageDialog(this, "Opening Privacy Policy...")));
        contentPanel.add(Box.createVerticalStrut(40));

        // 5. Footer (Version & Logout)
        JLabel versionLabel = new JLabel("SpendWise Version 1.0.0");
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        versionLabel.setForeground(UIConstants.GRAY_TEXT);
        versionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(versionLabel);
        contentPanel.add(Box.createVerticalStrut(15));

        JButton logoutButton = new JButton("Log Out");
        logoutButton.setFont(UIConstants.BUTTON_FONT);
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        logoutButton.addActionListener(e -> handleLogout());

        contentPanel.add(logoutButton);

        // Scroll Pane configuration
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    // --- LOGIC METHODS ---

    private void handleToggleChange(String settingName, boolean value) {
        // INTEGRATION: Update setting in the backend
        // SettingsService.updateSetting(settingName, value);
        System.out.println("[Backend Call] Setting Updated: " + settingName + " -> " + value);
    }

    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to log out?",
                "Logout",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            // INTEGRATION: Terminate session in backend
            // SettingsService.logout(UserSession.getCurrentUserId());

            // Navigate back to Login Screen
            if (onLogoutClicked != null) {
                onLogoutClicked.run();
            }
        }
    }

    public void setOnLogoutClicked(Runnable onLogoutClicked) {
        this.onLogoutClicked = onLogoutClicked;
    }

    // --- UI HELPER METHODS ---

    private JLabel createSectionHeader(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(UIConstants.PRIMARY_BLUE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel createToggleItem(String text, boolean initialValue) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.WHITE_BG);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(text);
        label.setFont(UIConstants.BODY_FONT);

        JCheckBox toggle = new JCheckBox();
        toggle.setSelected(initialValue);
        toggle.setBackground(UIConstants.WHITE_BG);
        toggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Trigger handleToggleChange when clicked
        toggle.addActionListener(e -> handleToggleChange(text, toggle.isSelected()));

        panel.add(label, BorderLayout.WEST);
        panel.add(toggle, BorderLayout.EAST);

        return panel;
    }

    private JPanel createNavigationItem(String text, Runnable onClickAction) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.WHITE_BG);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)));

        JLabel label = new JLabel(text);
        label.setFont(UIConstants.BODY_FONT);

        JLabel arrow = new JLabel(">");
        arrow.setForeground(UIConstants.GRAY_TEXT);
        arrow.setFont(new Font("Arial", Font.BOLD, 14));

        panel.add(label, BorderLayout.WEST);
        panel.add(arrow, BorderLayout.EAST);

        panel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(250, 250, 250));
            }

            public void mouseExited(MouseEvent e) {
                panel.setBackground(UIConstants.WHITE_BG);
            }

            public void mouseClicked(MouseEvent e) {
                onClickAction.run();
            }
        });

        return panel;
    }
}