package com.spendwise.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import com.spendwise.models.Product;
import com.spendwise.services.ProfileService;

public class WishlistDialog extends JDialog {

    private JPanel gridPanel;

    public WishlistDialog(JFrame parent) {
        super(parent, "My Wishlist", true);
        setSize(500, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UIConstants.WHITE_BG);

        // Alert Banner
        JPanel alertPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        alertPanel.setBackground(new Color(255, 244, 229));
        JLabel alertLabel = new JLabel("🔔 Price Drop Alert: Some items are on sale!");
        alertLabel.setForeground(new Color(180, 80, 0));
        alertPanel.add(alertLabel);
        add(alertPanel, BorderLayout.NORTH);

        // Grid
        gridPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        gridPanel.setBackground(UIConstants.WHITE_BG);
        gridPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        refreshWishlist();
    }

    private void refreshWishlist() {
        gridPanel.removeAll();

        // Backend Call
        List<Product> products = ProfileService.getSavedProducts(); // Using saved products as wishlist

        if (products != null) {
            for (Product p : products) {
                gridPanel.add(createWishlistItem(p));
            }
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel createWishlistItem(Product product) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        card.setBackground(Color.WHITE);

        JPanel imagePlaceholder = new JPanel();
        imagePlaceholder.setBackground(new Color(245, 245, 245));
        imagePlaceholder.setPreferredSize(new Dimension(100, 100));
        imagePlaceholder.setMaximumSize(new Dimension(Short.MAX_VALUE, 100));

        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel priceLabel = new JLabel("$" + product.getPrice());
        priceLabel.setForeground(UIConstants.PRIMARY_GREEN);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton removeBtn = new JButton("Remove");
        removeBtn.setFont(new Font("Arial", Font.PLAIN, 10));
        removeBtn.setForeground(Color.RED);
        removeBtn.setBorderPainted(false);
        removeBtn.setContentAreaFilled(false);
        removeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        removeBtn.addActionListener(e -> {
            // Backend
            ProfileService.removeFromWishlist(product);
            refreshWishlist();
        });

        card.add(imagePlaceholder);
        card.add(Box.createVerticalStrut(10));
        card.add(nameLabel);
        card.add(priceLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(removeBtn);

        return card;
    }
}