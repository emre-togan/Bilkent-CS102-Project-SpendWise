package com.spendwise.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import com.spendwise.models.Expense;
import com.spendwise.services.expenseService;

public class ExpensesPanel extends JPanel {

    private MainFrame mainFrame;
    private JPanel transactionsContainer;
    private JComboBox<String> categoryFilter;
    private JTextField searchField;
    
    private expenseService expenseServiceInstance;
    private List<Expense> currentExpenses;

    public ExpensesPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.expenseServiceInstance = new expenseService();
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
        addMenuItem(sideMenu, "💳", "Budget", startY + 60, false);
        addMenuItem(sideMenu, "🧾", "Expenses", startY + 120, true);
        addMenuItem(sideMenu, "🛍️", "Shop", startY + 180, false);
        addMenuItem(sideMenu, "💬", "Chat", startY + 240, false);
        addMenuItem(sideMenu, "👤", "Profile", startY + 300, false);
        addMenuItem(sideMenu, "⚙️", "Settings", startY + 360, false);

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

        JButton logoutBtn = new JButton("↩︎ Logout");
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

        btn.addActionListener(e -> mainFrame.showPanel(text.toUpperCase()));
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

        // Search and Filter Bar
        searchField = new JTextField("Search transactions...");
        searchField.setBounds(30, 120, 300, 40);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        content.add(searchField);

        String[] categories = {"All Categories", "Food", "Transport", "Shopping", "Health", "Entertainment"};
        categoryFilter = new JComboBox<>(categories);
        categoryFilter.setBounds(350, 120, 200, 40);
        categoryFilter.setFont(new Font("Arial", Font.PLAIN, 14));
        categoryFilter.addActionListener(e -> filterExpenses());
        content.add(categoryFilter);

        // Add Expense Button
        JButton addBtn = new JButton("➕ Add Expense");
        addBtn.setBounds(970, 120, 130, 40);
        addBtn.setFont(new Font("Arial", Font.BOLD, 14));
        addBtn.setBackground(UIConstants.PRIMARY_GREEN);
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.setBorderPainted(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.addActionListener(e -> showAddExpenseDialog());
        content.add(addBtn);

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
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);

        formPanel.add(new JLabel("Amount:"));
        JTextField amountField = new JTextField();
        formPanel.add(amountField);

        formPanel.add(new JLabel("Category:"));
        String[] categories = {"Food", "Transport", "Shopping", "Health", "Entertainment"};
        JComboBox<String> categoryBox = new JComboBox<>(categories);
        formPanel.add(categoryBox);

        formPanel.add(new JLabel("Description:"));
        JTextField descField = new JTextField();
        formPanel.add(descField);

        JButton saveBtn = new JButton("Add Expense");
        saveBtn.setBackground(UIConstants.PRIMARY_GREEN);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String category = (String) categoryBox.getSelectedItem();
                String description = descField.getText();
                
                // Create new expense
                Expense newExpense = new Expense(
                    0, // expenseId will be set by database
                    category,
                    description,
                    amount,
                    new java.sql.Date(System.currentTimeMillis())
                );
                
                // TODO: Add backend call
                // expenseServiceInstance.addExpense(newExpense);
                
                dialog.dispose();
                refreshData();
                JOptionPane.showMessageDialog(this, "Expense added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(saveBtn);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void filterExpenses() {
        String selectedCategory = (String) categoryFilter.getSelectedItem();
        displayExpenses(selectedCategory);
    }

    private void displayExpenses(String categoryFilter) {
        transactionsContainer.removeAll();

        List<Expense> expensesToShow = currentExpenses;
        
        // Filter by category if not "All Categories"
        if (categoryFilter != null && !categoryFilter.equals("All Categories")) {
            expensesToShow = new ArrayList<>();
            for (Expense exp : currentExpenses) {
                if (exp.getCategory().equals(categoryFilter)) {
                    expensesToShow.add(exp);
                }
            }
        }

        // Display expenses
        if (expensesToShow.isEmpty()) {
            JLabel emptyLabel = new JLabel("No expenses found");
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            transactionsContainer.add(Box.createVerticalStrut(50));
            transactionsContainer.add(emptyLabel);
        } else {
            for (Expense expense : expensesToShow) {
                transactionsContainer.add(createExpenseItem(expense));
                transactionsContainer.add(Box.createVerticalStrut(10));
            }
        }

        transactionsContainer.revalidate();
        transactionsContainer.repaint();
    }

    private JPanel createExpenseItem(Expense expense) {
        JPanel item = new JPanel();
        item.setLayout(new BorderLayout(15, 0));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(15, 15, 15, 15)
        ));

        // Left: Icon + Info
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
        
        JLabel categoryLabel = new JLabel(expense.getCategory() + " • " + expense.getDate());
        categoryLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        categoryLabel.setForeground(new Color(120, 120, 120));

        infoPanel.add(descLabel);
        infoPanel.add(categoryLabel);

        leftPanel.add(icon, BorderLayout.WEST);
        leftPanel.add(infoPanel, BorderLayout.CENTER);

        // Right: Amount
        JLabel amountLabel = new JLabel(String.format("-$%.2f", expense.getAmount()));
        amountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        amountLabel.setForeground(UIConstants.DANGER_RED);

        item.add(leftPanel, BorderLayout.CENTER);
        item.add(amountLabel, BorderLayout.EAST);

        return item;
    }

    private String getCategoryEmoji(String category) {
        switch (category) {
            case "Food": return "🍔";
            case "Transport": return "🚗";
            case "Shopping": return "🛍";
            case "Health": return "🏥";
            case "Entertainment": return "🎬";
            default: return "📋";
        }
    }

    /**
     * Refresh data from backend - called by MainFrame
     */
    public void refreshData() {
        try {
            // TODO: Get current user ID properly
            int userId = UserSession.getCurrentUserId();
            
            // Load expenses from backend
            currentExpenses = expenseServiceInstance.getExpensesOfTheUser(userId);
            
            if (currentExpenses == null) {
                currentExpenses = new ArrayList<>();
            }
            
            // Display all expenses
            displayExpenses("All Categories");
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error loading expenses: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            currentExpenses = new ArrayList<>();
            displayExpenses("All Categories");
        }
    }

    /**
     * Clear all data - called on logout
     */
    public void clearData() {
        currentExpenses.clear();
        transactionsContainer.removeAll();
        
        JLabel emptyLabel = new JLabel("Please login to view expenses");
        emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        emptyLabel.setForeground(Color.GRAY);
        emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        transactionsContainer.add(Box.createVerticalStrut(50));
        transactionsContainer.add(emptyLabel);
        
        transactionsContainer.revalidate();
        transactionsContainer.repaint();
        
        // Reset filters
        if (categoryFilter != null) {
            categoryFilter.setSelectedIndex(0);
        }
        if (searchField != null) {
            searchField.setText("Search transactions...");
        }
    }
}
