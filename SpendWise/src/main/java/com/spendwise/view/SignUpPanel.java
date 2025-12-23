package com.spendwise.view;

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
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.spendwise.controllers.AuthController;

public class SignUpPanel extends JPanel {

    private JTextField fullNameField;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    private Runnable onSignInClicked;
    private AuthController authController;

    public SignUpPanel() {
        this.authController = new AuthController();
        setLayout(new GridBagLayout());

        // 1. White Card Panel
        JPanel cardPanel = new JPanel();
        cardPanel.setPreferredSize(new Dimension(400, 650));
        cardPanel.setBackground(UIConstants.WHITE_BG);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // 2. Logo and header
        JLabel logoLabel;
        java.net.URL imgURL = getClass().getResource("/logo.png");
        if (imgURL != null) {
            ImageIcon originalIcon = new ImageIcon(imgURL);
            Image scaledImage = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            logoLabel = new JLabel(new ImageIcon(scaledImage));
        } else {
            logoLabel = new JLabel("W$");
            logoLabel.setFont(new Font("Arial", Font.BOLD, 40));
            logoLabel.setForeground(UIConstants.PRIMARY_GREEN);
        }
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(UIConstants.HEADING_FONT);
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 3. Form parts
        fullNameField = new JTextField();
        JPanel fullNamePanel = createFieldPanel("Full Name", fullNameField);

        usernameField = new JTextField();
        JPanel usernamePanel = createFieldPanel("Username", usernameField);

        emailField = new JTextField();
        JPanel emailPanel = createFieldPanel("Email Address", emailField);

        passwordField = new JPasswordField();
        JPanel passwordPanel = createFieldPanel("Password", passwordField);

        confirmPasswordField = new JPasswordField();
        JPanel confirmPassPanel = createFieldPanel("Confirm Password", confirmPasswordField);

        // 4. Button and link
        RoundedButton signUpButton = new RoundedButton("SIGN UP", 15);
        signUpButton.setFont(UIConstants.BUTTON_FONT);
        signUpButton.setBackground(UIConstants.PRIMARY_GREEN);
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        signUpButton.addActionListener(e -> handleSignUp());

        JLabel loginLink = new JLabel("Back to Login");
        loginLink.setFont(UIConstants.BODY_FONT);
        loginLink.setForeground(UIConstants.PRIMARY_BLUE);
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLink.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onSignInClicked != null) {
                    onSignInClicked.run();
                }
            }
        });

        cardPanel.add(logoLabel);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(titleLabel);
        cardPanel.add(Box.createVerticalStrut(20));

        cardPanel.add(fullNamePanel);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(usernamePanel);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(emailPanel);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(passwordPanel);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(confirmPassPanel);

        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(signUpButton);
        cardPanel.add(Box.createVerticalStrut(15));
        cardPanel.add(loginLink);

        add(cardPanel);
    }

    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(UIConstants.GRAY_TEXT);

        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, label.getPreferredSize().height));
        label.setHorizontalAlignment(SwingConstants.LEFT);

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        return panel;
    }

    public void setOnSignInClicked(Runnable callback) {
        this.onSignInClicked = callback;
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

    private void handleSignUp() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String pass = new String(passwordField.getPassword());
        String confirm = new String(confirmPasswordField.getPassword());

        // Validation
        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!pass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (pass.length() < 4) {
            JOptionPane.showMessageDialog(this, "Password must be at least 4 characters!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Backend registration
        boolean registrationSuccess = authController.register(username, pass, email);
        
        if (registrationSuccess) {
            JOptionPane.showMessageDialog(this, 
                "Account created successfully!\nYou can now login with your credentials.", 
                "Registration Successful", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Clear fields
            fullNameField.setText("");
            usernameField.setText("");
            emailField.setText("");
            passwordField.setText("");
            confirmPasswordField.setText("");
            
            // Go back to login
            if (onSignInClicked != null) {
                onSignInClicked.run();
            }
        } else {
            // More specific error message
            JOptionPane.showMessageDialog(this, 
                "<html><body style='width: 250px'>" +
                "<b>Registration Failed!</b><br><br>" +
                "This username or email is already registered.<br>" +
                "Please try with different credentials or login if you already have an account." +
                "</body></html>", 
                "Registration Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
