package com.franktech.mia.model;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by franktech on 18/11/17.
 */

public class User implements Serializable {

    public static final String USER_KEY = "user";
    private String name;
    private String id;
    private Date birthday;
    private LatLng latLng;
    private boolean isMale;
    private Drawable profilePic;

    public User(String name, String faceId, Date birthday, LatLng latLng, boolean isMale) {
        this(name, faceId, birthday, latLng, isMale, null);
    }

    public User(String name, String faceId, Date birthday, LatLng latLng, boolean isMale, Drawable profilePic) {
        this.name = name;
        this.id = faceId;
        this.birthday = birthday;
        this.latLng = latLng;
        this.isMale = isMale;
        this.profilePic = profilePic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    @Nullable
    public Drawable getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Drawable profilePic) {
        this.profilePic = profilePic;
    }
}
