package com.franktech.mia.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.franktech.mia.R;
import com.franktech.mia.ui.fragment.DecideSlidePageFragment;

public class MatchActivity extends FragmentActivity {

    private ViewPager matchPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        ViewPager matchPager = findViewById(R.id.match_pager);
        matchPager.setOffscreenPageLimit(5);
        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        matchPager.setAdapter(pagerAdapter);
        matchPager.setOffscreenPageLimit(4);



    }

//    @Override
//    public void onBackPressed() {
//        if (matchPager.getCurrentItem() == 0) {
//            // If the user is currently looking at the first step, allow the system to handle the
//            // Back button. This calls finish() on this activity and pops the back stack.
//            super.onBackPressed();
//        } else {
//            // Otherwise, select the previous step.
//            matchPager.setCurrentItem(matchPager.getCurrentItem() - 1);
//        }
//    }

    /**
     * A simple pager adapter that represents 5 DecideSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new DecideSlidePageFragment();
        }

        @Override
        public int getCount() {
            return 5;
        }
    }

}
