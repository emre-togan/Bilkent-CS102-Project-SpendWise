import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL; 

public class LoginPanel extends JPanel {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JToggleButton showPasswordToggle;

    public LoginPanel() {
        setLayout(new GridBagLayout());

        JPanel cardPanel = new JPanel();
        cardPanel.setPreferredSize(new Dimension(350, 450));
        cardPanel.setBackground(UIConstants.WHITE_BG);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel logoLabel = new JLabel();
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        URL logoUrl = getClass().getResource("/logo.png"); 

        if (logoUrl != null) {

            ImageIcon originalIcon = new ImageIcon(logoUrl);
            
            Image image = originalIcon.getImage();
            Image newimg = image.getScaledInstance(80, 80,  java.awt.Image.SCALE_SMOOTH); 
            
            logoLabel.setIcon(new ImageIcon(newimg));
        } else {
            logoLabel.setText("W$");
            logoLabel.setFont(new Font("Arial", Font.BOLD, 40));
            logoLabel.setForeground(UIConstants.PRIMARY_GREEN);
            System.out.println("Logo dosyası bulunamadı, varsayılan metin kullanılıyor.");
        }
       

        JLabel welcomeLabel = new JLabel("Welcome Back");
        welcomeLabel.setFont(UIConstants.HEADING_FONT);
        welcomeLabel.setForeground(UIConstants.TEXT_COLOR);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subLabel = new JLabel("Sign in to continue managing finances");
        subLabel.setFont(UIConstants.BODY_FONT);
        subLabel.setForeground(UIConstants.GRAY_TEXT);
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userLabel = new JLabel("Username or Email");
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, userLabel.getPreferredSize().height));

        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel passLabel = new JLabel("Password");
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, passLabel.getPreferredSize().height));

        JPanel passPanel = new JPanel(new BorderLayout());
        passPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        passPanel.setBackground(Color.WHITE);
        passPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField = new JPasswordField();
        showPasswordToggle = new JToggleButton("👁");
        showPasswordToggle.setMargin(new Insets(0, 5, 0, 5));
        showPasswordToggle.setFocusable(false);

        showPasswordToggle.addActionListener(e -> {
            if (showPasswordToggle.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });

        passPanel.add(passwordField, BorderLayout.CENTER);
        passPanel.add(showPasswordToggle, BorderLayout.EAST);

        JLabel forgotPassLabel = new JLabel("Forgot password?");
        forgotPassLabel.setFont(UIConstants.BODY_FONT);
        forgotPassLabel.setForeground(UIConstants.PRIMARY_BLUE);
        forgotPassLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotPassLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        forgotPassLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, forgotPassLabel.getPreferredSize().height));
        forgotPassLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JButton loginButton = new JButton("LOGIN");
        loginButton.setFont(UIConstants.BUTTON_FONT);
        loginButton.setBackground(UIConstants.PRIMARY_GREEN);
        loginButton.setForeground(Color.WHITE);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginButton.addActionListener(e -> handleLogin());

        JLabel signUpLabel = new JLabel("Don't have an account? Sign up");
        signUpLabel.setFont(UIConstants.BODY_FONT);
        signUpLabel.setForeground(UIConstants.PRIMARY_BLUE);
        signUpLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signUpLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        cardPanel.add(logoLabel);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(welcomeLabel);
        cardPanel.add(Box.createVerticalStrut(5));
        cardPanel.add(subLabel);
        cardPanel.add(Box.createVerticalStrut(30));
        cardPanel.add(userLabel);
        cardPanel.add(Box.createVerticalStrut(5));
        cardPanel.add(usernameField);
        cardPanel.add(Box.createVerticalStrut(15));
        cardPanel.add(passLabel);
        cardPanel.add(Box.createVerticalStrut(5));
        cardPanel.add(passPanel);
        cardPanel.add(Box.createVerticalStrut(5));
        cardPanel.add(forgotPassLabel);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(loginButton);
        cardPanel.add(Box.createVerticalStrut(15));
        cardPanel.add(signUpLabel);

        add(cardPanel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        GradientPaint gp = new GradientPaint(0, 0, UIConstants.PRIMARY_GREEN, getWidth(), getHeight(), UIConstants.PRIMARY_BLUE);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        // Backend
    }
}