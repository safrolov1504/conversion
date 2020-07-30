package ru.interview.utils;

public class StaticFaction {
    public static double round(Double value, int places) {
        Double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    public static boolean isDigit(String s) throws NumberFormatException {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
