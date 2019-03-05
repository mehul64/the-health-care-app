package com.thc.app.utils;


import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.toolbox.StringRequest;
import com.thc.app.models.OneDayData;
import com.thc.app.receiver.OnBootReceiver;

public class DataPreference {

    private static final String PREVIOUS_TIME = "prev_timestamp";
    private static final String VALUE_RUNNING = "value_running";
    private static final String VALUE_OTHERS = "value_others";
    private static final String VALUE_WORKING = "value_working";
    private static final String VALUE_ACTIVATION = "value_activation";

    private static final String BREAKFAST = "breakfast";
    private static final String BREAKFAST_TIME = "breakfast_time";
    private static final String BREAKFAST_CARBS = "breakfast_carbs";

    private static final String LUNCH = "lunch";
    private static final String LUNCH_TIME = "lunch_time";
    private static final String LUNCH_CARBS = "lunch_carbs";

    private static final String DINNER = "dinner";
    private static final String DINNER_TIME = "dinner_time";
    private static final String DINNER_CARBS = "dinner_carbs";

    private static final String MIDNIGHT_RECEIVER = "midnight_receiver";
    private static final String LUNCH_RECEIVER = "lunch_receiver";
    private static final String DINNER_RECEIVER = "dinner_receiver";
    private static final String BREAKFAST_RECEIVER = "breakfast_receiver";
    private static final String RECEIVER_INIT = "receiver_init";
    private static final String VALUE_IN_KILO = "show_in_kilometers";
    private static final String JOURNEY_ID = "journey_id";

    private static final String KM_GOAL = "kms goal";
    private static final String STEPS_GOAL = "steps goal";
    private static final String CAL_GOAL = "cal goal";

    private final String APP_PREF = "value_thc";
    private final SharedPreferences sharedPreferences;

    private Context context;

    public DataPreference(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
    }


    public void setReceiverInit(boolean value) {
        sharedPreferences.edit().putBoolean(RECEIVER_INIT, value).apply();
    }

    public boolean isReceiverInit() {
        return sharedPreferences.getBoolean(RECEIVER_INIT, false);
    }


    public void setTrackingON(boolean value) {
        sharedPreferences.edit().putBoolean(VALUE_ACTIVATION, value).apply();
    }

    public boolean showInKiloMeter() {
        return sharedPreferences.getBoolean(VALUE_IN_KILO, false);
    }

    public void setShowInKiloMeter(boolean value) {
        sharedPreferences.edit().putBoolean(VALUE_IN_KILO, value).apply();
    }

    public boolean isTrackingON() {
        return sharedPreferences.getBoolean(VALUE_ACTIVATION, true);
    }


    public void setPrevComputedTimeStamp(long value) {
        sharedPreferences.edit().putLong(PREVIOUS_TIME, value).apply();
    }

    public long getPrevComputedTimeStamp() {
        return sharedPreferences.getLong(PREVIOUS_TIME, System.currentTimeMillis());
    }

//    RUNNING

    public void updateTodaysRunningValue(long value) {
        sharedPreferences.edit().putLong(VALUE_RUNNING, getTodaysRunningValue() + value).apply();
    }

    public void resetRunningValue() {
        sharedPreferences.edit().putLong(VALUE_RUNNING, 0).apply();
    }

    public long getTodaysRunningValue() {
        return sharedPreferences.getLong(VALUE_RUNNING, 0);
    }

//    WALKING

    public long getTodaysWalkingValue() {
        return sharedPreferences.getLong(VALUE_WORKING, 0);
    }


    public void updateTodaysWalkingValue(long value) {
        sharedPreferences.edit().putLong(VALUE_WORKING, getTodaysWalkingValue() + value).apply();
    }

    public void resetWalkingValue() {
        sharedPreferences.edit().putLong(VALUE_WORKING, 0).apply();
    }


//    OTHERS

    public long getTodaysOtherValue() {
        return sharedPreferences.getLong(VALUE_OTHERS, 0);
    }


    public void updateTodaysOtherValue(long value) {
        sharedPreferences.edit().putLong(VALUE_OTHERS, getTodaysOtherValue() + value).apply();
    }

    public void resetOthersValue() {
        sharedPreferences.edit().putLong(VALUE_OTHERS, 0).apply();
    }

    public String getJourneyId() {
        return sharedPreferences.getString(JOURNEY_ID, null);
    }

    public void setJourneyId(String value) {
        sharedPreferences.edit().putString(JOURNEY_ID, value).apply();
    }


//    BREAKFAST

    public String getBreakFast() {
        return sharedPreferences.getString(BREAKFAST, "");
    }

    public void setBreakFast(String value) {
        sharedPreferences.edit().putString(BREAKFAST, value).apply();
    }

    public long getBreakFastCarbs() {
        return sharedPreferences.getLong(BREAKFAST_CARBS, 0);
    }

