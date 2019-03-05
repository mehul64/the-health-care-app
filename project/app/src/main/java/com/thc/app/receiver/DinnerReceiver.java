package com.thc.app.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.thc.app.R;
import com.thc.app.activities.SelectFoodDialogActivity;
import com.thc.app.utils.AppSharedPreference;
import com.thc.app.utils.DataPreference;

import java.util.Calendar;

import timber.log.Timber;

import static android.content.Context.NOTIFICATION_SERVICE;

public class DinnerReceiver extends BroadcastReceiver {
    private static final String TAG_VALUE = "MIDNIGHT_RECEIVER";

    public DinnerReceiver() {
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
                DinnerReceiver.sendNotification("DINNER", context);
            else
                BreakFastReceiver.sendNotification("DINNER",context);

            //store the next day to be processed
            Calendar todayDate = Calendar.getInstance();
            if (previousTimeStampProcessed == 0) {
                previousTimeStampProcessed = System.currentTimeMillis();
            }
            todayDate.setTimeInMillis(previousTimeStampProcessed);
            todayDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(new DataPreference(context).getDinnerTime()));
            todayDate.set(Calendar.MINUTE, 0);
            todayDate.set(Calendar.SECOND, 0);
            todayDate.set(Calendar.MILLISECOND, 0);
            preference.setLastProcessedMidnightTimeStamp(todayDate.getTimeInMillis());
        } while (!OnBootReceiver.verifyIfMidnightReceiverWasInvoked(context));
    }

    public static void sendNotification(String meal, Context context) {
        Timber.e("sendNotification");
        if (new AppSharedPreference(context).isLoggedIn()) {

            Intent intent = new Intent(context, SelectFoodDialogActivity.class);
            intent.putExtra("meal", meal);
            PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

            Notification noti = new Notification.Builder(context)
                    .setContentTitle(meal)
                    .setContentText("Tell us what did you have?").setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(pIntent)
                    .build();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            noti.flags |= Notification.FLAG_AUTO_CANCEL;

            switch (meal) {
                case "BREAKFAST":
                    notificationManager.notify(0, noti);
                    break;
                case "LUNCH":
                    notificationManager.notify(2, noti);
                    break;
                case "DINNER":
                    notificationManager.notify(3, noti);
                    break;
            }
        }
    }


}
