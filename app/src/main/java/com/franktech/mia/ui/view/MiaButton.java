package com.franktech.mia.ui.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by tzlilswimmer on 18/11/2017.
 */

public class MiaButton extends android.support.v7.widget.AppCompatButton{
    public MiaButton(Context context) {
        super(context);
        initFont(context);
    }

    public MiaButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFont(context);
    }

    public MiaButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFont(context);
    }


    private void initFont(Context context) {
        if (this.isInEditMode()) return ;

        this.setTypeface(
                Typeface.createFromAsset(context.getAssets(), "IndieFlower.ttf")
        );
    }
}
