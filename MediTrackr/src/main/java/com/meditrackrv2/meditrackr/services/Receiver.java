package com.meditrackrv2.meditrackr.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import com.meditrackrv2.meditrackr.Dosages;
import com.meditrackrv2.meditrackr.R;

public class Receiver extends BroadcastReceiver {

    String medName;
    int notificationID = 24;

    @Override
    public void onReceive(Context context, Intent intent) {
        medName = intent.getStringExtra("medication");

        // Create the notification intent
        Intent notificationIntent = new Intent(context, Dosages.class);
        PendingIntent pNotificationIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        // Build the notification
        Uri soundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notificationBuilder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pNotificationIntent)
                .setContentTitle("MediTrackr")
                .setContentText("Tablet: " + medName + " is to be taken.")
                .addAction(R.drawable.ic_action_accept, "Taken", pNotificationIntent)
                .setSound(soundURI)
                .build();

        NotificationManager nManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        nManager.notify(notificationID, notificationBuilder);

    }

    public void cancelNotification(Context context)
    {
        NotificationManager nManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        nManager.cancel(notificationID);
    }
}
