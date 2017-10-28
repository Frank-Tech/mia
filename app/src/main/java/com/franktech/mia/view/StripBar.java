package com.franktech.mia.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.franktech.mia.R;
import com.franktech.mia.activity.MatchActivity;

/**
 * Created by tzlil on 07/10/17.
 */

public class StripBar extends RelativeLayout {

    public StripBar(Context context) {
        super(context);
        initView(context);
    }

    public StripBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public StripBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(final Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.strip_bar, this);
        ImageButton interestedIcon= (ImageButton) view.findViewById(R.id.interested_icon);
        interestedIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MatchActivity.class);
                context.startActivity(intent);
            }
        });

    }
}
