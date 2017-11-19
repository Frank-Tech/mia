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
import com.franktech.mia.utilities.SharedPrefSingleton;

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

        RelativeLayout container = view.findViewById(R.id.strip_bar_container);
        MiaTextView title = view.findViewById(R.id.strip_bar_title);

        final int likesCount = SharedPrefSingleton.getInstance(context).getInt(SharedPrefSingleton.COUNT_LIKED_ME);

        if(likesCount == 0){
          title.setText(R.string.no_one_interested);
        }

        container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(likesCount > 0) {
                    Intent intent = new Intent(context, MatchActivity.class);
                    context.startActivity(intent);
                }
            }
        });

        MiaTextView likes = view.findViewById(R.id.likes_count);
        likes.setText(String.valueOf(likesCount));

    }
}
