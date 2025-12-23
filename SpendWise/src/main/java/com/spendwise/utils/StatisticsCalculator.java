package com.spendwise.utils;

import com.spendwise.models.Budget;
import com.spendwise.models.Expense;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsCalculator {

    public double calculateAverageSpending(List<Expense> expenses) {
        if (expenses == null || expenses.isEmpty())
            return 0.0;
        double sum = 0.0;
        int count = 0;
        for (Expense e : expenses) {
            if (e == null)
                continue;
            sum += e.getAmount();
            count++;
        }
        return count == 0 ? 0.0 : (sum / count);
    }

    public Map<String, Double> calculateCategoryTotals(List<Expense> expenses) {
        Map<String, Double> totals = new HashMap<>();
        if (expenses == null)
            return totals;

        for (Expense e : expenses) {
            if (e == null)
                continue;
            String cat = safeCategory(e.getCategory());
            totals.put(cat, totals.getOrDefault(cat, 0.0) + e.getAmount());
        }
        return totals;
    }

    public Map<String, Double> calculateCategoryPercentages(List<Expense> expenses) {
        Map<String, Double> totals = calculateCategoryTotals(expenses);
        double grandTotal = 0.0;
        for (double v : totals.values())
            grandTotal += v;

        Map<String, Double> pct = new HashMap<>();
        if (grandTotal <= 0.0)
            return pct;

        for (Map.Entry<String, Double> entry : totals.entrySet()) {
            pct.put(entry.getKey(), (entry.getValue() / grandTotal) * 100.0);
        }
        return pct;
    }

    public List<String> findTopCategories(List<Expense> expenses, int n) {
        Map<String, Double> totals = calculateCategoryTotals(expenses);
        List<Map.Entry<String, Double>> list = new ArrayList<>(totals.entrySet());
        list.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        List<String> top = new ArrayList<>();
        int limit = Math.min(n, list.size());
        for (int i = 0; i < limit; i++)
            top.add(list.get(i).getKey());
        return top;
    }

    public double calculateSavings(Budget budget, List<Expense> expenses) {
        if (budget == null)
            return 0.0;
        double spent = 0.0;
        if (expenses != null) {
            for (Expense e : expenses) {
                if (e == null)
                    continue;
                spent += e.getAmount();
            }
        }
        double savings = budget.getBudgetLimit() - spent;
        return Math.max(0.0, savings);
    }

    // Basit tahmin: son 3 ay ortalaması (yoksa genel ortalama)
    public double predictFutureSpending(List<Expense> expenses, int monthsAhead) {
        if (monthsAhead <= 0)
            monthsAhead = 1;
        if (expenses == null || expenses.isEmpty())
            return 0.0;

        YearMonth now = YearMonth.now();
        YearMonth start = now.minusMonths(2);

        List<Double> monthlyTotals = new ArrayList<>();
        for (YearMonth ym = start; !ym.isAfter(now); ym = ym.plusMonths(1)) {
            double t = sumForMonth(expenses, ym);
            monthlyTotals.add(t);
        }

        double sum = 0.0;
        int count = 0;
        for (double v : monthlyTotals) {
            sum += v;
            count++;
        }
        double avg = (count == 0) ? 0.0 : (sum / count);
        return avg * monthsAhead;
    }

    public String getSpendingTrend(List<Expense> expenses) {
        if (expenses == null || expenses.isEmpty())
            return "No data";

        YearMonth now = YearMonth.now();
        double thisMonth = sumForMonth(expenses, now);
        double lastMonth = sumForMonth(expenses, now.minusMonths(1));

        if (lastMonth == 0.0 && thisMonth == 0.0)
            return "Stable";
        if (lastMonth == 0.0)
            return "Increasing";

        double changePct = ((thisMonth - lastMonth) / lastMonth) * 100.0;
        if (changePct > 10.0)
            return "Increasing";
        if (changePct < -10.0)
            return "Decreasing";
        return "Stable";
    }

    private double sumForMonth(List<Expense> expenses, YearMonth ym) {
        double sum = 0.0;
        for (Expense e : expenses) {
            if (e == null || e.getDate() == null)
                continue;
            LocalDate d = e.getDate().toLocalDate();
            if (YearMonth.from(d).equals(ym))
                sum += e.getAmount();
        }
        return sum;
    }

    private String safeCategory(String c) {
        if (c == null)
            return "Other";
        String t = c.trim();
        return t.isEmpty() ? "Other" : t;
    }
}
