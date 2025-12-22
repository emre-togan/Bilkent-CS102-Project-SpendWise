package com.spendwise.models;

import java.sql.Date;
import java.util.ArrayList;

public class Budget {

    private int budgetId;
    private int userId;
    private double budgetLimit;
    private double totalSpending;
    private ArrayList<CategoryBudget> categories;
    private ArrayList<Expense> expenses;
    private Date startDate;
    private Date endDate;

    public Budget(int userId, double budgetLimit, Date startDate, Date endDate) {
        this.userId = userId;
        this.budgetLimit = budgetLimit;
        this.startDate = startDate;
        this.endDate = endDate;

        this.totalSpending = 0.0;
        this.categories = new ArrayList<>();
        this.expenses = new ArrayList<>();
    }

    // second constructor for getting datas from database
    public Budget(int budgetId, int userId, double monthlyLimit, double totalSpending, Date startDate, Date endDate) {
        this.budgetId = budgetId;
        this.userId = userId;
        this.budgetLimit = monthlyLimit;
        this.totalSpending = totalSpending;
        this.startDate = startDate;
        this.endDate = endDate;
        this.categories = new ArrayList<>();
        this.expenses = new ArrayList<>();
    }

    public static class CategoryBudget {

        String categoryName;
        double allocatedPart;
        double totalSpentInCategory;

        public CategoryBudget(String categoryName, double allocatedPart) {
            this.categoryName = categoryName;
            this.allocatedPart = allocatedPart;

            totalSpentInCategory = 0.0;
        }

        public void addExpense(double spendAmount) {
            totalSpentInCategory += spendAmount;
        }

        public double getRemainingBudget() {
            return allocatedPart - totalSpentInCategory;
        }

    }

    public void setLimit(double limit) {
        this.budgetLimit = limit;
    }

    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
    }

    public void setTotalSpending(double totalSpending) {
        this.totalSpending = totalSpending;
    }

    public void addExpense(Expense e) {
        this.expenses.add(e);

        double amount = e.getAmount();
        totalSpending += amount;

        for (CategoryBudget catBudget : this.categories) {

            if (catBudget.categoryName.equals(e.getCategory())) {
                catBudget.addExpense(amount);
                break;
            }
        }
    }

    public boolean isLimitInDanger() {

        double criticalPoint = (budgetLimit * 4) / 5;

        if (totalSpending >= criticalPoint) {
            return true;
        }

        return false;
    }

    public double getCurrentBudget() {
        double currBudget = this.budgetLimit - this.totalSpending;
        return currBudget;
    }

    public void addCategoryBudget(CategoryBudget newCatBudget) {
        this.categories.add(newCatBudget);
    }

    public int getUserId() {
        return userId;
    }

    public double getBudgetLimit() {
        return budgetLimit;
    }

    public ArrayList<CategoryBudget> getCategories() {
        return categories;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public double getTotalSpending() {
        return totalSpending;
    }

    public int getBudgetId() {
        return budgetId;
    }
}
