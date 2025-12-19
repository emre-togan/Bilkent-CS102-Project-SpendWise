package Services;

import java.sql.ResultSet;
import java.sql.SQLException;

import Database.DBconnection;
import Models.Budget;

public class BudgetService {
    

    public boolean createBudget(Budget budget){
        
        boolean isItCreated = false;
        String sql = "INSERT INTO budgets (user_id, monthly_limit, total_spent, start_date, end_date) VALUES (?, ?, ?, ?, ?)";

        int totalAffectedRows = DBconnection.executeUpdate(sql,
            budget.getUserId(),
            budget.getBudgetLimit(),
            budget.getTotalSpending(),
            budget.getStartDate(),
            budget.getEndDate()

        );

        if(totalAffectedRows > 0){
            isItCreated = true;
        }

        return isItCreated;
    }

    public Budget getSpesificUserBudget(int userId){
        String sql = "SELECT * FROM budgets WHERE user_id = ?";
        ResultSet resultSet = DBconnection.executeQuery(sql, userId);

        try{

            if(resultSet != null && resultSet.next()){

                int budgetId = resultSet.getInt("budget_id");
                double limit = resultSet.getDouble("monthly_limit");
                double spending = resultSet.getDouble("total_spent");
                java.sql.Date startDate = resultSet.getDate("start_date");
                java.sql.Date endDate = resultSet.getDate("end_date");

                return new Budget(budgetId, userId, limit, spending, startDate, endDate);
            }
        }

        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateBudgetList(Budget budget){

        boolean isItUpdated = false;
        String sql = "UPDATE budgets SET monthly_limit = ?, total_spent = ? WHERE budget_id = ?";

        int affectedBudgets = DBconnection.executeUpdate(sql, 
            budget.getBudgetLimit(), 
            budget.getTotalSpending(),
            budget.getBudgetId()
        );

        if(affectedBudgets > 0){
            isItUpdated = true;
        }

        return isItUpdated;
    }


    public double calculateTheSpendingPercentage(Budget budget){
        double percentage = 0.0;

        if(budget.getBudgetLimit() == 0){
            return percentage;
        }

        else{
            percentage = (budget.getTotalSpending() / budget.getBudgetLimit()) * 100;
        }

        return percentage;
    }
}
