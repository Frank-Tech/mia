package com.franktech.mia.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.franktech.mia.R;
import com.franktech.mia.fragment.MatchSlidePageFragment;
import com.franktech.mia.model.User;
import com.franktech.mia.model.UsersStatus;
import com.franktech.mia.utilities.FakeDataManager;
import com.franktech.mia.utilities.SharedPrefSingleton;

import java.util.Set;

public class MatchActivity extends FragmentActivity {
    private User user;
    private ViewPager matchPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        Intent intent = getIntent();
        if(intent != null){
            Set<String> blockedUsers =  SharedPrefSingleton.getInstance(this).getStringSet(SharedPrefSingleton.BLOCKED_USERS_KEY, null);
            Set<String> dislikedUsers =  SharedPrefSingleton.getInstance(this).getStringSet(SharedPrefSingleton.I_DISLIKED_USERS_KEY, null);

            if(intent.hasExtra("user_id")) {
                String userId = intent.getStringExtra("user_id");

                SharedPrefSingleton prefUtil = SharedPrefSingleton.getInstance(this);
                if (!(blockedUsers != null && blockedUsers.contains(userId))
                        && !(dislikedUsers != null && dislikedUsers.contains(userId))) {
                    User user = FakeDataManager.users.get(userId);
                    if (user.getId().equals(userId)) {
                        switch (UsersStatus.getStatus(getApplicationContext(), user.getId())) {
                            case I_LIKED: {
                                Set<String> set = prefUtil.getStringSet(SharedPrefSingleton.MATCHED_USERS_KEY, null);

                                if (!set.contains(user.getId())) {
                                    set.add(user.getId());
                                    prefUtil.putStringSet(SharedPrefSingleton.MATCHED_USERS_KEY, set);
                                }

                                break;
                            }
                        }
                    }
                }
            }else if(intent.hasExtra(User.USER_KEY)){
                user = (User)getIntent().getSerializableExtra(User.USER_KEY);
            }
        }

        ViewPager matchPager = findViewById(R.id.match_pager);
        matchPager.setOffscreenPageLimit(5);
        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(),
                user);
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


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private User user;
        public ScreenSlidePagerAdapter(FragmentManager fm, User user) {
            super(fm);
            this.user = user;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new MatchSlidePageFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(User.USER_KEY, user);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return FakeDataManager.users.size();
        }
    }

}
