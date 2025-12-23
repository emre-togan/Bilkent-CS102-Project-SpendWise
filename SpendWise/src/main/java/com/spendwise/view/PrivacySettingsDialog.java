package com.spendwise.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

// import Services.SettingsService; // Waiting for Backend

public class PrivacySettingsDialog extends JDialog {

    public PrivacySettingsDialog(JFrame parent) {
        super(parent, "Privacy Settings", true);
        setSize(400, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UIConstants.WHITE_BG);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(UIConstants.WHITE_BG);
        content.setBorder(new EmptyBorder(20, 20, 20, 20));

        // INTEGRATION: Get current settings from Backend
        // boolean showProfile = SettingsService.getPrivacySetting("show_profile");
        // boolean findByEmail = SettingsService.getPrivacySetting("find_by_email");
        // boolean dataUsage = SettingsService.getPrivacySetting("data_usage");

        // Using placeholders 'true/false' for now
        content.add(createToggle("Show my profile to friends", "show_profile", true));
        content.add(Box.createVerticalStrut(15));
        content.add(createToggle("Allow others to find me by email", "find_by_email", false));
        content.add(Box.createVerticalStrut(15));
        content.add(createToggle("Share usage data for improvements", "data_usage", true));

        add(content, BorderLayout.CENTER);

        RoundedButton closeBtn = new RoundedButton("Close", 15);
        closeBtn.setBackground(UIConstants.PRIMARY_BLUE);
        closeBtn.setForeground(Color.WHITE);
        closeBtn.addActionListener(e -> dispose());

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(UIConstants.WHITE_BG);
        btnPanel.add(closeBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private JCheckBox createToggle(String text, String settingKey, boolean isSelected) {
        JCheckBox box = new JCheckBox(text, isSelected);
        box.setBackground(UIConstants.WHITE_BG);
        box.setFont(new Font("Arial", Font.PLAIN, 14));

        // INTEGRATION: Save setting immediately when clicked
        box.addActionListener(e -> {
            // SettingsService.updatePrivacySetting(settingKey, box.isSelected());
            System.out.println("[Backend Call] Privacy Setting Updated: " + settingKey + " -> " + box.isSelected());
        });

        return box;
    }
}