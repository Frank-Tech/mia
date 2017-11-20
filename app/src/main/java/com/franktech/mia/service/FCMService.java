package com.franktech.mia.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.ArraySet;

import com.franktech.mia.R;
import com.franktech.mia.event.LikeEvent;
import com.franktech.mia.ui.activity.SlideType;
import com.franktech.mia.ui.activity.UsersSlideActivity;
import com.franktech.mia.utilities.SharedPrefSingleton;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.otto.Bus;

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

        SlideType slideType;

        if(iLiked.contains(remoteMessage.getFrom())){
            slideType = SlideType.MATCH;
        }else{

            Set<String> likedMe = prefUtil.getStringSet(SharedPrefSingleton.LIKED_ME_USERS_KEY, new ArraySet<String>());

            likedMe.add(remoteMessage.getFrom());
            prefUtil.putStringSet(SharedPrefSingleton.LIKED_ME_USERS_KEY, likedMe);

            slideType = SlideType.DECIDE;
        }

        PendingIntent pendingIntent =
                PendingIntent
                        .getBroadcast(
                                getApplicationContext(),
                                1234,
                                UsersSlideActivity.getActivityIntent(getApplicationContext(), slideType, remoteMessage.getFrom()),
                                0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_heart)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify(0, builder.build());
        }

        new Bus().post(new LikeEvent());
    }
}
