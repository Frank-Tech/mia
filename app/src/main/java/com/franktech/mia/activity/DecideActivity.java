package com.franktech.mia.activity;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.franktech.mia.R;
import com.franktech.mia.utilities.FacebookInfo;
import com.franktech.mia.utilities.FacebookProfilePicture;
import com.franktech.mia.utilities.SharedPreSingleton;

public class DecideActivity extends AbstractAppCompatActivity {

    private ImageView picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decide);

        picture = findViewById(R.id.picture);

        setProfilePicture();
    }

    private void setProfilePicture() {
        new AsyncTask<Void, Void, Drawable>() {
            @Override
            protected Drawable doInBackground(Void... voids) {
                Drawable pic = FacebookProfilePicture.getFacebookProfilePic(getApplicationContext(),
                        SharedPreSingleton.getInstance(getApplicationContext())
                                .getString(FacebookInfo.getInfoKeys().get(3), ""));
                return pic;
            }

            @Override
            protected void onPostExecute(Drawable drawable) {
                picture.setImageDrawable(drawable);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
