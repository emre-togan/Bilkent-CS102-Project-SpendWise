package com.spendwise.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.spendwise.services.SettingsService;
import com.spendwise.view.components.RoundedPanel;
import com.spendwise.view.components.SidebarPanel;

public class SettingsPanel extends JPanel {

    private MainFrame mainFrame;
    private Runnable onLogoutClicked;
    private SettingsService settingsService;
    private SidebarPanel sidebarPanel;

    // Toggle Buttons References (to update them)
    private JCheckBox budgetAlertsToggle;
    private JCheckBox discountAlertsToggle;

    public SettingsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.settingsService = new SettingsService();
        setLayout(new BorderLayout());
        setBackground(UIConstants.WHITE_BG);

        // Sidebar
        sidebarPanel = new SidebarPanel("SETTINGS", key -> mainFrame.showPanel(key), () -> {
            if (onLogoutClicked != null)
                onLogoutClicked.run();
            else
                mainFrame.logout();
        });
        add(sidebarPanel, BorderLayout.WEST);

        // Content wrapped in scroll pane
        JScrollPane scrollPane = new JScrollPane(createContent());
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        // Load UI and Settings
        loadSettings();
    }

    public void setOnLogoutClicked(Runnable callback) {
        this.onLogoutClicked = callback;
    }

    private JPanel createContent() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(250, 250, 250));
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(250, 250, 250));
        headerPanel.setMaximumSize(new Dimension(1100, 60));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Settings");
        title.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel subtitle = new JLabel("Manage your app preferences");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitle.setForeground(Color.GRAY);

        JPanel titleBlock = new JPanel(new GridLayout(2, 1));
        titleBlock.setBackground(new Color(250, 250, 250));
        titleBlock.add(title);
        titleBlock.add(subtitle);

        headerPanel.add(titleBlock, BorderLayout.WEST);
        contentPanel.add(headerPanel);
        contentPanel.add(Box.createVerticalStrut(30));

        // 1. Notifications Section
        addSectionHeader(contentPanel, "Notifications");
        RoundedPanel notificationsCard = new RoundedPanel(15, Color.WHITE);
        notificationsCard.setLayout(new BoxLayout(notificationsCard, BoxLayout.Y_AXIS));
        notificationsCard.setMaximumSize(new Dimension(1100, 150)); // Approx
        notificationsCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        notificationsCard.setBorder(new EmptyBorder(10, 0, 10, 0));

        budgetAlertsToggle = new JCheckBox();
        discountAlertsToggle = new JCheckBox(); // We'll set icons/listeners later

        notificationsCard
                .add(createToggleRow("🔔", "Budget Alerts", "Notify when exceeding budget", budgetAlertsToggle));
        notificationsCard.add(createDivider());
        notificationsCard
                .add(createToggleRow("🏷️", "Discount Alerts", "Get notified of new deals", discountAlertsToggle));

<<<<<<< HEAD
        RoundedButton logoutBtn = new RoundedButton("🚪 Logout", 15);
        logoutBtn.setBounds(15, 735, 230, 40);
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 14));
        logoutBtn.setForeground(new Color(220, 53, 69));
        logoutBtn.setBackground(Color.WHITE);
        logoutBtn.setBorder(BorderFactory.createLineBorder(new Color(220, 53, 69)));
=======
        contentPanel.add(notificationsCard);
        contentPanel.add(Box.createVerticalStrut(30));

        // 2. Account Section
        addSectionHeader(contentPanel, "Account");
        RoundedPanel accountCard = new RoundedPanel(15, Color.WHITE);
        accountCard.setLayout(new BoxLayout(accountCard, BoxLayout.Y_AXIS));
        accountCard.setMaximumSize(new Dimension(1100, 200));
        accountCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        accountCard.setBorder(new EmptyBorder(10, 0, 10, 0));

        accountCard.add(createNavigationRow("🛡️", "Personal Information",
                () -> new PersonalInfoDialog(mainFrame).setVisible(true)));
        accountCard.add(createDivider());
        accountCard.add(createNavigationRow("🔒", "Change Password",
                () -> new ChangePasswordDialog(mainFrame).setVisible(true)));
        accountCard.add(createDivider());
        accountCard.add(createNavigationRow("🛡️", "Privacy Settings",
                () -> new PrivacySettingsDialog(mainFrame).setVisible(true)));

        contentPanel.add(accountCard);
        contentPanel.add(Box.createVerticalStrut(30));

        // 3. Support Section
        addSectionHeader(contentPanel, "Support");
        RoundedPanel supportCard = new RoundedPanel(15, Color.WHITE);
        supportCard.setLayout(new BoxLayout(supportCard, BoxLayout.Y_AXIS));
        supportCard.setMaximumSize(new Dimension(1100, 200));
        supportCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        supportCard.setBorder(new EmptyBorder(10, 0, 10, 0));

        supportCard.add(createNavigationRow("❓", "Help Center",
                () -> JOptionPane.showMessageDialog(this, "Help Center Coming Soon!")));
        supportCard.add(createDivider());
        supportCard.add(createNavigationRow("📄", "Terms of Service",
                () -> JOptionPane.showMessageDialog(this, "Terms of Service...")));
        supportCard.add(createDivider());
        supportCard.add(createNavigationRow("🔐", "Privacy Policy",
                () -> JOptionPane.showMessageDialog(this, "Privacy Policy...")));

        contentPanel.add(supportCard);
        contentPanel.add(Box.createVerticalStrut(40));

        // Footer (Version & Logout)
        JPanel footer = new JPanel();
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setBackground(new Color(250, 250, 250));
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel versionLabel = new JLabel("SpendWise");
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        versionLabel.setForeground(Color.GRAY);
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel vNumLabel = new JLabel("Version 1.0.0");
        vNumLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        vNumLabel.setForeground(Color.GRAY);
        vNumLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton logoutBtn = new JButton("↪ Logout"); // Using text arrow for now
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 12));
        logoutBtn.setForeground(new Color(220, 53, 69)); // Red
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
>>>>>>> origin/main
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.addActionListener(e -> {
            if (onLogoutClicked != null)
                onLogoutClicked.run();
        });

        footer.add(versionLabel);
        footer.add(vNumLabel);
        footer.add(Box.createVerticalStrut(10));
        footer.add(logoutBtn);

        contentPanel.add(footer);

        // Fill remaining space
        contentPanel.add(Box.createVerticalGlue());

        return contentPanel;
    }