    public void setBreakFastCarbs(long value) {
        sharedPreferences.edit().putLong(BREAKFAST_CARBS, value).apply();
    }


    public String getBreakfastTime() {
        return sharedPreferences.getString(BREAKFAST_TIME,"9");
    }

    public String getLunchTime() {
        return sharedPreferences.getString(LUNCH_TIME, "14");
    }

    public String getDinnerTime() {
        return sharedPreferences.getString(DINNER_TIME, "21");
    }

    public void setBreakfastTime(String time)
    {
        sharedPreferences.edit().putString(BREAKFAST_TIME, time).apply();
    }

    public void setLunchTime(String time)
    {
        sharedPreferences.edit().putString(LUNCH_TIME, time).apply();
    }

    public void setDinnerTime(String time)
    {
        sharedPreferences.edit().putString(DINNER_TIME, time).apply();
    }


    //    LUNCH

    public String getLunch() {
        return sharedPreferences.getString(LUNCH, "");
    }

    public void setLunch(String value) {
        sharedPreferences.edit().putString(LUNCH, value).apply();
    }

    public long getLunchCarbs() {
        return sharedPreferences.getLong(LUNCH_CARBS, 0);
    }

    public void setLunchCarbs(long value) {
        sharedPreferences.edit().putLong(LUNCH_CARBS, value).apply();
    }


//    DINNER

    public String getDinner() {
        return sharedPreferences.getString(DINNER, "");
    }

    public void setDinner(String value) {
        sharedPreferences.edit().putString(DINNER, value).apply();
    }

    public long getDinnerCarbs() {
        return sharedPreferences.getLong(DINNER_CARBS, 0);
    }

    public void setDinnerCarbs(long value) {
        sharedPreferences.edit().putLong(DINNER_CARBS, value).apply();
    }


    public long getLastProcessedMidnightTimeStamp() {
        return sharedPreferences.getLong(MIDNIGHT_RECEIVER, 0);
    }

    public void setLastProcessedMidnightTimeStamp(long value) {
        sharedPreferences.edit().putLong(MIDNIGHT_RECEIVER, value).apply();
    }


    public long getLastProcessedBreakFastTimeStamp() {
        return sharedPreferences.getLong(BREAKFAST_RECEIVER, 0);
    }

    public void setLastProcessedBreakFastTimeStamp(long value) {
        sharedPreferences.edit().putLong(BREAKFAST_RECEIVER, value).apply();
    }


    public long getLastProcessedDinnerTimeStamp() {
        return sharedPreferences.getLong(DINNER_RECEIVER, 0);
    }

    public void setLastProcessedDinnerTimeStamp(long value) {
        sharedPreferences.edit().putLong(DINNER_RECEIVER, value).apply();
    }


    public long getLastProcessedLunchTimeStamp() {
        return sharedPreferences.getLong(LUNCH_RECEIVER, 0);
    }

    public void setLastProcessedLunchTimeStamp(long value) {
        sharedPreferences.edit().putLong(LUNCH_RECEIVER, value).apply();
    }

    public void setDailyKilometersGoal(long value)
    {
        sharedPreferences.edit().putLong(KM_GOAL, value).apply();
    }

    public void setDailyCaloriesGoal(long value)
    {
        sharedPreferences.edit().putLong(CAL_GOAL, value).apply();
    }

    public void setDailyStepsGoal(long value)
    {
        sharedPreferences.edit().putLong(STEPS_GOAL, value).apply();
    }

    public long getKilometersGoal()
    {
        return sharedPreferences.getLong(KM_GOAL,0);
    }

    public long getStepsGoal()
    {
        return sharedPreferences.getLong(STEPS_GOAL,0);
    }

    public long getCaloriesGoal()
    {
        return sharedPreferences.getLong(CAL_GOAL,0);
    }


    public void resetAllValues() {
        setDinner(null);
        setDinnerCarbs(0);

        setLunch(null);
        setLunchCarbs(0);

        setBreakFastCarbs(0);
        setBreakFast(null);

        resetOthersValue();
        resetRunningValue();
        resetWalkingValue();

    }

    public OneDayData getDataObject(String value) {

        OneDayData oneDayData = new OneDayData();
        oneDayData.setBreakFast(getBreakFast());
        oneDayData.setBreakFastCal(getBreakFastCarbs());
        oneDayData.setDinner(getDinner());
        oneDayData.setDinnerCal(getDinnerCarbs());
        oneDayData.setLunch(getLunch());
        oneDayData.setLunchCal(getLunchCarbs());
        oneDayData.setWalking(getTodaysWalkingValue());
        oneDayData.setRunning(getTodaysRunningValue());
        oneDayData.setOther(getTodaysOtherValue());
        oneDayData.setDateString(value);
        return oneDayData;
    }

}
