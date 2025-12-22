import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class ExpensesPanel extends JPanel {
    
    private MainFrame mainFrame;
    private JTextField searchField;
    private JComboBox<String> categoryDropdown;
    private JPanel expenseListPanel;
    
    public ExpensesPanel(MainFrame mainFrame) {
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
        addMenuItem(sideMenu, "💰", "Budget", startY + 60, false);
        addMenuItem(sideMenu, "📋", "Expenses", startY + 120, true);
        addMenuItem(sideMenu, "🛒", "Shop", startY + 180, false);
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
    
    private void addMenuItem(JPanel parent, String icon, String text, int y, boolean selected) {
        JButton btn = new JButton(icon + "  " + text);
        btn.setBounds(10, y, 240, 50);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(0, 15, 0, 0));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (selected) {
            btn.setBackground(UIConstants.PRIMARY_GREEN);
            btn.setForeground(Color.WHITE);
            btn.setOpaque(true);
        } else {
            btn.setContentAreaFilled(false);
            btn.setForeground(new Color(80, 80, 80));
        }
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!selected) {
                    btn.setBackground(new Color(245, 245, 245));
                    btn.setOpaque(true);
                }
            }
            public void mouseExited(MouseEvent e) {
                if (!selected) {
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
        
        JLabel header = new JLabel("Expenses");
        header.setBounds(30, 30, 400, 40);
        header.setFont(new Font("Arial", Font.BOLD, 28));
        content.add(header);
        
        JLabel subHeader = new JLabel("Track and manage your transactions");
        subHeader.setBounds(30, 75, 400, 20);
        subHeader.setFont(new Font("Arial", Font.PLAIN, 14));
        subHeader.setForeground(new Color(120, 120, 120));
        content.add(subHeader);
        
        createSearchBar(content);
        
        createFilterBar(content);
        
        createExpenseList(content);
        
        createAddExpenseFAB(content);
        
        return content;
    }
    
    private void createSearchBar(JPanel parent) {
        JPanel searchPanel = new JPanel();
        searchPanel.setBounds(30, 130, 1070, 55);
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setLayout(null);
        searchPanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setBounds(20, 15, 30, 25);
        searchIcon.setFont(new Font("Arial", Font.PLAIN, 20));
        searchPanel.add(searchIcon);
        
        searchField = new JTextField("Search transactions...");
        searchField.setBounds(60, 12, 980, 30);
        searchField.setFont(new Font("Arial", Font.PLAIN, 15));
        searchField.setForeground(new Color(160, 160, 160));
        searchField.setBorder(BorderFactory.createEmptyBorder());
        
        searchField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search transactions...")) {
                    searchField.setText("");
                    searchField.setForeground(new Color(50, 50, 50));
                }
            }
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search transactions...");
                    searchField.setForeground(new Color(160, 160, 160));
                }
            }
        });
        
        searchPanel.add(searchField);
        parent.add(searchPanel);
    }
    
    private void createFilterBar(JPanel parent) {
        JPanel filterPanel = new JPanel();
        filterPanel.setBounds(30, 205, 1070, 55);
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setLayout(null);
        filterPanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        
        String[] categories = {"All Categories", "Food", "Transport", "Shopping", "Entertainment", "Health"};
        categoryDropdown = new JComboBox<>(categories);
        categoryDropdown.setBounds(20, 12, 250, 30);
        categoryDropdown.setFont(new Font("Arial", Font.PLAIN, 14));
        categoryDropdown.setBackground(Color.WHITE);
        categoryDropdown.setFocusable(false);
        filterPanel.add(categoryDropdown);
        
        JButton dateFilterBtn = new JButton("📅");
        dateFilterBtn.setBounds(1020, 12, 35, 30);
        dateFilterBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        dateFilterBtn.setFocusPainted(false);
        dateFilterBtn.setBorderPainted(false);
        dateFilterBtn.setContentAreaFilled(false);
        dateFilterBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        filterPanel.add(dateFilterBtn);
        
        parent.add(filterPanel);
    }
    
    private void createExpenseList(JPanel parent) {
        expenseListPanel = new JPanel();
        expenseListPanel.setLayout(new BoxLayout(expenseListPanel, BoxLayout.Y_AXIS));
        expenseListPanel.setBackground(new Color(250, 250, 250));
        
        JScrollPane scrollPane = new JScrollPane(expenseListPanel);
        scrollPane.setBounds(30, 280, 1070, 540);
        scrollPane.setBorder(null);
        scrollPane.setBackground(new Color(250, 250, 250));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        addSampleExpenses();
        
        parent.add(scrollPane);
    }
    
    private void addSampleExpenses() {
        expenseListPanel.add(createDateSeparator("Yesterday"));
        expenseListPanel.add(Box.createVerticalStrut(10));
        
        String[][] yesterdayExpenses = {
            {"🛒", "Whole Foods Market", "Food • 14:30", "-$85.50"},
            {"🚗", "Uber Trip", "Transport • 09:15", "-$12.30"}
        };
        
        for (String[] exp : yesterdayExpenses) {
            expenseListPanel.add(createExpenseItem(exp[0], exp[1], exp[2], exp[3]));
            expenseListPanel.add(Box.createVerticalStrut(10));
        }
        
        expenseListPanel.add(Box.createVerticalStrut(10));
        
        expenseListPanel.add(createDateSeparator("Nov 8, 2025"));
        expenseListPanel.add(Box.createVerticalStrut(10));
        
        String[][] nov8Expenses = {
            {"🛍", "Amazon Purchase", "Shopping • 18:45", "-$156.00"},
            {"☕", "Starbucks Coffee", "Food • 08:00", "-$6.75"}
        };
        
        for (String[] exp : nov8Expenses) {
            expenseListPanel.add(createExpenseItem(exp[0], exp[1], exp[2], exp[3]));
            expenseListPanel.add(Box.createVerticalStrut(10));
        }
        
        expenseListPanel.add(Box.createVerticalStrut(10));
        
        expenseListPanel.add(createDateSeparator("Nov 7, 2025"));
        expenseListPanel.add(Box.createVerticalStrut(10));
        
        String[][] nov7Expenses = {
            {"⛽", "Gas Station", "Transport • 16:20", "-$45.00"}
        };
        
        for (String[] exp : nov7Expenses) {
            expenseListPanel.add(createExpenseItem(exp[0], exp[1], exp[2], exp[3]));
            expenseListPanel.add(Box.createVerticalStrut(10));
        }
        
        expenseListPanel.add(Box.createVerticalStrut(10));
        
        expenseListPanel.add(createDateSeparator("Nov 6, 2025"));
        expenseListPanel.add(Box.createVerticalStrut(10));
        
        String[][] nov6Expenses = {
            {"🍽", "Restaurant Dinner", "Food • 19:30", "-$78.90"}
        };
        
        for (String[] exp : nov6Expenses) {
            expenseListPanel.add(createExpenseItem(exp[0], exp[1], exp[2], exp[3]));
            expenseListPanel.add(Box.createVerticalStrut(10));
        }
        
        expenseListPanel.add(Box.createVerticalStrut(10));
        
        expenseListPanel.add(createDateSeparator("Nov 5, 2025"));
        expenseListPanel.add(Box.createVerticalStrut(10));
        
        String[][] nov5Expenses = {
            {"👟", "Nike Store", "Shopping • 15:00", "-$129.99"}
        };
        
        for (String[] exp : nov5Expenses) {
            expenseListPanel.add(createExpenseItem(exp[0], exp[1], exp[2], exp[3]));
            expenseListPanel.add(Box.createVerticalStrut(10));
        }
        
        expenseListPanel.add(Box.createVerticalStrut(10));
        
        expenseListPanel.add(createDateSeparator("Nov 4, 2025"));
        expenseListPanel.add(Box.createVerticalStrut(10));
        
        String[][] nov4Expenses = {
            {"💊", "Pharmacy", "Health • 11:45", "-$34.50"}
        };
        
        for (String[] exp : nov4Expenses) {
            expenseListPanel.add(createExpenseItem(exp[0], exp[1], exp[2], exp[3]));
            expenseListPanel.add(Box.createVerticalStrut(10));
        }
    }
    
    private JPanel createDateSeparator(String date) {
        JPanel separator = new JPanel();
        separator.setLayout(new BorderLayout());
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        separator.setBackground(new Color(245, 245, 245));
        
        JLabel dateLabel = new JLabel("  " + date);
        dateLabel.setFont(new Font("Arial", Font.BOLD, 13));
        dateLabel.setForeground(new Color(120, 120, 120));
        separator.add(dateLabel, BorderLayout.WEST);
        
        return separator;
    }
    
    private JPanel createExpenseItem(String emoji, String description, String details, String amount) {
        JPanel item = new JPanel();
        item.setLayout(null);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        item.setPreferredSize(new Dimension(1050, 70));
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240)));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JPanel iconPanel = new JPanel();
        iconPanel.setBounds(20, 15, 45, 45);
        iconPanel.setBackground(new Color(248, 249, 250));
        iconPanel.setLayout(new GridBagLayout());
        
        JLabel icon = new JLabel(emoji);
        icon.setFont(new Font("Arial", Font.PLAIN, 24));
        iconPanel.add(icon);
        item.add(iconPanel);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setBounds(80, 15, 600, 20);
        descLabel.setFont(new Font("Arial", Font.BOLD, 15));
        descLabel.setForeground(new Color(50, 50, 50));
        item.add(descLabel);
        
        JLabel detailsLabel = new JLabel(details);
        detailsLabel.setBounds(80, 38, 600, 18);
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        detailsLabel.setForeground(new Color(120, 120, 120));
        item.add(detailsLabel);
        
        JLabel amountLabel = new JLabel(amount);
        amountLabel.setBounds(900, 20, 130, 30);
        amountLabel.setFont(new Font("Arial", Font.BOLD, 18));
        amountLabel.setForeground(new Color(220, 53, 69));
        amountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        item.add(amountLabel);
        
        item.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                item.setBackground(new Color(248, 248, 248));
            }
            public void mouseExited(MouseEvent e) {
                item.setBackground(Color.WHITE);
            }
            public void mouseClicked(MouseEvent e) {
                showExpenseDetailsDialog(description, details, amount);
            }
        });
        
        return item;
    }
    
    private void createAddExpenseFAB(JPanel parent) {
        JButton fab = new JButton("+");
        fab.setBounds(1370, 750, 70, 70);
        fab.setFont(new Font("Arial", Font.BOLD, 40));
        fab.setBackground(UIConstants.PRIMARY_GREEN);
        fab.setForeground(Color.WHITE);
        fab.setFocusPainted(false);
        fab.setBorderPainted(false);
        fab.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        fab.setContentAreaFilled(true);
        fab.setOpaque(true);
        
        fab.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                fab.setBackground(UIConstants.PRIMARY_GREEN.darker());
            }
            public void mouseExited(MouseEvent e) {
                fab.setBackground(UIConstants.PRIMARY_GREEN);
            }
        });
        
        fab.addActionListener(e -> showAddExpenseDialog());
        
        parent.add(fab);
    }
    
    private void showAddExpenseDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Expense", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(null);
        
        JLabel title = new JLabel("Add New Expense");
        title.setBounds(30, 20, 300, 30);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        dialog.add(title);
        
        JLabel amountLabel = new JLabel("Amount");
        amountLabel.setBounds(30, 70, 380, 20);
        amountLabel.setFont(new Font("Arial", Font.BOLD, 13));
        dialog.add(amountLabel);
        
        JTextField amountField = new JTextField();
        amountField.setBounds(30, 95, 380, 40);
        amountField.setFont(new Font("Arial", Font.PLAIN, 15));
        amountField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        dialog.add(amountField);
        
        JLabel categoryLabel = new JLabel("Category");
        categoryLabel.setBounds(30, 150, 380, 20);
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 13));
        dialog.add(categoryLabel);
        
        String[] categories = {"Food", "Transport", "Shopping", "Entertainment", "Health"};
        JComboBox<String> categoryBox = new JComboBox<>(categories);
        categoryBox.setBounds(30, 175, 380, 40);
        categoryBox.setFont(new Font("Arial", Font.PLAIN, 14));
        dialog.add(categoryBox);
        
        JLabel descLabel = new JLabel("Description");
        descLabel.setBounds(30, 230, 380, 20);
        descLabel.setFont(new Font("Arial", Font.BOLD, 13));
        dialog.add(descLabel);
        
        JTextField descField = new JTextField();
        descField.setBounds(30, 255, 380, 40);
        descField.setFont(new Font("Arial", Font.PLAIN, 15));
        descField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        dialog.add(descField);
        
        JButton addButton = new JButton("Add Expense");
        addButton.setBounds(30, 315, 380, 45);
        addButton.setFont(new Font("Arial", Font.BOLD, 15));
        addButton.setBackground(UIConstants.PRIMARY_GREEN);
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorderPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addButton.addActionListener(e -> {
            // TODO: Backend integration
            JOptionPane.showMessageDialog(dialog, "Expense added successfully!");
            dialog.dispose();
            refreshData();
        });
        
        dialog.add(addButton);
        dialog.setVisible(true);
    }
    
    private void showExpenseDetailsDialog(String description, String details, String amount) {
        JOptionPane.showMessageDialog(
            this,
            "Description: " + description + "\nDetails: " + details + "\nAmount: " + amount,
            "Expense Details",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    public void refreshData() {
        // Backend integration here
    }
    
    public void clearData() {
        expenseListPanel.removeAll();
        expenseListPanel.revalidate();
        expenseListPanel.repaint();
    }
}