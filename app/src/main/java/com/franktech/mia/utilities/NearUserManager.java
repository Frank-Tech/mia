package com.franktech.mia.utilities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.franktech.mia.R;
import com.franktech.mia.VolleySingleton;
import com.franktech.mia.activity.DecideActivity;
import com.franktech.mia.model.MiaAsyncTask;
import com.franktech.mia.model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by franktech on 18/11/17.
 */

public class NearUserManager {

    private Map<String, User> users;

    private static NearUserManager instance;
    private GoogleMap mMap;

    public static NearUserManager getInstance(GoogleMap map) {
        if (instance == null) {
            synchronized (NearUserManager.class) {
                if (instance == null) {
                    instance = new NearUserManager(map);
                }
            }
        }

        return instance;
    }

    private NearUserManager(GoogleMap map) {
        mMap = map;
        users = new HashMap<>();
    }

    private void loadUsersToMap(final Context context, List<User> nearUsers) {

        Map<String, User> newUsers = new HashMap<>();

        Set<String> blockedUsers =  SharedPrefSingleton.getInstance(context).getStringSet(SharedPrefSingleton.BLOCKED_USERS_KEY, null);

        for (final User user : nearUsers) {
            if (!users.containsKey(user.getId())) {

                if(blockedUsers != null && blockedUsers.contains(user.getId())) continue;
                
                new MiaAsyncTask<Void ,Void, Drawable>() {
                    @Override
                    protected void onPostExecute(Drawable drawable) {
                        super.onPostExecute(drawable);

                        user.setProfilePic(drawable);

                        Marker marker = mMap.addMarker(
                                new MarkerOptions()
                                        .position(user.getLatLng())
                                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(context, user.getProfilePic()))));

                        user.setMarker(marker);

                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                Toast.makeText(context, "Test", Toast.LENGTH_LONG).show();
                            }
                        });

                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                Intent intent = new Intent(context, DecideActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(User.USER_KEY, user);
                                context.startActivity(intent);
                                return false;
                            }
                        });
                    }

                    @Override
                    protected Drawable doInBackground(Void... voids) {
                        return FacebookProfilePicture.getFacebookProfilePic(context, user.getId());
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

            newUsers.put(user.getId(), user);
        }

        for (User user : users.values()) {
            if (newUsers.containsKey(user.getId())) {
                user.getMarker().remove();
            }
        }

        this.users = newUsers;
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

    public void OnNearUsersUpdate(final Context context, final Location location) {

        LatLng user = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(
                new MarkerOptions()
                        .position(user));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(context, "Test", Toast.LENGTH_LONG).show();
            }
        });

        mMap.moveCamera(
                CameraUpdateFactory.newLatLng(
                        new LatLng(
                                location.getLatitude(),
                                location.getLongitude())));

        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

        final String url = String.format(
                context.getResources().getString(R.string.location_url),
                String.valueOf(location.getLatitude()),
                String.valueOf(location.getLongitude()),
                SharedPrefSingleton.getInstance(context).getString(FacebookInfo.EMAIL_KEY, "")
        );

        VolleySingleton.getInstance(context).request(url, new VolleySingleton.VolleyCallback() {
            @Override
            public void onSuccess(String response) {

                List<User> users = new ArrayList<>();

                LatLng latLng = new LatLng(location.getLatitude() - 0.0005, location.getLongitude());
                users.add(new User("Avraham Frank", "100015974979753", new Date(1511021743), latLng, true));

                latLng = new LatLng(location.getLatitude(), location.getLongitude() - 0.0005);
                users.add(new User("benjamin umi frank", "1020667303", new Date(1511121743), latLng, true));

                latLng = new LatLng(location.getLatitude() - 0.0005, location.getLongitude() - 0.0005);
                users.add(new User("Zvi Mendelson", "100002943197376", new Date(1511921743), latLng, true));

                latLng = new LatLng(location.getLatitude() + -0.0005, location.getLongitude());
                users.add(new User("Avi Salomon", "544356333", new Date(1510021743), latLng, true));

                latLng = new LatLng(location.getLatitude(), location.getLongitude() + 0.0005);
                users.add(new User("Miki Balin", "100004328658378", new Date(1411121743), latLng, true));

                latLng = new LatLng(location.getLatitude() + 0.0005, location.getLongitude() + 0.0005);
                users.add(new User("Ella Bar-Yaacov", "1397714808", new Date(1311921743), latLng, false));

                loadUsersToMap(context, users);

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
    }
}
