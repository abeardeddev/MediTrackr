package com.meditrackrv2.meditrackr.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;

import com.meditrackrv2.meditrackr.Dosages;

/**
 * Created by Andrew on 01/07/2014.
 */
public class BootNotification extends BroadcastReceiver {

    @Override
    public void onReceive(Context c, Intent i)
    {
        if(Intent.ACTION_BOOT_COMPLETED.equals(i.getAction())){
            // Restart notifications
            Intent doseIntent = new Intent(c, Dosages.class);
            doseIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            c.startActivity(doseIntent);
        }
    }
}
