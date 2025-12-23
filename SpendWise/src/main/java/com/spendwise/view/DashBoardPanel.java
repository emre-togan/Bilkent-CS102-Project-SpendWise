package com.spendwise.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.spendwise.models.Budget;
import com.spendwise.models.Expense;
import com.spendwise.models.User;
import com.spendwise.services.BudgetService;
import com.spendwise.services.expenseService;

public class DashBoardPanel extends JPanel {
    private MainFrame mainFrame;
    private User currentUser;
    private Budget currentBudget;
    private List<Expense> recentExpenses;

    private JPanel contentPanel;

    // UI Components that need updating
    private JLabel greetingLabel;
    private JLabel balanceLabel;
    private JLabel budgetLimitLabel;
    private JLabel spentAmountLabel;
    private JProgressBar budgetProgressBar;
    private JLabel percentageLabel;
    private JLabel remainingLabel;
    private JLabel trendLabel;
    private JPanel weeklyChartPanel;
    private JPanel transactionsPanel;

    // Services
    private BudgetService budgetService;
    private expenseService expenseServiceInstance;

    // Data
    private double[] weeklySpending = new double[7]; // Mon-Sun
    private double totalBalance;
    private double budgetLimit;
    private double totalSpent;
    private double remaining;
    private int percentageUsed;

    public DashBoardPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.budgetService = new BudgetService();
        this.expenseServiceInstance = new expenseService();
        this.recentExpenses = new ArrayList<>();

        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        this.add(createSideMenu(), BorderLayout.WEST);
        createContentPanel();
        this.add(contentPanel, BorderLayout.CENTER);

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
        addMenuButton(sideMenu, "🏠", "Dashboard", startY, true);
        addMenuButton(sideMenu, "💳", "Budget", startY + 60, false);
        addMenuButton(sideMenu, "🧾", "Expenses", startY + 120, false);
        addMenuButton(sideMenu, "🛍️", "Shop", startY + 180, false);
        addMenuButton(sideMenu, "💬", "Chat with Friends", startY + 240, false);
        addMenuButton(sideMenu, "👤", "Profile", startY + 300, false);
        addMenuButton(sideMenu, "⚙️", "Settings", startY + 360, false);

        // Profile Card - will be updated with real user data
        JPanel profileCard = createProfileCard();
        sideMenu.add(profileCard);

        JButton logoutBtn = new JButton("🚪 Logout");
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

    private JPanel createProfileCard() {
        JPanel profileCard = new JPanel();
        profileCard.setBounds(15, 650, 230, 70);
        profileCard.setBackground(new Color(248, 249, 250));
        profileCard.setLayout(null);
        profileCard.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        // Get current user
        currentUser = UserSession.getCurrentUser();

        // Avatar with initials
        String initials = getInitials(currentUser.getUserName());
        JLabel avatar = new JLabel(initials);
        avatar.setBounds(15, 15, 40, 40);
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setOpaque(true);
        avatar.setBackground(UIConstants.PRIMARY_GREEN);
        avatar.setForeground(Color.WHITE);
        avatar.setFont(new Font("Arial", Font.BOLD, 16));
        profileCard.add(avatar);

        JLabel userName = new JLabel(currentUser.getUserName());
        userName.setBounds(65, 18, 150, 18);
        userName.setFont(new Font("Arial", Font.BOLD, 13));
        profileCard.add(userName);

        JLabel userEmail = new JLabel(currentUser.geteMail());
        userEmail.setBounds(65, 37, 150, 15);
        userEmail.setFont(new Font("Arial", Font.PLAIN, 11));
        userEmail.setForeground(new Color(120, 120, 120));
        profileCard.add(userEmail);

        return profileCard;
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

    private void addMenuButton(JPanel panel, String icon, String text, int y, boolean selected) {
        JButton button = new JButton(icon + " " + text);
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

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!selected) {
                    button.setBackground(new Color(245, 245, 245));
                    button.setOpaque(true);
                }
            }

