package com.spendwise.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.spendwise.models.Address;
import com.spendwise.services.ProfileService;

// import Models.Address;
// import Services.ProfileService;

public class AddressesDialog extends JDialog {

    private JPanel mainContainer;
    private CardLayout cardLayout;
    private JPanel listContentPanel;

    private JTextField labelField, nameField, addressField, cityField, phoneField;

    public AddressesDialog(JFrame parent) {
        super(parent, "My Addresses", true);
        setSize(450, 600);
        setLocationRelativeTo(parent);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        mainContainer.add(createListView(), "LIST");
        mainContainer.add(createAddFormView(), "ADD_FORM");

        add(mainContainer);

        refreshAddressList();
    }

    private void refreshAddressList() {
        listContentPanel.removeAll();

        // Backend
        List<Address> addresses = ProfileService.getAddresses();

        if (addresses != null) {
            for (Address addr : addresses) {
                listContentPanel.add(createAddressItem(addr));
                listContentPanel.add(Box.createVerticalStrut(15));
            }
        }

        listContentPanel.revalidate();
        listContentPanel.repaint();
    }

    private JPanel createListView() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.WHITE_BG);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(UIConstants.WHITE_BG);

        RoundedButton addNewBtn = new RoundedButton("+ Add New Address", 15);
        addNewBtn.setBackground(UIConstants.PRIMARY_BLUE);
        addNewBtn.setForeground(Color.WHITE);
        addNewBtn.addActionListener(e -> {
            clearForm();
            cardLayout.show(mainContainer, "ADD_FORM");
        });

        topPanel.add(addNewBtn);
        panel.add(topPanel, BorderLayout.NORTH);

        listContentPanel = new JPanel();
        listContentPanel.setLayout(new BoxLayout(listContentPanel, BoxLayout.Y_AXIS));
        listContentPanel.setBackground(UIConstants.WHITE_BG);
        listContentPanel.setBorder(new EmptyBorder(0, 15, 0, 15));

        JScrollPane scrollPane = new JScrollPane(listContentPanel);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAddFormView() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.WHITE_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Add New Address");
        title.setFont(UIConstants.HEADING_FONT);

        JPanel formPanel = new JPanel(new GridLayout(6, 1, 0, 10));
        formPanel.setBackground(UIConstants.WHITE_BG);

        labelField = createInput("Label (e.g. Home, Work)");
        nameField = createInput("Full Name");
        addressField = createInput("Street Address");
        cityField = createInput("City, State, ZIP");
        phoneField = createInput("Phone Number");

        formPanel.add(labelField);
        formPanel.add(nameField);
        formPanel.add(addressField);
        formPanel.add(cityField);
        formPanel.add(phoneField);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(UIConstants.WHITE_BG);

        RoundedButton cancelBtn = new RoundedButton("Cancel", 15);
        cancelBtn.setBackground(Color.WHITE);
        cancelBtn.addActionListener(e -> cardLayout.show(mainContainer, "LIST"));

        RoundedButton saveBtn = new RoundedButton("Save Address", 15);
        saveBtn.setBackground(UIConstants.PRIMARY_GREEN);
        saveBtn.setForeground(Color.WHITE);

        saveBtn.addActionListener(e -> {
            // This part will be rewrited
            Address newAddr = new Address(
                    labelField.getText(), // label
                    nameField.getText(), // fullName
                    addressField.getText(), // street
                    cityField.getText(), // city
                    "", // state (Empty because UI doesn't have it)
                    "", // zip (Empty because UI doesn't have it)
                    phoneField.getText(), // phone
                    false // isDefault
            );

            // Backend
            ProfileService.addAddress(newAddr);

            refreshAddressList();
            cardLayout.show(mainContainer, "LIST");
        });

        btnPanel.add(cancelBtn);
        btnPanel.add(saveBtn);

        panel.add(title, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createAddressItem(Address addr) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(10, 10, 5, 10));

        JLabel iconLabel = new JLabel("🏠 " + addr.getLabel());
        iconLabel.setFont(new Font("Arial", Font.BOLD, 14));
        header.add(iconLabel, BorderLayout.WEST);

        if (addr.isDefault()) {
            JLabel badge = new JLabel("Default");
            badge.setOpaque(true);
            badge.setBackground(UIConstants.PRIMARY_BLUE);
            badge.setForeground(Color.WHITE);
            badge.setBorder(new EmptyBorder(2, 5, 2, 5));
            header.add(badge, BorderLayout.EAST);
        }

        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
        infoPanel.add(new JLabel(addr.getFullName()));
        infoPanel.add(new JLabel(addr.getFullAddress()));
        infoPanel.add(new JLabel(addr.getCityStateZip()));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.setOpaque(false);

        RoundedButton delete = new RoundedButton("Delete", 12);
        delete.setForeground(Color.RED);
        delete.setBackground(Color.WHITE);
        delete.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Delete this address?", "Confirm",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Backend
                ProfileService.deleteAddress(addr.getId());
                refreshAddressList();
            }
        });

        actions.add(delete);

        if (!addr.isDefault()) {
            RoundedButton setDef = new RoundedButton("Set as Default", 12);
            setDef.setBackground(UIConstants.PRIMARY_BLUE);
            setDef.setForeground(Color.WHITE);
            setDef.addActionListener(e -> {
                // Backend
                ProfileService.setDefaultAddress(addr.getId());
                refreshAddressList();
            });
            actions.add(setDef);
        }

        card.add(header, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(actions, BorderLayout.SOUTH);
        return card;
    }

    private JTextField createInput(String placeholder) {
        JTextField field = new JTextField();
        field.setBorder(BorderFactory.createTitledBorder(placeholder));
        return field;
    }

    private void clearForm() {
        labelField.setText("");
        nameField.setText("");
        addressField.setText("");
        cityField.setText("");
        phoneField.setText("");
    }
}