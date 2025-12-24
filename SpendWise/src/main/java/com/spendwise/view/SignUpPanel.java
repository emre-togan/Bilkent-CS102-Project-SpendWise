package com.spendwise.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.spendwise.controllers.AuthController;
import com.spendwise.view.components.RoundedButton;
import com.spendwise.view.components.RoundedPanel;
import com.spendwise.view.components.RoundedPasswordField;
import com.spendwise.view.components.RoundedTextField;

public class SignUpPanel extends JPanel {

    private RoundedTextField fullNameField;
    private RoundedTextField usernameField;
    private RoundedTextField emailField;
    private RoundedPasswordField passwordField;
    private RoundedPasswordField confirmPasswordField;

    private Runnable onSignInClicked;
    private AuthController authController;

    public SignUpPanel() {
        this.authController = new AuthController();
        setLayout(new GridBagLayout());

        // 1. White Card Panel - Taller for signup
        RoundedPanel cardPanel = new RoundedPanel(30, UIConstants.WHITE_BG);
        cardPanel.setPreferredSize(new Dimension(420, 700)); // Taller for more fields
        cardPanel.setBackground(UIConstants.WHITE_BG);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // 2. Logo and header
        JLabel logoLabel;
        java.net.URL imgURL = getClass().getResource("/Resim1.png");
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

        JLabel subLabel = new JLabel("Sign up to get started");
        subLabel.setFont(UIConstants.BODY_FONT);
        subLabel.setForeground(UIConstants.GRAY_TEXT);
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 3. Form parts
        fullNameField = new RoundedTextField(UIConstants.ROUNDED_RADIUS, "Enter your full name");
        JPanel fullNamePanel = createFieldPanel("Full Name", fullNameField);

        usernameField = new RoundedTextField(UIConstants.ROUNDED_RADIUS, "Enter your username");
        JPanel usernamePanel = createFieldPanel("Username", usernameField);

        emailField = new RoundedTextField(UIConstants.ROUNDED_RADIUS, "Enter your email");
        JPanel emailPanel = createFieldPanel("Email Address", emailField);

        passwordField = new RoundedPasswordField(UIConstants.ROUNDED_RADIUS, "Enter your password");
        JPanel passwordPanel = createFieldPanel("Password", passwordField);

        confirmPasswordField = new RoundedPasswordField(UIConstants.ROUNDED_RADIUS, "Confirm your password");
        JPanel confirmPassPanel = createFieldPanel("Confirm Password", confirmPasswordField);

        // 4. Button and link
        RoundedButton signUpButton = new RoundedButton("Sign Up", UIConstants.ROUNDED_RADIUS, UIConstants.PRIMARY_GREEN,
                UIConstants.darker(UIConstants.PRIMARY_GREEN));
        signUpButton.setFont(UIConstants.BUTTON_FONT);
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);

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

        // Add components
        cardPanel.add(logoLabel);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(titleLabel);
        cardPanel.add(Box.createVerticalStrut(5));
        cardPanel.add(subLabel);
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
        panel.setBackground(Color.WHITE); // Panel inside card needs to match card bg
        panel.setOpaque(false); // Or transparent
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(UIConstants.TEXT_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, label.getPreferredSize().height));
        label.setHorizontalAlignment(SwingConstants.LEFT);

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
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

        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!pass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (pass.length() < 4) {
            JOptionPane.showMessageDialog(this, "Password must be at least 4 characters!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean registrationSuccess = authController.register(username, pass, email);

        if (registrationSuccess) {
            JOptionPane.showMessageDialog(this,
                    "Account created successfully!\nYou can now login with your credentials.",
                    "Registration Successful",
                    JOptionPane.INFORMATION_MESSAGE);
            fullNameField.setText("");
            usernameField.setText("");
            emailField.setText("");
            passwordField.setText("");
            confirmPasswordField.setText("");
            if (onSignInClicked != null) {
                onSignInClicked.run();
            }
        } else {
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
