package com.franktech.mia.fragment;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.franktech.mia.R;
import com.franktech.mia.utilities.FacebookInfo;
import com.franktech.mia.utilities.FacebookProfilePicture;
import com.franktech.mia.utilities.SharedPreSingleton;
import com.franktech.mia.view.MiaTextView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by tzlil on 16/10/17.
 */

public class DecideSlidePageFragment extends Fragment {

    private ImageView picture;
    private MiaTextView details;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.item_match_layout, container, false);

        bindView(rootView);

        setUserPicture();
        setUserDetails();


        return rootView;
    }

    private void bindView(ViewGroup rootView) {
        picture = rootView.findViewById(R.id.match_picture);
        details = rootView.findViewById(R.id.match_details);
    }

    private void setUserPicture() {
        new AsyncTask<Void, Void, Drawable>() {
            @Override
            protected Drawable doInBackground(Void... voids) {
                Drawable pic = FacebookProfilePicture.getFacebookProfilePic(getContext(),
                        SharedPreSingleton.getInstance(getContext())
                                .getString(FacebookInfo.getInfoKeys().get(4), ""));
                return pic;
            }

            @Override
            protected void onPostExecute(Drawable drawable) {
                picture.setImageDrawable(drawable);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void setUserDetails() {
        String name = SharedPreSingleton.getInstance(getContext())
                .getString(FacebookInfo.getInfoKeys().get(1), "").split(" ")[0];

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1992);
        cal.set(Calendar.MONTH, Calendar.APRIL);
        cal.set(Calendar.DAY_OF_MONTH, 16);
        Date birthDate = cal.getTime();

        Date now = new Date();

        long age = (now.getTime() - birthDate.getTime()) / 1000 / 60 / 60 / 24 / 365;

        //get city from long and lat
        String city = "Tel Aviv";
        String userDetails = name + ", " + age + "\n" + city;

        details.setTag(userDetails);
    }
}

