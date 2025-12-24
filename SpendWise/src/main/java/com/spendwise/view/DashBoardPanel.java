package com.spendwise.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import com.spendwise.models.Budget;
import com.spendwise.models.Expense;
import com.spendwise.models.User;
import com.spendwise.services.BudgetService;
import com.spendwise.services.expenseService;
import com.spendwise.view.components.RoundedButton;
import com.spendwise.view.components.RoundedPanel;
import com.spendwise.view.components.SidebarPanel;

public class DashBoardPanel extends JPanel {
    private MainFrame mainFrame;
    private User currentUser;
    private Budget currentBudget;
    private List<Expense> recentExpenses;

    private JPanel contentPanel;
    private SidebarPanel sidebarPanel;

    // UI Components
    private JLabel greetingLabel;
    private JLabel balanceLabel;
    private JLabel budgetLimitLabel;
    private JLabel spentAmountLabel;
    private JProgressBar budgetProgressBar;
    private JLabel percentageLabel;
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
        this.setBackground(UIConstants.BACKGROUND_LIGHT);

        // Sidebar
        sidebarPanel = new SidebarPanel("DASHBOARD",
                panelName -> mainFrame.showPanel(panelName),
                () -> mainFrame.logout());
        this.add(sidebarPanel, BorderLayout.WEST);

        // Content
        createContentPanel();
        this.add(contentPanel, BorderLayout.CENTER);

        // Load initial data
        refreshData();
    }

