package com.franktech.mia.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by tzlilswimmer on 17/11/2017.
 */

public class MIATextView extends android.support.v7.widget.AppCompatTextView{
    public MIATextView(Context context) {
        super(context);
    }

    public MIATextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MIATextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        if (this.isInEditMode()) return ;

//        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.font_style);
//        final String customFont = a.getString(R.styleable.font_style[0]);

        //Build a custom typeface-cache here!
        this.setTypeface(
                Typeface.createFromAsset(context.getAssets(), "smart_watch.ttf")
        );
    }


}
