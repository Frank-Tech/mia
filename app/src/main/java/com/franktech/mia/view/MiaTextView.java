package com.franktech.mia.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by tzlilswimmer on 17/11/2017.
 */

public class MiaTextView extends android.support.v7.widget.AppCompatTextView{
    public MiaTextView(Context context) {
        super(context);
        initFont(context);
    }

    public MiaTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initFont(context);
    }

    public MiaTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        initFont(context);
    }

    private void initFont(Context context) {
        if (this.isInEditMode()) return ;

        this.setTypeface(
                Typeface.createFromAsset(context.getAssets(), "IndieFlower.ttf")
        );
    }


}
