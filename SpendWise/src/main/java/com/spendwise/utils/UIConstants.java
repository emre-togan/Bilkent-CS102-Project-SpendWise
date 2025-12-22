package com.spendwise.utils;

import java.awt.Color;

import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;

public class UIConstants {

    public static final Color PRIMARY_GREEN = new Color(76, 175, 80);
    public static final Color PRIMARY_BLUE = new Color(33, 150, 243);
    public static final Color PRIMARY_TEAL = new Color(76, 175, 80);

    public static final Color SUCCESS_GREEN = new Color(76, 175, 80);
    public static final Color DANGER_RED = new Color(76, 175, 80);
    public static final Color WARNING_ORANGE = new Color(76, 175, 80);
    public static final Color INFO_BLUE = new Color(76, 175, 80);

    public static final Color BACKGROUND_LIGHT = new Color(76, 175, 80);
    public static final Color BACKGROUND_DARK = new Color(76, 175, 80);
    public static final Color TEXT_DARK = new Color(76, 175, 80);
    public static final Color TEXT_LIGHT = new Color(76, 175, 80);
    public static final Color BORDER_COLOR = new Color(76, 175, 80);

    public static final Font HEADING_LARGE = new Font("Arial", Font.BOLD, 24);
    public static final Font HEADING_MEDIUM = new Font("Arial", Font.BOLD, 20);
    public static final Font HEADING_SMALL = new Font("Arial", Font.BOLD, 16);
    public static final Font BODY_LARGE = new Font("Arial", Font.PLAIN, 16);
    public static final Font BODY_MEDIUM = new Font("Arial", Font.PLAIN, 14);
    public static final Font BODY_SMALL = new Font("Arial", Font.PLAIN, 12);
    public static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);
    public static final Font CAPTION_FONT = new Font("Arial", Font.PLAIN, 12);

    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;

    public static final int SIDE_MENU_WIDTH = 250;
    public static final int CONTENT_WIDTH = 1030;

    public static final int BUTTON_HEIGHT = 40;
    public static final int INPUT_HEIGHT = 40;
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

    public static final String ICON_DASHBOARD = "dashboard.png";
    public static final String ICON_BUDGET = "budget.png";
    public static final String ICON_EXOENSES = "expenses.png";
    public static final String ICON_SHOP = "shop.png";
    public static final String ICON_CHAT = "chat.png";
    public static final String ICON_PROFILES = "profiles.png";
    public static final String ICON_SETTINGS = "settings.png";
    public static final String ICON_LOGOUT = "logout.png";

    public static ImageIcon loadIcon(String path, int width, int height) {
        try {
            java.net.URL imgURL = UIConstants.class.getResource(path);
            if (imgURL != null) {
                ImageIcon originalIcon = new ImageIcon(imgURL);
                Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }
        } catch (Exception e)

        {
            System.err.println("Could not load icon: " + path);
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
}