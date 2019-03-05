package com.thc.app.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.thc.app.utils.DataPreference;

import java.util.Calendar;

import timber.log.Timber;

public class OnBootReceiver extends BroadcastReceiver {
    public OnBootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.i("onReceive");
        try {
            Timber.d("onReceive");
            //reset all alarms
            setOrResetAllReceivers(context);

            //check if the Midnight receiver was evoked or not
            if (!verifyIfMidnightReceiverWasInvoked(context)) {
                MidnightProcessReceiver.sendDataToServerAndClearData(context);
            }

            if (!verifyIfDinnerReceiverWasInvoked(context)) {
                DinnerReceiver.processIt(context);
            }

            if (!verifyIfLunchReceiverWasInvoked(context)) {
                LunchReceiver.processIt(context);
            }

            if (!verifyIfBreakFastReceiverWasInvoked(context)) {
                BreakFastReceiver.processIt(context);
            }

        } catch (Exception ex) {
            Timber.e(Log.getStackTraceString(ex));
        }
    }

    public static boolean verifyIfMidnightReceiverWasInvoked(Context context) {
        DataPreference sharedPreference = new DataPreference(context);
        //check if the midnight receiver was evoked or not
        long midnightTimeStamp = sharedPreference.getLastProcessedMidnightTimeStamp();
        Calendar todayDate = Calendar.getInstance();
        todayDate.set(Calendar.HOUR_OF_DAY, 23);
        todayDate.set(Calendar.MINUTE, 59);
        todayDate.set(Calendar.SECOND, 59);
        todayDate.set(Calendar.MILLISECOND, 0);
        Timber.d("Check TimeStamps: " + todayDate.getTimeInMillis() + ":" + midnightTimeStamp);
        return todayDate.getTimeInMillis() <= midnightTimeStamp;
    }


    private static void initiateMidnightAlarm(Context context, AlarmManager alarmManager,
                                              DataPreference sharedPreference) {
        Timber.d("Midnight Alarm Initialized");

        if (sharedPreference.getLastProcessedMidnightTimeStamp() == -1) {
            Calendar resetCal = Calendar.getInstance();
            sharedPreference.setLastProcessedMidnightTimeStamp(resetCal.getTimeInMillis());
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Timber.d("Current TimeStamp : " + System.currentTimeMillis());
        Timber.d("Alarmed TimeStamp : " + calendar.getTimeInMillis());
        PendingIntent midnightIntent = PendingIntent.getBroadcast(context, 0,
                new Intent("com.thc.Midnight"), 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, midnightIntent);
    }


    public static boolean verifyIfBreakFastReceiverWasInvoked(Context context) {
        DataPreference sharedPreference = new DataPreference(context);
        //check if the midnight receiver was evoked or not
        long midnightTimeStamp = sharedPreference.getLastProcessedBreakFastTimeStamp();
        Calendar todayDate = Calendar.getInstance();
        todayDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(new DataPreference(context).getBreakfastTime()));
        todayDate.set(Calendar.MINUTE, 0);
        todayDate.set(Calendar.SECOND, 0);
        todayDate.set(Calendar.MILLISECOND, 0);
        Timber.d("Check TimeStamps: " + todayDate.getTimeInMillis() + ":" + midnightTimeStamp);
        return todayDate.getTimeInMillis() <= midnightTimeStamp;
    }


    private static void initiateBreakFastAlarm(Context context, AlarmManager alarmManager,
                                               DataPreference sharedPreference) {
        Timber.d("Midnight Alarm Initialized");

        if (sharedPreference.getLastProcessedBreakFastTimeStamp() == -1) {
            Calendar resetCal = Calendar.getInstance();
            sharedPreference.setLastProcessedBreakFastTimeStamp(resetCal.getTimeInMillis());
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(new DataPreference(context).getBreakfastTime()));
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Timber.d("Current TimeStamp : " + System.currentTimeMillis());
        Timber.d("Alarmed TimeStamp : " + calendar.getTimeInMillis());
        PendingIntent midnightIntent = PendingIntent.getBroadcast(context, 0,
                new Intent("com.thc.BreakFast"), 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, midnightIntent);
    }


    public static boolean verifyIfLunchReceiverWasInvoked(Context context) {
        DataPreference sharedPreference = new DataPreference(context);
        //check if the midnight receiver was evoked or not
        long midnightTimeStamp = sharedPreference.getLastProcessedLunchTimeStamp();
        Calendar todayDate = Calendar.getInstance();
        todayDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(new DataPreference(context).getLunchTime()));
        todayDate.set(Calendar.MINUTE, 0);
        todayDate.set(Calendar.SECOND, 0);
        todayDate.set(Calendar.MILLISECOND, 0);
        Timber.d("Check TimeStamps: " + todayDate.getTimeInMillis() + ":" + midnightTimeStamp);
        return todayDate.getTimeInMillis() <= midnightTimeStamp;
    }


    private static void initiateLunchAlarm(Context context, AlarmManager alarmManager,
                                           DataPreference sharedPreference) {
        Timber.d("Midnight Alarm Initialized");

        if (sharedPreference.getLastProcessedLunchTimeStamp() == -1) {
            Calendar resetCal = Calendar.getInstance();
            sharedPreference.setLastProcessedLunchTimeStamp(resetCal.getTimeInMillis());
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(new DataPreference(context).getLunchTime()));
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Timber.d("Current TimeStamp : " + System.currentTimeMillis());
        Timber.d("Alarmed TimeStamp : " + calendar.getTimeInMillis());
        PendingIntent midnightIntent = PendingIntent.getBroadcast(context, 0,
                new Intent("com.thc.Lunch"), 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, midnightIntent);
    }


    public static boolean verifyIfDinnerReceiverWasInvoked(Context context) {
        DataPreference sharedPreference = new DataPreference(context);
        //check if the midnight receiver was evoked or not
        long midnightTimeStamp = sharedPreference.getLastProcessedDinnerTimeStamp();
        Calendar todayDate = Calendar.getInstance();
        todayDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(new DataPreference(context).getDinnerTime()));
        todayDate.set(Calendar.MINUTE, 0);
        todayDate.set(Calendar.SECOND, 0);
        todayDate.set(Calendar.MILLISECOND, 0);
        Timber.d("Check TimeStamps: " + todayDate.getTimeInMillis() + ":" + midnightTimeStamp);
        return todayDate.getTimeInMillis() <= midnightTimeStamp;
    }


    private static void initiateDinnerAlarm(Context context, AlarmManager alarmManager,
                                            DataPreference sharedPreference) {
        Timber.d("Midnight Alarm Initialized");

        if (sharedPreference.getLastProcessedDinnerTimeStamp() == -1) {
            Calendar resetCal = Calendar.getInstance();
            sharedPreference.setLastProcessedDinnerTimeStamp(resetCal.getTimeInMillis());
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(new DataPreference(context).getDinnerTime()));
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Timber.d("Current TimeStamp : " + System.currentTimeMillis());
        Timber.d("Alarmed TimeStamp : " + calendar.getTimeInMillis());
        PendingIntent midnightIntent = PendingIntent.getBroadcast(context, 0,
                new Intent("com.thc.Dinner"), 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, midnightIntent);
    }

    private static void initiateExerciseAlarm(Context context, AlarmManager alarmManager, DataPreference sharedPreference) {
        Timber.d("Exer Alarm Initialized");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Timber.d("Current TimeStamp : " + System.currentTimeMillis());
        Timber.d("Alarmed TimeStamp : " + calendar.getTimeInMillis());
        PendingIntent midnightIntent = PendingIntent.getBroadcast(context, 0, new Intent("com.thc.Exercise"), 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, midnightIntent);
    }

    public static void setOrResetAllReceivers(Context context) {
        Timber.i("setOrResetAllReceivers");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        DataPreference sharedPreference = new DataPreference(context);
        initiateMidnightAlarm(context, alarmManager, sharedPreference);
        initiateBreakFastAlarm(context, alarmManager, sharedPreference);
        initiateLunchAlarm(context, alarmManager, sharedPreference);
        initiateDinnerAlarm(context, alarmManager, sharedPreference);
        initiateExerciseAlarm(context, alarmManager, sharedPreference);
    }

}
