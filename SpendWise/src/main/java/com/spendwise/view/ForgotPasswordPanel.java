package com.spendwise.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.spendwise.view.components.RoundedButton;
import com.spendwise.view.components.RoundedPanel;
import com.spendwise.view.components.RoundedTextField;

public class ForgotPasswordPanel extends JPanel {

    private RoundedTextField emailField;
    private Runnable onBackToLoginClicked;

    public ForgotPasswordPanel() {
        setLayout(new GridBagLayout());

        // 1. White Card Panel
        RoundedPanel cardPanel = new RoundedPanel(30, UIConstants.WHITE_BG);
        cardPanel.setPreferredSize(new Dimension(380, 480));
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // 2. Logo
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

        // Titles
        JLabel titleLabel = new JLabel("Forgot Password?");
        titleLabel.setFont(UIConstants.HEADING_FONT);
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel(
                "<html><center>Enter your email below to receive<br>password reset instructions.</center></html>");
        descLabel.setFont(UIConstants.BODY_FONT);
        descLabel.setForeground(UIConstants.GRAY_TEXT);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // 3. Email Field
        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 12));
        emailLabel.setForeground(UIConstants.TEXT_COLOR);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, emailLabel.getPreferredSize().height));
        emailLabel.setHorizontalAlignment(SwingConstants.LEFT);

        emailField = new RoundedTextField(UIConstants.ROUNDED_RADIUS, "Enter your email");
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 4. Button and Link
        RoundedButton sendButton = new RoundedButton("Send Reset Link", UIConstants.ROUNDED_RADIUS,
                UIConstants.PRIMARY_GREEN, UIConstants.darker(UIConstants.PRIMARY_GREEN));
        sendButton.setFont(UIConstants.BUTTON_FONT);
        sendButton.setForeground(Color.WHITE);
        sendButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        sendButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        sendButton.addActionListener(e -> handleSendReset());

        JLabel backLink = new JLabel("Back to Login");
        backLink.setFont(UIConstants.BODY_FONT);
        backLink.setForeground(UIConstants.PRIMARY_BLUE);
        backLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backLink.setAlignmentX(Component.CENTER_ALIGNMENT);

        backLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onBackToLoginClicked != null) {
                    onBackToLoginClicked.run();
                }
            }
        });

        // Add Components
        cardPanel.add(logoLabel);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(titleLabel);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(descLabel);
        cardPanel.add(Box.createVerticalStrut(30));

        cardPanel.add(emailLabel);
        cardPanel.add(Box.createVerticalStrut(8));
        cardPanel.add(emailField);

        cardPanel.add(Box.createVerticalStrut(25));
        cardPanel.add(sendButton);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(backLink);

        add(cardPanel);
    }

    public void setOnBackToLoginClicked(Runnable onBackToLoginClicked) {
        this.onBackToLoginClicked = onBackToLoginClicked;
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

    private void handleSendReset() {
        String email = emailField.getText();

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an email address.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        System.out.println("Reset Link Sent: " + email);

        JOptionPane.showMessageDialog(this, "Password reset link sent to your email!", "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
