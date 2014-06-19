/*  MediTrackr V2 - Created by Andrew Williams
 *  Re-write of previous version
 *  MainActivity */
package com.meditrackrv2.meditrackr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private static final int RESULT_SETTINGS = 1;
    boolean isTracking = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the title
        setTitle("MediTrackr - Overview");

        final SharedPreferences activitySettings = getPreferences(MODE_PRIVATE);

        // Load Image
        ImageView ivLogo = (ImageView)findViewById(R.id.ivLogo);
        ivLogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));

        // Load medication and other information from prefSettings into TextViews
        final SharedPreferences sharePrefs = PreferenceManager.getDefaultSharedPreferences(this);

        TextView tvName = (TextView)findViewById(R.id.tvMedName);
        TextView tvDosage = (TextView)findViewById(R.id.tvDayDosage);
        TextView tvTotal = (TextView)findViewById(R.id.tvTotalTab);

        String medicationName = sharePrefs.getString("nameMed", "N/A");
        String dayDoseStr = sharePrefs.getString("dailyMed", "N/A");
        String tabTotalStr = sharePrefs.getString("amountMed", "N/A"); // Change to Integer

        tvName.setText("Medication Name: " + medicationName);
        tvDosage.setText("Daily Dosage: " + dayDoseStr);
        tvTotal.setText("Total Tablets: " + tabTotalStr);

        // Track settings
        final TextView tvTrackStatus = (TextView)findViewById(R.id.tvTrackStatus);

        // Create listener for button
        final Button btnTrack = (Button)findViewById(R.id.btnStartTrack);

        // Set the correct text of button and tracking status
        if(isTracking = activitySettings.getBoolean("isRunning", false)){
            btnTrack.setText("Stop Tracking");
            tvTrackStatus.setText("Tracking Medication: Yes");
        } else {
            tvTrackStatus.setText("Tracking Medication: No");
        }

        btnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTracking = activitySettings.getBoolean("isRunning", false);
                if(!isTracking){
                    // Start tracking
                    btnTrack.setText("Stop Tracking");
                    tvTrackStatus.setText("Tracking Medication: Yes");
                    activitySettings.edit().putBoolean("isRunning", true).commit();
                    startTracking();
                    Log.i("Tracking Start", String.valueOf(isTracking));
                }
                else if(isTracking){
                    // Add stopTracking
                    btnTrack.setText("Start Tracking");
                    tvTrackStatus.setText("Tracking Medication: No");
                    activitySettings.edit().putBoolean("isRunning", false).commit();
                    Log.i("Tracking Stop", String.valueOf(isTracking));
                }

            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();

        final SharedPreferences sharePrefs = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences activitySettings = getPreferences(MODE_PRIVATE);

        TextView tvTotal = (TextView)findViewById(R.id.tvTotalTab);
        String tabTotalStr = sharePrefs.getString("amountMed", "N/A");

        tvTotal.setText("Total Tablets: " + tabTotalStr);

        // Track settings
        final TextView tvTrackStatus = (TextView)findViewById(R.id.tvTrackStatus);

        // Create listener for button
        final Button btnTrack = (Button)findViewById(R.id.btnStartTrack);

        // Set the correct text of button and tracking status
        if(isTracking = activitySettings.getBoolean("isRunning", false)){
            btnTrack.setText("Stop Tracking");
            tvTrackStatus.setText("Tracking Medication: Yes");
        } else {
            tvTrackStatus.setText("Tracking Medication: No");
        }
    }

    protected void startTracking() {
        // Pass to Dosages in order to start notifications
        Intent doseIntent = new Intent(this, Dosages.class);
        startActivity(doseIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        if (id == R.id.scan_view) {
            // Change to medication query activity
            Intent queryIntent = new Intent(this, MedicationQuery.class);
            startActivity(queryIntent);
        }
        return super.onOptionsItemSelected(item);
    }

}
