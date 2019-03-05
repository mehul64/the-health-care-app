package com.thc.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.thc.app.BaseApplication;
import com.thc.app.utils.DataPreference;

public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&activeNetwork.isConnectedOrConnecting();

        if(isConnected)
        {
            Log.e("NETWORK","CONNCTD");
            BaseApplication.sendDataToServer(context,new DataPreference(context));
        }
        else
            Log.e("NETWORK","DISCONNCTD");
    }
}
