package com.thc.app.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class AppSharedPreference {

    private static final String BALANCE = "balance";
    private static final String EMAIL_ID = "email";
    private static final String MOBILE_NO = "mobile_no";
    private static final String BASE_URL_IP = "base_url_ip";
    private static final String USER_ID = "user_id";
    private static final String TRACKING_ON = "tracking_on";
    private static final String WEIGHT = "weight";
    private static final String GENDER_VALUE = "gender";
    private static final String HEIGHT_VALUE = "height";
    private static final String AGE_VALUE = "age";
    private final String APP_PREF = "Contract-O-Clean";
    private final SharedPreferences sharedPreferences;

    private Context context;
    private String IS_LOGGED_IN = "is_logged_in";
    private String FULL_NAME = "full_name";

    public AppSharedPreference(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
    }

    public void setLoggedIn(boolean value) {
        sharedPreferences.edit().putBoolean(IS_LOGGED_IN, value).apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }


    public void setFullName(String value) {
        sharedPreferences.edit().putString(FULL_NAME, value).apply();
    }

    public String getFullName() {
        return sharedPreferences.getString(FULL_NAME, null);
    }

    public void setEmail(String value) {
        sharedPreferences.edit().putString(EMAIL_ID, value).apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(EMAIL_ID, null);
    }

    public void setMobile(String value) {
        sharedPreferences.edit().putString(MOBILE_NO, value).apply();
    }

    public String getMobile() {
        return sharedPreferences.getString(MOBILE_NO, null);
    }


    public String getWeight() {
        return sharedPreferences.getString(WEIGHT, "67");
    }

    public void setWeight(String value) {
        sharedPreferences.edit().putString(WEIGHT, value).apply();
    }

    public String getAge() {
        return sharedPreferences.getString(AGE_VALUE, "67");
    }

    public void setAge(String value) {
        sharedPreferences.edit().putString(AGE_VALUE, value).apply();
    }

    public String getHeight() {
        return sharedPreferences.getString(HEIGHT_VALUE, "67");
    }

    public void setHeight(String value) {
        sharedPreferences.edit().putString(HEIGHT_VALUE, value).apply();
    }

    public String getGender() {
        return sharedPreferences.getString(GENDER_VALUE, "67");
    }

    public void setGender(String value) {
        sharedPreferences.edit().putString(GENDER_VALUE, value).apply();
    }


    public void setBalance(float value) {
        sharedPreferences.edit().putFloat(BALANCE, value).apply();
    }

    public float getBalance() {
        return sharedPreferences.getFloat(BALANCE, -1);
    }


    public void setBaseUrlIP(String value) {
        sharedPreferences.edit().putString(BASE_URL_IP, value).apply();
    }

    public String getBaseUrlIP() {
//        return "192.168.1.4";
        return sharedPreferences.getString(BASE_URL_IP, null);
    }


    public void setUserID(String value) {
        sharedPreferences.edit().putString(USER_ID, value).apply();
    }

    public String getUserID() {
        return sharedPreferences.getString(USER_ID, null);
    }


    public void setTrackingON(boolean value) {
        sharedPreferences.edit().putBoolean(TRACKING_ON, value).apply();
    }

    public boolean isTrackingON() {
        return sharedPreferences.getBoolean(TRACKING_ON, true);
    }


}
