package com.franktech.mia.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.franktech.mia.R;
import com.franktech.mia.model.User;
import com.franktech.mia.model.UsersStatus;
import com.franktech.mia.ui.view.MiaTextView;
import com.franktech.mia.utilities.MiaLogger;
import com.franktech.mia.utilities.NearUserManager;
import com.franktech.mia.utilities.SharedPrefSingleton;
import com.franktech.mia.utilities.VolleySingleton;

import java.util.Set;

/**
 * Created by tzlilswimmer on 19/11/2017.
 */

public class DecideSlidePagerFragment extends Fragment {

    private static final Class TAG = DecideSlidePagerFragment.class;

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
        user = NearUserManager.getInstance().getFakeUsers(getContext()).get(getArguments().getString(User.USER_KEY));

        setStatus();
        setListeners();

        user.onBitmapReady(new User.IOnPicReady() {
            @Override
            public void load(Drawable profilePic) {

                Bitmap bitmap = ((BitmapDrawable)profilePic).getBitmap();

                int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
                picture.setImageBitmap(scaled);
            }
        });

        return rootView;
    }

    private void bindView(View view) {
        picture = view.findViewById(R.id.picture);
        block = view.findViewById(R.id.block);
        like = view.findViewById(R.id.like);
        unlike = view.findViewById(R.id.unlike);
        status = view.findViewById(R.id.status);
        decidePager = getActivity().findViewById(R.id.slide_pager);
    }

    private void setStatus() {
        switch (UsersStatus.getStatus(getContext(), user.getId())) {
            case I_LIKED: {
                status.setText(R.string.i_liked);
                like.setEnabled(false);
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

                Set<String> set =  prefUtil.getStringSet(SharedPrefSingleton.BLOCKED_USERS_KEY, new ArraySet<String>());

                if(!set.contains(user.getId())){
                    set.add(user.getId());
                    prefUtil.putStringSet(SharedPrefSingleton.BLOCKED_USERS_KEY, set);
                }

                if(decidePager.getAdapter().getCount() > 0 &&
                        decidePager.getAdapter().getCount() < decidePager.getAdapter().getItemPosition(user)) {
                    decidePager.setCurrentItem(decidePager.getAdapter().getItemPosition(user) + 1);
                    decidePager.getAdapter().notifyDataSetChanged();
                }else if(decidePager.getAdapter().getCount() > 0){
                    decidePager.setCurrentItem(decidePager.getAdapter().getItemPosition(user) - 1);
                    decidePager.getAdapter().notifyDataSetChanged();
                }else{
                    getActivity().finish();
                }

//                VolleySingleton.getInstance(getActivity()).request("block", new VolleySingleton.VolleyCallback() {
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
                Set<String> setILiked =  prefUtil.getStringSet(SharedPrefSingleton.I_DISLIKED_USERS_KEY, null);
                Set<String> setDislike =  prefUtil.getStringSet(SharedPrefSingleton.I_DISLIKED_USERS_KEY, new ArraySet<String>());

                if(setILiked!= null && !setILiked.contains(user.getId())){
                    setILiked.add(user.getId());
                    prefUtil.putStringSet(SharedPrefSingleton.I_LIKED_USERS_KEY, setILiked);
                }

                if(setDislike != null && setDislike.contains(user.getId())){
                    setDislike.remove(user.getId());
                    prefUtil.putStringSet(SharedPrefSingleton.I_LIKED_USERS_KEY, setDislike);
                }



                String url = String.format(getString(R.string.push_url),
                        prefUtil.getString(SharedPrefSingleton.FCM_TOKEN_KEY, ""),
                        "Someone likes you",
                        "click to see who likes you",
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

                like.setEnabled(false);
                unlike.setEnabled(true);

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
                Set<String> setDislike =  prefUtil.getStringSet(SharedPrefSingleton.I_DISLIKED_USERS_KEY, null);
                Set<String> setILiked =  prefUtil.getStringSet(SharedPrefSingleton.I_DISLIKED_USERS_KEY, new ArraySet<String>());


                if(setDislike != null && !setDislike.contains(user.getId())){
                    setDislike.add(user.getId());
                    prefUtil.putStringSet(SharedPrefSingleton.I_DISLIKED_USERS_KEY, setDislike);
                }

                if(setILiked != null && setILiked.contains(user.getId())){
                    setILiked.remove(user.getId());
                    prefUtil.putStringSet(SharedPrefSingleton.I_LIKED_USERS_KEY, setILiked);
                }


                unlike.setEnabled(false);
                like.setEnabled(true);

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
