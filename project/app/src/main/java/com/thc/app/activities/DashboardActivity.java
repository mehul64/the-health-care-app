package com.thc.app.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.location.DetectedActivity;
import com.thc.app.BaseActivity;
import com.thc.app.BaseFragment;
import com.thc.app.R;
import com.thc.app.fragments.AboutUsFragment;
import com.thc.app.fragments.HelpFragment;
import com.thc.app.fragments.HistoryFragment;
import com.thc.app.fragments.HomeFragment;
import com.thc.app.fragments.ProfileFragment;
import com.thc.app.fragments.SettingsFragment;
import com.thc.app.receiver.AlarmReceiver;
import com.thc.app.utils.AppSharedPreference;
import com.thc.app.utils.DataPreference;

import java.util.Calendar;

public class DashboardActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BaseFragment currentSelectedFragment;
    private HomeFragment homeFragment;
    private HistoryFragment historyFragment;
    private AboutUsFragment aboutUsFragment;
    private HelpFragment helpFragment;
    private ProfileFragment profileFragment;
    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(!new AppSharedPreference(this).isLoggedIn())
        {
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }

        setAlarm();

        if(getIntent().getStringExtra("dialog")!=null)
        {
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle("Set correct activity");
            final String[] types = {"Walking","Running","Cycling","Drivings"};
            b.setItems(types, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DataPreference dataPreference = new DataPreference(DashboardActivity.this);
                    long currentTimestamp = System.currentTimeMillis();
                    long differentTime = currentTimestamp - dataPreference.getPrevComputedTimeStamp();
                    differentTime = differentTime / 1000;
                    dataPreference.updateTodaysOtherValue(differentTime);
                    dataPreference.setPrevComputedTimeStamp(currentTimestamp);
                    dialog.dismiss();
                }
            });
            b.show();
        }


        if(new DataPreference(this).getLunchTime()==null)
        {
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle("Set lunch time");
            final String[] types=new String[24];
            for(int i=1;i<=24;i++)
                types[i-1]=""+i;
            b.setItems(types, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new DataPreference(DashboardActivity.this).setLunchTime(types[which]);
                    dialog.dismiss();
                }
            });
            b.show();
        }

        if(new DataPreference(this).getDinnerTime()==null)
        {
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle("Set dinner time");
            final String[] types=new String[24];
            for(int i=1;i<=24;i++)
                types[i-1]=""+i;
            b.setItems(types, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new DataPreference(DashboardActivity.this).setDinnerTime(types[which]);
                    dialog.dismiss();
                }
            });
            b.show();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        homeFragment = new HomeFragment();
        showThisFragment(homeFragment);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);
        }
    }

    private void setAlarm() {
        Calendar cal;
        cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        Intent in = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 1, in, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pi);
        Log.e("ALARM", "Setting alarm for " + cal.getTime().toString());

        boolean alarmUp = (PendingIntent.getBroadcast(this, 1, in, PendingIntent.FLAG_NO_CREATE) != null);
        if (alarmUp)
            Log.e("ALARM", "Alarm is already active");
        else
            Log.e("ALARM", "Alarm is not active");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_music) {
            Intent intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            homeFragment = new HomeFragment();
            showThisFragment(homeFragment);
        } else if (id == R.id.nav_history) {
            historyFragment = new HistoryFragment();
            showThisFragment(historyFragment);
        } else if (id == R.id.nav_settings) {
            if (settingsFragment == null) {
                settingsFragment = new SettingsFragment();
            }
            showThisFragment(settingsFragment);
        } else if (id == R.id.nav_profile) {
            if (profileFragment == null) {
                profileFragment = new ProfileFragment();
            }
            showThisFragment(profileFragment);
        } else if (id == R.id.nav_help) {
            if (helpFragment == null) {
                helpFragment = new HelpFragment();
            }
            showThisFragment(helpFragment);
        } else if (id == R.id.nav_about) {
            if (aboutUsFragment == null) {
                aboutUsFragment = new AboutUsFragment();
            }
            showThisFragment(aboutUsFragment);
        } else if (id == R.id.nav_logout) {
            AppSharedPreference appSharedPreference = new AppSharedPreference(activity);
            appSharedPreference.setFullName(null);
            appSharedPreference.setEmail(null);
            appSharedPreference.setMobile(null);
            appSharedPreference.setWeight(null);
            appSharedPreference.setLoggedIn(false);
            appSharedPreference.setAge(null);
            appSharedPreference.setHeight(null);
            appSharedPreference.setGender(null);
            DataPreference dataPreference = new DataPreference(activity);
            dataPreference.resetAllValues();
            startActivity(new Intent(activity, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (currentSelectedFragment instanceof HomeFragment) {
            homeFragment = new HomeFragment();
            showThisFragment(homeFragment);
        }
    }

    private void showThisFragment(BaseFragment fragment) {
        currentSelectedFragment = fragment;
        getSupportActionBar().setTitle(fragment.getFragmentTitle());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }


}
