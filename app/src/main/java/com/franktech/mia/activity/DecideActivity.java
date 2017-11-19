package com.franktech.mia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.franktech.mia.R;
import com.franktech.mia.fragment.DecideSlidePagerFragment;
import com.franktech.mia.model.User;
import com.franktech.mia.model.UsersStatus;
import com.franktech.mia.utilities.FakeDataManager;
import com.franktech.mia.utilities.SharedPrefSingleton;

import java.util.Set;

public class DecideActivity extends AbstractAppCompatActivity {

    private SharedPrefSingleton prefUtil;
    private ViewPager decidePager;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decide);

        Intent intent = getIntent();
        if(intent != null){
            Set<String> blockedUsers =  SharedPrefSingleton.getInstance(this).getStringSet(SharedPrefSingleton.BLOCKED_USERS_KEY, null);
            Set<String> dislikedUsers =  SharedPrefSingleton.getInstance(this).getStringSet(SharedPrefSingleton.I_DISLIKED_USERS_KEY, null);

            if(intent.hasExtra("user_id")) {
                String userId = intent.getStringExtra("user_id");

                if (!(blockedUsers != null && blockedUsers.contains(userId))
                        && !(dislikedUsers != null && dislikedUsers.contains(userId))) {
                    user = FakeDataManager.users.get(userId);
                    if (user.getId().equals(userId)) {
                        switch (UsersStatus.getStatus(getApplicationContext(), user.getId())) {
                            case LIKED_ME: {
                                Set<String> set = prefUtil.getStringSet(SharedPrefSingleton.LIKED_ME_USERS_KEY, null);

                                if (!set.contains(user.getId())) {
                                    set.add(user.getId());
                                    prefUtil.putStringSet(SharedPrefSingleton.LIKED_ME_USERS_KEY, set);
                                }
                                break;
                            }
                        }
                    }
                }
            }else if(getIntent().hasExtra(User.USER_KEY)){
                user = (User)getIntent().getSerializableExtra(User.USER_KEY);
            }
        }

        prefUtil = SharedPrefSingleton.getInstance(this);

        decidePager = findViewById(R.id.decide_pager);

        decidePager.setOffscreenPageLimit(5);
        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(),
                user);
        decidePager.setAdapter(pagerAdapter);
        decidePager.setOffscreenPageLimit(4);
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private User user;
        public ScreenSlidePagerAdapter(FragmentManager fm, User user) {
            super(fm);
            this.user = user;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new DecideSlidePagerFragment();
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
