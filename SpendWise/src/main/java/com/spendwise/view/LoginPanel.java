package com.spendwise.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.spendwise.controllers.AuthController;
import com.spendwise.models.User;
import com.spendwise.view.components.RoundedButton;
import com.spendwise.view.components.RoundedPanel;
import com.spendwise.view.components.RoundedPasswordField;
import com.spendwise.view.components.RoundedTextField;

public class LoginPanel extends JPanel {

    private RoundedTextField usernameField;
    private RoundedPasswordField passwordField;
    private JToggleButton showPasswordToggle;

    private Runnable onForgotPasswordClicked;
    private Runnable onSignUpClicked;
    private Runnable onLoginSuccess;

    private AuthController authController;

    public LoginPanel() {
        this.authController = new AuthController();
        setLayout(new GridBagLayout());

        // Use RoundedPanel instead of plain JPanel
        RoundedPanel cardPanel = new RoundedPanel(30, UIConstants.WHITE_BG);
        cardPanel.setPreferredSize(new Dimension(380, 550)); // Slightly taller for better spacing
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // --- Logo ---
        JLabel logoLabel = new JLabel();
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        java.net.URL logoUrl = getClass().getResource("/Resim1.png");
        if (logoUrl != null) {
            ImageIcon originalIcon = new ImageIcon(logoUrl);
            Image image = originalIcon.getImage();
            Image newimg = image.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH); // Slightly larger logo
            logoLabel.setIcon(new ImageIcon(newimg));
        } else {
            logoLabel.setText("W₺");
            logoLabel.setFont(new Font("Arial", Font.BOLD, 50));
            logoLabel.setForeground(UIConstants.PRIMARY_GREEN);
        }

