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
import com.spendwise.view.components.Icons;

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
            new Color(139, 195, 74),
            new Color(33, 150, 243),
            new Color(255, 152, 0),
            new Color(156, 39, 176),
            new Color(233, 30, 99),
            new Color(0, 150, 136)
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

        add(createContent(), BorderLayout.CENTER);

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

        JButton editBtn = new JButton();
        editBtn.setIcon(Icons.getEditIcon(24, UIConstants.PRIMARY_GREEN));
        editBtn.setBounds(1100, 20, 30, 30);
        editBtn.setBorderPainted(false);
        editBtn.setContentAreaFilled(false);
        editBtn.setFocusPainted(false);
        editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editBtn.addActionListener(e -> showEditBudgetDialog());
        card.add(editBtn);

        JLabel totalSpentTitle = new JLabel("Total Spent");
        totalSpentTitle.setBounds(30, 85, 100, 20);

        JLabel spentTitle = new JLabel("Total Spent");
        spentTitle.setBounds(270, 20, 100, 20);
        spentTitle.setForeground(Color.GRAY);
        card.add(spentTitle);

        totalSpentLabel = new JLabel("₺2.010");
        totalSpentLabel.setBounds(1050, 45, 80, 20);
        totalSpentLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalSpentLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        card.add(totalSpentLabel);

        totalSpentProgressBar = new JProgressBar(0, 100);
        totalSpentProgressBar.setBounds(270, 50, 770, 10);
        totalSpentProgressBar.setValue(0);
        totalSpentProgressBar.setForeground(new Color(50, 50, 50));
        totalSpentProgressBar.setBackground(new Color(240, 240, 240));
        totalSpentProgressBar.setBorderPainted(false);
        card.add(totalSpentProgressBar);

        remainingLabel = new JLabel(" Remaining: ₺0");
        remainingLabel.setIcon(Icons.getBudgetIcon(16, UIConstants.SELECTION_GREEN));
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

        JLabel icon = new JLabel(Icons.getInfoIcon(24, new Color(255, 143, 0))); // Warning Orange
        icon.setBounds(25, 25, 30, 30);
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

        JLabel emojiLabel = new JLabel(Icons.getCategoryIcon(data.category, 32, barColor));
        emojiLabel.setBounds(25, 25, 40, 40);
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

        JButton editBtn = new JButton();
        editBtn.setIcon(Icons.getEditIcon(18, Color.GRAY));
        editBtn.setBounds(1080, 25, 30, 30);
        editBtn.setBorderPainted(false);
        editBtn.setContentAreaFilled(false);
        editBtn.setFocusPainted(false);
        editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editBtn.addActionListener(e -> showEditCategoryDialog(data));
        card.add(editBtn);

        return card;
    }

    private void showEditCategoryDialog(CategoryData data) {

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

    // Category emoji helper removed

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

                monthlyLimitLabel.setText(String.format("₺%.0f", monthlyLimit));
                totalSpentLabel.setText(String.format("₺%.0f", totalSpent));
                remainingLabel.setText(String.format("Remaining: ₺%.0f", remaining));

                int percentage = (int) budgetService.calculateTheSpendingPercentage(currentBudget);
                totalSpentProgressBar.setValue(Math.min(100, percentage));
                if (percentage >= 100)
                    totalSpentProgressBar.setForeground(new Color(50, 50, 50));
                else
                    totalSpentProgressBar.setForeground(new Color(50, 50, 50));
            } else {
                monthlyLimitLabel.setText("No budget set");
                totalSpentLabel.setText("₺0");
                remainingLabel.setText("Remaining: ₺0");
                totalSpentProgressBar.setValue(0);
            }

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
        monthlyLimitLabel.setText("₺0");
        totalSpentLabel.setText("₺0");
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