package com.spendwise.view.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import com.spendwise.view.UIConstants;

public class RoundedPasswordField extends JPasswordField implements FocusListener {
    private int radius;
    private String placeholder;
    private boolean showingPlaceholder;

    public RoundedPasswordField(int radius, String placeholder) {
        this.radius = radius;
        this.placeholder = placeholder;
        setOpaque(false);
        setBorder(new EmptyBorder(10, 15, 10, 15)); // Inner padding
        addFocusListener(this);
        showingPlaceholder = true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Paint background
        g2.setColor(UIConstants.INPUT_BG_COLOR);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        super.paintComponent(g);

        if ((getPassword().length == 0) && !hasFocus()) {
            g2.setColor(UIConstants.GRAY_TEXT);
            g2.setFont(getFont());
            // Adjust y position to center vertically
            FontMetrics fm = g2.getFontMetrics();
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(placeholder, getInsets().left, y);
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        showingPlaceholder = false;
        repaint();
    }

    @Override
    public void focusLost(FocusEvent e) {
        showingPlaceholder = getPassword().length == 0;
        repaint();
    }
}
