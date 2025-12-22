import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Models.Product;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

// import Models.User; 
// import Models.Product;
// import Services.ProfileService;

public class ProfilePanel extends JPanel {

   
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel avatarLabel;
    private JLabel purchasesStatLabel;
    private JLabel savedStatLabel;
    private JLabel dealsStatLabel;
    private JPanel friendsListPanel;
    private JPanel savedProductsPanel;
    
    private String currentInitials = "";

    public ProfilePanel() {
        setLayout(new BorderLayout());
        setBackground(UIConstants.WHITE_BG);

        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(UIConstants.WHITE_BG);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. Header
        contentPanel.add(createHeaderSection());
        contentPanel.add(Box.createVerticalStrut(20));

        // 2. User Info Card
        contentPanel.add(createUserInfoCard());
        contentPanel.add(Box.createVerticalStrut(30));

        // 3. Friends Section
        contentPanel.add(createSectionHeader("Friends", "Add Friend"));
        contentPanel.add(Box.createVerticalStrut(10));
        friendsListPanel = new JPanel();
        friendsListPanel.setLayout(new BoxLayout(friendsListPanel, BoxLayout.Y_AXIS));
        friendsListPanel.setBackground(UIConstants.WHITE_BG);
        contentPanel.add(friendsListPanel);
        contentPanel.add(Box.createVerticalStrut(30));

        // 4. Saved Products Section
        contentPanel.add(createSectionHeader("Saved Products", "See All"));
        contentPanel.add(Box.createVerticalStrut(10));
        savedProductsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        savedProductsPanel.setBackground(UIConstants.WHITE_BG);
        contentPanel.add(savedProductsPanel);
        contentPanel.add(Box.createVerticalStrut(30));

        // 5. Menu Items
        contentPanel.add(createMenuItem("Purchase History", () -> showPurchaseHistory()));
        contentPanel.add(createMenuItem("Wishlist", () -> showWishlist()));
        contentPanel.add(createMenuItem("Addresses", () -> showAddresses()));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        
        loadUserProfile();
        loadFriends();
        loadSavedProducts();
    }

    // Methods

    // 1. loadUserProfile()
    private void loadUserProfile() {
        //Backend
        User user = ProfileService.getCurrentUser(); 
        
       
        nameLabel.setText(user.getUserName());
        emailLabel.setText(user.geteMail());
        
        if (user.getUserName() != null && !user.getUserName().isEmpty()) {
            this.currentInitials = user.getUserName().substring(0, 1).toUpperCase();
        }
        
        purchasesStatLabel.setText(user.getPurchaseCount() + " Purchases");
        savedStatLabel.setText(user.getTotalSaved() + " Saved");
        dealsStatLabel.setText(user.getDealsFound() + " Deals Found");
        
        repaint();
    }

    // 2. loadFriends()
    private void loadFriends() {
        friendsListPanel.removeAll();
        
        // Backend
        List<User> friends = ProfileService.getFriends();
        
        for (User friend : friends) {
            friendsListPanel.add(createFriendItem(friend.getUserName(), "Friend"));
            friendsListPanel.add(Box.createVerticalStrut(10)); 
        }
        
        friendsListPanel.revalidate();
        friendsListPanel.repaint();
    }

    // 3. loadSavedProducts()
    private void loadSavedProducts() {
        savedProductsPanel.removeAll();
        
       //Backend
        List<Product> products = ProfileService.getSavedProducts();
        
        for (Product p : products) {
            savedProductsPanel.add(createProductCard(p));
        }
        
        savedProductsPanel.revalidate();
        savedProductsPanel.repaint();
    }

    // 4. handleEditProfile()
    private void handleEditProfile() {
        String newName = JOptionPane.showInputDialog(this, "Enter new name:", nameLabel.getText());
        if (newName != null && !newName.isEmpty()) {
            // Backend
            ProfileService.updateProfile(newName, emailLabel.getText());
            
            loadUserProfile(); 
        }
    }
    
