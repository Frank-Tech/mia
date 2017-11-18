package com.franktech.mia.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by tzlilswimmer on 09/11/2017.
 */

public class SharedPrefSingleton {


    private final static String PREF_NAME  = "MIA_SHARED_PREF";
    public static final String UUID_KEY = "uuid";
    public static final String BLOCKED_USERS_KEY = "blocked_users";

    public static SharedPrefSingleton instance;

    private SharedPreferences preferences;

    public static SharedPrefSingleton getInstance(Context context){
        if(instance == null){
            synchronized (SharedPrefSingleton.class){
                if(instance == null){
                    instance = new SharedPrefSingleton(context);
                }
            }
        }

        return instance;
    }

    private SharedPrefSingleton(Context context){
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void putString(String key, String value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    // TODO: move to mongodb
    public void putStringSet(String key, Set<String> value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    public String getString(String key, String def) {
        return preferences.getString(key, def);
    }

    public Set<String> getStringSet(String key, Set<String> def) {
        return preferences.getStringSet(key, def);
    }

}
