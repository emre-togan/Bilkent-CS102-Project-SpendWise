package com.spendwise.utils;

import com.spendwise.models.Expense;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ExportUtil {

    public static void exportExpensesToCSV(List<Expense> expenses, String filepath) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(filepath))) {
            out.println("expense_id,user_id,category,description,amount,date");
            if (expenses == null)
                return;

            for (Expense e : expenses) {
                if (e == null)
                    continue;
                out.printf("%d,%d,%s,%s,%.2f,%s%n",
                        e.getExpenseId(),
                        e.getUserId(),
                        escapeCsv(e.getCategory()),
                        escapeCsv(e.getDescription()),
                        e.getAmount(),
                        (e.getDate() == null ? "" : e.getDate().toString()));
            }
        }
    }

    public static void exportExpensesToPDF(List<Expense> expenses, String filepath) {
        throw new UnsupportedOperationException("PDF export not implemented. Add iText and implement.");
    }

    public static void exportBudgetReportPDF(Object budget, List<Expense> expenses, String filepath) {
        throw new UnsupportedOperationException("Budget report PDF not implemented. Add iText and implement.");
    }

    private static String escapeCsv(String s) {
        if (s == null)
            return "";
        String t = s.replace("\"", "\"\"");
        if (t.contains(",") || t.contains("\n") || t.contains("\r")) {
            return "\"" + t + "\"";
        }
        return t;
    }
}
