package com.spendwise.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import com.spendwise.controllers.AuthController;
import com.spendwise.models.User;

public class LoginPanel extends JPanel {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JToggleButton showPasswordToggle;

    private Runnable onForgotPasswordClicked;
    private Runnable onSignUpClicked;
    private Runnable onLoginSuccess;
    
    private AuthController authController;

    public LoginPanel() {
        this.authController = new AuthController();
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
            Image newimg = image.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(newimg));
        } else {
            logoLabel.setText("W$");
            logoLabel.setFont(new Font("Arial", Font.BOLD, 40));
            logoLabel.setForeground(UIConstants.PRIMARY_GREEN);
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
        
        forgotPassLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (onForgotPasswordClicked != null) {
                    onForgotPasswordClicked.run();
                }
            }
        });

        RoundedButton loginButton = new RoundedButton("LOGIN", 15);
        loginButton.setFont(UIConstants.BUTTON_FONT);
        loginButton.setBackground(UIConstants.PRIMARY_GREEN);
        loginButton.setForeground(Color.WHITE);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        loginButton.addActionListener(e -> handleLogin());

        JLabel signUpLabel = new JLabel("Don't have an account? Sign up");
        signUpLabel.setFont(UIConstants.BODY_FONT);
        signUpLabel.setForeground(UIConstants.PRIMARY_BLUE);
        signUpLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signUpLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        signUpLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (onSignUpClicked != null) {
                    onSignUpClicked.run();
                }
            }
        });

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

        // Validation
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter username or email",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter password",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Backend login
        boolean loginSuccess = authController.login(username, password);
        
        if (loginSuccess) {
            User currentUser = authController.getCurrentUser();
            
            // Update UserSession
            if (currentUser != null) {
                UserSession.setCurrentUserId(currentUser.getId());
            }
            
            // Clear fields
            usernameField.setText("");
            passwordField.setText("");
            
            // Success callback
            if (onLoginSuccess != null) {
                onLoginSuccess.run();
            }
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid username or password",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
