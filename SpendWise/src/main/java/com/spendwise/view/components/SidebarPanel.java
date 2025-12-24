package com.spendwise.view.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.spendwise.models.User;
import com.spendwise.view.UIConstants;
import com.spendwise.view.UserSession;

public class SidebarPanel extends JPanel {

    private String activePanelKey;
    private Consumer<String> onNavigate;
    private Runnable onLogout;
    private Map<String, JButton> menuButtons;

    // Profile Labels
    private JLabel sidebarAvatarLabel;
    private JLabel sidebarNameLabel;
    private JLabel sidebarEmailLabel;

    public SidebarPanel(String activePanelKey, Consumer<String> onNavigate, Runnable onLogout) {
        this.activePanelKey = activePanelKey;
        this.onNavigate = onNavigate;
        this.onLogout = onLogout;
        this.menuButtons = new HashMap<>();

        setPreferredSize(new Dimension(260, 800)); // Default height, will stretch
        setBackground(UIConstants.SIDEBAR_BG);
        setLayout(null);
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(240, 240, 240)));

        initializeUI();
    }

    private void initializeUI() {
        // --- Logo ---
        JLabel logo = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/Resim1.png"));
            Image image = logoIcon.getImage();
            Image newimg = image.getScaledInstance(45, 45, java.awt.Image.SCALE_SMOOTH);
            logo.setIcon(new ImageIcon(newimg));
        } catch (Exception e) {
            logo.setText("W₺");
        }
        logo.setBounds(25, 25, 45, 45);
        logo.setFont(new Font("Arial", Font.BOLD, 40));
        logo.setForeground(UIConstants.PRIMARY_GREEN);
        add(logo);

        JLabel appName = new JLabel("Finance Assistant"); // As per image
        appName.setBounds(85, 38, 150, 20);
        appName.setFont(new Font("Arial", Font.BOLD, 14));
        appName.setForeground(new Color(80, 80, 80));
        add(appName);

        // --- Menu Items ---
        int startY = 110;
        int gap = 55;

        addMenuButton(Icons.getDashboardIcon(20, Color.GRAY), "Dashboard", "DASHBOARD", startY);
        addMenuButton(Icons.getBudgetIcon(20, Color.GRAY), "Budget", "BUDGET", startY + gap);
        addMenuButton(Icons.getExpensesIcon(20, Color.GRAY), "Expenses", "EXPENSES", startY + gap * 2);
        addMenuButton(Icons.getShopIcon(20, Color.GRAY), "Shop", "SHOP", startY + gap * 3);
        addMenuButton(Icons.getChatIcon(20, Color.GRAY), "Chat with Friends", "CHAT", startY + gap * 4);
        addMenuButton(Icons.getProfileIcon(20, Color.GRAY), "Profile", "PROFILE", startY + gap * 5);
        addMenuButton(Icons.getSettingsIcon(20, Color.GRAY), "Settings", "SETTINGS", startY + gap * 6);

        JPanel profileCard = createProfileCard();

        profileCard.setBounds(20, 780, 220, 60);

        profileCard.setBounds(20, 700, 220, 70);
        add(profileCard);

        JLabel logoutLabel = new JLabel("Logout");
        logoutLabel.setIcon(new ImageIcon("")); 


        JButton logoutBtn = new JButton("🛑 Logout");
        logoutBtn.setBounds(20, 790, 100, 30);
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 13));
        logoutBtn.setForeground(UIConstants.DANGER_RED);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setHorizontalAlignment(SwingConstants.LEFT);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            if (onLogout != null)
                onLogout.run();
        });
        add(logoutBtn);
    }

    private void addMenuButton(Icon icon, String text, String key, int y) {
        // Design: Rounded filled rectangle when active, otherwise text with icon
        RoundedButton btn = new RoundedButton(text, 20,
                key.equals(activePanelKey) ? UIConstants.SELECTION_GREEN : UIConstants.SIDEBAR_BG,
                key.equals(activePanelKey) ? UIConstants.SELECTION_GREEN : new Color(245, 245, 245));

        btn.setIcon(icon);
        btn.setBounds(20, y, 220, 45);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0, 15, 0, 0)); // Padding left

        if (key.equals(activePanelKey)) {
            btn.setForeground(Color.WHITE);

            if (key.equals("DASHBOARD"))
                btn.setIcon(Icons.getDashboardIcon(20, Color.WHITE));
            if (key.equals("BUDGET"))
                btn.setIcon(Icons.getBudgetIcon(20, Color.WHITE));
            if (key.equals("EXPENSES"))
                btn.setIcon(Icons.getExpensesIcon(20, Color.WHITE));
            if (key.equals("SHOP"))
                btn.setIcon(Icons.getShopIcon(20, Color.WHITE));
            if (key.equals("CHAT"))
                btn.setIcon(Icons.getChatIcon(20, Color.WHITE));
            if (key.equals("PROFILE"))
                btn.setIcon(Icons.getProfileIcon(20, Color.WHITE));
            if (key.equals("SETTINGS"))
                btn.setIcon(Icons.getSettingsIcon(20, Color.WHITE));

        } else {
            btn.setForeground(new Color(100, 100, 100));
        }

        btn.addActionListener(e -> {
            if (onNavigate != null) {
                onNavigate.accept(key);
            }
        });

        add(btn);
        menuButtons.put(key, btn);
    }

    private JPanel createProfileCard() {

        RoundedPanel card = new RoundedPanel(20, new Color(248, 249, 250));

        card.setLayout(null);

        User u = UserSession.getCurrentUser();
        String name = (u != null) ? u.getUserName() : "Guest";
        String email = (u != null) ? u.geteMail() : "desc";

        // Avatar
        sidebarAvatarLabel = new JLabel(getInitials(name));
        sidebarAvatarLabel.setBounds(10, 10, 40, 40);
        sidebarAvatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebarAvatarLabel.setOpaque(true);
        sidebarAvatarLabel.setBackground(UIConstants.SELECTION_GREEN); 
        sidebarAvatarLabel.setForeground(Color.WHITE);
        sidebarAvatarLabel.setFont(new Font("Arial", Font.BOLD, 14));

        card.add(sidebarAvatarLabel);

        sidebarNameLabel = new JLabel(name);
        sidebarNameLabel.setBounds(60, 12, 150, 18);
        sidebarNameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        sidebarNameLabel.setForeground(UIConstants.TEXT_DARK);
        card.add(sidebarNameLabel);

        sidebarEmailLabel = new JLabel(email);
        sidebarEmailLabel.setBounds(60, 32, 150, 14);
        sidebarEmailLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        sidebarEmailLabel.setForeground(Color.GRAY);
        card.add(sidebarEmailLabel);

        return card;
    }

    private String getInitials(String name) {
        if (name == null || name.isEmpty())
            return "??";
        return name.substring(0, Math.min(2, name.length())).toUpperCase();
    }

    public void updateUser() {
        User u = UserSession.getCurrentUser();
        if (u != null) {
            sidebarNameLabel.setText(u.getUserName());
            sidebarEmailLabel.setText(u.geteMail()); // Or title
            sidebarAvatarLabel.setText(getInitials(u.getUserName()));
        }
    }
}
