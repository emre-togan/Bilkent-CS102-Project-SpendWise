package com.spendwise.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import com.spendwise.models.User;
import com.spendwise.services.ChatService;

public class AddFriendDialog extends JDialog {

    private JTextField searchField;
    private JPanel resultsPanel;

    public AddFriendDialog(Frame parent) {
        super(parent, "Add New Friend", true);
        setSize(400, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UIConstants.WHITE_BG);

        // Header
        JPanel header = new JPanel(new BorderLayout(10, 10));
        header.setBackground(UIConstants.WHITE_BG);
        header.setBorder(new EmptyBorder(20, 20, 10, 20));

        searchField = new JTextField();
        searchField.setToolTipText("Enter username...");
        
        JButton searchBtn = new JButton("Search");
        searchBtn.setBackground(UIConstants.PRIMARY_BLUE);
        searchBtn.setForeground(Color.WHITE);
        searchBtn.addActionListener(e -> performSearch());

        header.add(new JLabel("Find Users:"), BorderLayout.NORTH);
        header.add(searchField, BorderLayout.CENTER);
        header.add(searchBtn, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // Results List
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(UIConstants.WHITE_BG);

        JScrollPane scroll = new JScrollPane(resultsPanel);
        scroll.setBorder(new EmptyBorder(10, 20, 20, 20));
        add(scroll, BorderLayout.CENTER);
    }

    private void performSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) return;

        resultsPanel.removeAll();
        // Uses ChatService to search. Ensure ChatService has searchUsers method!
        // If not, simply replace ChatService.searchUsers(query) with a dummy list for now.
        List<User> users = ChatService.searchUsers(query);
        int currentUserId = UserSession.getCurrentUserId();

        if (users.isEmpty()) {
            JLabel lbl = new JLabel("No users found.");
            lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            resultsPanel.add(lbl);
        } else {
            for (User u : users) {
                if (u.getId() == currentUserId) continue;

                JPanel row = new JPanel(new BorderLayout());
                row.setBackground(Color.WHITE);
                row.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                    new EmptyBorder(10, 5, 10, 5)
                ));
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

                JLabel name = new JLabel(u.getUserName());
                name.setFont(new Font("Arial", Font.BOLD, 14));

                JButton addBtn = new JButton("Add");
                addBtn.setForeground(UIConstants.PRIMARY_GREEN);
                addBtn.addActionListener(e -> {
                    // Ensure ChatService has addFriend method
                    boolean success = ChatService.addFriend(currentUserId, u.getId());
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Friend added!");
                        addBtn.setEnabled(false);
                        addBtn.setText("Added");
                    }
                });

                row.add(name, BorderLayout.CENTER);
                row.add(addBtn, BorderLayout.EAST);
                resultsPanel.add(row);
            }
        }
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }
}
