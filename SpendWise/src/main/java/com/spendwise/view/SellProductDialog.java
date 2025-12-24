package com.spendwise.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Timestamp;

import com.spendwise.models.Product;
import com.spendwise.services.productService;

public class SellProductDialog extends JDialog {

    private JTextField nameField;
    private JTextField priceField;
    private JComboBox<String> categoryBox;
    private JComboBox<String> conditionBox;
    private JTextField imageUrlField;
    private JTextArea descriptionArea;

    public SellProductDialog(Frame parent) {
        super(parent, "Sell an Item", true);
        setSize(500, 650);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UIConstants.WHITE_BG);

        JLabel header = new JLabel("List Item for Sale");
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setBorder(new EmptyBorder(20, 20, 10, 20));
        add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(UIConstants.WHITE_BG);
        formPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        nameField = createInput(formPanel, "Product Name");
        priceField = createInput(formPanel, "Price (₺)");

        formPanel.add(new JLabel("Category"));
        String[] categories = { "Electronics", "Clothing", "Home", "Books", "Sports", "Other" };
        categoryBox = new JComboBox<>(categories);
        categoryBox.setBackground(Color.WHITE);
        categoryBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(categoryBox);
        formPanel.add(Box.createVerticalStrut(15));

        formPanel.add(new JLabel("Condition"));
        String[] conditions = { "New", "Used - Like New", "Used - Good", "Used - Fair" };
        conditionBox = new JComboBox<>(conditions);
        conditionBox.setBackground(Color.WHITE);
        conditionBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(conditionBox);
        formPanel.add(Box.createVerticalStrut(15));

        imageUrlField = createInput(formPanel, "Image URL (Optional)");

        formPanel.add(new JLabel("Description"));
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        descScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(descScroll);

        add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(UIConstants.WHITE_BG);
        btnPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());

        JButton listBtn = new JButton("List Item");
        listBtn.setBackground(UIConstants.PRIMARY_GREEN);
        listBtn.setForeground(Color.WHITE);
        listBtn.setFocusPainted(false);
        listBtn.addActionListener(e -> saveItem());

        btnPanel.add(cancelBtn);
        btnPanel.add(listBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private JTextField createInput(JPanel panel, String label) {
        JLabel l = new JLabel(label);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(l);
        panel.add(Box.createVerticalStrut(5));

        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(field);
        panel.add(Box.createVerticalStrut(15));
        return field;
    }

    private void saveItem() {

        if (nameField.getText().trim().isEmpty() || priceField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in Name and Price", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double price = Double.parseDouble(priceField.getText().trim());
            String name = nameField.getText().trim();
            String cat = (String) categoryBox.getSelectedItem();
            String cond = (String) conditionBox.getSelectedItem();
            String desc = descriptionArea.getText().trim();
            String img = imageUrlField.getText().trim();

            if (img.isEmpty())
                img = "default_product.png";

            String sellerName = UserSession.getCurrentUser().getUserName();

            Product p = new Product(
                    0, name, desc, price, price, cat, img, 0.0, 0,
                    sellerName, true, cond, "User Listing", 0);

            boolean success = new productService().saveProduct(p);
            if (success) {
                JOptionPane.showMessageDialog(this, "Item listed successfully!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to list item.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Price format", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
