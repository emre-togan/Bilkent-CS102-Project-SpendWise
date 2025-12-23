package com.spendwise.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.spendwise.models.Budget;
import com.spendwise.models.Expense;
import com.spendwise.models.User;
import com.spendwise.services.BudgetService;
import com.spendwise.services.expenseService;

public class BudgetPanel extends JPanel {

    private MainFrame mainFrame;
    private User currentUser;
    private Budget currentBudget;
    private List<Expense> currentExpenses;

    // Services
    private BudgetService budgetService;
    private expenseService expenseServiceInstance;

    // UI Components
    private JLabel monthlyLimitLabel;
    private JLabel totalSpentLabel;
    private JLabel remainingLabel;
    private JProgressBar totalSpentProgressBar;
    private JPanel alertPanel;
    private JPanel categoriesContainer;

    // Data
    private double monthlyLimit;
    private double totalSpent;
    private double remaining;
    private Map<String, CategoryData> categorySpending;

    // Category colors
    private static final Color[] CATEGORY_COLORS = {
            new Color(76, 175, 80), // Green
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
        setBackground(new Color(250, 250, 250));

        add(createSideMenu(), BorderLayout.WEST);
        add(createContent(), BorderLayout.CENTER);

        // Load initial data
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
        addMenuItem(sideMenu, "🏠", "Dashboard", "DASHBOARD", startY, false);
        addMenuItem(sideMenu, "💰", "Budget", "BUDGET", startY + 60, true);
        addMenuItem(sideMenu, "📋", "Expenses", "EXPENSES", startY + 120, false);
        addMenuItem(sideMenu, "🛒", "Shop", "SHOP", startY + 180, false);
        addMenuItem(sideMenu, "💬", "Chat", "CHAT", startY + 240, false);
        addMenuItem(sideMenu, "👤", "Profile", "PROFILE", startY + 300, false);
        addMenuItem(sideMenu, "⚙️", "Settings", "SETTINGS", startY + 360, false);

        return sideMenu;
    }

    private void addMenuItem(JPanel parent, String emoji, String text, String targetPanelName, int y, boolean active) {
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

        btn.addActionListener(e -> mainFrame.showPanel(targetPanelName));
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

        monthlyLimitLabel = new JLabel("₺0");
        monthlyLimitLabel.setBounds(25, 40, 200, 30);
        monthlyLimitLabel.setFont(new Font("Arial", Font.BOLD, 24));
        card.add(monthlyLimitLabel);

        JButton editBtn = new JButton("✏");
        editBtn.setBounds(1010, 30, 40, 40);
        editBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        editBtn.setFocusPainted(false);
        editBtn.setBorderPainted(false);
        editBtn.setContentAreaFilled(false);
        editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editBtn.addActionListener(e -> showEditBudgetDialog());
        card.add(editBtn);

        JLabel totalSpentTitle = new JLabel("Total Spent");
        totalSpentTitle.setBounds(400, 15, 200, 20);
        totalSpentTitle.setFont(new Font("Arial", Font.PLAIN, 13));
        totalSpentTitle.setForeground(new Color(120, 120, 120));
        card.add(totalSpentTitle);

        totalSpentProgressBar = new JProgressBar(0, 100);
        totalSpentProgressBar.setBounds(400, 45, 640, 10);
        totalSpentProgressBar.setValue(0);
        totalSpentProgressBar.setForeground(new Color(50, 50, 50));
        totalSpentProgressBar.setBackground(new Color(230, 230, 230));
        totalSpentProgressBar.setBorderPainted(false);
        card.add(totalSpentProgressBar);

        remainingLabel = new JLabel("💸 Remaining: ₺0");
        remainingLabel.setBounds(400, 65, 200, 20);
        remainingLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        remainingLabel.setForeground(UIConstants.PRIMARY_GREEN);
        card.add(remainingLabel);

        totalSpentLabel = new JLabel("₺0");
        totalSpentLabel.setBounds(900, 15, 140, 25);
        totalSpentLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalSpentLabel.setForeground(new Color(220, 53, 69));
        totalSpentLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        card.add(totalSpentLabel);

        parent.add(card);
    }