            public void mouseExited(MouseEvent e) {
                if (!selected) {
                    button.setContentAreaFilled(false);
                }
            }
        });

        button.addActionListener(e -> {
            String panelName = text.replace("with Friends", "").trim().toUpperCase();
            mainFrame.showPanel(panelName);
        });
        panel.add(button);
    }

    private void createContentPanel() {
        contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(new Color(250, 250, 250));

        // Greeting
        currentUser = UserSession.getCurrentUser();
        String greeting = getGreeting();
        greetingLabel = new JLabel(greeting + ", " + currentUser.getUserName());
        greetingLabel.setBounds(30, 30, 600, 40);
        greetingLabel.setFont(new Font("Arial", Font.BOLD, 28));
        contentPanel.add(greetingLabel);

        // Balance Panel
        JPanel balancePanel = new JPanel();
        balancePanel.setBounds(900, 30, 200, 80);
        balancePanel.setBackground(Color.WHITE);
        balancePanel.setLayout(null);
        balancePanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        JLabel balanceTitle = new JLabel("Total Balance");
        balanceTitle.setBounds(15, 15, 170, 20);
        balanceTitle.setFont(new Font("Arial", Font.PLAIN, 13));
        balanceTitle.setForeground(new Color(120, 120, 120));
        balancePanel.add(balanceTitle);

        balanceLabel = new JLabel("$0");
        balanceLabel.setBounds(15, 35, 170, 35);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 32));
        balanceLabel.setForeground(UIConstants.PRIMARY_GREEN);
        balancePanel.add(balanceLabel);

        contentPanel.add(balancePanel);

        // Budget Card
        createBudgetCard(contentPanel);

        // Action Buttons
        JButton addExpenseButton = createActionButton("➕ Add Expense", UIConstants.PRIMARY_GREEN);
        addExpenseButton.setBounds(30, 280, 510, 60);
        addExpenseButton.addActionListener(e -> mainFrame.showPanel("EXPENSES"));
        contentPanel.add(addExpenseButton);

        JButton viewDiscountsButton = createActionButton("📈 View Discounts", new Color(255, 152, 0));
        viewDiscountsButton.setBounds(560, 280, 540, 60);
        viewDiscountsButton.addActionListener(e -> mainFrame.showPanel("SHOP"));
        contentPanel.add(viewDiscountsButton);

        // Weekly Chart
        createWeeklyChart(contentPanel);

        // Recent Transactions
        createRecentTransactions(contentPanel);
    }

    private String getGreeting() {
        int hour = java.time.LocalTime.now().getHour();
        if (hour < 12)
            return "Good Morning";
        if (hour < 18)
            return "Good Afternoon";
        return "Good Evening";
    }

    private void createBudgetCard(JPanel panel) {
        JPanel budgetCard = new JPanel();
        budgetCard.setBounds(30, 130, 1070, 120);
        budgetCard.setBackground(Color.WHITE);
        budgetCard.setLayout(null);
        budgetCard.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        JLabel titleLabel = new JLabel("Total Budget");
        titleLabel.setBounds(30, 20, 150, 25);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(120, 120, 120));
        budgetCard.add(titleLabel);

        budgetLimitLabel = new JLabel("$0");
        budgetLimitLabel.setBounds(30, 40, 200, 30);
        budgetLimitLabel.setFont(new Font("Arial", Font.BOLD, 24));
        budgetCard.add(budgetLimitLabel);

        JLabel spentLabel = new JLabel("Spent");
        spentLabel.setBounds(930, 20, 100, 25);
        spentLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        spentLabel.setForeground(new Color(120, 120, 120));
        spentLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        budgetCard.add(spentLabel);

        spentAmountLabel = new JLabel("$0");
        spentAmountLabel.setBounds(930, 40, 100, 25);
        spentAmountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        spentAmountLabel.setForeground(new Color(220, 53, 69));
        spentAmountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        budgetCard.add(spentAmountLabel);

        budgetProgressBar = new JProgressBar(0, 100);
        budgetProgressBar.setBounds(30, 75, 1010, 12);
        budgetProgressBar.setValue(0);
        budgetProgressBar.setForeground(new Color(33, 150, 243));
        budgetProgressBar.setBackground(new Color(230, 230, 230));
        budgetProgressBar.setBorderPainted(false);
        budgetCard.add(budgetProgressBar);

        remainingLabel = new JLabel("💸 Remaining: $0");
        remainingLabel.setBounds(30, 92, 200, 25);
        remainingLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        remainingLabel.setForeground(UIConstants.PRIMARY_GREEN);
        budgetCard.add(remainingLabel);

        percentageLabel = new JLabel("0% used");
        percentageLabel.setBounds(890, 92, 150, 25);
        percentageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        percentageLabel.setForeground(new Color(120, 120, 120));
        percentageLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        budgetCard.add(percentageLabel);

        panel.add(budgetCard);
    }

    private JButton createActionButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void createWeeklyChart(JPanel panel) {
        weeklyChartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                String[] days = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };

                // Find max value for scaling
                double maxValue = 1.0; // Minimum to avoid division by zero
                for (double val : weeklySpending) {
                    if (val > maxValue)
                        maxValue = val;
                }

                int chartHeight = 200;
                int baseY = 250;
                int barWidth = 60;
                int spacing = 15;

                // Draw chart
                for (int i = 0; i < days.length; i++) {
                    int barHeight = (int) ((weeklySpending[i] * chartHeight) / maxValue);
                    int x = 50 + (i * (barWidth + spacing));
                    int y = baseY - barHeight;

                    // Draw line between points
                    if (i > 0) {
                        int prevHeight = (int) ((weeklySpending[i - 1] * chartHeight) / maxValue);
                        int prevX = 50 + ((i - 1) * (barWidth + spacing)) + barWidth / 2;
                        int prevY = baseY - prevHeight;
                        int currentX = x + barWidth / 2;

                        g2d.setColor(UIConstants.PRIMARY_GREEN);
                        g2d.setStroke(new BasicStroke(3));
                        g2d.drawLine(prevX, prevY, currentX, y);
                    }

                    // Draw point
                    g2d.setColor(UIConstants.PRIMARY_GREEN);
                    g2d.fillOval(x + barWidth / 2 - 5, y - 5, 10, 10);

                    // Draw value
                    g2d.setColor(new Color(50, 50, 50));
                    g2d.setFont(new Font("Arial", Font.BOLD, 12));
                    String valueStr = String.format("$%.0f", weeklySpending[i]);
                    int strWidth = g2d.getFontMetrics().stringWidth(valueStr);
                    g2d.drawString(valueStr, x + (barWidth - strWidth) / 2, y - 15);

                    // Draw day label
                    g2d.setColor(new Color(120, 120, 120));
                    g2d.setFont(new Font("Arial", Font.PLAIN, 12));
                    int dayWidth = g2d.getFontMetrics().stringWidth(days[i]);
                    g2d.drawString(days[i], x + (barWidth - dayWidth) / 2, baseY + 25);
                }
            }
        };

        weeklyChartPanel.setBounds(30, 360, 650, 330);
        weeklyChartPanel.setBackground(Color.WHITE);
        weeklyChartPanel.setLayout(null);
        weeklyChartPanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        JLabel chartTitle = new JLabel("Weekly Spending");
        chartTitle.setBounds(20, 15, 200, 25);
        chartTitle.setFont(new Font("Arial", Font.BOLD, 16));
        weeklyChartPanel.add(chartTitle);

        trendLabel = new JLabel("--");
        trendLabel.setBounds(560, 15, 70, 25);
        trendLabel.setFont(new Font("Arial", Font.BOLD, 13));
        trendLabel.setForeground(new Color(120, 120, 120));
        weeklyChartPanel.add(trendLabel);

        panel.add(weeklyChartPanel);
    }

    private void createRecentTransactions(JPanel panel) {
        transactionsPanel = new JPanel();
        transactionsPanel.setBounds(700, 360, 400, 330);
        transactionsPanel.setBackground(Color.WHITE);
        transactionsPanel.setLayout(null);
        transactionsPanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        JLabel title = new JLabel("Recent Transactions");
        title.setBounds(20, 15, 300, 25);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        transactionsPanel.add(title);

        panel.add(transactionsPanel);
    }

    private void updateRecentTransactions() {
        // Remove old transaction items (keep only title)
        Component[] components = transactionsPanel.getComponents();
        for (Component comp : components) {
            if (comp.getBounds().y > 50) { // Remove items below title
                transactionsPanel.remove(comp);
            }
        }

        // Get last 3 expenses
        int itemY = 60;
        int maxTransactions = Math.min(3, recentExpenses.size());

        for (int i = 0; i < maxTransactions; i++) {
            Expense exp = recentExpenses.get(i);
            String emoji = getCategoryEmoji(exp.getCategory());
            String desc = exp.getDescription();
            String category = exp.getCategory();
            String amount = String.format("$%.2f", exp.getAmount());

            JPanel item = createTransactionItem(emoji, desc, category, amount);
            item.setBounds(15, itemY, 370, 70);
            transactionsPanel.add(item);
            itemY += 80;
        }

        if (recentExpenses.isEmpty()) {
            JLabel emptyLabel = new JLabel("No transactions yet");
            emptyLabel.setBounds(15, 150, 370, 30);
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            transactionsPanel.add(emptyLabel);
        }

        transactionsPanel.revalidate();
        transactionsPanel.repaint();
    }

    private JPanel createTransactionItem(String emoji, String desc, String category, String amount) {
        JPanel item = new JPanel();
        item.setLayout(null);
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)));

        JLabel icon = new JLabel(emoji);
        icon.setBounds(10, 15, 40, 40);
        icon.setFont(new Font("Arial", Font.PLAIN, 28));
        item.add(icon);

        JLabel descLabel = new JLabel(desc);
        descLabel.setBounds(60, 12, 200, 20);
        descLabel.setFont(new Font("Arial", Font.BOLD, 14));
        item.add(descLabel);

        JLabel catLabel = new JLabel(category);
        catLabel.setBounds(60, 35, 200, 18);
        catLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        catLabel.setForeground(new Color(120, 120, 120));
        item.add(catLabel);

        JLabel amountLabel = new JLabel(amount);
        amountLabel.setBounds(270, 20, 90, 25);
        amountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        amountLabel.setForeground(new Color(220, 53, 69));
        amountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        item.add(amountLabel);

        return item;
    }

    private String getCategoryEmoji(String category) {
        if (category == null)
            return "📋";
        switch (category) {
            case "Food":
                return "🛒";
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
     * Refresh all dashboard data from backend
     */
    public void refreshData() {
        try {
            // Get current user
            currentUser = UserSession.getCurrentUser();
            int userId = currentUser.getId();

            // Update greeting
            String greeting = getGreeting();
            greetingLabel.setText(greeting + ", " + currentUser.getUserName());

            // Load budget data
            currentBudget = budgetService.getSpesificUserBudget(userId);

            if (currentBudget != null) {
                budgetLimit = currentBudget.getBudgetLimit();
                totalSpent = currentBudget.getTotalSpending();
                remaining = budgetLimit - totalSpent;
                percentageUsed = (int) budgetService.calculateTheSpendingPercentage(currentBudget);

                // Update budget UI
                budgetLimitLabel.setText(String.format("$%.2f", budgetLimit));
                spentAmountLabel.setText(String.format("$%.2f", totalSpent));
                remainingLabel.setText(String.format("💸 Remaining: $%.2f", remaining));
                percentageLabel.setText(percentageUsed + "% used");
                budgetProgressBar.setValue(Math.min(100, percentageUsed));

                // Change progress bar color based on percentage
                if (percentageUsed >= 100) {
                    budgetProgressBar.setForeground(new Color(220, 53, 69)); // Red
                } else if (percentageUsed >= 80) {
                    budgetProgressBar.setForeground(new Color(255, 152, 0)); // Orange
                } else {
                    budgetProgressBar.setForeground(new Color(33, 150, 243)); // Blue
                }

                // Calculate total balance (budget remaining)
                totalBalance = remaining;
            } else {
                // No budget set
                budgetLimitLabel.setText("No budget set");
                spentAmountLabel.setText("$0.00");
                remainingLabel.setText("💸 Remaining: $0.00");
                percentageLabel.setText("0% used");
                budgetProgressBar.setValue(0);
                totalBalance = 0;
            }

            balanceLabel.setText(String.format("$%.0f", totalBalance));

            // Load expenses for weekly chart and recent transactions
            List<Expense> allExpenses = expenseServiceInstance.getExpensesOfTheUser(userId);
            if (allExpenses == null) {
                allExpenses = new ArrayList<>();
            }

            // Calculate weekly spending
            calculateWeeklySpending(allExpenses);

            // Get recent expenses (last 3)
            recentExpenses = getRecentExpenses(allExpenses, 3);

            // Update UI components
            updateRecentTransactions();
            weeklyChartPanel.repaint();

            // Calculate and display trend
            updateTrend();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error refreshing dashboard data: " + e.getMessage());
        }
    }

    /**
     * Calculate spending for each day of current week
     */
    private void calculateWeeklySpending(List<Expense> expenses) {
        // Initialize array
        weeklySpending = new double[7];

        // Get start and end of current week (Monday to Sunday)
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        // Calculate spending for each day
        for (Expense expense : expenses) {
            if (expense.getDate() != null) {
                LocalDate expenseDate = expense.getDate().toLocalDate();

                // Check if expense is in current week
                if (!expenseDate.isBefore(monday) && !expenseDate.isAfter(monday.plusDays(6))) {
                    int dayIndex = expenseDate.getDayOfWeek().getValue() - 1; // Monday = 0
                    weeklySpending[dayIndex] += expense.getAmount();
                }
            }
        }
    }

    /**
     * Get most recent N expenses
     */
    private List<Expense> getRecentExpenses(List<Expense> allExpenses, int count) {
        // Sort by date (most recent first)
        List<Expense> sorted = new ArrayList<>(allExpenses);
        sorted.sort((e1, e2) -> {
            if (e1.getDate() == null)
                return 1;
            if (e2.getDate() == null)
                return -1;
            return e2.getDate().compareTo(e1.getDate());
        });

        // Return first N
        return sorted.subList(0, Math.min(count, sorted.size()));
    }

    /**
     * Calculate and update spending trend
     */
    private void updateTrend() {
        // Calculate if spending is increasing or decreasing
        if (weeklySpending.length >= 2) {
            double firstHalf = 0;
            double secondHalf = 0;

            for (int i = 0; i < 3; i++) {
                firstHalf += weeklySpending[i];
            }
            for (int i = 4; i < 7; i++) {
                secondHalf += weeklySpending[i];
            }

            if (secondHalf > firstHalf * 1.1) {
                trendLabel.setText("↗ +12%");
                trendLabel.setForeground(new Color(220, 53, 69));
            } else if (secondHalf < firstHalf * 0.9) {
                trendLabel.setText("↘ -12%");
                trendLabel.setForeground(UIConstants.PRIMARY_GREEN);
            } else {
                trendLabel.setText("→ Stable");
                trendLabel.setForeground(new Color(120, 120, 120));
            }
        }
    }

    /**
     * Clear all data - called on logout
     */
    public void clearData() {
        greetingLabel.setText("Good Morning");
        balanceLabel.setText("$0");
        budgetLimitLabel.setText("$0");
        spentAmountLabel.setText("$0");
        remainingLabel.setText("💸 Remaining: $0");
        percentageLabel.setText("0% used");
        budgetProgressBar.setValue(0);

        weeklySpending = new double[7];
        recentExpenses.clear();

        updateRecentTransactions();
        weeklyChartPanel.repaint();
    }
}
