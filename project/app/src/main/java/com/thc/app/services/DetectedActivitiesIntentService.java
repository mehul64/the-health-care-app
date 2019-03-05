/**
 * Copyright 2014 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thc.app.services;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.thc.app.BaseApplication;
import com.thc.app.R;
import com.thc.app.activities.DashboardActivity;
import com.thc.app.utils.AppSharedPreference;
import com.thc.app.utils.DataPreference;

import java.util.ArrayList;

import timber.log.Timber;

public class DetectedActivitiesIntentService extends IntentService {

    protected static final String TAG = "DetectedActivitiesIS";

    public DetectedActivitiesIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        Timber.e("onHandleIntent");
        DataPreference dataPreference = new DataPreference(this);

        if (dataPreference.isTrackingON() && new AppSharedPreference(this).isLoggedIn()) {
            BaseApplication.sendDataToServer(getApplicationContext(),new DataPreference(getApplicationContext()));

            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

            ArrayList<DetectedActivity> detectedActivities = (ArrayList) result.getProbableActivities();

            int detectedValue = -1;

            // Log each activity.
//            Log.e(TAG, "activities detected");
            DetectedActivity detectedActivity=null;

            int max=-1;
            for (DetectedActivity da : detectedActivities) {

                Log.e("ACTIVITY",da.getType()+" "+da.getConfidence());

                if (da.getConfidence() >= max) {
                    max=da.getConfidence();
                    detectedValue = da.getType();
                    detectedActivity=da;
                }
            }

            long currentTimestamp = System.currentTimeMillis();

            long differentTime = currentTimestamp - dataPreference.getPrevComputedTimeStamp();
            differentTime = differentTime / 1000;
            if (detectedValue == DetectedActivity.WALKING && detectedActivity.getConfidence()>60) {
                Log.e(TAG, "walking detected: "+detectedActivity.getConfidence());
                dataPreference.updateTodaysWalkingValue(differentTime);
                sendNotification("You are currently walking");

            } else if (detectedValue == DetectedActivity.RUNNING && detectedActivity.getConfidence()>60) {
                Log.e(TAG, "running detected: "+detectedActivity.getConfidence());
                dataPreference.updateTodaysRunningValue(differentTime);
                sendNotification("You are currently running");

            } else if (detectedValue == DetectedActivity.ON_BICYCLE && detectedActivity.getConfidence()>60) {
                Log.e(TAG, "cycling detected: "+detectedActivity.getConfidence());
                sendNotification("You are currently cycling");

            } else if (detectedValue == DetectedActivity.IN_VEHICLE && detectedActivity.getConfidence()>60) {
                Log.e(TAG, "driving detected: "+detectedActivity.getConfidence());
                sendNotification("You are currently driving");
            } else {
                Log.e(TAG, "other detected:"+detectedActivity.getType());
                dataPreference.updateTodaysOtherValue(differentTime);
//                sendNotification("");
            }
            dataPreference.setPrevComputedTimeStamp(currentTimestamp);
        }
    }

    private void sendNotification(String msg) {

//        if(msg.equals(""))
//        {
//            NotificationManagerCompat.from(this).cancelAll();
//            return;
//        }
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        intent.putExtra("dialog","yes");
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 2, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentText(msg);
        builder.setSmallIcon(R.mipmap.ic_launcher );
        builder.setContentTitle( getString( R.string.app_name ) );
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        NotificationManagerCompat.from(this).notify(0, builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("ACITIVITY SERVICE","DESTROYED");
    }
}
