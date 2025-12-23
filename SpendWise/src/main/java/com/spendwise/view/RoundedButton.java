package com.spendwise.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;

public class RoundedButton extends JButton {
    
    private int cornerRadius = 15; // Default rounded corner radius
    private Color hoverBackgroundColor;
    private Color pressedBackgroundColor;
    
    public RoundedButton() {
        super();
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
    }

    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
    }

    public RoundedButton(String text, int radius) {
        super(text);
        this.cornerRadius = radius;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Determine background color based on button state
        Color bgColor = getBackground();
        
        if (getModel().isPressed()) {
            bgColor = pressedBackgroundColor != null ? pressedBackgroundColor : darkerColor(bgColor);
        } else if (getModel().isRollover()) {
            bgColor = hoverBackgroundColor != null ? hoverBackgroundColor : lighterColor(bgColor);
        }

        // Draw rounded rectangle background
        g2.setColor(bgColor);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

        // Draw border if needed
        if (getBorder() != null && getBorder() instanceof javax.swing.border.LineBorder) {
            javax.swing.border.LineBorder lineBorder = (javax.swing.border.LineBorder) getBorder();
            g2.setColor(lineBorder.getLineColor());
            g2.setStroke(new BasicStroke(lineBorder.getThickness()));
            g2.draw(new RoundRectangle2D.Float(
                lineBorder.getThickness() / 2f, 
                lineBorder.getThickness() / 2f, 
                getWidth() - lineBorder.getThickness(), 
                getHeight() - lineBorder.getThickness(), 
                cornerRadius, 
                cornerRadius
            ));
        }

        g2.dispose();

        // Paint text and icon
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Border is painted in paintComponent
    }

    @Override
    public boolean contains(int x, int y) {
        // Make the button only clickable within the rounded rectangle
        Shape shape = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        return shape.contains(x, y);
    }

    private Color lighterColor(Color color) {
        int r = Math.min(255, color.getRed() + 20);
        int g = Math.min(255, color.getGreen() + 20);
        int b = Math.min(255, color.getBlue() + 20);
        return new Color(r, g, b);
    }

    private Color darkerColor(Color color) {
        int r = Math.max(0, color.getRed() - 20);
        int g = Math.max(0, color.getGreen() - 20);
        int b = Math.max(0, color.getBlue() - 20);
        return new Color(r, g, b);
    }

    public void setHoverBackgroundColor(Color color) {
        this.hoverBackgroundColor = color;
    }

    public void setPressedBackgroundColor(Color color) {
        this.pressedBackgroundColor = color;
    }
}

