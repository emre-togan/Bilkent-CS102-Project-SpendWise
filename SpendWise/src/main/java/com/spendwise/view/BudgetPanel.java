<<<<<<< HEAD
package com.spendwise.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

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

        RoundedButton editBtn = new RoundedButton("✏", 10);
        editBtn.setBounds(1010, 30, 40, 40);
        editBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        editBtn.setBackground(new Color(245, 245, 245));
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

        RoundedButton addCategoryBtn = new RoundedButton("➕ Add Category", 15);
        addCategoryBtn.setBounds(920, 340, 180, 35);
        addCategoryBtn.setFont(new Font("Arial", Font.BOLD, 14));
        addCategoryBtn.setBackground(UIConstants.PRIMARY_GREEN);
        addCategoryBtn.setForeground(Color.WHITE);
        addCategoryBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(addCategoryBtn);

        // Categories
        String[][] categories = {
                { "🍔", "Food", "720", "800", "90", "green" },
                { "🚗", "Transport", "340", "400", "85", "blue" },
                { "🛍", "Shopping", "950", "800", "119", "red" },
                { "🎬", "Entertainment", "0", "300", "0", "gray" },
                { "🏥", "Health", "0", "200", "0", "gray" },
                { "🔧", "Others", "0", "500", "0", "gray" }
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
        if (pct == 0)
            bar.setForeground(new Color(200, 200, 200));
        else if (pct < 80)
            bar.setForeground(UIConstants.PRIMARY_GREEN);
        else if (pct < 100)
            bar.setForeground(new Color(33, 150, 243));
        else
            bar.setForeground(new Color(220, 53, 69));

        bar.setBackground(new Color(240, 240, 240));
        card.add(bar);

        JLabel percentage = new JLabel(data[4] + "%");
        percentage.setBounds(920, 32, 60, 25);
        percentage.setFont(new Font("Arial", Font.BOLD, 15));
        card.add(percentage);

        RoundedButton editBtn = new RoundedButton("✏", 10);
        editBtn.setBounds(990, 25, 35, 35);
        editBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        editBtn.setBackground(new Color(245, 245, 245));
        card.add(editBtn);

        RoundedButton deleteBtn = new RoundedButton("🗑", 10);
        deleteBtn.setBounds(1030, 25, 35, 35);
        deleteBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        deleteBtn.setForeground(new Color(220, 53, 69));
        deleteBtn.setBackground(Color.WHITE);
        card.add(deleteBtn);

        return card;
    }

    public void refreshData() {
    }

    public void clearData() {
    }
}
=======
package com.spendwise.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.spendwise.models.Budget;
import com.spendwise.models.Expense;
import com.spendwise.models.User;
import com.spendwise.services.BudgetService;
import com.spendwise.services.expenseService;
import com.spendwise.view.components.RoundedButton;
import com.spendwise.view.components.RoundedPanel;
import com.spendwise.view.components.SidebarPanel;

public class BudgetPanel extends JPanel {

    private MainFrame mainFrame;
    private User currentUser;
    private Budget currentBudget;
    private List<Expense> currentExpenses;

    // Services
    private BudgetService budgetService;
    private expenseService expenseServiceInstance;

    // UI Components
    private SidebarPanel sidebarPanel;
    private JLabel monthlyLimitLabel;
    private JLabel totalSpentLabel;
    private JLabel remainingLabel;
    private JProgressBar totalSpentProgressBar;
    private RoundedPanel alertPanel;
    private JLabel alertMessageLabel;
    private JPanel categoriesContainer;

    // Data
    private double monthlyLimit;
    private double totalSpent;
    private double remaining;
    private Map<String, CategoryData> categorySpending;

    // Category colors
    private static final Color[] CATEGORY_COLORS = {
            new Color(139, 195, 74), // Green (matching theme preferably)
            new Color(33, 150, 243), // Blue
            new Color(255, 152, 0), // Orange
            new Color(156, 39, 176), // Purple
            new Color(233, 30, 99), // Pink
            new Color(0, 150, 136) // Teal
    };

