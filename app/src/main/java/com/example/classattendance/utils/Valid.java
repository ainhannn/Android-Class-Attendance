package com.example.classattendance.utils;

public class Valid {
    public static boolean isCode(String str) {
        if (str.isEmpty()) return false;
        if (str.length() != 6) return false;

        return true;
    }

    public static boolean isInteger(String str) {
        if (str.isEmpty()) return false;

        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
