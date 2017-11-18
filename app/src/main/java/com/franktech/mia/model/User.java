package com.franktech.mia.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by franktech on 18/11/17.
 */

public class User {

    private String name;
    private String id;
    private Date birthday;
    private LatLng latLng;
    private boolean isMale;

    public User(String name, String faceId, Date birthday, LatLng latLng, boolean isMale) {
        this.name = name;
        this.id = faceId;
        this.birthday = birthday;
        this.latLng = latLng;
        this.isMale = isMale;
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
}
