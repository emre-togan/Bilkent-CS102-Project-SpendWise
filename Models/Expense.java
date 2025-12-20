package Models;

import java.sql.Date;

public class Expense {
    
    private int userId;
    private int expenseId;
    private String category;
    private String description;
    private double amount;
    private Date date;

    public Expense(int expenseId, String category, String description, double amount, Date date){
        this.expenseId = expenseId;
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    // this is for store expenses in database
    public Expense(int userId, int expenseId, String category, String description, double amount, Date date){
        this.userId = userId;
        this.expenseId = expenseId;
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public int getExpenseId() {
        return expenseId;
    }
}
