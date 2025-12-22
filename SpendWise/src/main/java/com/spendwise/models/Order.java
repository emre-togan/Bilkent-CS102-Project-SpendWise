package com.spendwise.models;

public class Order {
    private String orderId;
    private String status;
    private String date;
    private String productSummary;
    private double totalAmount;

    public Order(String orderId, String status, String date, String productSummary, double totalAmount) {
        this.orderId = orderId;
        this.status = status;
        this.date = date;
        this.productSummary = productSummary;
        this.totalAmount = totalAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getProductSummary() {
        return productSummary;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}
