package com.spendwise.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import com.spendwise.models.Product;
import com.spendwise.services.productService;
import com.spendwise.view.components.RoundedButton;
import com.spendwise.view.components.RoundedPanel;

public class WishlistDialog extends JDialog {

    private JPanel productsContainer;
    private productService productServiceInstance;

    public WishlistDialog(JFrame parent) {
        super(parent, "My Wishlist", true);
        this.productServiceInstance = new productService();

        setSize(450, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(250, 250, 250));

        // --- Header ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("Your Wishlist");
        title.setFont(new Font("Arial", Font.BOLD, 20));

        headerPanel.add(title, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // --- Scrollable Content ---
        productsContainer = new JPanel();
        productsContainer.setLayout(new BoxLayout(productsContainer, BoxLayout.Y_AXIS));
        productsContainer.setBackground(new Color(250, 250, 250));
        productsContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(productsContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // --- Close Button Area ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 20));

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        bottomPanel.add(closeBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load the actual data
        loadWishlistData();
    }

    private void loadWishlistData() {
        productsContainer.removeAll();

        int currentUserId = UserSession.getCurrentUserId();
        // 1. Fetch data using the new service method
        List<Product> wishlist = productServiceInstance.getWishlist(currentUserId);

        if (wishlist == null || wishlist.isEmpty()) {
            JLabel empty = new JLabel("Your wishlist is empty.", SwingConstants.CENTER);
            empty.setFont(new Font("Arial", Font.PLAIN, 14));
            empty.setForeground(Color.GRAY);
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);

            productsContainer.add(Box.createVerticalStrut(50));
            productsContainer.add(empty);
        } else {
            for (Product p : wishlist) {
                productsContainer.add(createWishlistRow(p));
                productsContainer.add(Box.createVerticalStrut(10));
            }
        }

        productsContainer.revalidate();
        productsContainer.repaint();
    }

    private JPanel createWishlistRow(Product p) {
        RoundedPanel row = new RoundedPanel(15, Color.WHITE);
        row.setLayout(new BorderLayout());
        row.setMaximumSize(new Dimension(400, 80));
        row.setPreferredSize(new Dimension(400, 80));
        row.setBorder(new EmptyBorder(10, 10, 10, 10));
        row.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Left: Info
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(p.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel priceLabel = new JLabel("₺" + p.getPriceAfterDiscount());
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        priceLabel.setForeground(new Color(39, 174, 96));

        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);

        // Right: Remove Button
        RoundedButton removeBtn = new RoundedButton("Remove", 10, new Color(255, 230, 230), Color.RED);
        removeBtn.setForeground(Color.RED);
        removeBtn.setFont(new Font("Arial", Font.BOLD, 11));
        removeBtn.setPreferredSize(new Dimension(80, 30));
        removeBtn.addActionListener(e -> {
            // Remove from DB
            int userId = UserSession.getCurrentUserId();
            productServiceInstance.toggleWishlist(userId, p.getProductId());
            // Refresh UI
            loadWishlistData();
        });

        row.add(infoPanel, BorderLayout.CENTER);
        row.add(removeBtn, BorderLayout.EAST);

        return row;
    }
}