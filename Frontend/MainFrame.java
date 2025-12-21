import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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
        this.setTitle("SpendWise - Personal Finance & Smart Shopping");
        this.setSize(1200, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.cardLayout = new CardLayout();
        this.mainPanel = new JPanel();
        initializePanels();
        this.add(mainPanel);
        showPanel("LOGIN");

    }

    private void initializePanels(){
        this.loginPanel = new LoginPanel();
        this.mainPanel.add(loginPanel, "LOGIN");

        this.signUpPanel = new SignUpPanel();
        this.mainPanel.add(signUpPanel, "SIGNUP");

        this.forgotPasswordPanel = new ForgotPasswordPanel();
        this.forgotPasswordPanel.setOnBackToLoginClicked(() -> showPanel("LOGIN"));
        this.mainPanel.add(forgotPasswordPanel, "FORGOTPASSWORD");  

        this.dashBoardPanel = new DashBoardPanel(this);
        this.mainPanel.add(dashBoardPanel, "DASHBOARD");

        this.budgetPanel = new BudgetPanel(this);
        this.mainPanel.add(budgetPanel, "BUDGET");

        this.expensesPanel = new ExpensesPanel(this);
        this.mainPanel.add(expensesPanel, "EXPENSE");
    
        this.shopPanel = new ShopPanel(this);
        this.mainPanel.add(shopPanel, "SHOP");

        this.chatPanel = new ChatPanel();
        this.mainPanel.add(chatPanel, "CHAT");

        this.profilePanel = new ProfilePanel();
        this.mainPanel.add(profilePanel, "PROFILE");

        this.settingsPanel = new SettingsPanel();
        settingsPanel.setOnLogoutClicked(() -> logout());
        this.mainPanel.add(settingsPanel, "SETTINGS");


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

    public void showDashBoard(RegularUser user){
        this.currentUser = user;
        this.dashBoardPanel.setCurrentUser(user);
        this.expensesPanel.setCurrentUser(user);
        this.shopPanel.setCurrentUser(user);

        showPanel("DASHBOARD");
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

        showPanel("LOGIN");

    }

    public static void main (String[] args){
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);

        });
    }
}
