package com.spendwise.models;

import java.sql.Date;
import java.util.Random;

public class Friend {

    private int userId;
    private String name;
    private String initials;
    private Date friendSince;
    private String avatarColor; 

    public Friend(int userId, String name, Date friendSince){
        this.userId = userId;
        this.name = name;
        this.friendSince = friendSince;
        this.initials = generateInitials(name);
        this.avatarColor = generateRandomColor();
    }

    public Friend(int userId, String name, String initials, Date friendSince, String avatarColor) {
        this.userId = userId;
        this.name = name;
        this.initials = initials;
        this.friendSince = friendSince;
        this.avatarColor = avatarColor;
    }

    public String generateInitials(String fullName) {
        if (fullName == null)
            return "??";
        String trimmed = fullName.trim();
        if (trimmed.isEmpty())
            return "??";

        String[] parts = trimmed.split("\\s+");
        if (parts.length == 1) {
            String p = parts[0];
            return (p.length() >= 2) ? ("" + Character.toUpperCase(p.charAt(0)) + Character.toUpperCase(p.charAt(1)))
                    : ("" + Character.toUpperCase(p.charAt(0)) + "?");
        }

        char first = Character.toUpperCase(parts[0].charAt(0));
        char last = Character.toUpperCase(parts[parts.length - 1].charAt(0));
        return "" + first + last;
    }

    public String generateRandomColor() {
        Random r = new Random();
        int red = 50 + r.nextInt(156);
        int green = 50 + r.nextInt(156);
        int blue = 50 + r.nextInt(156);
        return String.format("#%02X%02X%02X", red, green, blue);
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getInitials() {
        return initials;
    }

    public Date getFriendSince() {
        return friendSince;
    }

    public String getAvatarColor() {
        return avatarColor;
    }
}
