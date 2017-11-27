package com.franktech.mia.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.franktech.mia.utilities.FacebookInfo;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.Date;

/**
 * Created by franktech on 18/11/17.
 */

public class User {

    public static final String USER_KEY = "user";
    private String name;
    private String id;
    private Date birthday;
    private LatLng latLng;
    private boolean isMale;
    private Drawable profilePic;
    private Marker marker;
    private IOnPicReady onBitmapReady;

    public User(Context context, String name, String faceId, Date birthday, LatLng latLng, boolean isMale) {
        this(context, name, faceId, birthday, latLng, isMale, null);
    }

    public User(final Context context, String name, final String faceId, Date birthday, LatLng latLng, boolean isMale, Marker marker) {
        this.name = name;
        this.id = faceId;
        this.birthday = birthday;
        this.latLng = latLng;
        this.isMale = isMale;
        this.marker = marker;

        new MiaAsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                profilePic = FacebookInfo.getProfilePic(context, faceId);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(onBitmapReady != null){
                    onBitmapReady.load(profilePic);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

    public void onBitmapReady(IOnPicReady ready) {
        if(profilePic != null) {
            ready.load(profilePic);
        }else{
            onBitmapReady = ready;
        }
    }

    public void setProfilePic(Drawable profilePic) {
        this.profilePic = profilePic;
    }


    public void removeMarker() {
        if(marker!= null){
            marker.remove();
        }
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Marker getMarker() {
        return marker;
    }

    public interface IOnPicReady{
        void load(Drawable profilePic);
    }
}