<<<<<<< HEAD
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

    private void addMenuButton(JPanel panel, String icon, String text, int y, boolean selected) {
        RoundedButton button = new RoundedButton(icon + " " + text, 15);
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

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!selected) {
                    button.setBackground(new Color(245, 245, 245));
                    button.setOpaque(true);
                }
            }
            public void mouseExited(MouseEvent e) {
                if (!selected) {
                    button.setBackground(Color.WHITE);
                }
            }
        });

        button.addActionListener(e -> {
            String panelName = text.replace("with Friends", "").trim().toUpperCase();
            mainFrame.showPanel(panelName);
        });
        panel.add(button);
    }

    private JPanel createContentPanel() {
=======
    private void createContentPanel() {
>>>>>>> origin/main
        contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(UIConstants.BACKGROUND_LIGHT);

        // Header Section
        // Greeting
        currentUser = UserSession.getCurrentUser();
        String greeting = getGreeting();
        greetingLabel = new JLabel("Good Morning, Guest");
        greetingLabel.setBounds(30, 30, 600, 30);
        greetingLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Bold and larger
        contentPanel.add(greetingLabel);

        // Small subtitle under greeting ("Dashboard")
        JLabel dashboardTitle = new JLabel("Dashboard");
        dashboardTitle.setBounds(30, 10, 200, 20);
        dashboardTitle.setFont(new Font("Arial", Font.BOLD, 14));
        dashboardTitle.setForeground(Color.GRAY);
        contentPanel.add(dashboardTitle);

        // Total Balance (Top Right)
        JLabel balanceTitle = new JLabel("Total Balance");
        balanceTitle.setBounds(1000, 15, 150, 20);
        balanceTitle.setFont(new Font("Arial", Font.PLAIN, 12));
        balanceTitle.setHorizontalAlignment(SwingConstants.RIGHT);
        balanceTitle.setForeground(Color.GRAY);
        contentPanel.add(balanceTitle);

        balanceLabel = new JLabel("$0");
        balanceLabel.setBounds(1000, 35, 150, 30);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 24));
        balanceLabel.setForeground(UIConstants.PRIMARY_GREEN); // Green per design
        balanceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        contentPanel.add(balanceLabel);

        // 1. Budget Card (Top Large Card)
        createBudgetCard(contentPanel);

        // 2. Action Buttons (Green and Orange)
        int btnY = 240;
        int btnHeight = 60;

        RoundedButton addExpenseButton = new RoundedButton("⊕ Add Expense", 20, UIConstants.SELECTION_GREEN,
                UIConstants.darker(UIConstants.SELECTION_GREEN));
        addExpenseButton.setBounds(30, btnY, 400, btnHeight);
        addExpenseButton.setForeground(Color.WHITE);
        addExpenseButton.setFont(new Font("Arial", Font.BOLD, 14));
        addExpenseButton.addActionListener(e -> mainFrame.showPanel("EXPENSES"));
        contentPanel.add(addExpenseButton);

        RoundedButton viewDiscountsButton = new RoundedButton("🏷 View Discounts", 20, new Color(255, 193, 7),
                new Color(255, 160, 0)); // Warning/Orange color
        viewDiscountsButton.setBounds(450, btnY, 400, btnHeight); // Next to it
        viewDiscountsButton.setForeground(Color.WHITE);
        viewDiscountsButton.setFont(new Font("Arial", Font.BOLD, 14));
        viewDiscountsButton.addActionListener(e -> mainFrame.showPanel("SHOP"));
        contentPanel.add(viewDiscountsButton);

        // 3. Weekly Chart (Middle)
        createWeeklyChart(contentPanel);

        // 4. Recent Transactions (Bottom/Right)
        // Design shows chart is full width or large, transactions below or side.
        // Image shows: Weekly Spending (Full width graph) AND Recent Transactions (List
        // below).
        // Let's place Chart full width below buttons.
        createRecentTransactions(contentPanel);
    }

    private void createBudgetCard(JPanel panel) {
        // Design: White, rounded, width spanning most of the screen.
        RoundedPanel budgetCard = new RoundedPanel(20, Color.WHITE);
        budgetCard.setBounds(30, 80, 1150, 130); // Wider
        budgetCard.setLayout(null);

        JLabel titleLabel = new JLabel("Total Budget");
        titleLabel.setBounds(30, 20, 150, 25);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.GRAY);
        budgetCard.add(titleLabel);

        budgetLimitLabel = new JLabel("$3.000");
        budgetLimitLabel.setBounds(30, 45, 200, 30);
        budgetLimitLabel.setFont(new Font("Arial", Font.BOLD, 28));
        budgetCard.add(budgetLimitLabel);

        // Spent Label (Right side)
        JLabel spentTitle = new JLabel("Spent");
        spentTitle.setBounds(1000, 20, 100, 25);
        spentTitle.setFont(new Font("Arial", Font.PLAIN, 12));
        spentTitle.setForeground(Color.GRAY);
        spentTitle.setHorizontalAlignment(SwingConstants.RIGHT);
        budgetCard.add(spentTitle);

        spentAmountLabel = new JLabel("$2.010");
        spentAmountLabel.setBounds(1000, 45, 100, 25);
        spentAmountLabel.setFont(new Font("Arial", Font.BOLD, 18));
        spentAmountLabel.setForeground(UIConstants.DANGER_RED);
        spentAmountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        budgetCard.add(spentAmountLabel);

        // Progress Bar
        budgetProgressBar = new JProgressBar(0, 100);
        budgetProgressBar.setBounds(30, 90, 1070, 10); // Thin and wide
        budgetProgressBar.setValue(67);
        budgetProgressBar.setForeground(new Color(67, 160, 71)); // Darker green part
        // Per image, progress bar is multi-colored (Gradient maybe?). JProgressBar is
        // single color usually.
        // The image shows a gradient green-blue bar with a grey background.
        // For simplicity, we use Green.
        budgetProgressBar.setBackground(new Color(240, 240, 240));
        budgetProgressBar.setBorderPainted(false);
        budgetCard.add(budgetProgressBar);

        // Percentage label
        percentageLabel = new JLabel("67% used");
        percentageLabel.setBounds(1030, 105, 70, 20);
        percentageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        percentageLabel.setForeground(Color.GRAY);
        percentageLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        budgetCard.add(percentageLabel);

        panel.add(budgetCard);
    }

<<<<<<< HEAD
    private RoundedButton createActionButton(String text, Color bgColor) {
        RoundedButton button = new RoundedButton(text, 15);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener((new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        }));

        return button;
    }

