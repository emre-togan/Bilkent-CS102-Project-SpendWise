package com.spendwise.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private LoginPanel loginPanel;
    private SignUpPanel signUpPanel;
    private ForgotPasswordPanel forgotPasswordPanel;
    private DashBoardPanel dashboardPanel;
    private BudgetPanel budgetPanel;
    private ExpensesPanel expensesPanel;
    private ShopPanel shopPanel;
    private ChatPanel chatPanel;
    private ProfilePanel profilePanel;
    private SettingsPanel settingsPanel;

    public MainFrame() {
        setTitle("SpendWise - Personal Finance & Smart Shopping");
        setSize(1500, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        initializePanels();

        add(mainPanel);

        showPanel("LOGIN");
    }

    private void initializePanels() {

        loginPanel = new LoginPanel();
        loginPanel.setOnForgotPasswordClicked(() -> showPanel("FORGOT_PASSWORD"));
        loginPanel.setOnSignUpClicked(() -> showPanel("SIGNUP"));
        loginPanel.setOnLoginSuccess(this::showDashboard);

        signUpPanel = new SignUpPanel();
        signUpPanel.setOnSignInClicked(() -> showPanel("LOGIN"));

        forgotPasswordPanel = new ForgotPasswordPanel();
        forgotPasswordPanel.setOnBackToLoginClicked(() -> showPanel("LOGIN"));

        dashboardPanel = new DashBoardPanel(this);
        budgetPanel = new BudgetPanel(this);
        expensesPanel = new ExpensesPanel(this);
        shopPanel = new ShopPanel(this);
        chatPanel = new ChatPanel(this);
        profilePanel = new ProfilePanel(this);
        settingsPanel = new SettingsPanel(this);

        settingsPanel.setOnLogoutClicked(this::logout);

        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(signUpPanel, "SIGNUP");
        mainPanel.add(forgotPasswordPanel, "FORGOT_PASSWORD");
        
        mainPanel.add(dashboardPanel, "DASHBOARD");
        mainPanel.add(budgetPanel, "BUDGET");
        mainPanel.add(expensesPanel, "EXPENSES");
        mainPanel.add(shopPanel, "SHOP");
        mainPanel.add(chatPanel, "CHAT");
        mainPanel.add(profilePanel, "PROFILE"); 
        mainPanel.add(settingsPanel, "SETTINGS"); 
    }

    public void showPanel(String panelName) {

        cardLayout.show(mainPanel, panelName);

        switch (panelName) {
            case "DASHBOARD":
                dashboardPanel.refreshData();
                break;
            case "BUDGET":
                budgetPanel.refreshData();
                break;
            case "EXPENSES":
                expensesPanel.refreshData();
                break;
            case "SHOP":
                shopPanel.refreshData();
                break;
            case "CHAT":
                chatPanel.refreshData();
                break;
            case "PROFILE":
                profilePanel.refreshData(); 
                break;
            case "SETTINGS":
                settingsPanel.refreshData(); 
                break;
        }
    }

    public void showDashboard() {
        showPanel("DASHBOARD");
    }

    public void logout() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {

            dashboardPanel.clearData();
            budgetPanel.clearData();
            expensesPanel.clearData();
            shopPanel.clearData();
            chatPanel.clearData();
            profilePanel.clearData();

            UserSession.setCurrentUserId(0); 

            showPanel("LOGIN");
        }
    }
}