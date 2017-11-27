package com.franktech.mia.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.franktech.mia.R;
import com.franktech.mia.model.User;
import com.franktech.mia.model.UsersStatus;
import com.franktech.mia.ui.activity.SlideType;
import com.franktech.mia.ui.activity.UsersSlideActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by franktech on 18/11/17.
 */

public class NearUserManager {

    private static final Class TAG = NearUserManager.class;
    private Map<String, User> users;
    private Map<String, User> fakeUsers;

    private static NearUserManager instance;
    private GoogleMap mMap;

    public static NearUserManager getInstance() {
        if (instance == null) {
            synchronized (NearUserManager.class) {
                if (instance == null) {
                    instance = new NearUserManager();
                }
            }
        }

        return instance;
    }

    private NearUserManager() {
        users = new HashMap<>();
        fakeUsers = new HashMap<>();
    }

    private void initMap(final Context context, GoogleMap map) {

        mMap = map;

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(context, "Test", Toast.LENGTH_LONG).show();
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                for(User user : users.values()){

                    if(marker.getPosition().longitude == user.getLatLng().longitude &&
                            marker.getPosition().latitude == user.getLatLng().latitude){

                        SlideType type = UsersStatus.getStatus(context, user.getId()) == UsersStatus.MATCHED ? SlideType.MATCH : SlideType.DECIDE;
                        context.startActivity(UsersSlideActivity.getActivityIntent(context, type, user.getId()));
                    }
                }
                return false;
            }
        });
    }

    public void OnNearUsersUpdate(final Context context, GoogleMap map, boolean reload, final Location location) {

        initMap(context, map);
        loadUsersToMap(context, reload, getFakeUsers(context));

        // TODO: 18/11/2017 when implementing server - filter by gender
        final String url = String.format(
                context.getResources().getString(R.string.location_url),
                String.valueOf(location.getLatitude()),
                String.valueOf(location.getLongitude()),
                SharedPrefSingleton.getInstance(context).getString(FacebookInfo.EMAIL_KEY, "")
        );

        VolleySingleton.getInstance().request(context, url, new VolleySingleton.VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                MiaLogger.d(TAG, response);
//                        get users from server
//                        try {
//
//                            Map<String, User> newUsers = new HashMap<>();
//
//                            JSONObject nearUsers = new JSONObject(response);
//                            Iterator<?> keys = nearUsers.keys();
//
//                            while (keys.hasNext()) {
//                                String key = (String) keys.next();
//                                if (nearUsers.get(key) instanceof JSONObject) {
//                                    double userLong = ((JSONObject) nearUsers.get(key)).getDouble("longitude");
//                                    double userLat = ((JSONObject) nearUsers.get(key)).getDouble("latitude");
//                                    String name = ((JSONObject) nearUsers.get(key)).getString("name");
//                                    String faceId = ((JSONObject) nearUsers.get(key)).getString("face_id");
//                                    String birthday = ((JSONObject) nearUsers.get(key)).getString("birthday");
//                                    String gender = ((JSONObject) nearUsers.get(key)).getString("gender");
//
//
//                                    newUsers
//                                        .put(faceId,
//                                            new User(name, faceId, new Date(birthday), new LatLng(userLat, userLong), gender.equals("male")));
//
//                                    loadUsersToMap(context, newUsers);
//                                }
//                            }
//
//                        } catch (JSONException e) {
//                            MiaLogger.e(TAG, e.getMessage(), e);
//                        }
            }

            @Override
            public void onFailed(VolleyError error) {
                MiaLogger.e(TAG, error.getMessage(), error);
            }
        });
    }

    private void loadUsersToMap(final Context context, boolean reload, final Map<String, User> newUsers) {

        Map<String, User> oldUsers = new HashMap<>(users);

        for (User user : oldUsers.values()) {
            if (!newUsers.containsKey(user.getId())) {
                user.removeMarker();
                users.remove(user.getId());
            }else if(newUsers.containsKey(user.getId()) && !reload){
                newUsers.remove(user.getId());
            }
        }

        for (final User user : newUsers.values()) {
            setMarker(context, user);
        }

        users.putAll(newUsers);

    }

    private void setMarker(final Context context, final User user) {

        final View customMarkerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker, null);
        final ImageView markerImageView = customMarkerView.findViewById(R.id.profile_image);
        ImageView statusImageView = customMarkerView.findViewById(R.id.status_icon);

        switch (UsersStatus.getStatus(context, user.getId())) {
            case BLOCK: {
                return;
            }
            case I_LIKED: {

                Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_heart, null);

                if(drawable != null) {
                    PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.green), PorterDuff.Mode.SRC_IN);
                    drawable.setColorFilter(colorFilter);
                }

                statusImageView.setImageDrawable(drawable);
                break;
            }
            case LIKED_ME: {
                statusImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart));
                break;
            }
            case I_DISLIKED: {
                statusImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_dislike));
                break;
            }
            case MATCHED: {
                statusImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hearts));
                break;
            }
            case NONE: {
                statusImageView.setVisibility(View.GONE);
                break;
            }
        }

        user.onBitmapReady(new User.IOnPicReady() {
            @Override
            public void load(Drawable profilePic) {
                markerImageView.setImageDrawable(profilePic);

                markerImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

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

                Marker marker = mMap.addMarker(
                        new MarkerOptions()
                                .position(user.getLatLng())
                                .icon(BitmapDescriptorFactory.fromBitmap(returnedBitmap))
                                .title(user.getName())
                );

                user.setMarker(marker);
            }
        });
    }

    public Map<String, User> getFakeUsers(Context context) {
        if(fakeUsers.size() == 0){
            fakeUsers = new HashMap<>();

            double lat = 32.069021;
            double lng = 34.816194;
            LatLng latLng = new LatLng(lat - 0.0005, lng);
            fakeUsers.put("100015974979753", new User(context, "Avraham Frank", "100015974979753", new Date(1511021743), latLng, true));

            latLng = new LatLng(lat, lng - 0.0005);
            fakeUsers.put("1020667303", new User(context,"benjamin umi frank", "1020667303", new Date(1511121743), latLng, true));

            latLng = new LatLng(lat - 0.0005, lng - 0.0005);
            fakeUsers.put("100002943197376", new User(context,"Zvi Mendelson", "100002943197376", new Date(1511921743), latLng, true));

            latLng = new LatLng(lat + 0.0005, lng);
            fakeUsers.put("544356333", new User(context,"Avi Salomon", "544356333", new Date(1510021743), latLng, true));

            latLng = new LatLng(lat, lng + 0.0005);
            fakeUsers.put("100004328658378", new User(context,"Miki Balin", "100004328658378", new Date(1411121743), latLng, true));

            latLng = new LatLng(lat + 0.0005, lng + 0.0005);
            fakeUsers.put("1397714808", new User(context,"Ella Bar-Yaacov", "1397714808", new Date(1311921743), latLng, false));
            Set<String> set = new ArraySet<>();
            set.add("100004328658378");
            SharedPrefSingleton.getInstance(context).putStringSet(SharedPrefSingleton.MATCHED_USERS_KEY, set);
        }

        Map<String, User> temp = new HashMap<>(fakeUsers);

        for(User user : temp.values()){
            if ((SharedPrefSingleton.getInstance(context).getString(FacebookInfo.getInfoKeys().get(2), "").equals("female") && !user.isMale() ||
                    (SharedPrefSingleton.getInstance(context).getString(FacebookInfo.getInfoKeys().get(2), "").equals("male") &&
                            user.isMale()))) continue;
            fakeUsers.remove(user.getId());
        }

        return fakeUsers;
    }

    public Map<String,User> getUsers() {
        return users;
    }
}
