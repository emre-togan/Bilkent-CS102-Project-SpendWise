package com.spendwise.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import com.spendwise.models.Purchase;
import com.spendwise.services.ProfileService;

public class PurchaseHistoryDialog extends JDialog {

    public PurchaseHistoryDialog(JFrame parent) {
        super(parent, "Purchase History", true);
        setSize(600, 700);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UIConstants.WHITE_BG);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.WHITE_BG);
        headerPanel.setBorder(new EmptyBorder(15, 20, 10, 20));
        headerPanel.add(new JLabel("My Orders"), BorderLayout.WEST);
        headerPanel.add(new JButton("Export Data"), BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(UIConstants.WHITE_BG);

        // Sends filter to backend
        tabbedPane.addTab("All Orders", createOrderListPanel("All"));
        tabbedPane.addTab("Delivered", createOrderListPanel("Delivered"));
        tabbedPane.addTab("Shipped", createOrderListPanel("Shipped"));
        tabbedPane.addTab("Cancelled", createOrderListPanel("Cancelled"));

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createOrderListPanel(String filter) {
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(UIConstants.WHITE_BG);
        listPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Fetch data from Backend
        List<Purchase> orders = ProfileService.getOrders(filter);

        if (orders == null || orders.isEmpty()) {
            JPanel emptyPanel = new JPanel(new FlowLayout());
            emptyPanel.setBackground(UIConstants.WHITE_BG);
            emptyPanel.add(new JLabel("No orders found for: " + filter));
            listPanel.add(emptyPanel);
        } else {
            for (Purchase p : orders) {
                listPanel.add(createOrderItem(p));
                listPanel.add(Box.createVerticalStrut(15));
            }
        }

        // Wrap in ScrollPane
        JPanel container = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        container.add(scrollPane, BorderLayout.CENTER);

        return container;
    }

    private JPanel createOrderItem(Purchase order) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        card.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(new JLabel("Order #" + order.getOrderId()), BorderLayout.WEST);

        JLabel statusLabel = new JLabel(" " + order.getStatus() + " ");
        statusLabel.setOpaque(true);
        statusLabel.setBackground(UIConstants.getStatusColor(order.getStatus()));
        statusLabel.setForeground(Color.WHITE);
        topPanel.add(statusLabel, BorderLayout.EAST);

        JPanel centerPanel = new JPanel(new GridLayout(3, 1));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
        centerPanel.add(new JLabel("Date: " + order.getOrderDate()));
        centerPanel.add(new JLabel("Items: " + order.getProductSummary()));
        centerPanel.add(new JLabel("Total: ₺" + order.getTotalAmount()));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);

        JButton trackBtn = new JButton("Track Order");
        trackBtn.setBackground(Color.WHITE);
        trackBtn.addActionListener(e -> ProfileService.trackOrder(order.getOrderId()));

        JButton buyAgainBtn = new JButton("Buy Again");
        buyAgainBtn.setBackground(UIConstants.PRIMARY_BLUE);
        buyAgainBtn.setForeground(Color.WHITE);
        buyAgainBtn.addActionListener(e -> ProfileService.buyAgain(order.getOrderId()));

        bottomPanel.add(trackBtn);
        bottomPanel.add(buyAgainBtn);

        card.add(topPanel, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
    }
}