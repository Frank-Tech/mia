package com.franktech.mia.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.franktech.mia.R;
import com.franktech.mia.model.User;
import com.franktech.mia.model.UsersStatus;
import com.franktech.mia.ui.fragment.DecideSlidePagerFragment;
import com.franktech.mia.ui.fragment.MatchSlidePageFragment;
import com.franktech.mia.utilities.NearUserManager;
import com.franktech.mia.utilities.SharedPrefSingleton;

import java.util.Set;

/**
 * Created by franktech on 20/11/17.
 */

public class UsersSlideActivity extends AbstractAppCompatActivity {

    private SlideType type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);

        if (getIntent() != null) {

            type = SlideType.valueOf(getIntent().getStringExtra(SlideType.TYPE_KEY));
            String userId = getIntent().getStringExtra(User.USER_KEY);

            User user = NearUserManager.getInstance().getFakeUsers(this).get(userId);
            UsersStatus status = UsersStatus.getStatus(getApplicationContext(), user.getId());

            switch (type) {
                case DECIDE: {

                    if(!status.equals(UsersStatus.LIKED_ME)) break;

                    Set<String> set = prefUtil.getStringSet(SharedPrefSingleton.LIKED_ME_USERS_KEY, null);

                    if (!set.contains(user.getId())) {
                        set.add(user.getId());
                        prefUtil.putStringSet(SharedPrefSingleton.LIKED_ME_USERS_KEY, set);
                    }

                    break;

                }case MATCH: {

                    if(!status.equals(UsersStatus.I_LIKED)) break;

                    Set<String> set = prefUtil.getStringSet(SharedPrefSingleton.MATCHED_USERS_KEY, null);

                    if (!set.contains(user.getId())) {
                        set.add(user.getId());
                        prefUtil.putStringSet(SharedPrefSingleton.MATCHED_USERS_KEY, set);
                    }

                    break;

                }
            }

            ViewPager decidePager = findViewById(R.id.slide_pager);

            decidePager.setOffscreenPageLimit(5);

            PagerAdapter pagerAdapter =
                    new ScreenSlidePagerAdapter(this);

            decidePager.setAdapter(pagerAdapter);
            decidePager.setOffscreenPageLimit(4);

            Object[] users = NearUserManager.getInstance().getFakeUsers(getApplicationContext()).keySet().toArray();
            int i = 0;

            while (i < users.length){
                if(users[i].equals(userId)){
                    decidePager.setCurrentItem(i);
                }

                i++;
            }
        }
    }

    public static Intent getActivityIntent(Context context, SlideType type, String userId) {
        Intent intent = new Intent(context, UsersSlideActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(User.USER_KEY, userId);
        bundle.putString(SlideType.TYPE_KEY, String.valueOf(type));
        intent.putExtras(bundle);

        return intent;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private Context context;

        ScreenSlidePagerAdapter(Context context) {
            super(getSupportFragmentManager());
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = type == SlideType.MATCH ?
                    new MatchSlidePageFragment() :
                    new DecideSlidePagerFragment();

            Bundle bundle = new Bundle();

            bundle.putString(
                    User.USER_KEY,
                    ((User)NearUserManager.getInstance().getFakeUsers(context).values().toArray()[position]).getId());

            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return NearUserManager.getInstance().getFakeUsers(context).size();
        }
    }
}
