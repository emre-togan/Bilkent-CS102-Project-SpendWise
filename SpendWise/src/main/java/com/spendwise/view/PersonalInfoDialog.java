package com.spendwise.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

// import Services.SettingsService; 
// import Models.User;              

public class PersonalInfoDialog extends JDialog {

    private JTextField nameField, emailField, phoneField;

    public PersonalInfoDialog(JFrame parent) {
        super(parent, "Personal Information", true);
        setSize(400, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UIConstants.WHITE_BG);

        JPanel content = new JPanel(new GridLayout(6, 1, 0, 15));
        content.setBackground(UIConstants.WHITE_BG);
        content.setBorder(new EmptyBorder(30, 30, 30, 30));

        // INTEGRATION: Fetch current user data from Backend
        // User currentUser = SettingsService.getCurrentUser();
        // String currentName = currentUser.getName();
        // String currentEmail = currentUser.getEmail();
        // String currentPhone = currentUser.getPhone();

        // Using placeholders until backend is connected
        nameField = createInput("Full Name", ""); // Should be currentName
        emailField = createInput("Email Address", ""); // Should be currentEmail
        phoneField = createInput("Phone Number", ""); // Should be currentPhone

        content.add(new JLabel("Update your personal details:"));
        content.add(nameField);
        content.add(emailField);
        content.add(phoneField);

        add(content, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(UIConstants.WHITE_BG);

        RoundedButton saveBtn = new RoundedButton("Save Changes", 15);
        saveBtn.setBackground(UIConstants.PRIMARY_BLUE);
        saveBtn.setForeground(Color.WHITE);

        saveBtn.addActionListener(e -> {

            // INTEGRATION: Send update request to Backend
            // SettingsService.updatePersonalInfo(newName, newEmail, newPhone);

            JOptionPane.showMessageDialog(this, "Profile Updated Successfully!");
            dispose();
        });

        btnPanel.add(saveBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private JTextField createInput(String title, String value) {
        JTextField field = new JTextField(value);
        field.setBorder(BorderFactory.createTitledBorder(title));
        return field;
    }
}