import Utils.UIConstants;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class DashBoardPanel extends JPanel{
    private MainFrame mainFrame;
    private RegularUser currentUser;

    private JPanel sideMenuPanel;
    private JPanel menuPanel;
    private JPanel headerPanel;
    private JPanel footerPanel;
    private JPanel contentPanel;
    
    private JButton dashBoardButton;
    private JButton budgetButton;
    private JButton expensesButton;
    private JButton shopButton;
    private JButton chatButton;
    private JButton profileButton;
    private JButton settingsButton;
    private JButton logoutButton;

    private JLabel greetingLabel;
    private JLabel balanceLabel;
    private JLabel totalBudgetLabel;
    private JLabel totalSpentLabel;
    private JProgressBar budgetProgressBar;
    private JLabel remainingLabel;
    private JLabel percentageLabel;
    private JButton addExpenseButton;
    private JButton viewDiscountsButton;
    private JPanel weeklyChartPanel;
    private JPanel recentTransactionsPanel;
    private JPanel userProfileCard;

    public DashBoardPanel(MainFrame mainFrame){
        this.mainFrame = mainFrame;
        this.setLayout(new BorderLayout());
        this.setBackground(UIConstants.WHITE_BG);
        this.add(createSideMenu(),BorderLayout.WEST);
        createContent();
        this.add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createSideMenu(){


        JPanel sideMenu = new JPanel();
        sideMenu.setPreferredSize(new Dimension(260, 800));
        sideMenu.setBackground(Color.WHITE);
        sideMenu.setLayout(null);
        sideMenu.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(240, 240, 240)));
        
        JLabel logo = new JLabel("W$");
        logo.setBounds(20, 25, 50, 50);
        logo.setFont(new Font("Arial", Font.BOLD, 40));
        logo.setForeground(UIConstants.PRIMARY_GREEN);
        sideMenu.add(logo);
        
        JLabel appName = new JLabel("Finance Assistant");
        appName.setBounds(80, 35, 150, 30);
        appName.setFont(new Font("Arial", Font.PLAIN, 15));
        appName.setForeground(new Color(100, 100, 100));
        sideMenu.add(appName);
        
        int startY = 120;
        addMenuButton(sideMenu, "🏠", "Dashboard", startY, true);
        addMenuButton(sideMenu, "💳", "Budget", startY + 60, false);
        addMenuButton(sideMenu, "🧾", "Expenses", startY + 120, false);
        addMenuButton(sideMenu, "🛍️", "Shop", startY + 180, false);
        addMenuButton(sideMenu, "💬", "Chat with Friends", startY + 240, false);
        addMenuButton(sideMenu, "👤", "Profile", startY + 300, false);
        addMenuButton(sideMenu, "⚙️", "Settings", startY + 360, false);
        
        JPanel profileCard = new JPanel();
        profileCard.setBounds(15, 650, 230, 70);
        profileCard.setBackground(new Color(248, 249, 250));
        profileCard.setLayout(null);
        profileCard.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        
        JLabel avatar = new JLabel("SJ");
        avatar.setBounds(15, 15, 40, 40);
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setOpaque(true);
        avatar.setBackground(UIConstants.PRIMARY_GREEN);
        avatar.setForeground(Color.WHITE);
        avatar.setFont(new Font("Arial", Font.BOLD, 16));
        profileCard.add(avatar);
        
        JLabel userName = new JLabel("Sarah Johnson");
        userName.setBounds(65, 18, 150, 18);
        userName.setFont(new Font("Arial", Font.BOLD, 13));
        profileCard.add(userName);
        
        JLabel userEmail = new JLabel("sarah@email.com");
        userEmail.setBounds(65, 37, 150, 15);
        userEmail.setFont(new Font("Arial", Font.PLAIN, 11));
        userEmail.setForeground(new Color(120, 120, 120));
        profileCard.add(userEmail);
        
        sideMenu.add(profileCard);
        
        JButton logoutBtn = new JButton("↩︎ Logout");
        logoutBtn.setBounds(15, 735, 230, 40);
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 14));
        logoutBtn.setForeground(new Color(220, 53, 69));
        logoutBtn.setBackground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createLineBorder(new Color(220, 53, 69)));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> mainFrame.logout());
        sideMenu.add(logoutBtn);
        
        return sideMenu;

    }

    private void addMenuButton(JPanel panel, String icon, String text, int y, boolean selected){
        JButton button = new JButton(icon + " " + text);
        button.setBounds(10,y,240,50);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(new EmptyBorder(0,15,0,0));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if(selected){
            button.setBackground(UIConstants.PRIMARY_GREEN);
            button.setForeground(Color.WHITE);
            button.setOpaque(true);

        }
        else{
            button.setContentAreaFilled(false);
            button.setForeground(new Color(80,80,80));
        }

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e){
                if(!selected){
                    button.setBackground(new Color(245,245,245));
                    button.setOpaque(true);                   
                }
            }
            public void mouseExisted(MouseEvent e){
                if(!selected){
                    button.setContentAreaFilled(false);
                }
            }
        });

        button.addActionListener(e -> {
            String panelName = text.replace("with Friends", "").toUpperCase();
            mainFrame.showPanel(panelName);
        });
        panel.add(button);
    }

    private JPanel createContentPanel(){
        JPanel content = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(new Color(250,250,250));

        greetingLabel = new JLabel("Good Morning, " + currentUser.getName());
        greetingLabel.setBounds(30,30,600,40);
        greetingLabel.setFont(new Font("Arial", Font.BOLD, 28));
        contentPanel.add(greetingLabel);

        JPanel balancePanel = new JPanel();
        balancePanel.setBounds(900,30,200,80);
        balancePanel.setBackground(Color.WHITE);
        balancePanel.setLayout(null);
        balancePanel.setBorder(BorderFactory.createLineBorder(new Color(230,230,230)));

        JLabel balanceTitle = new JLabel("Total Balance");
        balanceTitle.setBounds(15,15,170,20);
        balanceTitle.setFont(new Font("Arial", Font.PLAIN, 13));
        balanceTitle.setForeground(new Color(120,120,120));
        balancePanel.add(balanceTitle);

        balanceLabel = new JLabel(" &... "); // balancı alıcaz
        balanceLabel.setBounds(15,35,170,35);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 32));
        balanceLabel.setForeground(UIConstants.PRIMARY_GREEN);
        balancePanel.add(balanceLabel);

        contentPanel.add(balancePanel);

        createBudgetCard(contentPanel);

        addExpenseButton = createActionButton("➕ Add Expense", UIConstants.PRIMARY_GREEN);
        addExpenseButton.setBounds(30,280,510,60);
        addExpenseButton.addActionListener(e -> mainFrame.showPanel("EXPENSES"));
        contentPanel.add(addExpenseButton);

        viewDiscountsButton = createActionButton("📈 View Discounts", new Color(255,152,0));
        viewDiscountsButton.setBounds(560,280,540,60);
        viewDiscountsButton.addActionListener(e -> mainFrame.showPanel("SHOP"));
        contentPanel.add(viewDiscountsButton);

        createWeeklyChart(contentPanel);

        createRecentTransactions(contentPanel);
        
        return contentPanel;
    }

    private void createBudgetCard(JPanel panel){
        JPanel budgetCard= new JPanel();
        budgetCard.setBounds(30,130,1070,130);
        budgetCard.setBackground(Color.WHITE);
        budgetCard.setLayout(null);
        budgetCard.setBorder(BorderFactory.createLineBorder(new Color(230,230,230)));

        JLabel budgetLabel = new JLabel("Total Budget");
        budgetLabel.setBounds(30,20,200,25);
        budgetLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(budgetLabel);

        JLabel budgetAmount = new JLabel("&..."); // budget yazılacak
        budgetAmount.setBounds(30,20,200,25);
        budgetAmount.setFont(new Font("Arial",Font.BOLD, 18));
        budgetCard.add(budgetAmount);

        JLabel spentLabel = new JLabel("Spent");
        spentLabel.setBounds(930,20,100,25);
        spentLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        spentLabel.setForeground(new Color(120,120,120));
        spentLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        budgetCard.add(spentLabel);
        
        JLabel spentAmount = new JLabel("..."); // spent yazılacak
        spentAmount.setBounds(930,20,100,25);
        spentAmount.setFont(new Font("Arial", Font.BOLD, 16));
        spentAmount.setForeground(new Color(220,53,69));
        spentAmount.setHorizontalAlignment(SwingConstants.RIGHT);
        budgetCard.add(spentAmount);

        budgetProgressBar = new JProgressBar(00,100);
        budgetProgressBar.setBounds(30,60,1010,12);
        budgetProgressBar.setValue(67); // veri yazılacak
        budgetProgressBar.setForeground(new Color(33,150,243));
        budgetProgressBar.setBackground(new Color(230,230,230));
        budgetProgressBar.setBorderPainted(false);
        budgetCard.add(budgetProgressBar);

        JLabel remainingLabel = new JLabel("💸 Remaining: " + "..."); // kalan yazılacak
        remainingLabel.setBounds(30,85,200,25);
        remainingLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        remainingLabel.setForeground(UIConstants.PRIMARY_GREEN);
        budgetCard.add(remainingLabel);

        percentageLabel = new JLabel("%... used"); // yüzde yazılacak
        percentageLabel.setBounds(890,85,150,25);
        percentageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        percentageLabel.setForeground(new Color(120,120,120));
        percentageLabel.setHorizontalAlignment((SwingConstants.RIGHT));
        budgetCard.add(percentageLabel);

        panel.add(budgetCard);
    }

    private JButton createActionButton(String text, Color bgColor){
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener((new MouseAdapter() {
            public void mouseEntered(MouseEvent e){
                button.setBackground(bgColor.darker());
            }
            public void mouseExisted(MouseEvent e){
                button.setBackground(bgColor);
            }
        }));

        return button;
    }

    private void createWeeklyChart(JPanel panel){
        JPanel chartCard = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
                int[] values = {1,2,23,4,545,54}; // values yazılavcak

                int maxValue = 800;
                int chartHeight = 200;
                int baseY= 250;
                int barWidth = 60;
                int spacing = 15;

                for(int i = 0; i < days.length; i++){
                    int barHeight = (values[i] * chartHeight) / maxValue;
                    int x = 50 * (i * (barWidth * spacing));
                    int y = baseY - barHeight;

                    if(i> 0){
                        int prevHeight = (values[i-1]* chartHeight) / maxValue;
                        int prevX = 50 + ((i-1) * (barWidth + spacing)) + barWidth/2;
                        int prevY = baseY - prevHeight;
                        int currentX = x + barWidth/2;

                        g2d.setColor(UIConstants.PRIMARY_GREEN);
                        g2d.setStroke(new BasicStroke(3));
                        g2d.drawLine(prevX, prevY, currentX, y);
                    }

                    g2d.setColor(UIConstants.PRIMARY_GREEN);
                    g2d.fillOval(x + barWidth/2 -5, y - 5, 10, 10);

                    g2d.setColor(new Color(50,50,50));
                    g2d.setFont(new Font("Arial", Font.BOLD, 12));
                    String valueStr = "&" + values[i];
                    int strWidth = g2d.getFontMetrics().stringWidth(valueStr);
                    g2d.drawString(valueStr, x + (barWidth - strWidth) / 2, y - 15);

                    g2d.setColor(new Color(120,120,120));
                    g2d.setFont(new Font("Arial", Font.PLAIN, 12));
                    int dayWidth = g2d.getFontMetrics().stringWidth(days[i]);
                    g2d.drawString(days[i], x + (barWidth - dayWidth) / 2, baseY + 25);

                }

            }
        };

        chartCard.setBounds(30,360,650,330);
        chartCard.setBackground(Color.WHITE);
        chartCard.setLayout(null);
        chartCard.setBorder(BorderFactory.createLineBorder(new Color(230,230,230)));

        JLabel chartTitle = new JLabel("Weekly Spending");
        chartTitle.setBounds(20,15,200,25);
        chartTitle.setFont(new Font("Arial", Font.BOLD, 16));
        chartCard.add(chartTitle);

        JLabel trendLabel = new JLabel("..."); // bir şeyler yazılacak
        trendLabel.setBounds(560,15,70,25);
        trendLabel.setFont(new Font("Arial", Font.BOLD, 13));
        trendLabel.setForeground(new Color(220,53,69));
        chartCard.add(trendLabel);

        panel.add(chartCard);
    }

    private void createRecentTransactions(JPanel panel){
        JPanel transCard = new JPanel();
        transCard.setBounds(700,360,400,330);
        transCard.setBackground(Color.WHITE);
        transCard.setLayout(null);
        transCard.setBorder(BorderFactory.createLineBorder(new Color(230,230,230)));

        JLabel title = new JLabel("Recent Transactions");
        title.setBounds(20,15,300,25);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        transCard.add(title);

        String[][] transactions = {
            {"🛒", "Grocery Shopping", "Food", "$85.50"},
            {"🚗", "Uber Ride", "Transport", "$12.30"},
            {"🛍", "Online Purchase", "Shopping", "$156.00"}
        };

        int itemY = 60;

        for(String[] trans : transactions){
            JPanel item = createTransactionItem(trans[0],trans[1],trans[2],trans[3]);
            item.setBounds(15,itemY,370,70);
            transCard.add(item);
            itemY += 80;
        }

        panel.add(transCard);

    }

    private JPanel createTransactionsItem(String emoji, String desc, String category, String amount){
        JPanel item = new JPanel();
        item.setLayout(null);
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createMatteBorder(0,0,1,0,new Color(240,240,240)));

        JLabel icon = new JLabel(emoji);
        icon.setBounds(10,15,40,40);
        icon.setFont(new Font("Arial", Font.PLAIN,28));
        item.add(icon);

        JLabel descLabel = new JLabel(desc);
        descLabel.setBounds(60,12,200,20);
        descLabel.setFont(new Font("Arial", Font.BOLD,14));
        item.add(descLabel);

        JLabel catLabel = new JLabel(category);
        catLabel.setBounds(60,35,200,18);
        catLabel.setFont(new Font("Arial", Font.PLAIN,12));
        catLabel.setForeground(new Color(120,120,120));
        item.add(catLabel);

        JLabel amountLabel = new JLabel(amount);
        amountLabel.setBounds(270,20,90,25);
        amountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        amountLabel.setForeground(new Color(220,53,69));
        amountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        item.add(amountLabel);

        return item;

    }

    public void refreshData(){
        //backend integration
    }

    public void clearData(){
        greetingLabel.setText("Good Morning");
        balanceLabel.setText("&0");
    }

}
