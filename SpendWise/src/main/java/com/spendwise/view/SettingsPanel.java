package com.spendwise.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import com.spendwise.services.SettingsService;

public class SettingsPanel extends JPanel {

    private MainFrame mainFrame;
    private Runnable onLogoutClicked;
    private SettingsService settingsService;
    private JPanel contentPanel;

    // Toggle Buttons References (to update them)
    private JCheckBox budgetAlertsToggle;
    private JCheckBox discountAlertsToggle;

    // Sidebar Profile Labels
    private JLabel sidebarAvatarLabel;
    private JLabel sidebarNameLabel;
    private JLabel sidebarEmailLabel;

    public SettingsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.settingsService = new SettingsService();
        setLayout(new BorderLayout());
        setBackground(UIConstants.WHITE_BG);

        add(createSideMenu(), BorderLayout.WEST);
        add(createContent(), BorderLayout.CENTER);

        // Load UI and Settings
        loadSettings();
    }

    public void setOnLogoutClicked(Runnable callback) {
        this.onLogoutClicked = callback;
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
        addMenuButton(sideMenu, "🏠", "Dashboard", "DASHBOARD", startY, false);
        addMenuButton(sideMenu, "💳", "Budget", "BUDGET", startY + 60, false);
        addMenuButton(sideMenu, "🧾", "Expenses", "EXPENSES", startY + 120, false);
        addMenuButton(sideMenu, "🛍️", "Shop", "SHOP", startY + 180, false);
        addMenuButton(sideMenu, "💬", "Chat", "CHAT", startY + 240, false);
        addMenuButton(sideMenu, "👤", "Profile", "PROFILE", startY + 300, false);
        addMenuButton(sideMenu, "⚙️", "Settings", "SETTINGS", startY + 360, true);

        // Profile Card at bottom
        JPanel profileCard = new JPanel();
        profileCard.setBounds(15, 650, 230, 70);
        profileCard.setBackground(new Color(248, 249, 250));
        profileCard.setLayout(null);
        profileCard.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        sidebarAvatarLabel = new JLabel("??");
        sidebarAvatarLabel.setBounds(15, 15, 40, 40);
        sidebarAvatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebarAvatarLabel.setOpaque(true);
        sidebarAvatarLabel.setBackground(UIConstants.PRIMARY_GREEN);
        sidebarAvatarLabel.setForeground(Color.WHITE);
        sidebarAvatarLabel.setFont(new Font("Arial", Font.BOLD, 16));
        profileCard.add(sidebarAvatarLabel);

        sidebarNameLabel = new JLabel("Guest");
        sidebarNameLabel.setBounds(65, 18, 150, 18);
        sidebarNameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        profileCard.add(sidebarNameLabel);

        sidebarEmailLabel = new JLabel("guest@email.com");
        sidebarEmailLabel.setBounds(65, 37, 150, 15);
        sidebarEmailLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        sidebarEmailLabel.setForeground(new Color(120, 120, 120));
        profileCard.add(sidebarEmailLabel);

        sideMenu.add(profileCard);

        JButton logoutBtn = new JButton("🚪 Logout");
        logoutBtn.setBounds(15, 735, 230, 40);
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 14));
        logoutBtn.setForeground(new Color(220, 53, 69));
        logoutBtn.setBackground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createLineBorder(new Color(220, 53, 69)));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            if (onLogoutClicked != null)
                onLogoutClicked.run();
        });
        sideMenu.add(logoutBtn);

        return sideMenu;
    }

    private void addMenuButton(JPanel panel, String icon, String text, String targetPanelName, int y,
            boolean selected) {
        JButton button = new JButton(icon + "  " + text);
        button.setBounds(10, y, 240, 50);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(new EmptyBorder(0, 15, 0, 0));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (selected) {
            button.setBackground(UIConstants.PRIMARY_GREEN);
            button.setForeground(Color.WHITE);
            button.setOpaque(true);
        } else {
            button.setContentAreaFilled(false);
            button.setForeground(new Color(80, 80, 80));
        }

        button.addActionListener(e -> mainFrame.showPanel(targetPanelName));
        panel.add(button);
    }

    private JPanel createContent() {
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(UIConstants.WHITE_BG);
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("Settings");
        title.setFont(UIConstants.HEADING_FONT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(title);
        contentPanel.add(Box.createVerticalStrut(20));

        // 1. Account Section
        addSectionHeader("Account");
        contentPanel.add(
                createNavigationItem("Personal Information", () -> new PersonalInfoDialog(mainFrame).setVisible(true)));
        contentPanel.add(
                createNavigationItem("Change Password", () -> new ChangePasswordDialog(mainFrame).setVisible(true)));
        contentPanel.add(
                createNavigationItem("Privacy Settings", () -> new PrivacySettingsDialog(mainFrame).setVisible(true)));
        contentPanel.add(Box.createVerticalStrut(20));

        // 2. Notifications Section
        addSectionHeader("Notifications");
        budgetAlertsToggle = new JCheckBox();
        discountAlertsToggle = new JCheckBox();
        contentPanel.add(createToggleItem("Budget Alerts", budgetAlertsToggle));
        contentPanel.add(createToggleItem("Discount Alerts", discountAlertsToggle));
        contentPanel.add(Box.createVerticalStrut(20));

        // 3. Support Section
        addSectionHeader("Support");
        contentPanel.add(createNavigationItem("Help Center",
                () -> JOptionPane.showMessageDialog(this, "Help Center Coming Soon!")));
        contentPanel.add(createNavigationItem("Terms of Service",
                () -> JOptionPane.showMessageDialog(this, "Terms of Service...")));

        // Delete Account (Red)
        JButton deleteAccBtn = new JButton("Delete Account");
        deleteAccBtn.setForeground(Color.RED);
        deleteAccBtn.setBorderPainted(false);
        deleteAccBtn.setContentAreaFilled(false);
        deleteAccBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        deleteAccBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        deleteAccBtn.addActionListener(e -> handleDeleteAccount());

        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(deleteAccBtn);

        // --- FIXED SCROLL PANE LOGIC ---
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);

        // Wrap the scroll pane in a panel to force it to fill the center
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(UIConstants.WHITE_BG); // Ensure background matches
        wrapper.add(scrollPane, BorderLayout.CENTER);

        return wrapper;
    }

    private void addSectionHeader(String title) {
        JLabel label = new JLabel(title);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(UIConstants.GRAY_TEXT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(label);
        contentPanel.add(Box.createVerticalStrut(10));
    }

    private JPanel createToggleItem(String text, JCheckBox toggle) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.WHITE_BG);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(text);
        label.setFont(UIConstants.BODY_FONT);

        toggle.setBackground(UIConstants.WHITE_BG);
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

    private void loadSettings() {
        // Fetch from backend
        int userId = UserSession.getCurrentUserId();
        Map<String, Boolean> settings = settingsService.getSettings(userId);

        if (settings != null) {
            budgetAlertsToggle.setSelected(settings.getOrDefault("Budget Alerts", false));
            discountAlertsToggle.setSelected(settings.getOrDefault("Discount Alerts", false));
        }
    }

    private void handleToggleChange(String type, boolean isEnabled) {
        int userId = UserSession.getCurrentUserId();
        settingsService.toggleNotification(userId, type, isEnabled);
        // Optional: Show small feedback toast
    }

    private void handleDeleteAccount() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete your account? This cannot be undone.",
                "Delete Account", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = settingsService.deleteAccount(UserSession.getCurrentUserId());
            if (success) {
                JOptionPane.showMessageDialog(this, "Account deleted.");
                if (onLogoutClicked != null)
                    onLogoutClicked.run();
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting account.");
            }
        }
    }

    public void refreshData() {
        loadSettings();
        // Update Sidebar Profile
        com.spendwise.models.User currentUser = UserSession.getCurrentUser();
        if (sidebarNameLabel != null && currentUser != null) {
            sidebarNameLabel.setText(currentUser.getUserName());
            sidebarEmailLabel.setText(currentUser.geteMail());
            sidebarAvatarLabel.setText(getInitials(currentUser.getUserName()));
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
}