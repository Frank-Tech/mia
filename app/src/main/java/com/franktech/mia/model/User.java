package com.franktech.mia.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Marker;

import java.io.ByteArrayOutputStream;
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
    private double lat;
    private double lng;
    private boolean isMale;
    private byte[] profilePic;
    private transient Marker marker;

    public User(String name, String faceId, Date birthday, LatLng latLng, boolean isMale) {
        this(name, faceId, birthday, latLng, isMale, null, null);
    }

    public User(String name, String faceId, Date birthday, LatLng latLng, boolean isMale, Drawable profilePic,
                Marker marker) {
        this.name = name;
        this.id = faceId;
        this.birthday = birthday;
        this.lat = latLng.latitude;
        this.lng = latLng.longitude;
        this.isMale = isMale;
        this.profilePic = drawableToByreArray(profilePic);
        this.marker = marker;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LatLng getLatLng() {
        return new LatLng(lat, lng);
    }

    public void setLatLng(LatLng latLng) {
        this.lat = latLng.latitude;
        this.lng = latLng.longitude;
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
        return byteArrayToDrawable();
    }

    public void setProfilePic(Drawable profilePic) {
        this.profilePic = drawableToByreArray(profilePic);
    }

    private byte[] drawableToByreArray(Drawable drawable){

        if (drawable == null) return null;

        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();

        return b;
    }

    private Drawable byteArrayToDrawable() {
        return new BitmapDrawable(BitmapFactory.decodeByteArray(profilePic, 0, profilePic.length));
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }
}
