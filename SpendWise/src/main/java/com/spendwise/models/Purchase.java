package com.spendwise.models;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Purchase {

    private int orderId;
    private int userId;
    private ArrayList<OrderItem> items;
    private double totalAmount;
    private Timestamp orderDate;
    private String status; // Delivered, Shipped, Processing vs.

    // Create new purchase (before DB assigns orderId)
    public Purchase(int userId, Timestamp orderDate, String status) {
        this.userId = userId;
        this.orderDate = orderDate;
        this.status = status;
        this.items = new ArrayList<>();
        this.totalAmount = 0.0;
    }

    // Load from DB
    public Purchase(int orderId, int userId, double totalAmount, Timestamp orderDate, String status) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.status = status;
        this.items = new ArrayList<>();
    }

    public static class OrderItem {
        private int itemId;
        private int productId;
        private int quantity;
        private double price; // unit price

        // create new item (before DB assigns itemId)
        public OrderItem(int productId, int quantity, double price) {
            this.productId = productId;
            this.quantity = quantity;
            this.price = price;
        }

        // load from DB
        public OrderItem(int itemId, int productId, int quantity, double price) {
            this.itemId = itemId;
            this.productId = productId;
            this.quantity = quantity;
            this.price = price;
        }

        public double getLineTotal() {
            return price * quantity;
        }

        public int getItemId() {
            return itemId;
        }

        public int getProductId() {
            return productId;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
        calculateTotal();
    }

    public void calculateTotal() {
        double sum = 0.0;
        for (OrderItem item : items) {
            sum += item.getLineTotal();
        }
        this.totalAmount = sum;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getUserId() {
        return userId;
    }

    public ArrayList<OrderItem> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }

    public String getProductSummary() {
        if (items == null || items.isEmpty()) {
            return "No items";
        }
        StringBuilder summary = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            // We need to fetch product name, but OrderItem only has productId.
            // For now return generic item description or use productId.
            // Ideal solution: OrderItem should have productName or we fetch it.
            // Given constraint, I will just say "Product #" + productId
            summary.append("Product #").append(items.get(i).getProductId());
            if (i < items.size() - 1) {
                summary.append(", ");
            }
        }
        return summary.toString();
    }
}
