package com.franktech.mia.utilities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.franktech.mia.R;
import com.franktech.mia.VolleySingleton;
import com.franktech.mia.activity.DecideActivity;
import com.franktech.mia.model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by franktech on 17/11/17.
 */

public class MiaLocationManager {

    private LocationListener locationListener;
    public static final long MIN_TIME = 5;
    public static final long MIN_DISTANCE = 5;

    private GoogleMap mMap;
    private Marker mMarker;

    private static MiaLocationManager instance;

    public static MiaLocationManager getInstance(Context context, GoogleMap map){
        if(instance == null){
            synchronized (MiaLocationManager.class){
                if(instance == null){
                    instance = new MiaLocationManager(context, map);
                }
            }
        }

        return instance;
    }

    private MiaLocationManager(final Context context, GoogleMap map) {

        this.mMap = map;
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(final Location location) {

                String url = String.format(
                        context.getResources().getString(R.string.location_url),
                        String.valueOf(location.getLatitude()),
                        String.valueOf(location.getLongitude()),
                        SharedPreSingleton.getInstance(context).getString(FacebookInfo.EMAIL_KEY, "")
                );

                VolleySingleton.getInstance(context).request(url, new VolleySingleton.VolleyCallback() {
                    @Override
                    public void onSuccess(String response) {

                            List<User> users = new ArrayList<>();

                            LatLng latLng = new LatLng(location.getLatitude() - 0.000005, location.getLongitude());
                            users.add(new User("Avraham Frank", "100015974979753", new Date(1511021743), latLng, true));

                            latLng = new LatLng(location.getLatitude(), location.getLongitude() - 0.000005);
                            users.add(new User("benjamin umi frank", "1020667303", new Date(1511121743), latLng, true));

                            latLng = new LatLng(location.getLatitude() - 0.000005, location.getLongitude() - 0.000005);
                            users.add(new User("Zvi Mendelson", "100002943197376", new Date(1511921743), latLng, true));

                            latLng = new LatLng(location.getLatitude() + -0.000005, location.getLongitude());
                            users.add(new User("Avi Salomon", "544356333", new Date(1510021743), latLng, true));

                            latLng = new LatLng(location.getLatitude(), location.getLongitude() + 0.000005);
                            users.add(new User("Miki Balin", "100004328658378", new Date(1411121743), latLng, true));

                            latLng = new LatLng(location.getLatitude() + 0.000005, location.getLongitude() + 0.000005);
                            users.add(new User("Ella Bar-Yaacov", "1397714808", new Date(1311921743), latLng, false));

//                        try {
//                            JSONObject nearUsers = new JSONObject(response);
//                            Iterator<?> keys = nearUsers.keys();

//                            while (keys.hasNext()) {
//                                String key = (String) keys.next();
//                                if (nearUsers.get(key) instanceof JSONObject) {
//                                    double userLong = ((JSONObject) nearUsers.get(key)).getDouble("longitude");
//                                    double userLat = ((JSONObject) nearUsers.get(key)).getDouble("latitude");
//                                    String faceId = ((JSONObject) nearUsers.get(key)).getString("face_id");
////                                }
////                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }

                    @Override
                    public void onFailed(VolleyError error) {

                    }
                });

                setUserLocation(new LatLng(location.getLatitude(), location.getLongitude()));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
    }

    public LocationListener getLocationListener() {
        return locationListener;
    }

    private void setLocation(final Context context, final LatLng location){
        AsyncTask<Void, Void, Drawable> loadPic = new AsyncTask<Void, Void, Drawable>() {
            @Override
            protected Drawable doInBackground(Void... voids) {
                Drawable pic = FacebookProfilePicture.getFacebookProfilePic(context,SharedPreSingleton.getInstance(context).
                        getString(FacebookInfo.getInfoKeys().get(4), ""));
                return pic;
            }

            @Override
            protected void onPostExecute(Drawable pic) {
                mMarker = mMap.addMarker(
                        new MarkerOptions()
                                .position(location)
                                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(context, pic))));

                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Toast.makeText(context, "Test",Toast.LENGTH_LONG).show();
                    }
                });

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Intent intent = new Intent(context, DecideActivity.class);
                        context.startActivity(intent);
                        return false;
                    }
                });

                mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                mMap.animateCamera(CameraUpdateFactory.zoomTo( 17.0f ) );
            }
        };

        loadPic.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void setUserLocation(final LatLng location){
        mMarker = mMap.addMarker(
                new MarkerOptions()
                        .position(location));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.zoomTo( 17.0f ) );

    }


    private Bitmap getMarkerBitmapFromView(Context context, Drawable drawable) {

        View customMarkerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker, null);
        ImageView markerImageView = customMarkerView.findViewById(R.id.profile_image);

        markerImageView.setImageDrawable(drawable);

        customMarkerView.measure(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();

        Bitmap returnedBitmap = Bitmap.createBitmap(
                customMarkerView.getMeasuredWidth(),
                customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888
        );

        Canvas canvas = new Canvas(returnedBitmap);
        Drawable resultDrawable = customMarkerView.getBackground();

        if (resultDrawable != null) resultDrawable.draw(canvas);

        customMarkerView.draw(canvas);

        return returnedBitmap;
    }


    public Marker getMarker() {
        return mMarker;
    }
}
