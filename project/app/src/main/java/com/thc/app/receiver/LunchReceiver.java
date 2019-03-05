package com.thc.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.thc.app.utils.DataPreference;

import java.util.Calendar;

import timber.log.Timber;

public class LunchReceiver extends BroadcastReceiver {
    private static final String TAG_VALUE = "MIDNIGHT_RECEIVER";

    public LunchReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.i("onReceive");
//        processIt(context);
    }

    public static void processIt(Context context) {
        Timber.i("Invoked");
        DataPreference preference = new DataPreference(context);
        do {
            long previousTimeStampProcessed = preference.getLastProcessedMidnightTimeStamp();
            Log.d(TAG_VALUE, "previousTimeStampProcessed : " + previousTimeStampProcessed);

            if(new DataPreference(context).getDinner()!=null)
                DinnerReceiver.sendNotification("LUNCH", context);
            else
                BreakFastReceiver.sendNotification("LUNCH",context);

            //store the next day to be processed
            Calendar todayDate = Calendar.getInstance();
            if (previousTimeStampProcessed == 0) {
                previousTimeStampProcessed = System.currentTimeMillis();
            }
            todayDate.setTimeInMillis(previousTimeStampProcessed);
            todayDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(new DataPreference(context).getLunchTime()));
            todayDate.set(Calendar.MINUTE, 0);
            todayDate.set(Calendar.SECOND, 0);
            todayDate.set(Calendar.MILLISECOND, 0);
            preference.setLastProcessedMidnightTimeStamp(todayDate.getTimeInMillis());
        } while (!OnBootReceiver.verifyIfMidnightReceiverWasInvoked(context));
    }


}
