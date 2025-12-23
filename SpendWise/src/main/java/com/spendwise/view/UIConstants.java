package com.spendwise.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import javax.swing.ImageIcon;

public class UIConstants {

    public static final Color PRIMARY_GREEN = new Color(76, 175, 80);
    public static final Color PRIMARY_BLUE = new Color(33, 150, 243);
    public static final Color PRIMARY_TEAL = new Color(0, 150, 136);

    public static final Color SUCCESS_GREEN = new Color(76, 175, 80);
    public static final Color DANGER_RED = new Color(220, 53, 69);
    public static final Color WARNING_ORANGE = new Color(255, 152, 0);
    public static final Color INFO_BLUE = new Color(33, 150, 243);

    public static final Color WHITE_BG = Color.WHITE;
    public static final Color BACKGROUND_LIGHT = new Color(250, 250, 250);
    public static final Color BACKGROUND_DARK = new Color(245, 245, 245);

    public static final Color TEXT_COLOR = new Color(50, 50, 50);
    public static final Color TEXT_DARK = new Color(33, 33, 33);
    public static final Color TEXT_LIGHT = new Color(255, 255, 255);
    public static final Color GRAY_TEXT = new Color(120, 120, 120);

    public static final Color BORDER_COLOR = new Color(230, 230, 230);
    public static final Color BORDER_LIGHT = new Color(240, 240, 240);

    public static final Font HEADING_LARGE = new Font("Arial", Font.BOLD, 28);
    public static final Font HEADING_FONT = new Font("Arial", Font.BOLD, 24);
    public static final Font HEADING_MEDIUM = new Font("Arial", Font.BOLD, 20);
    public static final Font HEADING_SMALL = new Font("Arial", Font.BOLD, 16);

    public static final Font BODY_LARGE = new Font("Arial", Font.PLAIN, 16);
    public static final Font SUBTITLE_FONT = new Font("Arial", Font.PLAIN, 14);
    public static final Font BODY_FONT = new Font("Arial", Font.PLAIN, 12);
    public static final Font BODY_MEDIUM = new Font("Arial", Font.PLAIN, 14);
    public static final Font BODY_SMALL = new Font("Arial", Font.PLAIN, 12);

    public static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);
    public static final Font CAPTION_FONT = new Font("Arial", Font.PLAIN, 11);

    public static final int WINDOW_WIDTH = 1500;
    public static final int WINDOW_HEIGHT = 900;

    public static final int SIDE_MENU_WIDTH = 260;
    public static final int CONTENT_WIDTH = WINDOW_WIDTH - SIDE_MENU_WIDTH;

    public static final int BUTTON_HEIGHT = 40;
    public static final int INPUT_HEIGHT = 35;

    public static final int CARD_RADIUS = 12;
    public static final int PRODUCT_CARD_HEIGHT = 200;
    public static final int PRODUCT_CARD_WIDTH = 200;

    public static final int PADDING_XSMALL = 4;
    public static final int PADDING_SMALL = 8;
    public static final int PADDING_MEDIUM = 12;
    public static final int PADDING_LARGE = 16;
    public static final int PADDING_XLARGE = 20;

    public static final int MARGIN_SMALL = 4;
    public static final int MARGIN_MEDIUM = 8;
    public static final int MARGIN_LARGE = 12;

    public static final String ICON_DASHBOARD = "/icons/dashboard.png";
    public static final String ICON_BUDGET = "/icons/budget.png";
    public static final String ICON_EXPENSES = "/icons/expenses.png";
    public static final String ICON_SHOP = "/icons/shop.png";
    public static final String ICON_CHAT = "/icons/chat.png";
    public static final String ICON_PROFILE = "/icons/profile.png";
    public static final String ICON_SETTINGS = "/icons/settings.png";
    public static final String ICON_LOGOUT = "/icons/logout.png";

    public static ImageIcon loadIcon(String path, int width, int height) {
        try {
            java.net.URL imgURL = UIConstants.class.getResource(path);
            if (imgURL != null) {
                ImageIcon originalIcon = new ImageIcon(imgURL);
                Image scaledImage = originalIcon.getImage().getScaledInstance(
                        width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }
        } catch (Exception e) {
            System.err.println("Could not load icon: " + path);
            e.printStackTrace();
        }
        return null;
    }

    public static Color getStatusColor(String status) {
        if (status == null)
            return Color.GRAY;

        switch (status.toUpperCase()) {
            case "DELIVERED":
            case "COMPLETED":
            case "PAID":
                return SUCCESS_GREEN;

            case "SHIPPED":
            case "IN_TRANSIT":
                return PRIMARY_BLUE;

            case "PROCESSING":
            case "PENDING":
                return WARNING_ORANGE;

            case "CANCELLED":
            case "FAILED":
                return DANGER_RED;

            default:
                return Color.GRAY;
        }
    }

    public static Color lighter(Color color) {
        int r = Math.min(255, color.getRed() + 20);
        int g = Math.min(255, color.getGreen() + 20);
        int b = Math.min(255, color.getBlue() + 20);
        return new Color(r, g, b);
    }

    public static Color darker(Color color) {
        int r = Math.max(0, color.getRed() - 20);
        int g = Math.max(0, color.getGreen() - 20);
        int b = Math.max(0, color.getBlue() - 20);
        return new Color(r, g, b);
    }
}