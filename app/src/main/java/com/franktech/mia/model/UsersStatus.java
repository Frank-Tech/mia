package com.franktech.mia.model;

import android.content.Context;

import com.franktech.mia.utilities.SharedPrefSingleton;

import java.util.Collections;
import java.util.Set;

/**
 * Created by franktech on 19/11/17.
 */

public enum UsersStatus {
    BLOCK(SharedPrefSingleton.BLOCKED_USERS_KEY),
    I_LIKED(SharedPrefSingleton.I_LIKED_USERS_KEY),
    I_DISLIKED(SharedPrefSingleton.I_DISLIKED_USERS_KEY),
    LIKED_ME(SharedPrefSingleton.LIKED_ME_USERS_KEY),
    MATCHED(SharedPrefSingleton.MATCHED_USERS_KEY),
    NONE(null);

    private String key;

    UsersStatus(String key) {
        this.key = key;
    }

    public static UsersStatus getStatus(Context context, String userId){

        for (UsersStatus status : UsersStatus.values()){
            if(status.getVal(context).contains(userId)){
                return status;
            }
        }

        return NONE;
    }

    public String getKey() {
        return key;
    }

    public Set<String> getVal(Context context) {
        return SharedPrefSingleton.getInstance(context).getStringSet(key, Collections.<String>emptySet());
    }

    public void setKey(String key) {
        this.key = key;
    }
}
