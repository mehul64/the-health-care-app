package com.thc.app.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.thc.app.R;
import com.thc.app.activities.DashboardActivity;
import com.thc.app.utils.AppSharedPreference;
import com.thc.app.utils.DataPreference;

import java.util.Calendar;
import java.util.Random;

import timber.log.Timber;

public class ExerciseReceiver extends BroadcastReceiver {

    private static final String TAG_VALUE = "MIDNIGHT_RECEIVER";

    public ExerciseReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.i("onReceive");
        processIt(context);
    }

    public static void processIt(Context context) {
        Timber.i("Invoked");
        DataPreference preference = new DataPreference(context);
            long previousTimeStampProcessed = preference.getLastProcessedMidnightTimeStamp();
            Log.d(TAG_VALUE, "previousTimeStampProcessed : " + previousTimeStampProcessed);

            //store the next day to be processed
            Calendar todayDate = Calendar.getInstance();
            if (previousTimeStampProcessed == 0) {
                previousTimeStampProcessed = System.currentTimeMillis();
            }
            sendNotification(context);
            todayDate.setTimeInMillis(previousTimeStampProcessed);
            todayDate.set(Calendar.HOUR_OF_DAY, 17);
            todayDate.set(Calendar.MINUTE, 0);
            todayDate.set(Calendar.SECOND, 0);
            todayDate.set(Calendar.MILLISECOND, 0);

    }

    private static void sendNotification(Context context) {

        String str[]={
                "Do remember to drink 10 litres of water every day",
                "Do remember to take 10,000 steps every day",
                "Do remember to run 20 to 30 minutes every day",
        };

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentText(str[new Random().nextInt()]);
        builder.setSmallIcon(R.mipmap.ic_launcher );
        builder.setContentTitle(context.getString( R.string.app_name ) );
        builder.setAutoCancel(true);
        NotificationManagerCompat.from(context).notify(0, builder.build());
    }


}
