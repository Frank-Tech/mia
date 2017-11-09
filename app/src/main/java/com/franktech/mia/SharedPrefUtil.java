package com.franktech.mia;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by tzlilswimmer on 09/11/2017.
 */

public class SharedPrefUtil{

    public final static String PREF_NAME  = "MIA_SHARED_PREF";
    public final static String EMAIL_KEY = "email";
    public final static String GENDER_KEY = "gender";
    public final static String BIRTH_DAY_KEY = "birth_day";

    public static SharedPreferences getSharedPref(Context context){
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

}
