package com.franktech.mia.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.franktech.mia.R;
import com.franktech.mia.utilities.VolleySingleton;
import com.franktech.mia.model.User;
import com.franktech.mia.utilities.SharedPrefSingleton;

import java.util.Set;

public class DecideActivity extends AbstractAppCompatActivity {

    private ImageView picture;
    private Button like;
    private Button unlike;
    private ImageView block;
    private User user;
    private SharedPrefSingleton prefUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decide);

        prefUtil = SharedPrefSingleton.getInstance(this);
        user = (User) getIntent().getSerializableExtra(User.USER_KEY);

        bindView();
        setListeners();

        picture.setImageDrawable(user.getProfilePic());
    }

    private void bindView() {
        picture = findViewById(R.id.picture);
        block = findViewById(R.id.block);
        like = findViewById(R.id.like);
        unlike = findViewById(R.id.unlike);
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

                String url = String.format(getString(R.string.push_url),
                        prefUtil.getString(SharedPrefSingleton.FCM_TOKEN_KEY, ""),
                        "Someone likes you",
                        "click to see who likes you");

                VolleySingleton.getInstance().request(getApplicationContext(), url,
                        new VolleySingleton.VolleyCallback() {
                            @Override
                            public void onSuccess(String response) {

                            }

                            @Override
                            public void onFailed(VolleyError error) {

                            }
                        });
            }
        });

        unlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