<<<<<<< HEAD
    private void addMenuButton(JPanel panel, String icon, String text, int y, boolean selected) {
        RoundedButton button = new RoundedButton(icon + "  " + text, 15);
        button.setBounds(10, y, 240, 50);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(new EmptyBorder(0, 15, 0, 0));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (selected) {
            button.setBackground(UIConstants.PRIMARY_GREEN);
            button.setForeground(Color.WHITE);
            button.setOpaque(true);
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(new Color(80, 80, 80));
        }

        button.addActionListener(e -> mainFrame.showPanel(text.toUpperCase()));
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
        RoundedButton deleteAccBtn = new RoundedButton("Delete Account", 15);
        deleteAccBtn.setForeground(Color.RED);
        deleteAccBtn.setBackground(Color.WHITE);
        deleteAccBtn.setBorder(BorderFactory.createLineBorder(Color.RED));
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
=======
    private void addSectionHeader(JPanel parent, String title) {
>>>>>>> origin/main
        JLabel label = new JLabel(title);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(80, 80, 80));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(label);
        parent.add(Box.createVerticalStrut(10));
    }

    private JPanel createToggleRow(String icon, String title, String subtitle, JCheckBox toggle) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(1100, 60));
        row.setPreferredSize(new Dimension(1100, 60));
        row.setBorder(new EmptyBorder(0, 20, 0, 20));

        // Use BoxLayout for better vertical centering
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.X_AXIS));
        left.setBackground(Color.WHITE);

        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        // Fix icon width for alignment
        iconLabel.setPreferredSize(new Dimension(45, 45));
        iconLabel.setMaximumSize(new Dimension(45, 45));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(Color.WHITE);
        textPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel t = new JLabel(title);
        t.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel s = new JLabel(subtitle);
        s.setFont(new Font("Arial", Font.PLAIN, 12));
        s.setForeground(Color.GRAY);
        textPanel.add(t);
        textPanel.add(s);

        left.add(iconLabel);
        left.add(Box.createHorizontalStrut(10));
        left.add(textPanel);

        // Right side
        toggle.setBackground(Color.WHITE);
        toggle.addActionListener(e -> handleToggleChange(title, toggle.isSelected()));

        row.add(left, BorderLayout.WEST);
        row.add(toggle, BorderLayout.EAST);

        return row;
    }

    private JPanel createNavigationRow(String icon, String text, Runnable action) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(1100, 50));
        row.setPreferredSize(new Dimension(1100, 50));
        row.setBorder(new EmptyBorder(0, 20, 0, 20));
        row.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.X_AXIS));
        left.setBackground(Color.WHITE);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        iconLabel.setPreferredSize(new Dimension(40, 40));
        iconLabel.setMaximumSize(new Dimension(40, 40));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Arial", Font.BOLD, 14));
        textLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        left.add(iconLabel);
        left.add(Box.createHorizontalStrut(10));
        left.add(textLabel);

        JLabel chevron = new JLabel("›");
        chevron.setFont(new Font("Arial", Font.PLAIN, 18));
        chevron.setForeground(Color.LIGHT_GRAY);

        row.add(left, BorderLayout.WEST);
        row.add(chevron, BorderLayout.EAST);

        row.addMouseListener(new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (action != null)
                    action.run();
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
        JPanel div = new JPanel(new BorderLayout());
        div.setBackground(Color.WHITE);
        div.setMaximumSize(new Dimension(1100, 1));
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(240, 240, 240));
        sep.setBackground(new Color(240, 240, 240));
        div.add(sep);
        div.setBorder(new EmptyBorder(0, 20, 0, 20));
        return div;
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

    public void refreshData() {
        loadSettings();
        // Update Sidebar Profile
        if (sidebarPanel != null) {
            sidebarPanel.updateUser();
        }
    }
}