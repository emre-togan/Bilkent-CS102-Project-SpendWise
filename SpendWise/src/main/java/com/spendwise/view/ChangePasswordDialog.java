package com.spendwise.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

// import Services.SettingsService; // Waiting for Backend

public class ChangePasswordDialog extends JDialog {

    private JPasswordField currentPass, newPass, confirmPass;

    public ChangePasswordDialog(JFrame parent) {
        super(parent, "Change Password", true);
        setSize(400, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UIConstants.WHITE_BG);

        JPanel content = new JPanel(new GridLayout(6, 1, 0, 15));
        content.setBackground(UIConstants.WHITE_BG);
        content.setBorder(new EmptyBorder(30, 30, 30, 30));

        currentPass = createPassField("Current Password");
        newPass = createPassField("New Password");
        confirmPass = createPassField("Confirm New Password");

        content.add(new JLabel("Secure your account:"));
        content.add(currentPass);
        content.add(newPass);
        content.add(confirmPass);

        add(content, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(UIConstants.WHITE_BG);

        JButton updateBtn = new JButton("Update Password");
        updateBtn.setBackground(UIConstants.PRIMARY_GREEN);
        updateBtn.setForeground(Color.WHITE);

        updateBtn.addActionListener(e -> {
            String p1 = new String(newPass.getPassword());
            String p2 = new String(confirmPass.getPassword());

            if (p1.isEmpty() || !p1.equals(p2)) {
                JOptionPane.showMessageDialog(this, "New passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // INTEGRATION: Call backend to change password
            // boolean success = SettingsService.changePassword(curr, p1);

            // Simulation logic (Delete this when backend is ready)
            boolean success = true;

            if (success) {
                JOptionPane.showMessageDialog(this, "Password Changed Successfully!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect current password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnPanel.add(updateBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private JPasswordField createPassField(String title) {
        JPasswordField field = new JPasswordField();
        field.setBorder(BorderFactory.createTitledBorder(title));
        return field;
    }
}