    private void createAlertBanner(JPanel parent) {
        alertPanel = new JPanel();
        alertPanel.setBounds(30, 250, 1070, 70);
        alertPanel.setBackground(new Color(255, 244, 229));
        alertPanel.setLayout(null);
        alertPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 193, 7)));
        alertPanel.setVisible(false); // Hidden by default

        JLabel icon = new JLabel("⚠");
        icon.setBounds(25, 20, 30, 30);
        icon.setFont(new Font("Arial", Font.PLAIN, 28));
        alertPanel.add(icon);

        JLabel title = new JLabel("Budget Alert");
        title.setBounds(65, 15, 200, 20);
        title.setFont(new Font("Arial", Font.BOLD, 15));
        title.setForeground(new Color(180, 80, 0));
        alertPanel.add(title);

        JLabel message = new JLabel("You've exceeded your budget in some categories");
        message.setBounds(65, 38, 400, 18);
        message.setFont(new Font("Arial", Font.PLAIN, 13));
        message.setForeground(new Color(100, 50, 0));
        alertPanel.add(message);

        parent.add(alertPanel);
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
        addCategoryBtn.addActionListener(e -> showAddCategoryDialog());
        parent.add(addCategoryBtn);

        // Categories container with scroll
        categoriesContainer = new JPanel();
        categoriesContainer.setLayout(new BoxLayout(categoriesContainer, BoxLayout.Y_AXIS));
        categoriesContainer.setBackground(new Color(250, 250, 250));

        JScrollPane scrollPane = new JScrollPane(categoriesContainer);
        scrollPane.setBounds(30, 390, 1070, 380);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        parent.add(scrollPane);
    }

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

                // Update or create budget
                int userId = UserSession.getCurrentUserId();

                if (currentBudget == null) {
                    // Create new budget
                    YearMonth currentMonth = YearMonth.now();
                    LocalDate startDate = currentMonth.atDay(1);
                    LocalDate endDate = currentMonth.atEndOfMonth();

                    Budget newBudget = new Budget(
                            userId,
                            newLimit,
                            Date.valueOf(startDate),
                            Date.valueOf(endDate));

                    boolean success = budgetService.createBudget(newBudget);
                    if (success) {
                        JOptionPane.showMessageDialog(dialog, "Budget created successfully!");
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Failed to create budget!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else {
                    // Update existing budget
                    currentBudget.setLimit(newLimit);
                    boolean success = budgetService.updateBudgetList(currentBudget);
                    if (success) {
                        JOptionPane.showMessageDialog(dialog, "Budget updated successfully!");
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Failed to update budget!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
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
                    JOptionPane.showMessageDialog(dialog, "Amount must be greater than 0!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Check if category already exists
                if (categorySpending.containsKey(category)) {
                    JOptionPane.showMessageDialog(dialog, "Category already exists! Edit it instead.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Add category (in real implementation, save to database)
                CategoryData newCategory = new CategoryData(category, amount, 0);
                categorySpending.put(category, newCategory);

                JOptionPane.showMessageDialog(dialog, "Category added successfully!");
                dialog.dispose();
                refreshData();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid amount!", "Error",
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

    private void updateCategories() {
        categoriesContainer.removeAll();

        if (categorySpending.isEmpty()) {
            JLabel emptyLabel = new JLabel("No categories yet. Add one to start tracking!");
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            emptyLabel.setForeground(Color.GRAY);
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
                categoriesContainer.add(Box.createVerticalStrut(10));

                colorIndex++;
            }
        }

        categoriesContainer.revalidate();
        categoriesContainer.repaint();
    }

    private JPanel createCategoryCard(CategoryData data, Color barColor) {
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        card.setPreferredSize(new Dimension(1070, 90));

        String emoji = getCategoryEmoji(data.category);
        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setBounds(25, 25, 40, 40);
        emojiLabel.setFont(new Font("Arial", Font.PLAIN, 32));
        card.add(emojiLabel);

        JLabel name = new JLabel(data.category);
        name.setBounds(80, 20, 200, 25);
        name.setFont(new Font("Arial", Font.BOLD, 16));
        card.add(name);

        JLabel amounts = new JLabel(String.format("₺%.2f / ₺%.2f", data.spent, data.budget));
        amounts.setBounds(80, 47, 200, 20);
        amounts.setFont(new Font("Arial", Font.PLAIN, 13));
        amounts.setForeground(new Color(120, 120, 120));
        card.add(amounts);

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setBounds(300, 38, 600, 15);
        int percentage = (int) ((data.spent / data.budget) * 100);
        bar.setValue(Math.min(100, percentage));
        bar.setBorderPainted(false);

        // Color based on usage
        if (percentage >= 100) {
            bar.setForeground(new Color(220, 53, 69)); // Red - exceeded
        } else if (percentage >= 80) {
            bar.setForeground(new Color(255, 152, 0)); // Orange - warning
        } else {
            bar.setForeground(barColor); // Normal color
        }
        bar.setBackground(new Color(240, 240, 240));
        card.add(bar);

        JLabel percentageLabel = new JLabel(percentage + "%");
        percentageLabel.setBounds(920, 32, 60, 25);
        percentageLabel.setFont(new Font("Arial", Font.BOLD, 15));

        if (percentage >= 100) {
            percentageLabel.setForeground(new Color(220, 53, 69));
        } else {
            percentageLabel.setForeground(new Color(50, 50, 50));
        }
        card.add(percentageLabel);

        JButton editBtn = new JButton("✏");
        editBtn.setBounds(990, 25, 35, 35);
        editBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        editBtn.setFocusPainted(false);
        editBtn.setBorderPainted(false);
        editBtn.setContentAreaFilled(false);
        editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editBtn.addActionListener(e -> showEditCategoryDialog(data));
        card.add(editBtn);

        JButton deleteBtn = new JButton("🗑");
        deleteBtn.setBounds(1030, 25, 35, 35);
        deleteBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        deleteBtn.setForeground(new Color(220, 53, 69));
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorderPainted(false);
        deleteBtn.setContentAreaFilled(false);
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteBtn.addActionListener(e -> deleteCategory(data.category));
        card.add(deleteBtn);

        return card;
    }

    private void showEditCategoryDialog(CategoryData data) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Category: " + data.category,
                true);
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
                double newAmount = Double.parseDouble(amountField.getText());
                if (newAmount <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Amount must be greater than 0!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                data.budget = newAmount;
                JOptionPane.showMessageDialog(dialog, "Category updated successfully!");
                dialog.dispose();
                refreshData();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid amount!", "Error",
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

    private void deleteCategory(String category) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this category?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            categorySpending.remove(category);
            refreshData();
            JOptionPane.showMessageDialog(this, "Category deleted successfully!");
        }
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

    /**
     * Refresh all budget data from backend
     */
    public void refreshData() {
        try {
            // Get current user
            currentUser = UserSession.getCurrentUser();
            int userId = currentUser.getId();

            // Load budget
            currentBudget = budgetService.getSpesificUserBudget(userId);

            if (currentBudget != null) {
                monthlyLimit = currentBudget.getBudgetLimit();
                totalSpent = currentBudget.getTotalSpending();
                remaining = monthlyLimit - totalSpent;

                // Update UI
                monthlyLimitLabel.setText(String.format("₺%.2f", monthlyLimit));
                totalSpentLabel.setText(String.format("₺%.2f", totalSpent));
                remainingLabel.setText(String.format("💸 Remaining: ₺%.2f", remaining));

                // Update progress bar
                int percentage = (int) budgetService.calculateTheSpendingPercentage(currentBudget);
                totalSpentProgressBar.setValue(Math.min(100, percentage));

                // Change color based on percentage
                if (percentage >= 100) {
                    totalSpentProgressBar.setForeground(new Color(220, 53, 69));
                } else if (percentage >= 80) {
                    totalSpentProgressBar.setForeground(new Color(255, 152, 0));
                } else {
                    totalSpentProgressBar.setForeground(new Color(50, 50, 50));
                }
            } else {
                monthlyLimitLabel.setText("No budget set");
                totalSpentLabel.setText("₺0.00");
                remainingLabel.setText("💸 Remaining: ₺0.00");
                totalSpentProgressBar.setValue(0);
            }

            // Load expenses and calculate category spending
            currentExpenses = expenseServiceInstance.getExpensesOfTheUser(userId);
            if (currentExpenses == null) {
                currentExpenses = new ArrayList<>();
            }

            calculateCategorySpending();
            updateCategories();
            checkBudgetAlerts();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error refreshing budget data: " + e.getMessage());
        }
    }

    /**
     * Calculate spending per category
     */
    private void calculateCategorySpending() {
        // Initialize with default categories
        if (categorySpending.isEmpty()) {
            categorySpending.put("Food", new CategoryData("Food", 800, 0));
            categorySpending.put("Transport", new CategoryData("Transport", 400, 0));
            categorySpending.put("Shopping", new CategoryData("Shopping", 800, 0));
        }

        // Calculate actual spending per category
        for (Expense expense : currentExpenses) {
            String category = expense.getCategory();
            if (category != null && categorySpending.containsKey(category)) {
                categorySpending.get(category).spent += expense.getAmount();
            }
        }
    }

    /**
     * Check if any budget alerts should be shown
     */
    private void checkBudgetAlerts() {
        boolean hasAlert = false;

        // Check if any category exceeded its budget
        for (CategoryData data : categorySpending.values()) {
            if (data.spent > data.budget) {
                hasAlert = true;
                break;
            }
        }

        alertPanel.setVisible(hasAlert);
    }

    /**
     * Clear all data - called on logout
     */
    public void clearData() {
        monthlyLimitLabel.setText("₺0");
        totalSpentLabel.setText("₺0");
        remainingLabel.setText("💸 Remaining: ₺0");
        totalSpentProgressBar.setValue(0);

        categorySpending.clear();
        currentExpenses.clear();
        alertPanel.setVisible(false);

        updateCategories();
    }

    /**
     * Helper class to store category budget data
     */
    private static class CategoryData {
        String category;
        double budget;
        double spent;

        CategoryData(String category, double budget, double spent) {
            this.category = category;
            this.budget = budget;
            this.spent = spent;
        }
    }
}
