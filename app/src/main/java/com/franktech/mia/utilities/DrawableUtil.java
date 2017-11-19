package com.franktech.mia.utilities;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;

/**
 * Created by tzlil on 19/11/17.
 */

public class DrawableUtil {

    public static Drawable getIconWithColor(Context context, int icon, int color) {


        Drawable drawable;
        drawable = ResourcesCompat.getDrawable(context.getResources(), icon, null);

        if(drawable != null) {
            PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN);

            drawable.setColorFilter(colorFilter);

        }

        return drawable;
    }

}
