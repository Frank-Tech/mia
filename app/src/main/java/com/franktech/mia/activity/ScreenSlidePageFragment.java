package com.franktech.mia.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.franktech.mia.R;

/**
 * Created by tzlil on 16/10/17.
 */

public class ScreenSlidePageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.item_match_layout, container, false);
        ImageView picture = (ImageView) rootView.findViewById(R.id.match_picture);
        picture.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.barby));


        return rootView;
    }
}