=======
>>>>>>> origin/main
    private void createWeeklyChart(JPanel panel) {
        // Design: White rounded panel, smooth green line graph
        weeklyChartPanel = new RoundedPanel(20, Color.WHITE) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Call chart drawing logic
                drawChart(g);
            }
        };
        weeklyChartPanel.setBounds(30, 320, 1150, 200);
        weeklyChartPanel.setLayout(null);

        JLabel chartTitle = new JLabel("Weekly Spending");
        chartTitle.setBounds(30, 20, 200, 25);
        chartTitle.setFont(new Font("Arial", Font.BOLD, 15));
        weeklyChartPanel.add(chartTitle);

        trendLabel = new JLabel("↗ +12%");
        trendLabel.setBounds(1070, 20, 70, 25);
        trendLabel.setFont(new Font("Arial", Font.BOLD, 13));
        trendLabel.setForeground(UIConstants.DANGER_RED);
        weeklyChartPanel.add(trendLabel);

        panel.add(weeklyChartPanel);
    }

    private void drawChart(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        String[] days = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };

        // Mock points if empty
        if (weeklySpending == null || weeklySpending.length == 0)
            return;

        double maxValue = 1.0;
        for (double val : weeklySpending)
            if (val > maxValue)
                maxValue = val;

        int chartHeight = 100;
        int baseY = 160;
        int startX = 50;
        int sectionWidth = 1050 / (days.length - 1); // Spread across width

        int[] xPoints = new int[days.length];
        int[] yPoints = new int[days.length];

        for (int i = 0; i < days.length; i++) {
            int barHeight = (int) ((weeklySpending[i] * chartHeight) / maxValue);
            xPoints[i] = startX + (i * sectionWidth);
            yPoints[i] = baseY - barHeight;

            // Draw Day Text
            g2d.setColor(Color.GRAY);
            g2d.setFont(new Font("Arial", Font.PLAIN, 11));
            String day = days[i];
            int textW = g2d.getFontMetrics().stringWidth(day);
            g2d.drawString(day, xPoints[i] - textW / 2, baseY + 20);
        }

        // Draw Polyline for Graph
        g2d.setColor(UIConstants.SELECTION_GREEN);
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawPolyline(xPoints, yPoints, days.length);

        // Draw Circles
        for (int i = 0; i < days.length; i++) {
            g2d.setColor(Color.WHITE);
            g2d.fillOval(xPoints[i] - 4, yPoints[i] - 4, 8, 8);
            g2d.setColor(UIConstants.SELECTION_GREEN);
            g2d.drawOval(xPoints[i] - 4, yPoints[i] - 4, 8, 8);
        }
    }

    private void createRecentTransactions(JPanel panel) {
        transactionsPanel = new RoundedPanel(20, Color.WHITE);
        transactionsPanel.setBounds(30, 540, 1150, 240); // Bottom section
        transactionsPanel.setLayout(null);

        JLabel title = new JLabel("Recent Transactions");
        title.setBounds(30, 20, 200, 25);
        title.setFont(new Font("Arial", Font.BOLD, 15));
        transactionsPanel.add(title);

        panel.add(transactionsPanel);
    }

    private void updateRecentTransactions() {
        // Clear items below header
        Component[] components = transactionsPanel.getComponents();
        for (Component comp : components) {
            if (comp.getY() > 50) {
                transactionsPanel.remove(comp);
            }
        }

        int y = 60;
        for (Expense exp : recentExpenses) {
            JPanel item = createTransactionItem(exp);
            item.setBounds(30, y, 1090, 50);
            transactionsPanel.add(item);
            y += 60;
            if (y > 200)
                break; // Limit visible items
        }

        transactionsPanel.revalidate();
        transactionsPanel.repaint();
    }

    private JPanel createTransactionItem(Expense exp) {
        JPanel item = new JPanel();
        item.setLayout(null);
        item.setOpaque(false);
        // item.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new
        // Color(240,240,240)));
        // Design doesn't show borders, just clean spacing.

        String emo = getCategoryEmoji(exp.getCategory());
        JLabel icon = new JLabel(emo);
        icon.setFont(new Font("Arial", Font.PLAIN, 20));
        icon.setBounds(0, 10, 30, 30);
        item.add(icon);

        JLabel desc = new JLabel(exp.getDescription());
        desc.setFont(new Font("Arial", Font.BOLD, 14));
        desc.setBounds(40, 15, 300, 20);
        item.add(desc);

        JLabel date = new JLabel(exp.getDate().toString());
        date.setFont(new Font("Arial", Font.PLAIN, 12));
        date.setForeground(Color.GRAY);
        date.setBounds(400, 15, 150, 20);
        item.add(date);

        JLabel price = new JLabel("-$" + exp.getAmount());
        price.setFont(new Font("Arial", Font.BOLD, 14));
        price.setForeground(UIConstants.DANGER_RED);
        price.setBounds(1000, 15, 80, 20);
        price.setHorizontalAlignment(SwingConstants.RIGHT);
        item.add(price);

        return item;
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

    private String getGreeting() {
        int hour = java.time.LocalTime.now().getHour();
        if (hour < 12)
            return "Good Morning";
        if (hour < 18)
            return "Good Afternoon";
        return "Good Evening";
    }

    public void refreshData() {
        try {
            currentUser = UserSession.getCurrentUser();
            int userId = currentUser.getId();

            // 1. Sidebar
            sidebarPanel.updateUser();

            // 2. Greeting
            greetingLabel.setText(getGreeting() + ", " + currentUser.getUserName());

            // 3. Budget
            currentBudget = budgetService.getSpesificUserBudget(userId);
            if (currentBudget != null) {
                budgetLimit = currentBudget.getBudgetLimit();
                totalSpent = currentBudget.getTotalSpending();
                percentageUsed = (int) budgetService.calculateTheSpendingPercentage(currentBudget);

                budgetLimitLabel.setText(String.format("$%.0f", budgetLimit));
                spentAmountLabel.setText(String.format("$%.0f", totalSpent));
                percentageLabel.setText(percentageUsed + "% used");
                budgetProgressBar.setValue(Math.min(100, percentageUsed));

                // Set color of progress bar based on percentage
                if (percentageUsed >= 100)
                    budgetProgressBar.setForeground(new Color(220, 53, 69));
                else
                    budgetProgressBar.setForeground(new Color(67, 160, 71));

                balanceLabel.setText(String.format("$%.0f", budgetLimit - totalSpent));
            } else {
                budgetLimitLabel.setText("$0");
                spentAmountLabel.setText("$0");
                balanceLabel.setText("$0");
            }

            // 4. Chart Data
            List<Expense> allExpenses = expenseServiceInstance.getExpensesOfTheUser(userId);
            if (allExpenses == null)
                allExpenses = new ArrayList<>();
            calculateWeeklySpending(allExpenses);
            weeklyChartPanel.repaint();

            // 5. Recent List
            allExpenses.sort((e1, e2) -> e2.getDate().compareTo(e1.getDate()));
            recentExpenses = allExpenses.subList(0, Math.min(3, allExpenses.size()));
            updateRecentTransactions();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculateWeeklySpending(List<Expense> expenses) {
        weeklySpending = new double[7];
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        for (Expense expense : expenses) {
            if (expense.getDate() != null) {
                LocalDate expenseDate = expense.getDate().toLocalDate();
                if (!expenseDate.isBefore(monday) && !expenseDate.isAfter(monday.plusDays(6))) {
                    int dayIndex = expenseDate.getDayOfWeek().getValue() - 1;
                    weeklySpending[dayIndex] += expense.getAmount();
                }
            }
        }
    }

    public void clearData() {
        greetingLabel.setText("Good Morning");
        balanceLabel.setText("$0");
        budgetLimitLabel.setText("$0");
        spentAmountLabel.setText("$0");
        budgetProgressBar.setValue(0);
        weeklySpending = new double[7];
        recentExpenses.clear();
        updateRecentTransactions();
        weeklyChartPanel.repaint();
    }
}
