package com.franktech.mia.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.franktech.mia.R;

public class DecideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decide);

        ImageView picture = (ImageView) findViewById(R.id.picture);
        picture.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.barby));
    }
}
