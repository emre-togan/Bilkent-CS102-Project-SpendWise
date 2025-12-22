import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class BudgetPanel extends JPanel {
    
    private MainFrame mainFrame;
    
    public BudgetPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(250, 250, 250));
        
        add(createSideMenu(), BorderLayout.WEST);
        add(createContent(), BorderLayout.CENTER);
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
        addMenuItem(sideMenu, "💰", "Budget", startY + 60, true);
        addMenuItem(sideMenu, "📋", "Expenses", startY + 120, false);
        addMenuItem(sideMenu, "🛒", "Shop", startY + 180, false);
        addMenuItem(sideMenu, "💬", "Chat", startY + 240, false);
        addMenuItem(sideMenu, "👤", "Profile", startY + 300, false);
        addMenuItem(sideMenu, "⚙️", "Settings", startY + 360, false);
        
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
        
        btn.addActionListener(e -> mainFrame.showPanel(text.toUpperCase()));
        parent.add(btn);
    }
    
    private JPanel createContent() {
        JPanel content = new JPanel();
        content.setLayout(null);
        content.setBackground(new Color(250, 250, 250));
        
        JLabel header = new JLabel("Budget Management");
        header.setBounds(30, 30, 400, 40);
        header.setFont(new Font("Arial", Font.BOLD, 28));
        content.add(header);
        
        JLabel subHeader = new JLabel("Manage your monthly spending limits");
        subHeader.setBounds(30, 75, 400, 20);
        subHeader.setFont(new Font("Arial", Font.PLAIN, 14));
        subHeader.setForeground(new Color(120, 120, 120));
        content.add(subHeader);
        
        createMonthlyLimitCard(content);
        
        createAlertBanner(content);
        
        createCategorySection(content);
        
        return content;
    }
    
    private void createMonthlyLimitCard(JPanel parent) {
        JPanel card = new JPanel();
        card.setBounds(30, 130, 1070, 100);
        card.setBackground(Color.WHITE);
        card.setLayout(null);
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        
        JLabel limitLabel = new JLabel("Monthly Limit");
        limitLabel.setBounds(25, 15, 200, 25);
        limitLabel.setFont(new Font("Arial", Font.BOLD, 16));
        card.add(limitLabel);
        
        JLabel limitAmount = new JLabel("$3,000");
        limitAmount.setBounds(25, 40, 200, 30);
        limitAmount.setFont(new Font("Arial", Font.BOLD, 24));
        card.add(limitAmount);
        
        JButton editBtn = new JButton("✏");
        editBtn.setBounds(1010, 30, 40, 40);
        editBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        editBtn.setFocusPainted(false);
        editBtn.setBorderPainted(false);
        editBtn.setContentAreaFilled(false);
        editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.add(editBtn);
        
        JLabel totalSpent = new JLabel("Total Spent");
        totalSpent.setBounds(400, 15, 200, 20);
        totalSpent.setFont(new Font("Arial", Font.PLAIN, 13));
        totalSpent.setForeground(new Color(120, 120, 120));
        card.add(totalSpent);
        
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setBounds(400, 45, 640, 10);
        progressBar.setValue(67);
        progressBar.setForeground(new Color(50, 50, 50));
        progressBar.setBackground(new Color(230, 230, 230));
        progressBar.setBorderPainted(false);
        card.add(progressBar);
        
        JLabel remaining = new JLabel("💸 Remaining: $990");
        remaining.setBounds(400, 65, 200, 20);
        remaining.setFont(new Font("Arial", Font.PLAIN, 13));
        remaining.setForeground(UIConstants.PRIMARY_GREEN);
        card.add(remaining);
        
        JLabel spentAmount = new JLabel("$2,010");
        spentAmount.setBounds(900, 15, 140, 25);
        spentAmount.setFont(new Font("Arial", Font.BOLD, 18));
        spentAmount.setForeground(new Color(220, 53, 69));
        spentAmount.setHorizontalAlignment(SwingConstants.RIGHT);
        card.add(spentAmount);
        
        parent.add(card);
    }
    
    private void createAlertBanner(JPanel parent) {
        JPanel alert = new JPanel();
        alert.setBounds(30, 250, 1070, 70);
        alert.setBackground(new Color(255, 244, 229));
        alert.setLayout(null);
        alert.setBorder(BorderFactory.createLineBorder(new Color(255, 193, 7)));
        
        JLabel icon = new JLabel("⚠");
        icon.setBounds(25, 20, 30, 30);
        icon.setFont(new Font("Arial", Font.PLAIN, 28));
        alert.add(icon);
        
        JLabel title = new JLabel("Budget Alert");
        title.setBounds(65, 15, 200, 20);
        title.setFont(new Font("Arial", Font.BOLD, 15));
        title.setForeground(new Color(180, 80, 0));
        alert.add(title);
        
        JLabel message = new JLabel("You've exceeded your Shopping budget");
        message.setBounds(65, 38, 400, 18);
        message.setFont(new Font("Arial", Font.PLAIN, 13));
        message.setForeground(new Color(100, 50, 0));
        alert.add(message);
        
        parent.add(alert);
    }
    
    private void createCategorySection(JPanel parent) {
        JLabel sectionTitle = new JLabel("Category Breakdown");
        sectionTitle.setBounds(30, 340, 300, 30);
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 18));
        parent.add(sectionTitle);
        
        JButton addCategoryBtn = new JButton("➕ Add Category");
        addCategoryBtn.setBounds(920, 340, 180, 35);
        addCategoryBtn.setFont(new Font("Arial", Font.BOLD, 14));
        addCategoryBtn.setBackground(UIConstants.PRIMARY_GREEN);
        addCategoryBtn.setForeground(Color.WHITE);
        addCategoryBtn.setFocusPainted(false);
        addCategoryBtn.setBorderPainted(false);
        addCategoryBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(addCategoryBtn);
        
        // Categories
        String[][] categories = {
            {"🍔", "Food", "720", "800", "90", "green"},
            {"🚗", "Transport", "340", "400", "85", "blue"},
            {"🛍", "Shopping", "950", "800", "119", "red"},
            {"🎬", "Entertainment", "0", "300", "0", "gray"},
            {"🏥", "Health", "0", "200", "0", "gray"},
            {"🔧", "Others", "0", "500", "0", "gray"}
        };
        
        int y = 390;
        for (String[] cat : categories) {
            JPanel categoryCard = createCategoryCard(cat);
            categoryCard.setBounds(30, y, 1070, 90);
            parent.add(categoryCard);
            y += 100;
        }
    }
    
    private JPanel createCategoryCard(String[] data) {
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        
        JLabel emoji = new JLabel(data[0]);
        emoji.setBounds(25, 25, 40, 40);
        emoji.setFont(new Font("Arial", Font.PLAIN, 32));
        card.add(emoji);
        
        JLabel name = new JLabel(data[1]);
        name.setBounds(80, 20, 200, 25);
        name.setFont(new Font("Arial", Font.BOLD, 16));
        card.add(name);
        
        JLabel amounts = new JLabel("$" + data[2] + " / $" + data[3]);
        amounts.setBounds(80, 47, 200, 20);
        amounts.setFont(new Font("Arial", Font.PLAIN, 13));
        amounts.setForeground(new Color(120, 120, 120));
        card.add(amounts);
        
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setBounds(300, 38, 600, 15);
        bar.setValue(Integer.parseInt(data[4]));
        bar.setBorderPainted(false);
        
        int pct = Integer.parseInt(data[4]);
        if (pct == 0) bar.setForeground(new Color(200, 200, 200));
        else if (pct < 80) bar.setForeground(UIConstants.PRIMARY_GREEN);
        else if (pct < 100) bar.setForeground(new Color(33, 150, 243));
        else bar.setForeground(new Color(220, 53, 69));
        
        bar.setBackground(new Color(240, 240, 240));
        card.add(bar);
        
        JLabel percentage = new JLabel(data[4] + "%");
        percentage.setBounds(920, 32, 60, 25);
        percentage.setFont(new Font("Arial", Font.BOLD, 15));
        card.add(percentage);
        
        JButton editBtn = new JButton("✏");
        editBtn.setBounds(990, 25, 35, 35);
        editBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        editBtn.setFocusPainted(false);
        editBtn.setBorderPainted(false);
        editBtn.setContentAreaFilled(false);
        card.add(editBtn);
        
        JButton deleteBtn = new JButton("🗑");
        deleteBtn.setBounds(1030, 25, 35, 35);
        deleteBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        deleteBtn.setForeground(new Color(220, 53, 69));
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorderPainted(false);
        deleteBtn.setContentAreaFilled(false);
        card.add(deleteBtn);
        
        return card;
    }
    
    public void refreshData() {}
    public void clearData() {}
}