    public BudgetPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.budgetService = new BudgetService();
        this.expenseServiceInstance = new expenseService();
        this.categorySpending = new HashMap<>();
        this.currentExpenses = new ArrayList<>();

        setLayout(new BorderLayout());
        setBackground(UIConstants.BACKGROUND_LIGHT);

        // Sidebar
        sidebarPanel = new SidebarPanel("BUDGET",
                panelName -> mainFrame.showPanel(panelName),
                () -> mainFrame.logout());
        add(sidebarPanel, BorderLayout.WEST);

        // Content
        add(createContent(), BorderLayout.CENTER);

        // Load initial data
        refreshData();
    }

    private JPanel createContent() {
        JPanel content = new JPanel();
        content.setLayout(null);
        content.setBackground(UIConstants.BACKGROUND_LIGHT);

        JLabel header = new JLabel("Budget Management");
        header.setBounds(30, 30, 400, 35);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        content.add(header);

        JLabel subHeader = new JLabel("Manage your monthly spending limits");
        subHeader.setBounds(30, 65, 400, 20);
        subHeader.setFont(new Font("Arial", Font.PLAIN, 14));
        subHeader.setForeground(Color.GRAY);
        content.add(subHeader);

        createMonthlyLimitCard(content);
        createAlertBanner(content);
        createCategorySection(content);

        return content;
    }

    private void createMonthlyLimitCard(JPanel parent) {
        RoundedPanel card = new RoundedPanel(20, Color.WHITE);
        card.setBounds(30, 110, 1150, 120);
        card.setLayout(null);

        JLabel limitLabel = new JLabel("Monthly Limit");
        limitLabel.setBounds(30, 20, 200, 25);
        limitLabel.setFont(new Font("Arial", Font.BOLD, 14));
        limitLabel.setForeground(Color.GRAY);
        card.add(limitLabel);

        monthlyLimitLabel = new JLabel("₺0");
        monthlyLimitLabel.setBounds(30, 45, 200, 30);
        monthlyLimitLabel.setFont(new Font("Arial", Font.BOLD, 28));
        card.add(monthlyLimitLabel);

        JButton editBtn = new JButton("✏"); // Simple text icon for now, or use image
        // To style it better
        editBtn.setBounds(1100, 20, 30, 30);
        editBtn.setFont(new Font("SansSerif", Font.PLAIN, 18));
        editBtn.setForeground(UIConstants.PRIMARY_GREEN);
        editBtn.setBorderPainted(false);
        editBtn.setContentAreaFilled(false);
        editBtn.setFocusPainted(false);
        editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editBtn.addActionListener(e -> showEditBudgetDialog());
        card.add(editBtn);

        // Right side: Progress + Spent + Remaining
        // Design: "Total Spent" text above bar? Or just bar.
        // Image shows: "Total Spent" label left of bar? No, image has "Total Spent"
        // title above bar on right side maybe.
        // Let's follow image: "Total Spent ------------------- $2,010"

        JLabel totalSpentTitle = new JLabel("Total Spent");
        totalSpentTitle.setBounds(30, 85, 100, 20); // Actually looking at image, it's laid out differently.
        // Image: Left side: Monthly Limit $3000.
        // Right side (or below): Total Spent bar.

        // Let's do:
        // Top Left: Monthly Limit $3.000
        // Below that: Total Spent title ... Bar ... Amount

        JLabel spentTitle = new JLabel("Total Spent");
        spentTitle.setBounds(270, 20, 100, 20);
        spentTitle.setForeground(Color.GRAY);
        card.add(spentTitle);

        totalSpentLabel = new JLabel("$2.010");
        totalSpentLabel.setBounds(1050, 45, 80, 20);
        totalSpentLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalSpentLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        card.add(totalSpentLabel);

        totalSpentProgressBar = new JProgressBar(0, 100);
        totalSpentProgressBar.setBounds(270, 50, 770, 10);
        totalSpentProgressBar.setValue(0);
        totalSpentProgressBar.setForeground(new Color(50, 50, 50)); // Black/Dark Grey in image
        totalSpentProgressBar.setBackground(new Color(240, 240, 240));
        totalSpentProgressBar.setBorderPainted(false);
        card.add(totalSpentProgressBar);

        remainingLabel = new JLabel("💸 Remaining: ₺0");
        remainingLabel.setBounds(270, 70, 200, 20);
        remainingLabel.setFont(new Font("Arial", Font.BOLD, 13));
        remainingLabel.setForeground(UIConstants.SELECTION_GREEN);
        card.add(remainingLabel);

        parent.add(card);
    }

    private void createAlertBanner(JPanel parent) {
        // Light Orange background
        alertPanel = new RoundedPanel(20, new Color(255, 248, 225));
        alertPanel.setBounds(30, 250, 1150, 80);
        alertPanel.setLayout(null);
        alertPanel.setVisible(false);

        JLabel icon = new JLabel("ⓘ"); // Simple info icon
        icon.setBounds(25, 25, 30, 30);
        icon.setFont(new Font("SansSerif", Font.BOLD, 24));
        icon.setForeground(new Color(255, 143, 0));
        alertPanel.add(icon);

        JLabel title = new JLabel("Budget Alert");
        title.setBounds(65, 20, 200, 20);
        title.setFont(new Font("Arial", Font.BOLD, 15));
        title.setForeground(new Color(60, 60, 60));
        alertPanel.add(title);

        alertMessageLabel = new JLabel("You've exceeded your budget");
        alertMessageLabel.setBounds(65, 42, 500, 18);
        alertMessageLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        alertMessageLabel.setForeground(Color.GRAY);
        alertPanel.add(alertMessageLabel);

        parent.add(alertPanel);
    }

    private void createCategorySection(JPanel parent) {
        JLabel sectionTitle = new JLabel("Category Breakdown");
        sectionTitle.setBounds(30, 350, 300, 30);
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 18));
        parent.add(sectionTitle);

        RoundedButton addCategoryBtn = new RoundedButton("+ Add Category", 20, UIConstants.SELECTION_GREEN,
                UIConstants.darker(UIConstants.SELECTION_GREEN));
        addCategoryBtn.setBounds(1000, 350, 180, 35);
        addCategoryBtn.setForeground(Color.WHITE);
        addCategoryBtn.setFont(new Font("Arial", Font.BOLD, 13));
        addCategoryBtn.addActionListener(e -> showAddCategoryDialog());
        parent.add(addCategoryBtn);

        // Categories container with scroll
        categoriesContainer = new JPanel();
        categoriesContainer.setLayout(new BoxLayout(categoriesContainer, BoxLayout.Y_AXIS));
        categoriesContainer.setBackground(UIConstants.BACKGROUND_LIGHT);

        JScrollPane scrollPane = new JScrollPane(categoriesContainer);
        scrollPane.setBounds(30, 400, 1150, 380);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(UIConstants.BACKGROUND_LIGHT);
        parent.add(scrollPane);
    }

    // ... Dialogs and logic mostly unchanged but ensured using correct styling ...

    private void showEditBudgetDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Monthly Budget", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);

        formPanel.add(new JLabel("Monthly Limit:"));
        JTextField limitField = new JTextField(String.valueOf(monthlyLimit));
        formPanel.add(limitField);

        JButton saveBtn = new JButton("Save Budget");
        saveBtn.setBackground(UIConstants.PRIMARY_GREEN);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> {
            try {
                double newLimit = Double.parseDouble(limitField.getText());
                if (newLimit <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Budget must be greater than 0!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int userId = UserSession.getCurrentUserId();
                if (currentBudget == null) {
                    YearMonth currentMonth = YearMonth.now();
                    Budget newBudget = new Budget(userId, newLimit, Date.valueOf(currentMonth.atDay(1)),
                            Date.valueOf(currentMonth.atEndOfMonth()));
                    if (budgetService.createBudget(newBudget))
                        JOptionPane.showMessageDialog(dialog, "Budget created successfully!");
                } else {
                    currentBudget.setLimit(newLimit);
                    if (budgetService.updateBudgetList(currentBudget))
                        JOptionPane.showMessageDialog(dialog, "Budget updated successfully!");
                }
                dialog.dispose();
                refreshData();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid number!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(saveBtn);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showAddCategoryDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Category Budget", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);

        formPanel.add(new JLabel("Category:"));
        String[] categories = { "Food", "Transport", "Shopping", "Health", "Entertainment", "Other" };
        JComboBox<String> categoryBox = new JComboBox<>(categories);
        formPanel.add(categoryBox);

        formPanel.add(new JLabel("Budget Amount:"));
        JTextField amountField = new JTextField();
        formPanel.add(amountField);

        JButton saveBtn = new JButton("Add Category");
        saveBtn.setBackground(UIConstants.PRIMARY_GREEN);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> {
            try {
                String category = (String) categoryBox.getSelectedItem();
                double amount = Double.parseDouble(amountField.getText());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (categorySpending.containsKey(category)) {
                    JOptionPane.showMessageDialog(dialog, "Category exists", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                categorySpending.put(category, new CategoryData(category, amount, 0));
                JOptionPane.showMessageDialog(dialog, "Category added!");
                dialog.dispose();
                refreshData();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid number", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(saveBtn);
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void updateCategories() {
        categoriesContainer.removeAll();
        if (categorySpending.isEmpty()) {
            JLabel emptyLabel = new JLabel("No categories yet. Add one to start tracking!");
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            categoriesContainer.add(Box.createVerticalStrut(50));
            categoriesContainer.add(emptyLabel);
        } else {
            int colorIndex = 0;
            for (Map.Entry<String, CategoryData> entry : categorySpending.entrySet()) {
                CategoryData data = entry.getValue();
                Color categoryColor = CATEGORY_COLORS[colorIndex % CATEGORY_COLORS.length];
                JPanel categoryCard = createCategoryCard(data, categoryColor);
                categoryCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
                categoriesContainer.add(categoryCard);
                categoriesContainer.add(Box.createVerticalStrut(15));
                colorIndex++;
            }
        }
        categoriesContainer.revalidate();
        categoriesContainer.repaint();
    }

    private JPanel createCategoryCard(CategoryData data, Color barColor) {
        RoundedPanel card = new RoundedPanel(20, Color.WHITE);
        card.setLayout(null);
        card.setPreferredSize(new Dimension(1120, 90));

        String emoji = getCategoryEmoji(data.category);
        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setBounds(25, 25, 40, 40);
        emojiLabel.setFont(new Font("Arial", Font.PLAIN, 32));
        card.add(emojiLabel);

        JLabel name = new JLabel(data.category);
        name.setBounds(80, 20, 200, 25);
        name.setFont(new Font("Arial", Font.BOLD, 16));
        card.add(name);

        JLabel amounts = new JLabel(String.format("₺%.0f / ₺%.0f", data.spent, data.budget));
        amounts.setBounds(80, 47, 200, 20);
        amounts.setFont(new Font("Arial", Font.PLAIN, 13));
        amounts.setForeground(Color.GRAY);
        card.add(amounts);

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setBounds(300, 38, 700, 10);
        int percentage = (int) ((data.spent / data.budget) * 100);
        bar.setValue(Math.min(100, percentage));
        bar.setBorderPainted(false);
        bar.setForeground(barColor);
        bar.setBackground(new Color(245, 245, 245));
        card.add(bar);

        JLabel percentageLabel = new JLabel(percentage + "%");
        percentageLabel.setBounds(1020, 32, 60, 25);
        percentageLabel.setFont(new Font("Arial", Font.BOLD, 14));
        percentageLabel.setForeground(Color.GRAY);
        card.add(percentageLabel);

        // Edit/Delete
        // Using simple small buttons
        JButton editBtn = new JButton("✎");
        editBtn.setBounds(1080, 25, 30, 30);
        editBtn.setBorderPainted(false);
        editBtn.setContentAreaFilled(false);
        editBtn.setForeground(Color.GRAY);
        editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editBtn.addActionListener(e -> showEditCategoryDialog(data));
        card.add(editBtn);

        return card;
    }

    private void showEditCategoryDialog(CategoryData data) {
        // ... Similar to showAddCategoryDialog logic but editing ...
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit " + data.category, true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);

        formPanel.add(new JLabel("Budget Amount:"));
        JTextField amountField = new JTextField(String.valueOf(data.budget));
        formPanel.add(amountField);

        JButton saveBtn = new JButton("Save Changes");
        saveBtn.setBackground(UIConstants.PRIMARY_GREEN);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> {
            try {
                double val = Double.parseDouble(amountField.getText());
                if (val > 0) {
                    data.budget = val;
                    dialog.dispose();
                    refreshData();
                }
            } catch (Exception ex) {
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(saveBtn, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private String getCategoryEmoji(String category) {
        if (category == null)
            return "📋";
        switch (category) {
            case "Food":
                return "🍔";
            case "Transport":
                return "🚗";
            case "Shopping":
                return "🛍";
            case "Health":
                return "🏥";
            case "Entertainment":
                return "🎬";
            default:
                return "📋";
        }
    }

    public void refreshData() {
        try {
            sidebarPanel.updateUser();
            currentUser = UserSession.getCurrentUser();
            int userId = currentUser.getId();

            currentBudget = budgetService.getSpesificUserBudget(userId);

            if (currentBudget != null) {
                monthlyLimit = currentBudget.getBudgetLimit();
                totalSpent = currentBudget.getTotalSpending();
                remaining = monthlyLimit - totalSpent;

                monthlyLimitLabel.setText(String.format("$%.0f", monthlyLimit));
                totalSpentLabel.setText(String.format("$%.0f", totalSpent));
                remainingLabel.setText(String.format("Remaining: $%.0f", remaining));

                int percentage = (int) budgetService.calculateTheSpendingPercentage(currentBudget);
                totalSpentProgressBar.setValue(Math.min(100, percentage));
                if (percentage >= 100)
                    totalSpentProgressBar.setForeground(new Color(50, 50, 50)); // Keep it dark grey/black
                else
                    totalSpentProgressBar.setForeground(new Color(50, 50, 50));
            } else {
                monthlyLimitLabel.setText("No budget set");
                totalSpentLabel.setText("$0");
                remainingLabel.setText("Remaining: $0");
                totalSpentProgressBar.setValue(0);
            }

            // Categories
            currentExpenses = expenseServiceInstance.getExpensesOfTheUser(userId);
            if (currentExpenses == null)
                currentExpenses = new ArrayList<>();
            calculateCategorySpending();
            updateCategories();
            checkBudgetAlerts();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculateCategorySpending() {
        if (categorySpending.isEmpty()) {
            categorySpending.put("Food", new CategoryData("Food", 800, 0));
            categorySpending.put("Transport", new CategoryData("Transport", 400, 0));
            categorySpending.put("Shopping", new CategoryData("Shopping", 800, 0));
            categorySpending.put("Health", new CategoryData("Health", 200, 0));
            categorySpending.put("Entertainment", new CategoryData("Entertainment", 300, 0));
        }

        for (CategoryData data : categorySpending.values())
            data.spent = 0.0;
        for (Expense expense : currentExpenses) {
            String cat = expense.getCategory();
            if (cat != null && categorySpending.containsKey(cat)) {
                categorySpending.get(cat).spent += expense.getAmount();
            }
        }
    }

    private void checkBudgetAlerts() {
        boolean hasAlert = false;
        String exceededCategory = "";

        for (CategoryData data : categorySpending.values()) {
            if (data.spent > data.budget) {
                hasAlert = true;
                exceededCategory = data.category;
                break;
            }
        }

        if (hasAlert) {
            alertMessageLabel.setText("You've exceeded your " + exceededCategory + " budget");
            alertPanel.setVisible(true);
        } else {
            alertPanel.setVisible(false);
        }
    }

    public void clearData() {
        monthlyLimitLabel.setText("$0");
        totalSpentLabel.setText("$0");
        categorySpending.clear();
        currentExpenses.clear();
        alertPanel.setVisible(false);
    }

    private static class CategoryData {
        String category;
        double budget;
        double spent;

        CategoryData(String c, double b, double s) {
            this.category = c;
            this.budget = b;
            this.spent = s;
        }
    }
}
>>>>>>> origin/main
