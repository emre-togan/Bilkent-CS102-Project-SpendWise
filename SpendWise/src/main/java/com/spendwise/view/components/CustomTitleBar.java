package com.spendwise.view.components;

import com.spendwise.view.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomTitleBar extends JPanel {

    private JLabel titleLabel;
    private int pX, pY;

    public CustomTitleBar(JFrame frame) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(frame.getWidth(), 35));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240))); // Light gray bottom border

        // Dragging functionality
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                pX = me.getX();
                pY = me.getY();
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent me) {
                frame.setLocation(frame.getLocation().x + me.getX() - pX,
                        frame.getLocation().y + me.getY() - pY);
            }
        });

        // Left side: Icon + Title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        leftPanel.setOpaque(false);

        // Small Logo (using text fallback if image missing, similar to others for
        // safety)
        JLabel iconLabel = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/Resim1.png"));
            Image image = logoIcon.getImage();
            Image newimg = image.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(newimg));
        } catch (Exception e) {
            iconLabel.setText("W₺");
            iconLabel.setFont(new Font("Arial", Font.BOLD, 14));
            iconLabel.setForeground(UIConstants.PRIMARY_GREEN);
        }
        leftPanel.add(iconLabel);

        titleLabel = new JLabel(frame.getTitle());
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(Color.DARK_GRAY);
        leftPanel.add(titleLabel);

        add(leftPanel, BorderLayout.WEST);

        // Right side: Controls
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);

        JButton minimizeBtn = createControlButton("-");
        minimizeBtn.addActionListener(e -> frame.setExtendedState(JFrame.ICONIFIED));

        JButton closeBtn = createControlButton("X");
        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeBtn.setBackground(new Color(232, 17, 35)); // Red
                closeBtn.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeBtn.setBackground(Color.WHITE);
                closeBtn.setForeground(Color.BLACK);
            }
        });
        closeBtn.addActionListener(e -> System.exit(0));

        rightPanel.add(minimizeBtn);
        rightPanel.add(closeBtn);

        add(rightPanel, BorderLayout.EAST);
    }

    private JButton createControlButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(45, 35));
        btn.setFont(new Font("Arial", Font.PLAIN, 12));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        return btn;
    }

    public void updateTitle(String newTitle) {
        titleLabel.setText(newTitle);
    }
}
