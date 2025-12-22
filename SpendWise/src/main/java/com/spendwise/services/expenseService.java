package com.spendwise.services;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.spendwise.database.DBconnection;
import com.spendwise.models.Expense;

public class expenseService {

    public boolean addExpense(Expense expense) {

        boolean isCreated = false;
        String sql = "INSERT INTO expenses (user_id, description, amount, category, expense_date) VALUES (?, ?, ?, ?, ?)";

        int affectedRows = DBconnection.executeUpdate(sql,
                expense.getUserId(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getCategory(),
                expense.getDate());

        if (affectedRows > 0) {
            isCreated = true;
        }

        return isCreated;
    }

    public ArrayList<Expense> getExpensesOfTheUser(int userId) {

        ArrayList<Expense> totalExpenses = new ArrayList<>();

        String sql = "SELECT * FROM expenses WHERE user_id = ?";
        ResultSet connector = DBconnection.executeQuery(sql, userId);

        try {

            if (connector != null) {

                while (connector.next()) {
                    int expenseId = connector.getInt("expense_id");
                    String description = connector.getString("description");
                    double amount = connector.getDouble("amount");
                    String category = connector.getString("category");
                    Date date = connector.getDate("expense_date");

                    Expense expense = new Expense(userId, expenseId, category, description, amount, date);
                    totalExpenses.add(expense);
                }
            }

        }

        catch (SQLException expense) {
            expense.printStackTrace();
        }

        return totalExpenses;
    }

    public ArrayList<Expense> getExpenseByCategory(int userId, String CategoryName) {

        ArrayList<Expense> filteredExpenses = new ArrayList<>();

        String sql = "SELECT * FROM expenses WHERE user_id = ? AND category = ?";
        ResultSet connector = DBconnection.executeQuery(sql, userId, CategoryName);

        try {
            if (connector != null) {

                while (connector.next()) {
                    int expenseId = connector.getInt("expense_id");
                    String description = connector.getString("description");
                    double amount = connector.getDouble("amount");
                    Date date = connector.getDate("expense_date");

                    Expense expense = new Expense(userId, expenseId, CategoryName, description, amount, date);
                    filteredExpenses.add(expense);
                }
            }
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return filteredExpenses;
    }

    public boolean deleteExpensesFromUser(int expenseId) {

        boolean isAnyRowAffected = false;
        String sql = "DELETE FROM expenses WHERE expense_id = ?";
        int rowsWillBeDeleted = DBconnection.executeUpdate(sql, expenseId);

        if (rowsWillBeDeleted > 0) {
            return true;
        }

        return isAnyRowAffected;
    }
}
