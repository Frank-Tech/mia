package com.franktech.mia;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.franktech.mia.utilities.SharedPreSingleton;

import java.util.UUID;

/**
 * Created by frank on 10/28/17.
 */

public class MiaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        String uuid = SharedPreSingleton.getInstance(this).getString(SharedPreSingleton.UUID_KEY, null);

        if(uuid == null){
            uuid = UUID.randomUUID().toString();
            SharedPreSingleton.getInstance(this).putString(SharedPreSingleton.UUID_KEY, uuid);
        }
    }
}
