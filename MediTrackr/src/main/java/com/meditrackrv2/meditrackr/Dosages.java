package com.meditrackrv2.meditrackr;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.meditrackrv2.meditrackr.services.Receiver;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class Dosages extends ActionBarActivity {

    private static final int RESULT_SETTINGS = 1;
    static int todaysDose = 1;
    static int tabletTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosages);

        // Lets cancel the notification
        Receiver notifyMgr = new Receiver();
        notifyMgr.cancelNotification(this);

        setNotification();
    }

    @SuppressWarnings("deprecation")
    private void setNotification() {

        final SharedPreferences activitySettings = getPreferences(MODE_PRIVATE);

        todaysDose = activitySettings.getInt("doseCount", 1);

        final SharedPreferences sharePrefs = PreferenceManager.getDefaultSharedPreferences(this);

        tabletTotal = Integer.valueOf(sharePrefs.getString("amountMed", "30"));

        // Check that the dosages have not been completed for today
        if(todaysDose <= Integer.valueOf(sharePrefs.getString("dailyMed", "3"))) {
            // Obtain the notification hour from preferences
            int notifyHour = Integer.valueOf(sharePrefs.getString("doseInterval", "7"));

            // Create the calendar and set to current time
            Calendar cal = Calendar.getInstance();

            Date date = new Date();
            int currentTime = date.getHours();

            if(todaysDose == 1)
            {
                // Launch at first time
                cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(sharePrefs.getString("firstTime", "7")));
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
            } else {
                // Create timed intent based on current hour and time between dosage
                cal.set(Calendar.HOUR_OF_DAY, currentTime + notifyHour);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);

                // Remove a tablet from total
                tabletTotal -= 1;
                sharePrefs.edit().putString("amountMed", String.valueOf(tabletTotal)).commit();
            }

            // Create the pending intent and set it in the alarm manager
            Intent mainIntent = new Intent(getBaseContext(), Receiver.class);
            mainIntent.putExtra("medication", sharePrefs.getString("nameMed", null));

            PendingIntent pIntent = PendingIntent.getBroadcast(getBaseContext(), 0, mainIntent,
                    0);

            AlarmManager notifyAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);
            notifyAlarm.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pIntent);

            //Update the days dosage
            todaysDose += 1;

            activitySettings.edit().putInt("doseCount", todaysDose).commit();

            // Exit out of activity
            this.finish();

        } else { // New Day
            // Remove a tablet from total
            tabletTotal -= 1;
            sharePrefs.edit().putString("amountMed", String.valueOf(tabletTotal)).commit();

            // Tell the user today is finished
            TextView tvComplete = (TextView)findViewById(R.id.tvComplete);
            tvComplete.setText("You have completed today's course of medication");

            // Create the calendar and set to current time
            Calendar cal = Calendar.getInstance();

            // Set notification for next day at first dosage time
            cal.add(Calendar.DATE, 1);
            cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(sharePrefs.getString("firstTime", "7")));
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            // Create the pending intent and set it in the alarm manager
            Intent mainIntent = new Intent(getBaseContext(), Receiver.class);
            mainIntent.putExtra("medication", sharePrefs.getString("nameMed", null));

            PendingIntent pIntent = PendingIntent.getBroadcast(getBaseContext(), 0, mainIntent,
                    0);

            AlarmManager notifyAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);
            notifyAlarm.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pIntent);

            //Reset the days dosage to 1
            todaysDose = 1;
            activitySettings.edit().putInt("doseCount", todaysDose).commit();

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dosages, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            // Change to settings
            Intent settingsIntent = new Intent(this, PrefSettings.class);
            startActivityForResult(settingsIntent, RESULT_SETTINGS);
        }
        return super.onOptionsItemSelected(item);
    }

}
