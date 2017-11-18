package com.franktech.mia.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.net.URL;

/**
 * Created by tzlilswimmer on 18/11/2017.
 */

public class FacebookProfilePicture {

    public static Drawable getFacebookProfilePic(Context context, String faceId) {
        Drawable drawable = null;

        try {
            URL imageUrl = new URL("https://graph.facebook.com/" + faceId + "/picture?type=large");
            Bitmap bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());

            drawable = new BitmapDrawable(context.getResources(), bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return drawable;
    }
}
