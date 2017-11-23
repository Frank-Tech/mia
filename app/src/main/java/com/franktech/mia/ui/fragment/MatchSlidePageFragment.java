package com.franktech.mia.ui.fragment;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.franktech.mia.R;
import com.franktech.mia.model.User;

import com.franktech.mia.ui.view.MiaTextView;
import com.franktech.mia.utilities.MiaLogger;
import com.franktech.mia.utilities.SharedPrefSingleton;
import com.franktech.mia.utilities.VolleySingleton;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

/**
 * Created by tzlil on 16/10/17.
 */

public class MatchSlidePageFragment extends Fragment {

    private static final Class TAG = MatchSlidePageFragment.class;
    private ImageView picture;
    private MiaTextView details;
    private User user;
    private Button talk;
    private Button dismiss;
    private ViewPager viewPager;
    private SharedPrefSingleton prefUtil;
    private MiaTextView dontWasteTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.item_match_layout, container, false);
        prefUtil = SharedPrefSingleton.getInstance(getContext());
        bindView(rootView);
        user = (User)getArguments().getSerializable(User.USER_KEY);

        setUserDetails();
        setButtonsListeners();

        user.onBitmapReady(new User.IOnPicReady() {
            @Override
            public void load(Drawable profilePic) {
                picture.setImageDrawable(profilePic);
            }
        });

        return rootView;
    }

    private void bindView(ViewGroup rootView) {
        picture = rootView.findViewById(R.id.match_picture);
        details = rootView.findViewById(R.id.match_details);
        talk = rootView.findViewById(R.id.talk);
        dismiss = rootView.findViewById(R.id.dismiss);
        viewPager = getActivity().findViewById(R.id.slide_pager);
        dontWasteTime = rootView.findViewById(R.id.dont_waste_time);

    }

    private void setUserDetails() {


        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, user.getBirthday().getYear());
        cal.set(Calendar.MONTH, user.getBirthday().getMonth());
        cal.set(Calendar.DAY_OF_MONTH, user.getBirthday().getDay());
        Date birthDate = cal.getTime();

        Date now = new Date();

        long age = (now.getTime() - birthDate.getTime()) / 1000 / 60 / 60 / 24 / 365;

        //get city from long and lat
        String city = "Tel Aviv";
        String userDetails = user.getName() + ", " + age + "\n" + city;

        details.setText(userDetails);

        if(user.isMale()){
            dontWasteTime.setText(R.string.dont_waste_time_he);
        }else{
            dontWasteTime.setText(R.string.dont_waste_time_she);
        }
    }

    private void setButtonsListeners() {
        talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

                alertDialog.setTitle("Meet and talk!");

                alertDialog.create();
                alertDialog.show();

                String url = String.format(getString(R.string.push_url),
                        prefUtil.getString(SharedPrefSingleton.FCM_TOKEN_KEY, ""),
                        "You got a match",
                        "click to see who liked you too",
                        user.getId());

                VolleySingleton.getInstance().request(getContext(), url,
                        new VolleySingleton.VolleyCallback() {
                            @Override
                            public void onSuccess(String response) {
                                MiaLogger.d(TAG, response);
                            }

                            @Override
                            public void onFailed(VolleyError error) {
                                MiaLogger.e(TAG, error.getMessage(), error);
                            }
                        });

                viewPager.setCurrentItem(viewPager.getAdapter().getItemPosition(user) - 1);
                viewPager.getAdapter().notifyDataSetChanged();
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getAdapter().getItemPosition(user) + 1);

                Set<String> setMatch =  prefUtil.getStringSet(SharedPrefSingleton.LIKED_ME_USERS_KEY, null);
                Set<String> setLikedMe =  prefUtil.getStringSet(SharedPrefSingleton.LIKED_ME_USERS_KEY, null);

                if(setMatch.contains(user.getId())){
                    setMatch.remove(user.getId());
                    prefUtil.putStringSet(SharedPrefSingleton.MATCHED_USERS_KEY, setMatch);
                }

                if(!setMatch.contains(user.getId())){
                    setLikedMe.add(user.getId());
                    prefUtil.putStringSet(SharedPrefSingleton.LIKED_ME_USERS_KEY, setLikedMe);
                }

                viewPager.getAdapter().notifyDataSetChanged();

            }
        });
    }
}

