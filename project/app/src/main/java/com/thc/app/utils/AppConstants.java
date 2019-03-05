package com.thc.app.utils;


import java.text.DecimalFormat;

public class AppConstants {

    public static String getOtherActivityTime(long todaysOtherValue) {
        double value = todaysOtherValue * 0.000277777778;
        DecimalFormat df = new DecimalFormat("####0.00");
        return df.format(value);
    }

    public static String getOtherActivityTime(float todaysOtherValue) {
        double value = todaysOtherValue * 0.000277777778;
        DecimalFormat df = new DecimalFormat("####0.00");
        return df.format(value);
    }

    public static String getRunningValue(boolean showInKilo, float todaysRunningValue) {
        double value;
        if (showInKilo) {
            value = todaysRunningValue * 0.001;
        } else {
            value = todaysRunningValue * 0.00062137;
        }
        DecimalFormat df = new DecimalFormat("####0.00");
        return df.format(value);
    }

    public static String getWalking(long todaysRunningValue) {
        long value = todaysRunningValue;
        return String.valueOf(value);
    }

    public static String getWalking(float todaysRunningValue) {
        float value = todaysRunningValue;
        return String.valueOf(value);
    }

    public static String getCalarieBurn(float valueCalarie) {
        DecimalFormat df = new DecimalFormat("####0.00");
        return df.format(valueCalarie);
    }

    public static String getCalarieBurn(double valueCalarie) {
        DecimalFormat df = new DecimalFormat("####0.00");
        return df.format(valueCalarie);
    }
}
