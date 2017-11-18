package com.franktech.mia.utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by tzlilswimmer on 09/11/2017.
 */

public class SharedPreSingleton {


    private final static String PREF_NAME  = "MIA_SHARED_PREF";
    public static final String UUID_KEY = "uuid";
    ;
    public static SharedPreSingleton instance;

    private SharedPreferences preferences;

    public static SharedPreSingleton getInstance(Context context){
        if(instance == null){
            synchronized (SharedPreSingleton.class){
                if(instance == null){
                    instance = new SharedPreSingleton(context);
                }
            }
        }

        return instance;
    }

    private SharedPreSingleton(Context context){
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void putString(String key, String value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String def) {
        return preferences.getString(key, def);
    }
}
