package com.franktech.mia;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.franktech.mia.utilities.SharedPrefSingleton;

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

        String uuid = SharedPrefSingleton.getInstance(this).getString(SharedPrefSingleton.UUID_KEY, null);

        if(uuid == null){
            uuid = UUID.randomUUID().toString();
            SharedPrefSingleton.getInstance(this).putString(SharedPrefSingleton.UUID_KEY, uuid);
        }
    }
}
