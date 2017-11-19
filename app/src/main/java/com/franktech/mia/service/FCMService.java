package com.franktech.mia.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.franktech.mia.R;
import com.franktech.mia.activity.DecideActivity;
import com.franktech.mia.activity.MatchActivity;
import com.franktech.mia.utilities.SharedPrefSingleton;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Set;

/**
 * Created by franktech on 18/11/17.
 */

public class FCMService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        SharedPrefSingleton prefUtil = SharedPrefSingleton.getInstance(getApplicationContext());

        Set<String> iLiked =  prefUtil.getStringSet(SharedPrefSingleton.I_LIKED_USERS_KEY, null);
        Set<String> likedMe =  prefUtil.getStringSet(SharedPrefSingleton.LIKED_ME_USERS_KEY, null);

        Intent intent = null;
        PendingIntent pendingIntent;
        if(iLiked != null && iLiked.contains(remoteMessage.getFrom())){
            intent = new Intent(getApplicationContext(), MatchActivity.class);
            intent.putExtra("userId", remoteMessage.getFrom());
        }else if(likedMe != null && !likedMe.contains(remoteMessage.getFrom())){
            prefUtil.putInt(SharedPrefSingleton.COUNT_LIKED_ME, prefUtil.getInt(SharedPrefSingleton.COUNT_LIKED_ME) + 1);

            intent = new Intent(getApplicationContext(), DecideActivity.class);
            intent.putExtra("userId", remoteMessage.getFrom());
        }

        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1234,intent,0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_heart)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(0, builder.build());
        }


    }
}
