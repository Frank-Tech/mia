package com.franktech.mia.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.franktech.mia.R;
import com.franktech.mia.utilities.FacebookInfo;
import com.franktech.mia.utilities.FacebookProfilePicture;
import com.franktech.mia.utilities.SharedPreSingleton;

public class DecideActivity extends AbstractAppCompatActivity {

    private ImageView picture;
    private Button like;
    private Button unlike;
    private ImageView block;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decide);

        bindView();
        setListeners();
        setProfilePicture();
    }

    private void bindView() {
        picture = findViewById(R.id.picture);
        block = findViewById(R.id.block);
        like = findViewById(R.id.like);
        unlike = findViewById(R.id.unlike);
    }

    private void setListeners() {
        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        unlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void setProfilePicture() {
        new AsyncTask<Void, Void, Drawable>() {
            @Override
            protected Drawable doInBackground(Void... voids) {
                Drawable pic = FacebookProfilePicture.getFacebookProfilePic(getApplicationContext(),
                        SharedPreSingleton.getInstance(getApplicationContext())
                                .getString(FacebookInfo.getInfoKeys().get(4), ""));
                return pic;
            }

            @Override
            protected void onPostExecute(Drawable drawable) {
                picture.setImageDrawable(drawable);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