        // --- Brand Name ---
        JLabel brandLabel = new JLabel("SpendWise"); // Or whatever the app name is
        brandLabel.setFont(new Font("Arial", Font.BOLD, 14));
        brandLabel.setForeground(UIConstants.GRAY_TEXT);
        brandLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sloganLabel = new JLabel("Personal Finance & Smart Shopping");
        sloganLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        sloganLabel.setForeground(UIConstants.GRAY_TEXT);
        sloganLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Welcome Text ---
        JLabel welcomeLabel = new JLabel("Welcome Back");
        welcomeLabel.setFont(UIConstants.HEADING_FONT); // 24px Bold
        welcomeLabel.setForeground(UIConstants.TEXT_COLOR);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subLabel = new JLabel("Sign in to continue managing your finances");
        subLabel.setFont(UIConstants.BODY_FONT);
        subLabel.setForeground(UIConstants.GRAY_TEXT);
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Username Field ---
        JLabel userLabel = new JLabel("Username or Email");
        userLabel.setFont(new Font("Arial", Font.BOLD, 12));
        userLabel.setForeground(UIConstants.TEXT_COLOR);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, userLabel.getPreferredSize().height));
        userLabel.setHorizontalAlignment(SwingConstants.LEFT);

        usernameField = new RoundedTextField(UIConstants.ROUNDED_RADIUS, "Enter your username or email");
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Password Field ---
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Arial", Font.BOLD, 12));
        passLabel.setForeground(UIConstants.TEXT_COLOR);
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, passLabel.getPreferredSize().height));
        passLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JLayeredPane passLayer = new JLayeredPane();
        passLayer.setPreferredSize(new Dimension(300, 40)); // Fixed height for input
        passLayer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passLayer.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField = new RoundedPasswordField(UIConstants.ROUNDED_RADIUS, "Enter your password");
        passwordField.setBounds(0, 0, 300, 40);

        JPanel passWrapper = new JPanel();
        passWrapper.setLayout(new OverlayLayout(passWrapper));
        passWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        passWrapper.setOpaque(false);

        passwordField = new RoundedPasswordField(UIConstants.ROUNDED_RADIUS, "Enter your password");

        passwordField.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 40)); // Right padding for eye

        showPasswordToggle = new JToggleButton("👁");
        showPasswordToggle.setMargin(new Insets(0, 0, 0, 0));
        showPasswordToggle.setFocusable(false);
        showPasswordToggle.setContentAreaFilled(false);
        showPasswordToggle.setBorderPainted(false);
        showPasswordToggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        showPasswordToggle.addActionListener(e -> {
            if (showPasswordToggle.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });

        // Alignment helper for Eye Icon
        JPanel eyePanel = new JPanel(new BorderLayout());
        eyePanel.setOpaque(false);
        eyePanel.add(showPasswordToggle, BorderLayout.EAST);
        eyePanel.setBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 10)); // Margin from right

        passWrapper.add(eyePanel); // On Top
        passWrapper.add(passwordField); // Below

        // --- Forgot Password ---
        JLabel forgotPassLabel = new JLabel("Forgot password?");
        forgotPassLabel.setFont(UIConstants.BODY_FONT);
        forgotPassLabel.setForeground(UIConstants.PRIMARY_BLUE);
        forgotPassLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotPassLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        forgotPassLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, forgotPassLabel.getPreferredSize().height));
        forgotPassLabel.setHorizontalAlignment(SwingConstants.RIGHT); // Right aligned as in typical designs

        forgotPassLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (onForgotPasswordClicked != null) {
                    onForgotPasswordClicked.run();
                }
            }
        });

        // --- Login Button ---
        RoundedButton loginButton = new RoundedButton("Login", UIConstants.ROUNDED_RADIUS, UIConstants.PRIMARY_GREEN,
                UIConstants.darker(UIConstants.PRIMARY_GREEN));
        loginButton.setFont(UIConstants.BUTTON_FONT);
        loginButton.setForeground(Color.WHITE);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginButton.addActionListener(e -> handleLogin());

        // --- Sign Up Link ---
        JLabel signUpLabel = new JLabel("Don't have an account? Sign up");
        signUpLabel.setFont(UIConstants.BODY_FONT);
        signUpLabel.setForeground(UIConstants.GRAY_TEXT);
        signUpLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signUpLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Make "Sign up" blue
        signUpLabel.setText("<html>Don't have an account? <font color='#2196F3'>Sign up</font></html>");

        signUpLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (onSignUpClicked != null) {
                    onSignUpClicked.run();
                }
            }
        });

        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(logoLabel);
        cardPanel.add(Box.createVerticalStrut(15));

        cardPanel.add(sloganLabel);

        cardPanel.add(Box.createVerticalStrut(30));
        cardPanel.add(welcomeLabel);
        cardPanel.add(Box.createVerticalStrut(5));
        cardPanel.add(subLabel);
        cardPanel.add(Box.createVerticalStrut(30));

        cardPanel.add(userLabel);
        cardPanel.add(Box.createVerticalStrut(8));
        cardPanel.add(usernameField);
        cardPanel.add(Box.createVerticalStrut(15));

        cardPanel.add(passLabel);
        cardPanel.add(Box.createVerticalStrut(8));
        cardPanel.add(passWrapper);
        cardPanel.add(Box.createVerticalStrut(10));

        cardPanel.add(forgotPassLabel);
        cardPanel.add(Box.createVerticalStrut(25));

        cardPanel.add(loginButton);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(signUpLabel);

        add(cardPanel);
    }

    public void setOnForgotPasswordClicked(Runnable callback) {
        this.onForgotPasswordClicked = callback;
    }

    public void setOnSignUpClicked(Runnable callback) {
        this.onSignUpClicked = callback;
    }

    public void setOnLoginSuccess(Runnable callback) {
        this.onLoginSuccess = callback;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        GradientPaint gp = new GradientPaint(0, 0, UIConstants.PRIMARY_GREEN, getWidth(), getHeight(),
                UIConstants.PRIMARY_BLUE);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username or email", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter password", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean loginSuccess = authController.login(username, password);

        if (loginSuccess) {
            User currentUser = authController.getCurrentUser();
            if (currentUser != null) {
                UserSession.setCurrentUserId(currentUser.getId());
            }
            usernameField.setText("");
            passwordField.setText("");
            if (onLoginSuccess != null) {
                onLoginSuccess.run();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
