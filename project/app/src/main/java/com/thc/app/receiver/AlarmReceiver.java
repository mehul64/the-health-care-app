package com.thc.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.thc.app.BaseApplication;
import com.thc.app.R;
import com.thc.app.utils.DataPreference;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("ALARM","RECEIVED");

        DataPreference preference = new DataPreference(context);
        BaseApplication.sendDataToServer(context,preference);
        preference.resetAllValues();

//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//        builder.setContentText("ALARM RECEIVED");
//        builder.setSmallIcon(R.mipmap.ic_launcher );
//        builder.setContentTitle( context.getString( R.string.app_name ) );
//        builder.setAutoCancel(true);
//        NotificationManagerCompat.from(context).notify(0, builder.build());
    }
}
