<<<<<<< HEAD
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

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

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!active) {
                    btn.setBackground(new Color(245, 245, 245));
                    btn.setOpaque(true);
                }
            }

            public void mouseExited(MouseEvent e) {
                if (!active) {
                    btn.setBackground(Color.WHITE);
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
        RoundedButton addBtn = new RoundedButton("➕ Add Expense", 15);
        addBtn.setBounds(970, 120, 130, 40);
        addBtn.setFont(new Font("Arial", Font.BOLD, 14));
        addBtn.setBackground(UIConstants.PRIMARY_GREEN);
        addBtn.setForeground(Color.WHITE);
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

        RoundedButton saveBtn = new RoundedButton("Add Expense", 15);
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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;

import com.spendwise.models.Budget;
import com.spendwise.models.Expense;
import com.spendwise.models.User;
import com.spendwise.services.BudgetService;
import com.spendwise.services.expenseService;
import com.spendwise.view.components.RoundedButton;
import com.spendwise.view.components.RoundedPanel;
import com.spendwise.view.components.RoundedTextField;
import com.spendwise.view.components.SidebarPanel;

public class ExpensesPanel extends JPanel {

    private MainFrame mainFrame;
    private User currentUser;
    private SidebarPanel sidebarPanel;

    // UI Components
    private JPanel transactionsContainer;
    private JComboBox<String> categoryFilter;
    private RoundedTextField searchField;
    private JLayeredPane layeredPane; 
    private JButton fabBtn;

    // Services & Data
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
        setBackground(UIConstants.BACKGROUND_LIGHT);

        // Sidebar
        sidebarPanel = new SidebarPanel("EXPENSES",
                panelName -> mainFrame.showPanel(panelName),
                () -> mainFrame.logout());
        add(sidebarPanel, BorderLayout.WEST);

        // Content Area with LayeredPane for FAB
        createContent();

        // Initial data load
        refreshData();

        // Handle resizing for FAB
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateFABPosition();
            }
        });
    }

    private void createContent() {
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        add(layeredPane, BorderLayout.CENTER);

        // Main Content Panel (Scrollable)
        JPanel mainContent = new JPanel();
        mainContent.setLayout(null);
        mainContent.setBackground(UIConstants.BACKGROUND_LIGHT);
        mainContent.setBounds(0, 0, 1200, 900); // Initial size, will be handled by layout manager typically but
                                                // JLayedPane needs bounds

        // We need the mainContent to fill the layeredPane
        layeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                mainContent.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
                updateFABPosition();
            }
        });

        // Header
        JLabel header = new JLabel("Expenses");
        header.setBounds(30, 30, 400, 40);
        header.setFont(new Font("Arial", Font.BOLD, 28));
        mainContent.add(header);

        JLabel subHeader = new JLabel("Track and manage your transactions");
        subHeader.setBounds(30, 70, 400, 20);
        subHeader.setFont(new Font("Arial", Font.PLAIN, 14));
        subHeader.setForeground(Color.GRAY);
        mainContent.add(subHeader);

        // Controls Area
        // Search
        searchField = new RoundedTextField(20);
        searchField.setPlaceholder("Search transactions...");
        searchField.setBounds(30, 110, 800, 45);
        searchField.addActionListener(e -> filterExpenses());
        mainContent.add(searchField);

        // Filter
        String[] categories = { "All Categories", "Food", "Transport", "Shopping", "Health", "Entertainment" };
        categoryFilter = new JComboBox<>(categories);
        categoryFilter.setBounds(850, 110, 250, 45);
        categoryFilter.setFont(new Font("Arial", Font.PLAIN, 14));
        categoryFilter.setBackground(Color.WHITE);
        categoryFilter.addActionListener(e -> filterExpenses());
        mainContent.add(categoryFilter);

        // Calendar Button (Visual only based on image)
        JButton calendarBtn = new JButton("📅");
        calendarBtn.setBounds(1110, 110, 45, 45);
        calendarBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        calendarBtn.setBackground(Color.WHITE);
        calendarBtn.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        calendarBtn.setFocusPainted(false);
        mainContent.add(calendarBtn);

        // Transactions Scroll List
        transactionsContainer = new JPanel();
        transactionsContainer.setLayout(new BoxLayout(transactionsContainer, BoxLayout.Y_AXIS));
        transactionsContainer.setBackground(UIConstants.BACKGROUND_LIGHT);

        JScrollPane scrollPane = new JScrollPane(transactionsContainer);
        scrollPane.setBounds(30, 170, 1125, 600); // Adjust height
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(UIConstants.BACKGROUND_LIGHT);
        mainContent.add(scrollPane);

        layeredPane.add(mainContent, JLayeredPane.DEFAULT_LAYER);

        // Floating Action Button
        fabBtn = new JButton("+") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillOval(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        fabBtn.setFont(new Font("Arial", Font.PLAIN, 30));
        fabBtn.setForeground(Color.WHITE);
        fabBtn.setBackground(UIConstants.SELECTION_GREEN);
        fabBtn.setBorderPainted(false);
        fabBtn.setFocusPainted(false);
        fabBtn.setContentAreaFilled(false);
        fabBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fabBtn.addActionListener(e -> showAddExpenseDialog());

        // Add FAB to POPUP layer
        layeredPane.add(fabBtn, JLayeredPane.POPUP_LAYER);
    }

    private void updateFABPosition() {
        if (layeredPane != null && fabBtn != null) {
            int x = layeredPane.getWidth() - 90;
            int y = layeredPane.getHeight() - 90;
            fabBtn.setBounds(x, y, 60, 60);
        }
    }

    // --- Add Expense Dialog ---
    private void showAddExpenseDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Expense", true);
        dialog.setSize(450, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Close button (X) top right - usually handled by Window decorations but image
        // shows it inside.
        // We'll trust native window decoration for Close for now to keep it standard
        // swing.

        // Amount
        JLabel amountLbl = new JLabel("Amount");
        amountLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        amountLbl.setFont(new Font("Arial", Font.BOLD, 14));
        content.add(amountLbl);
        content.add(Box.createVerticalStrut(5));

        RoundedTextField amountField = new RoundedTextField(15);
        amountField.setPlaceholder("0.00");
        amountField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        amountField.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(amountField);
        content.add(Box.createVerticalStrut(15));

        // Category
        JLabel catLbl = new JLabel("Category");
        catLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        catLbl.setFont(new Font("Arial", Font.BOLD, 14));
        content.add(catLbl);
        content.add(Box.createVerticalStrut(5));

        String[] cats = { "Select category", "Food", "Transport", "Shopping", "Health", "Entertainment" };
        JComboBox<String> catBox = new JComboBox<>(cats);
        catBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        catBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        catBox.setBackground(Color.WHITE);
        content.add(catBox);
        content.add(Box.createVerticalStrut(15));

        // Description
        JLabel descLbl = new JLabel("Description");
        descLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        descLbl.setFont(new Font("Arial", Font.BOLD, 14));
        content.add(descLbl);
        content.add(Box.createVerticalStrut(5));

        RoundedTextField descField = new RoundedTextField(15);
        descField.setPlaceholder("What did you buy?");
        descField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        descField.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(descField);
        content.add(Box.createVerticalStrut(25));

        // Add Button
        RoundedButton addBtn = new RoundedButton("Add Expense", 20, UIConstants.SELECTION_GREEN,
                UIConstants.darker(UIConstants.SELECTION_GREEN));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFont(new Font("Arial", Font.BOLD, 15));
        addBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        addBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        addBtn.addActionListener(e -> {
            try {
                String amountText = amountField.getText();
                double amount = Double.parseDouble(amountText);
                String cat = (String) catBox.getSelectedItem();
                String desc = descField.getText();

                if (amount <= 0 || cat.equals("Select category")) {
                    JOptionPane.showMessageDialog(dialog, "Please enter valid details", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int userId = UserSession.getCurrentUserId();
                Expense newExp = new Expense(userId, 0, cat, desc, amount,
                        new java.sql.Date(System.currentTimeMillis()));

                if (expenseServiceInstance.addExpense(newExp)) {
                    // Update budget if exists
                    if (currentBudget != null) {
                        currentBudget.addExpense(newExp);
                        budgetService.updateBudgetList(currentBudget);
                    }
                    dialog.dispose();
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to add expense", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        content.add(addBtn);

        dialog.add(content, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    // --- List View ---

    private void filterExpenses() {
        String cat = (String) categoryFilter.getSelectedItem();
        String query = searchField.getText().toLowerCase();

        List<Expense> filtered = currentExpenses.stream().filter(e -> {
            boolean matchesCat = cat.equals("All Categories")
                    || (e.getCategory() != null && e.getCategory().equals(cat));
            boolean matchesSearch = query.isEmpty() || e.getDescription().toLowerCase().contains(query);
            return matchesCat && matchesSearch;
        }).collect(Collectors.toList());

        populateList(filtered);
    }

    // Updated Date Grouping
    private void populateList(List<Expense> expenses) {
        transactionsContainer.removeAll();

        if (expenses.isEmpty()) {
            transactionsContainer.add(new JLabel("No expenses found"));
            transactionsContainer.repaint();
            transactionsContainer.revalidate();
            return;
        }

        String lastDateHeader = "";

        for (Expense exp : expenses) {
            String dateStr = formatDate(exp.getDate());

            if (!dateStr.equals(lastDateHeader)) {
                transactionsContainer.add(Box.createVerticalStrut(20));

                JLabel dateLbl = new JLabel(dateStr);
                dateLbl.setFont(new Font("Arial", Font.PLAIN, 13));
                dateLbl.setForeground(Color.GRAY);

                JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
                p.setBackground(UIConstants.BACKGROUND_LIGHT);
                p.add(dateLbl);
                p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
                transactionsContainer.add(p);

                lastDateHeader = dateStr;
            }

            transactionsContainer.add(createExpenseCard(exp));
            transactionsContainer.add(Box.createVerticalStrut(10));
        }

        transactionsContainer.revalidate();
        transactionsContainer.repaint();
    }

    private JPanel createExpenseCard(Expense exp) {
        RoundedPanel card = new RoundedPanel(15, Color.WHITE);
        card.setLayout(null);
        card.setPreferredSize(new Dimension(1000, 70));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        // Icon
        JLabel icon = new JLabel(getCategoryEmoji(exp.getCategory()));
        icon.setFont(new Font("Arial", Font.PLAIN, 24));
        icon.setBounds(20, 15, 40, 40);
        card.add(icon);

        // Title
        JLabel title = new JLabel(exp.getDescription());
        title.setFont(new Font("Arial", Font.BOLD, 15));
        title.setBounds(70, 15, 400, 20);
        card.add(title);

        // Subtitle
        String timeStr = (exp.getDate() != null) ? exp.getDate().toString() : ""; // Could format better
        JLabel subtitle = new JLabel(exp.getCategory() + " • " + timeStr);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitle.setForeground(Color.GRAY);
        subtitle.setBounds(70, 38, 400, 20);
        card.add(subtitle);

        // Amount
        JLabel amount = new JLabel(String.format("-$%.2f", exp.getAmount()));
        amount.setFont(new Font("Arial", Font.BOLD, 15));
        amount.setForeground(UIConstants.DANGER_RED);
        amount.setHorizontalAlignment(SwingConstants.RIGHT);

        // We can't use simple setBounds for right aligned unless we know parent width.
        // But JPanel in BoxLayout has width. Let's assume ~1100 minus padding.
        // Better to use LayoutManager, but setBounds simple for now.
        // Dynamically find right edge? No, standard placement.
        amount.setBounds(980, 25, 100, 20);
        // Wait, width is variable.
        // Let's use BorderLayout for the card internal structure to play nice with
        // resizing.
        card.setLayout(new BorderLayout());

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        left.setOpaque(false);
        left.add(icon);

        JPanel center = new JPanel(new GridLayout(2, 1));
        center.setOpaque(false);
        center.add(title);
        center.add(subtitle);
        left.add(center);

        card.add(left, BorderLayout.CENTER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        right.setOpaque(false);
        right.add(amount);
        card.add(right, BorderLayout.EAST);

        return card;
    }

    private String formatDate(java.sql.Date date) {
        if (date == null)
            return "Unknown";
        LocalDate d = date.toLocalDate();
        LocalDate today = LocalDate.now();
        if (d.equals(today))
            return "Today, " + d.getYear(); // Simplified
        if (d.equals(today.minusDays(1)))
            return "Yesterday";
        return d.getMonth() + " " + d.getDayOfMonth() + ", " + d.getYear();
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
        sidebarPanel.updateUser();
        int uid = UserSession.getCurrentUserId();
        currentBudget = budgetService.getSpesificUserBudget(uid);
        currentExpenses = expenseServiceInstance.getExpensesOfTheUser(uid);
        if (currentExpenses == null)
            currentExpenses = new ArrayList<>();

        // Sort Sort by date desc
        currentExpenses.sort((e1, e2) -> e2.getDate().compareTo(e1.getDate()));

        filterExpenses();
    }

    public void clearData() {
        currentExpenses.clear();
        transactionsContainer.removeAll();
        transactionsContainer.repaint();
    }
}
>>>>>>> origin/main
