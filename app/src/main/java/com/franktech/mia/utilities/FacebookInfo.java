package com.franktech.mia.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by franktech on 17/11/17.
 */

public class FacebookInfo {

    public static final String FIELDS_KEY = "fields";
    public static final String FIELDS_VALUE = "id, name, link, email, gender, birthday, age_range";
    public static final String EMAIL_KEY = "email";
    private static final Class TAG = FacebookInfo.class;


    private static String[] infoKeys = new String[]{
            EMAIL_KEY, "name", "gender", "birth_day", "id"
    };

    private static String[] permissions = new String[]{
            "public_profile", "email", "user_birthday", "user_friends", "user_about_me"
    };

    public static List<String> getInfoKeys() {
        return Arrays.asList(infoKeys);
    }

    public static List<String> getPermissions() {
        return Arrays.asList(permissions);
    }

    public static Drawable getFacebookProfilePic(Context context, String faceId) {
        Drawable drawable = null;

        try {
            URL imageUrl = new URL("https://graph.facebook.com/" + faceId + "/picture?type=large");
            Bitmap bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
            drawable = new BitmapDrawable(context.getResources(), bitmap);
        } catch (Exception e) {
            MiaLogger.e(TAG, e.getMessage(), e);
        }

        return drawable;
    }
}
