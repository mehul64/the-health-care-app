package com.thc.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.thc.app.BaseApplication;
import com.thc.app.activities.DashboardActivity;
import com.thc.app.database.GpsLocationTable;
import com.thc.app.models.UserData;
import com.thc.app.utils.AppSharedPreference;
import com.thc.app.utils.DataPreference;
import com.thc.app.utils.Urls;
import com.thc.app.utils.VolleySingleton;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class MidnightProcessReceiver extends BroadcastReceiver {
    private static final String TAG_VALUE = "MIDNIGHT_RECEIVER";

    public MidnightProcessReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.i("onReceive");
        sendDataToServerAndClearData(context);
    }

/*
    public static void sendDataToServerAndClearData(Context context) {
        Timber.i("Invoked");
        DataPreference preference = new DataPreference(context);
        do {
            long previousTimeStampProcessed = preference.getLastProcessedMidnightTimeStamp();
            Log.d(TAG_VALUE, "previousTimeStampProcessed : " + previousTimeStampProcessed);

            //TODO push data to server

            BaseApplication.sendDataToServer(context, preference);

            preference.resetAllValues();

            //store the next day to be processed
            Calendar todayDate = Calendar.getInstance();
            if (previousTimeStampProcessed == 0) {
                previousTimeStampProcessed = System.currentTimeMillis();
            }
            todayDate.setTimeInMillis(previousTimeStampProcessed);
            todayDate.set(Calendar.HOUR_OF_DAY, 1);
            todayDate.set(Calendar.MINUTE, 0);
            todayDate.set(Calendar.SECOND, 0);
            todayDate.set(Calendar.MILLISECOND, 0);
            preference.setLastProcessedMidnightTimeStamp(todayDate.getTimeInMillis());
            preference.setJourneyId(todayDate.getTimeInMillis() + "");
        } while (!OnBootReceiver.verifyIfMidnightReceiverWasInvoked(context));
    }
*/

    public static void sendDataToServerAndClearData(Context context) {
        Timber.i("Invoked");
        DataPreference preference = new DataPreference(context);
        do {
            long previousTimeStampProcessed = preference.getLastProcessedMidnightTimeStamp();
            Log.d(TAG_VALUE, "previousTimeStampProcessed : " + previousTimeStampProcessed);

            BaseApplication.sendDataToServer(context, preference);

            preference.resetAllValues();

            //store the next day to be processed
            Calendar todayDate = Calendar.getInstance();
            if (previousTimeStampProcessed == 0) {
                previousTimeStampProcessed = System.currentTimeMillis();
            }
            todayDate.setTimeInMillis(previousTimeStampProcessed);
            todayDate.set(Calendar.HOUR_OF_DAY, 23);
            todayDate.set(Calendar.MINUTE, 59);
            todayDate.set(Calendar.SECOND, 59);
            todayDate.set(Calendar.MILLISECOND, 0);
            preference.setLastProcessedMidnightTimeStamp(todayDate.getTimeInMillis());
            preference.setJourneyId(todayDate.getTimeInMillis() + "");
        } while (!OnBootReceiver.verifyIfMidnightReceiverWasInvoked(context));
    }
}
