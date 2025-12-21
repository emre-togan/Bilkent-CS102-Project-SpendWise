package Utils;

import Models.Budget;
import Models.Expense;
import Services.BudgetService;
import Services.expenseService;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportGenerator {

    private final expenseService expenseService;
    private final BudgetService budgetService;
    private final StatisticsCalculator stats;

    public ReportGenerator() {
        this.expenseService = new expenseService();
        this.budgetService = new BudgetService();
        this.stats = new StatisticsCalculator();
    }

    public String generateMonthlyReport(int userId) {
        List<Expense> expenses = safeList(expenseService.getExpensesOfTheUser(userId));

        double total = sum(expenses);
        double avg = stats.calculateAverageSpending(expenses);
        List<String> topCats = stats.findTopCategories(expenses, 3);
        String trend = stats.getSpendingTrend(expenses);
        double predicted = stats.predictFutureSpending(expenses, 1);

        StringBuilder sb = new StringBuilder();
        sb.append("Monthly Report\n");
        sb.append("User: ").append(userId).append("\n");
        sb.append("Total Spent: ").append(fmt(total)).append("\n");
        sb.append("Average Expense: ").append(fmt(avg)).append("\n");
        sb.append("Top Categories: ").append(topCats).append("\n");
        sb.append("Trend: ").append(trend).append("\n");
        sb.append("Predicted Next Month: ").append(fmt(predicted)).append("\n");
        return sb.toString();
    }

    public String generateCategoryReport(int userId, String category) {
        List<Expense> expenses = safeList(expenseService.getExpensesOfTheUser(userId));

        Map<String, Double> totals = stats.calculateCategoryTotals(expenses);
        Map<String, Double> pct = stats.calculateCategoryPercentages(expenses);

        String cat = (category == null) ? "Other" : category.trim();
        double catTotal = totals.getOrDefault(cat, 0.0);
        double catPct = pct.getOrDefault(cat, 0.0);

        StringBuilder sb = new StringBuilder();
        sb.append("Category Report\n");
        sb.append("User: ").append(userId).append("\n");
        sb.append("Category: ").append(cat).append("\n");
        sb.append("Category Total: ").append(fmt(catTotal)).append("\n");
        sb.append("Category Percentage: ").append(fmt(catPct)).append("%\n");
        return sb.toString();
    }

    public String generateComparisonReport(int userId, YearMonth month1, YearMonth month2) {
        List<Expense> expenses = safeList(expenseService.getExpensesOfTheUser(userId));

        double t1 = sumForMonth(expenses, month1);
        double t2 = sumForMonth(expenses, month2);

        double diff = t2 - t1;
        double pct = (t1 == 0.0) ? (t2 == 0.0 ? 0.0 : 100.0) : (diff / t1) * 100.0;

        StringBuilder sb = new StringBuilder();
        sb.append("Comparison Report\n");
        sb.append("User: ").append(userId).append("\n");
        sb.append("Month 1: ").append(month1).append(" Total: ").append(fmt(t1)).append("\n");
        sb.append("Month 2: ").append(month2).append(" Total: ").append(fmt(t2)).append("\n");
        sb.append("Difference: ").append(fmt(diff)).append("\n");
        sb.append("Change: ").append(fmt(pct)).append("%\n");
        return sb.toString();
    }

    public String generateSavingsReport(int userId) {
        Budget budget = budgetService.getSpesificUserBudget(userId);
        List<Expense> expenses = safeList(expenseService.getExpensesOfTheUser(userId));

        double spent = sum(expenses);
        double savings = stats.calculateSavings(budget, expenses);

        StringBuilder sb = new StringBuilder();
        sb.append("Savings Report\n");
        sb.append("User: ").append(userId).append("\n");
        if (budget == null) {
            sb.append("Budget not found\n");
            sb.append("Total Spent: ").append(fmt(spent)).append("\n");
            sb.append("Savings: 0.00\n");
            return sb.toString();
        }
        sb.append("Budget Limit: ").append(fmt(budget.getBudgetLimit())).append("\n");
        sb.append("Total Spent: ").append(fmt(spent)).append("\n");
        sb.append("Savings: ").append(fmt(savings)).append("\n");
        return sb.toString();
    }

    private List<Expense> safeList(List<Expense> list) {
        return (list == null) ? new ArrayList<>() : list;
    }

    private double sum(List<Expense> expenses) {
        double s = 0.0;
        for (Expense e : expenses) if (e != null) s += e.getAmount();
        return s;
    }

    private double sumForMonth(List<Expense> expenses, YearMonth ym) {
        double s = 0.0;
        for (Expense e : expenses) {
            if (e == null || e.getDate() == null) continue;
            if (YearMonth.from(e.getDate().toLocalDate()).equals(ym)) s += e.getAmount();
        }
        return s;
    }

    private String fmt(double v) {
        return String.format("%.2f", v);
    }
}
