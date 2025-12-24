package com.spendwise.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.spendwise.models.Budget;
import com.spendwise.models.Expense;
import com.spendwise.models.User;
import com.spendwise.services.BudgetService;
import com.spendwise.services.expenseService;

public class ExpensesPanel extends JPanel {

    private MainFrame mainFrame;
    private User currentUser;
    private JPanel transactionsContainer;
    private JComboBox<String> categoryFilter;
    private JTextField searchField;
    private JButton addExpenseBtn;

    // Sidebar Live References
    private JLabel sidebarNameLabel;
    private JLabel sidebarEmailLabel;
    private JLabel sidebarAvatarLabel;

    private expenseService expenseServiceInstance;
    private BudgetService budgetService;
    private List<Expense> currentExpenses;
    private Budget currentBudget;

    public ExpensesPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.expenseServiceInstance = new expenseService();
        this.budgetService = new BudgetService();
        this.currentExpenses = new ArrayList<>();

        setLayout(new BorderLayout());
        setBackground(new Color(250, 250, 250));

        add(createSideMenu(), BorderLayout.WEST);
        add(createContent(), BorderLayout.CENTER);

        // Initial data load
        refreshData();
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
        addMenuItem(sideMenu, "🏠", "Dashboard", "DASHBOARD", startY, false);
        addMenuItem(sideMenu, "💳", "Budget", "BUDGET", startY + 60, false);
        addMenuItem(sideMenu, "🧾", "Expenses", "EXPENSES", startY + 120, true);
        addMenuItem(sideMenu, "🛍️", "Shop", "SHOP", startY + 180, false);
        addMenuItem(sideMenu, "💬", "Chat", "CHAT", startY + 240, false);
        addMenuItem(sideMenu, "👤", "Profile", "PROFILE", startY + 300, false);
        addMenuItem(sideMenu, "⚙️", "Settings", "SETTINGS", startY + 360, false);

        // Profile card
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

        User u = UserSession.getCurrentUser();
        String name = (u != null) ? u.getUserName() : "Guest";
        String email = (u != null) ? u.geteMail() : "";

        // Initialize and keep references to update later
        sidebarAvatarLabel = new JLabel(getInitials(name));
        sidebarAvatarLabel.setBounds(15, 15, 40, 40);
        sidebarAvatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebarAvatarLabel.setOpaque(true);
        sidebarAvatarLabel.setBackground(UIConstants.PRIMARY_GREEN);
        sidebarAvatarLabel.setForeground(Color.WHITE);
        sidebarAvatarLabel.setFont(new Font("Arial", Font.BOLD, 16));
        profileCard.add(sidebarAvatarLabel);

        sidebarNameLabel = new JLabel(name);
        sidebarNameLabel.setBounds(65, 18, 150, 18);
        sidebarNameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        profileCard.add(sidebarNameLabel);

        sidebarEmailLabel = new JLabel(email);
        sidebarEmailLabel.setBounds(65, 37, 150, 15);
        sidebarEmailLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        sidebarEmailLabel.setForeground(new Color(120, 120, 120));
        profileCard.add(sidebarEmailLabel);

