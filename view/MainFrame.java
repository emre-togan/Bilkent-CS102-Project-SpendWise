package view;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.CardLayout;
import java.awt.color.ProfileDataException;
import java.io.BufferedReader;


public class MainFrame extends JFrame{
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    private LoginPanel loginPanel;
    private SignUpPanel signUpPanel;
    private ForgotPasswordPanel forgotPasswordPanel;
    private DashBoardPanel dashBoardPanel;
    private BudgetPanel budgetPanel;
    private ExpensesPanel expensesPanel;
    private ShopPanel shopPanel;
    private ChatPanel chatPanel;
    private ProfilePanel profilePanel;
    private SettingsPanel settingsPanel;

    private RegularUser currentUser;

    public MainFrame(){
        this.setSize(1200, 800);
        this.cardLayout = new CardLayout();
        this.mainPanel = new JPanel();
        initializePanels();
        showPanel(loginPanel);

    }

    private void initializePanels(){
        this.loginPanel = new JPanel();
        this.mainPanel.add(loginPanel, "LOGIN_PANEL");

        this.signUpPanel = new JPanel();
        this.mainPanel.add(signUpPanel, "SIGNUP_PANEL");

        this.forgotPasswordPanel = new JPanel();
        this.mainPanel.add(forgotPasswordPanel, "FORGOTPASSWORD_PANEL");  

        this.dashBoardPanel = new JPanel();
        this.mainPanel.add(dashBoardPanel, "DASHBOARD_PANEL");

        this.budgetPanel = new JPanel();
        this.mainPanel.add(budgetPanel, "BUDGET_PANEL");

        this.expensesPanel = new JPanel();
        this.mainPanel.add(expensesPanel, "EXPENSE_PANEL");
    
        this.shopPanel = new JPanel();
        this.mainPanel.add(shopPanel, "SHOP_PANEL");

        this.chatPanel = new JPanel();
        this.mainPanel.add(chatPanel, "CHAT_PANEL");

        this.profilePanel = new JPanel();
        this.mainPanel.add(profilePanel, "PROFILE_PANEL");

        this.settingsPanel = new JPanel();
        this.mainPanel.add(settingsPanel, "SETTINGS_PANEL");


    }

    private void showPanel(JPanel panel){
        cardLayout.show(panel, panel.getName());
        refreshCurrentPanel(panel.getName());

    }

    private void refreshCurrentPanel(String panelName){
        if(panelName.equals("LOGIN_PANEL")){
            loginPanel.refreshData();

        }
        else if(panelName.equals("SIGNUP_PANEL")){
            signUpPanel.refreshData();
            
        }
        else if(panelName.equals("FORGOTPASSWORD_PANEL")){
            forgotPasswordPanel.refreshData();
            
        }
        else if(panelName.equals("DASHBOARD_PANEL")){
            dashBoardPanel.refreshData();
            
        }
        else if(panelName.equals("BUDGET_PANEL")){
            budgetPanel.refreshData();
            
        }
        else if(panelName.equals("EXPENSE_PANEL")){
            expensesPanel.refreshData();
            
        }
        else if(panelName.equals("SHOP_PANEL")){
            shopPanel.refreshData();
            
        }
        else if(panelName.equals("CHAT_PANEL")){
            chatPanel.refreshData();
            
        }
        else if(panelName.equals("PROFILE_PANEL")){
            profilePanel.refreshData();
            
        }
        else if(panelName.equals("SETTINGS_PANEL")){
            settingsPanel.refreshData();
            
        }

        
    }

    public RegularUser getCurrentUser() {
        return currentUser;
    }

    public void logout(){
        currentUser = null;

        loginPanel.clearData();
        signUpPanel.clearData();
        forgotPasswordPanel.clearData();
        dashBoardPanel.clearData();
        budgetPanel.clearData();
        expensesPanel.clearData();
        shopPanel.clearData();
        chatPanel.clearData();
        profilePanel.clearData();
        settingsPanel.clearData();

        showPanel(loginPanel);

    }
}