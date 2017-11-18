package com.franktech.mia.fragment;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.franktech.mia.R;
import com.franktech.mia.utilities.FacebookInfo;
import com.franktech.mia.utilities.FacebookProfilePicture;
import com.franktech.mia.utilities.SharedPreSingleton;
import com.franktech.mia.view.MiaTextView;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

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

        LocalDate birthDate = new LocalDate (1992, 4, 16);
        LocalDate now = new LocalDate();
        long age = Period.between(birthDate, now).getYears();

        //get city from long and lat
        String city = "Tel Aviv";
        String userDetails = name + ", " + age + "\n" + city;

        details.setTag(userDetails);
    }
}