        return profileCard;
    }

    // New Method: Updates the sidebar with the latest logged-in user info
    private void updateSidebarUser() {
        User u = UserSession.getCurrentUser();
        if (u != null) {
            if (sidebarNameLabel != null)
                sidebarNameLabel.setText(u.getUserName());
            if (sidebarEmailLabel != null)
                sidebarEmailLabel.setText(u.geteMail());
            if (sidebarAvatarLabel != null)
                sidebarAvatarLabel.setText(getInitials(u.getUserName()));
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

    private void addMenuItem(JPanel parent, String emoji, String text, String panelKey, int y, boolean active) {
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

        btn.addActionListener(e -> mainFrame.showPanel(panelKey));
        parent.add(btn);
    }

    private JPanel createContent() {
        JPanel content = new JPanel();
        content.setLayout(null);
        content.setBackground(new Color(250, 250, 250));

        // Header
        JLabel header = new JLabel("Expenses");
        header.setBounds(30, 30, 400, 40);
        header.setFont(new Font("Arial", Font.BOLD, 28));
        content.add(header);

        JLabel subHeader = new JLabel("Track and manage your transactions");
        subHeader.setBounds(30, 75, 400, 20);
        subHeader.setFont(new Font("Arial", Font.PLAIN, 14));
        subHeader.setForeground(new Color(120, 120, 120));
        content.add(subHeader);

        // Search Field
        searchField = new JTextField("Search transactions...");
        searchField.setBounds(30, 120, 300, 40);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setForeground(Color.GRAY);

        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals("Search transactions...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search transactions...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });
        content.add(searchField);

        // Category Filter
        String[] categories = { "All Categories", "Food", "Transport", "Shopping", "Health", "Entertainment" };
        categoryFilter = new JComboBox<>(categories);
        categoryFilter.setBounds(350, 120, 200, 40);
        categoryFilter.setFont(new Font("Arial", Font.PLAIN, 14));
        categoryFilter.addActionListener(e -> filterExpenses());
        content.add(categoryFilter);

        // Add Expense Button
        addExpenseBtn = new JButton("➕ Add Expense");
        addExpenseBtn.setBounds(970, 120, 130, 40);
        addExpenseBtn.setFont(new Font("Arial", Font.BOLD, 14));
        addExpenseBtn.setBackground(UIConstants.PRIMARY_GREEN);
        addExpenseBtn.setForeground(Color.WHITE);
        addExpenseBtn.setFocusPainted(false);
        addExpenseBtn.setBorderPainted(false);
        addExpenseBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addExpenseBtn.addActionListener(e -> showAddExpenseDialog());
        content.add(addExpenseBtn);

        // Transactions Container
        transactionsContainer = new JPanel();
        transactionsContainer.setLayout(new BoxLayout(transactionsContainer, BoxLayout.Y_AXIS));
        transactionsContainer.setBackground(new Color(250, 250, 250));

        JScrollPane scrollPane = new JScrollPane(transactionsContainer);
        scrollPane.setBounds(30, 180, 1070, 500);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        content.add(scrollPane);

        return content;
    }

    private void showAddExpenseDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Expense", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel amountLabel = new JLabel("Amount");
        amountLabel.setFont(new Font("Arial", Font.BOLD, 13));
        amountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(amountLabel);
        mainPanel.add(Box.createVerticalStrut(8));

        JTextField amountField = new JTextField();
        amountField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        amountField.setFont(new Font("Arial", Font.PLAIN, 16));
        amountField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(8, 12, 8, 12)));
        mainPanel.add(amountField);
        mainPanel.add(Box.createVerticalStrut(20));

        JLabel categoryLabel = new JLabel("Category");
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 13));
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(categoryLabel);
        mainPanel.add(Box.createVerticalStrut(8));

        String[] categories = { "Select category", "Food", "Transport", "Shopping", "Health", "Entertainment" };
        JComboBox<String> categoryBox = new JComboBox<>(categories);
        categoryBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        categoryBox.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(categoryBox);
        mainPanel.add(Box.createVerticalStrut(20));

        JLabel descLabel = new JLabel("Description");
        descLabel.setFont(new Font("Arial", Font.BOLD, 13));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(descLabel);
        mainPanel.add(Box.createVerticalStrut(8));

        JTextField descField = new JTextField();
        descField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        descField.setFont(new Font("Arial", Font.PLAIN, 14));
        descField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(8, 12, 8, 12)));
        mainPanel.add(descField);
        mainPanel.add(Box.createVerticalStrut(30));

        JButton addBtn = new JButton("Add Expense");
        addBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        addBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addBtn.setFont(new Font("Arial", Font.BOLD, 15));
        addBtn.setBackground(UIConstants.PRIMARY_GREEN);
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.setBorderPainted(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        addBtn.addActionListener(e -> {
            try {
                if (amountField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please enter an amount!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double amount = Double.parseDouble(amountField.getText().trim());

                if (amount <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Amount must be greater than 0!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String category = (String) categoryBox.getSelectedItem();
                if (category == null || category.equals("Select category")) {
                    JOptionPane.showMessageDialog(dialog, "Please select a category!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String description = descField.getText().trim();
                if (description.isEmpty()) {
                    description = category + " expense";
                }

                int userId = UserSession.getCurrentUserId();
                Expense newExpense = new Expense(
                        userId,
                        0,
                        category,
                        description,
                        amount,
                        new java.sql.Date(System.currentTimeMillis()));

                boolean success = expenseServiceInstance.addExpense(newExpense);

                if (success) {
                    if (currentBudget != null) {
                        currentBudget.addExpense(newExpense);
                        budgetService.updateBudgetList(currentBudget);
                    }
                    JOptionPane.showMessageDialog(dialog, "Expense added successfully!");
                    dialog.dispose();
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to add expense!", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid amount!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        mainPanel.add(addBtn);
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void filterExpenses() {
        String selectedCategory = (String) categoryFilter.getSelectedItem();
        displayExpenses(selectedCategory);
    }

    private void displayExpenses(String categoryFilter) {
        transactionsContainer.removeAll();
        List<Expense> expensesToShow = currentExpenses;

        if (categoryFilter != null && !categoryFilter.equals("All Categories")) {
            expensesToShow = new ArrayList<>();
            for (Expense exp : currentExpenses) {
                if (exp.getCategory() != null && exp.getCategory().equals(categoryFilter)) {
                    expensesToShow.add(exp);
                }
            }
        }

        String currentDateHeader = null;

        if (expensesToShow.isEmpty()) {
            JLabel emptyLabel = new JLabel("No expenses found");
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            transactionsContainer.add(Box.createVerticalStrut(50));
            transactionsContainer.add(emptyLabel);
        } else {
            for (Expense expense : expensesToShow) {
                String expenseDate = formatDate(expense.getDate());
                if (!expenseDate.equals(currentDateHeader)) {
                    if (currentDateHeader != null) {
                        transactionsContainer.add(Box.createVerticalStrut(15));
                    }
                    JLabel dateHeader = new JLabel(expenseDate);
                    dateHeader.setFont(new Font("Arial", Font.BOLD, 13));
                    dateHeader.setForeground(new Color(100, 100, 100));
                    dateHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
                    transactionsContainer.add(dateHeader);
                    transactionsContainer.add(Box.createVerticalStrut(8));
                    currentDateHeader = expenseDate;
                }
                transactionsContainer.add(createExpenseItem(expense));
                transactionsContainer.add(Box.createVerticalStrut(10));
            }
        }
        transactionsContainer.revalidate();
        transactionsContainer.repaint();
    }

    private String formatDate(java.sql.Date date) {
        if (date == null)
            return "Unknown Date";
        LocalDate expenseDate = date.toLocalDate();
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        if (expenseDate.equals(today))
            return "Today";
        else if (expenseDate.equals(yesterday))
            return "Yesterday";
        else
            return expenseDate.getMonth().toString().substring(0, 3) + " " + expenseDate.getDayOfMonth() + ", "
                    + expenseDate.getYear();
    }

    private JPanel createExpenseItem(Expense expense) {
        JPanel item = new JPanel();
        item.setLayout(new BorderLayout(15, 0));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(15, 15, 15, 15)));

        JPanel leftPanel = new JPanel(new BorderLayout(10, 0));
        leftPanel.setOpaque(false);
        String emoji = getCategoryEmoji(expense.getCategory());
        JLabel icon = new JLabel(emoji);
        icon.setFont(new Font("Arial", Font.PLAIN, 28));
        icon.setPreferredSize(new Dimension(40, 40));

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setOpaque(false);
        JLabel descLabel = new JLabel(expense.getDescription());
        descLabel.setFont(new Font("Arial", Font.BOLD, 14));
        String timeStr = expense.getDate() != null ? formatTime(expense.getDate()) : "Unknown time";
        JLabel categoryLabel = new JLabel(expense.getCategory() + " • " + timeStr);
        categoryLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        categoryLabel.setForeground(new Color(120, 120, 120));

        infoPanel.add(descLabel);
        infoPanel.add(categoryLabel);
        leftPanel.add(icon, BorderLayout.WEST);
        leftPanel.add(infoPanel, BorderLayout.CENTER);

        JLabel amountLabel = new JLabel(String.format("-$%.2f", expense.getAmount()));
        amountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        amountLabel.setForeground(UIConstants.DANGER_RED);

        item.add(leftPanel, BorderLayout.CENTER);
        item.add(amountLabel, BorderLayout.EAST);
        return item;
    }

    private String formatTime(java.sql.Date date) {
        if (date == null)
            return "";
        LocalDate expenseDate = date.toLocalDate();
        LocalDate today = LocalDate.now();
        if (expenseDate.equals(today))
            return "Today";
        else
            return expenseDate.getMonth().toString().substring(0, 3) + " " + expenseDate.getDayOfMonth();
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
        // IMPORTANT: Update Sidebar Info whenever panel is refreshed
        updateSidebarUser();

        try {
            int userId = UserSession.getCurrentUserId();
            currentBudget = budgetService.getSpesificUserBudget(userId);
            currentExpenses = expenseServiceInstance.getExpensesOfTheUser(userId);
            if (currentExpenses == null)
                currentExpenses = new ArrayList<>();

            currentExpenses.sort((e1, e2) -> {
                if (e1.getDate() == null)
                    return 1;
                if (e2.getDate() == null)
                    return -1;
                return e2.getDate().compareTo(e1.getDate());
            });

            displayExpenses("All Categories");

        } catch (Exception e) {
            e.printStackTrace();
            currentExpenses = new ArrayList<>();
            displayExpenses("All Categories");
        }
    }

    public void clearData() {
        currentExpenses.clear();
        transactionsContainer.removeAll();
        transactionsContainer.revalidate();
        transactionsContainer.repaint();

        if (categoryFilter != null)
            categoryFilter.setSelectedIndex(0);
        if (searchField != null) {
            searchField.setText("Search transactions...");
            searchField.setForeground(Color.GRAY);
        }
    }
}