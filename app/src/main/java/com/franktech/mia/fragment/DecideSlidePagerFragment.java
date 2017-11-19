package com.franktech.mia.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.franktech.mia.R;
import com.franktech.mia.VolleySingleton;
import com.franktech.mia.model.User;
import com.franktech.mia.model.UsersStatus;
import com.franktech.mia.utilities.SharedPrefSingleton;
import com.franktech.mia.view.MiaTextView;

import java.util.Set;

/**
 * Created by tzlilswimmer on 19/11/2017.
 */

public class DecideSlidePagerFragment extends Fragment {

    private ImageView picture;
    private Button like;
    private Button unlike;
    private ImageView block;
    private User user;
    private MiaTextView status;
    private SharedPrefSingleton prefUtil;
    private ViewPager decidePager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.item_decide_layout, container, false);
        prefUtil = SharedPrefSingleton.getInstance(getContext());
        bindView(rootView);
        user = (User)getArguments().getSerializable(User.USER_KEY);

        setStatus();
        setListeners();

        Bitmap profilePic = ((BitmapDrawable)user.getProfilePic()).getBitmap();

        int nh = (int) ( profilePic.getHeight() * (512.0 / profilePic.getWidth()) );
        Bitmap scaled = Bitmap.createScaledBitmap(profilePic, 512, nh, true);
        picture.setImageBitmap(scaled);

        return rootView;
    }

    private void bindView(View view) {
        picture = view.findViewById(R.id.picture);
        block = view.findViewById(R.id.block);
        like = view.findViewById(R.id.like);
        unlike = view.findViewById(R.id.unlike);
        status = view.findViewById(R.id.status);
        decidePager = getActivity().findViewById(R.id.decide_pager);
    }

    private void setStatus() {
        switch (UsersStatus.getStatus(getContext(), user.getId())) {
            case I_LIKED: {
                status.setText(R.string.i_liked);
                like.setVisibility(View.INVISIBLE);
                break;
            }
            case LIKED_ME: {
                status.setText(R.string.liked_me);
                break;
            }
            case I_DISLIKED: {
                status.setText(R.string.i_disliked);
                break;
            }
            case NONE: {
                status.setText(R.string.waiting_for_choosing);
                break;
            }
        }
    }

    private void setListeners() {
        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Set<String> set =  prefUtil.getStringSet(SharedPrefSingleton.BLOCKED_USERS_KEY, null);

                if(!set.contains(user.getId())){
                    set.add(user.getId());
                    prefUtil.putStringSet(SharedPrefSingleton.BLOCKED_USERS_KEY, set);
                }

                if(decidePager.getAdapter().getCount() > 0 &&
                        decidePager.getAdapter().getCount() < decidePager.getAdapter().getItemPosition(user)) {
                    decidePager.setCurrentItem(decidePager.getAdapter().getItemPosition(user) - 1);
                    decidePager.getAdapter().notifyDataSetChanged();
                }else if(decidePager.getAdapter().getCount() > 0){
                    decidePager.setCurrentItem(decidePager.getAdapter().getItemPosition(user) + 1);
                    decidePager.getAdapter().notifyDataSetChanged();
                }else{
                    getActivity().finish();
                }

//                VolleySingleton.getInstance(DecideActivity.this).request("block", new VolleySingleton.VolleyCallback() {
//                    @Override
//                    public void onSuccess(String response) {
//
//                    }
//
//                    @Override
//                    public void onFailed(VolleyError error) {
//
//                    }
//                });
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> set =  prefUtil.getStringSet(SharedPrefSingleton.I_LIKED_USERS_KEY, null);

                if(set!= null && !set.contains(user.getId())){
                    set.add(user.getId());
                    prefUtil.putStringSet(SharedPrefSingleton.I_LIKED_USERS_KEY, set);
                }

                String url = String.format(getString(R.string.base_url) + getString(R.string.push_url),
                        prefUtil.getString(SharedPrefSingleton.FCM_TOKEN_KEY, ""),
                        "Someone likes you",
                        "click to see who likes you",
                        user.getId());

                VolleySingleton.getInstance(getContext()).request(url,
                        new VolleySingleton.VolleyCallback() {
                            @Override
                            public void onSuccess(String response) {

                            }

                            @Override
                            public void onFailed(VolleyError error) {

                            }
                        });

                like.setVisibility(View.INVISIBLE);
                unlike.setVisibility(View.VISIBLE);

                if(decidePager.getAdapter().getCount() > decidePager.getAdapter().getItemPosition(user)) {
                    decidePager.setCurrentItem(decidePager.getAdapter().getItemPosition(user) + 1);
                    decidePager.getAdapter().notifyDataSetChanged();
                }else if(decidePager.getAdapter().getCount() > 0){
                    decidePager.setCurrentItem(decidePager.getCurrentItem() -1);
                    decidePager.getAdapter().notifyDataSetChanged();
                }else{
                    getActivity().finish();
                }
            }
        });

        unlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> set =  prefUtil.getStringSet(SharedPrefSingleton.I_DISLIKED_USERS_KEY, null);

                if(!set.contains(user.getId())){
                    set.add(user.getId());
                    prefUtil.putStringSet(SharedPrefSingleton.I_DISLIKED_USERS_KEY, set);
                }

                unlike.setVisibility(View.INVISIBLE);
                unlike.setVisibility(View.VISIBLE);

                if(decidePager.getAdapter().getCount() > 0 &&
                        decidePager.getAdapter().getCount() < decidePager.getAdapter().getItemPosition(user)) {
                    decidePager.setCurrentItem(decidePager.getAdapter().getItemPosition(user) - 1);
                    decidePager.getAdapter().notifyDataSetChanged();
                }else if(decidePager.getAdapter().getCount() > 0){
                    decidePager.setCurrentItem(decidePager.getAdapter().getItemPosition(user) + 1);
                    decidePager.getAdapter().notifyDataSetChanged();
                }else{
                    getActivity().finish();
                }
            }
        });
    }
}
