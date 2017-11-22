package com.franktech.mia.utilities;

import android.util.Log;

/**
 * Created by franktech on 20/11/17.
 */

public class MiaLogger {
    public static void d(Class TAG, String message){
        Log.d(TAG.getName(), message);
    }
    public static void i(Class TAG, String message){
        Log.i(TAG.getName(), message);
    }
    public static void e(Class TAG, String message){
        Log.e(TAG.getName(), message);
    }
    public static void v(Class TAG, String message){
        Log.v(TAG.getName(), message);
    }
    public static void w(Class TAG, String message){
        Log.w(TAG.getName(), message);
    }

    public static void d(Class TAG, String message, Throwable t){
        Log.d(TAG.getName(), message, t);
    }
    public static void i(Class TAG, String message, Throwable t){
        Log.i(TAG.getName(), message, t);
    }
    public static void e(Class TAG, String message, Throwable t){
        Log.e(TAG.getName(), message, t);
    }
    public static void v(Class TAG, String message, Throwable t){
        Log.v(TAG.getName(), message, t);
    }
    public static void w(Class TAG, String message, Throwable t){
        Log.w(TAG.getName(), message, t);
    }
}
