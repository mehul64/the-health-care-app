package com.thc.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orm.SugarApp;
import com.thc.app.database.DBHelper;
import com.thc.app.models.OneDayData;
import com.thc.app.receiver.AlarmReceiver;
import com.thc.app.receiver.NetworkReceiver;
import com.thc.app.receiver.OnBootReceiver;
import com.thc.app.services.LocationReceiverService;
import com.thc.app.services.MyReceiverService;
import com.thc.app.utils.AppSharedPreference;
import com.thc.app.utils.DataPreference;
import com.thc.app.utils.Urls;
import com.thc.app.utils.VolleySingleton;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class BaseApplication extends SugarApp {


    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

        DataPreference preference = new DataPreference(this);

        if (!preference.isReceiverInit()) {
            Timber.e("INITIALIZING");
            OnBootReceiver.setOrResetAllReceivers(this);
            preference.setReceiverInit(true);
        }

        Intent intent = new Intent(this, MyReceiverService.class);
        startService(intent);

        Intent locationIntent = new Intent(this, LocationReceiverService.class);
        startService(locationIntent);

        DBHelper dbHelper=new DBHelper(this,"THC",null,1);

        NetworkReceiver networkReceiver=new NetworkReceiver();
        registerReceiver(networkReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static void sendDataToServer(final Context context, final DataPreference preference) {
        final AppSharedPreference sharedPreference = new AppSharedPreference(context);

        if (sharedPreference.isLoggedIn()) {
//            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//            DatabaseReference questionsListDatabaseRef = firebaseDatabase.getReference().child("users");
//            DatabaseReference dataDatabaseRef1 = questionsListDatabaseRef.child(sharedPreference.getBaseUrlIP());
//            Timber.e("********* " + sharedPreference.getBaseUrlIP());
//            DatabaseReference dataDatabaseRef2 = dataDatabaseRef1.child("activities");
//            DatabaseReference dataDatabaseRef3 = dataDatabaseRef2.child(todaysTimstamp());
//            dataDatabaseRef3.setValue(preference.getDataObject(todaysTimstamp()));

            StringRequest stringRequest=new StringRequest(Request.Method.POST, Urls.update_progress,
                    new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("update res",response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("update err",error.toString());
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map=new HashMap<>();
                    OneDayData oneDayData=preference.getDataObject(todaysTimstamp());
                    map.put("breakfast",oneDayData.getBreakFast());
                    map.put("breakfast-calorie",""+oneDayData.getBreakFastCal());
                    map.put("lunch",oneDayData.getLunch());
                    map.put("lunch-calorie",""+oneDayData.getLunchCal());
                    map.put("dinner",oneDayData.getDinner());
                    map.put("dinner-calorie",""+oneDayData.getDinnerCal());
                    map.put("walking",""+(oneDayData.getWalking()*0.0013));
                    map.put("running",""+(oneDayData.getRunning()*0.0022));
                    map.put("other",""+oneDayData.getOther());
                    map.put("email",sharedPreference.getEmail());
                    Log.e("SEND DATA",map.toString());
                    return map;
                }
            };
            VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
        }
    }

    public static String todaysTimstamp() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        return day + "-" + month + "-" + year;
    }
}
