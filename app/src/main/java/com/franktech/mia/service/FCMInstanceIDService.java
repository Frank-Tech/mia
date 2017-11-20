package com.franktech.mia.service;

import com.franktech.mia.utilities.MiaLogger;
import com.franktech.mia.utilities.SharedPrefSingleton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by franktech on 18/11/17.
 */

public class FCMInstanceIDService extends FirebaseInstanceIdService {

    private static final Class TAG = FCMInstanceIDService.class;

    @Override
    public void onTokenRefresh() {
        try {
            SharedPrefSingleton
                    .getInstance(getApplicationContext())
                    .putString(SharedPrefSingleton.FCM_TOKEN_KEY, URLEncoder.encode(FirebaseInstanceId.getInstance().getToken(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            MiaLogger.e(TAG, e.getMessage(), e);
        }
    }

}