    // 5. showPurchaseHistory()
    private void showPurchaseHistory() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        new PurchaseHistoryDialog(parentFrame).setVisible(true);
    }

    // 6. showWishlist()
    private void showWishlist() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        new WishlistDialog(parentFrame).setVisible(true);
    }

    // 7. showAddresses()
    private void showAddresses() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        new AddressesDialog(parentFrame).setVisible(true);
    }

    //UI Creators
    private JPanel createHeaderSection() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.WHITE_BG);
        JLabel title = new JLabel("Profile");
        title.setFont(UIConstants.HEADING_FONT);
        JButton editButton = new JButton("Edit");
        editButton.setForeground(UIConstants.PRIMARY_BLUE);
        editButton.setBorderPainted(false);
        editButton.setContentAreaFilled(false);
        editButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        editButton.addActionListener(e -> handleEditProfile());
        panel.add(title, BorderLayout.WEST);
        panel.add(editButton, BorderLayout.EAST);
        return panel;
    }

    private JPanel createUserInfoCard() {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(new Color(245, 245, 245));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        avatarLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UIConstants.PRIMARY_GREEN);
                g2.fillOval(0, 0, 70, 70);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 24));
                FontMetrics fm = g2.getFontMetrics();
                int x = (70 - fm.stringWidth(currentInitials)) / 2;
                int y = (70 + fm.getAscent()) / 2 - 3;
                g2.drawString(currentInitials, x, y);
            }
        };
        avatarLabel.setPreferredSize(new Dimension(70, 70));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        nameLabel = new JLabel("Loading...");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        emailLabel = new JLabel("Loading...");
        emailLabel.setForeground(UIConstants.GRAY_TEXT);
        textPanel.add(nameLabel);
        textPanel.add(emailLabel);

        JPanel statsPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        statsPanel.setOpaque(false);
        purchasesStatLabel = new JLabel("0 Purchases");
        purchasesStatLabel.setFont(new Font("Arial", Font.BOLD, 12));
        savedStatLabel = new JLabel("$0 Saved");
        savedStatLabel.setFont(new Font("Arial", Font.BOLD, 12));
        savedStatLabel.setForeground(UIConstants.PRIMARY_GREEN);
        dealsStatLabel = new JLabel("0 Deals Found");
        dealsStatLabel.setFont(new Font("Arial", Font.BOLD, 12));
        dealsStatLabel.setForeground(UIConstants.PRIMARY_BLUE);
        
        statsPanel.add(purchasesStatLabel);
        statsPanel.add(savedStatLabel);
        statsPanel.add(dealsStatLabel);

        card.add(avatarLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        card.add(statsPanel, BorderLayout.EAST);
        return card;
    }

    private JPanel createFriendItem(String name, String subText) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(UIConstants.WHITE_BG);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        JLabel avatar = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillOval(0, 0, 40, 40);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 14));
                g2.drawString(name.substring(0, 1), 15, 25);
            }
        };
        avatar.setPreferredSize(new Dimension(40, 40));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        JLabel nameLbl = new JLabel(name);
        nameLbl.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel subLbl = new JLabel(subText);
        subLbl.setFont(new Font("Arial", Font.PLAIN, 11));
        subLbl.setForeground(UIConstants.GRAY_TEXT);
        textPanel.add(nameLbl);
        textPanel.add(subLbl);

        JLabel arrow = new JLabel(">");
        arrow.setForeground(UIConstants.GRAY_TEXT);

        panel.add(avatar, BorderLayout.WEST);
        panel.add(textPanel, BorderLayout.CENTER);
        panel.add(arrow, BorderLayout.EAST);
        return panel;
    }

    private JPanel createProductCard(Product product) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        card.setBackground(Color.WHITE);
        
        JPanel imagePlaceholder = new JPanel();
        imagePlaceholder.setBackground(new Color(240, 240, 240));
        imagePlaceholder.setPreferredSize(new Dimension(100, 80));
        imagePlaceholder.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 11));
        JLabel priceLabel = new JLabel(product.getPrice());
        priceLabel.setForeground(UIConstants.PRIMARY_GREEN);
        
        card.add(imagePlaceholder);
        card.add(Box.createVerticalStrut(5));
        card.add(nameLabel);
        card.add(priceLabel);
        return card;
    }
    
    private JPanel createSectionHeader(String title, String actionText) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.WHITE_BG);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JButton actionBtn = new JButton(actionText);
        actionBtn.setForeground(UIConstants.PRIMARY_BLUE);
        actionBtn.setBorderPainted(false);
        actionBtn.setContentAreaFilled(false);
        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(actionBtn, BorderLayout.EAST);
        return panel;
    }

    private JPanel createMenuItem(String text, Runnable onClick) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(250, 250, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        JLabel arrow = new JLabel(">");
        arrow.setForeground(UIConstants.GRAY_TEXT);
        panel.add(label, BorderLayout.WEST);
        panel.add(arrow, BorderLayout.EAST);
        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { onClick.run(); }
        });
        return panel;
    }
}