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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ForgotPasswordPanel extends JPanel {

    private JTextField emailField;
    private Runnable onBackToLoginClicked; // Geri dönüş için sinyalci

    public ForgotPasswordPanel() {
        setLayout(new GridBagLayout()); // Kartı ekranda ortalamak için

        // 1. Beyaz Kart Paneli
        JPanel cardPanel = new JPanel();
        cardPanel.setPreferredSize(new Dimension(350, 400)); // Biraz daha kısa bir kart yeterli
        cardPanel.setBackground(UIConstants.WHITE_BG);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // 2. Logo (Diğer panellerle aynı mantık)
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

        // Başlıklar
        JLabel titleLabel = new JLabel("Forgot Password?");
        titleLabel.setFont(UIConstants.HEADING_FONT);
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel("<html><center>Enter your email to receive a reset link</center></html>");
        descLabel.setFont(UIConstants.BODY_FONT);
        descLabel.setForeground(UIConstants.GRAY_TEXT);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 3. Email Alanı (Senin sevdiğin sola yaslı düzen)
        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 12));
        emailLabel.setForeground(UIConstants.GRAY_TEXT);
        // Sola yaslama ayarları
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, emailLabel.getPreferredSize().height));
        emailLabel.setHorizontalAlignment(SwingConstants.LEFT);

        emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 4. Buton ve Link
        RoundedButton sendButton = new RoundedButton("SEND RESET LINK", 15);
        sendButton.setFont(UIConstants.BUTTON_FONT);
        sendButton.setBackground(UIConstants.PRIMARY_GREEN);
        sendButton.setForeground(Color.WHITE);
        sendButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        sendButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        sendButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        sendButton.addActionListener(e -> handleSendReset());

        JLabel backLink = new JLabel("Back to Login");
        backLink.setFont(UIConstants.BODY_FONT);
        backLink.setForeground(UIConstants.PRIMARY_BLUE);
        backLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backLink.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Geri Dönüş Tıklama Olayı
        backLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onBackToLoginClicked != null) {
                    onBackToLoginClicked.run();
                }
            }
        });

        // Elemanları Ekleme
        cardPanel.add(logoLabel);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(titleLabel);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(descLabel);
        cardPanel.add(Box.createVerticalStrut(30));

        cardPanel.add(emailLabel);
        cardPanel.add(Box.createVerticalStrut(5));
        cardPanel.add(emailField);

        cardPanel.add(Box.createVerticalStrut(25));
        cardPanel.add(sendButton);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(backLink);

        add(cardPanel);
    }

    // MainFrame'den bu metoda "Login'e dön" emri bağlanacak
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
            // "Lütfen e-posta girin" yerine İngilizcesi
            JOptionPane.showMessageDialog(this, "Please enter an email address.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Backend entegrasyonu (Mock)
        System.out.println("Reset Link Sent: " + email);

        // "Bağlantı gönderildi" yerine İngilizcesi
        JOptionPane.showMessageDialog(this, "Password reset link sent to your email!", "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
