package com.franktech.mia.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.franktech.mia.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by franktech on 18/11/17.
 */

public class FCMService extends FirebaseMessagingService {
    
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel mChannel =
                new NotificationChannel(
                        getString(R.string.channel_id),
                        getString(R.string.channel_name),
                        NotificationManager.IMPORTANCE_HIGH);

        mChannel.setDescription(getString(R.string.channel_description));
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.RED);
        mChannel.enableVibration(true);

        if (mNotificationManager != null) {
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }
}
