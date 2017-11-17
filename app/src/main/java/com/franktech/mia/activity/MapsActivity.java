package com.franktech.mia.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.franktech.mia.Permissions;
import com.franktech.mia.R;
import com.franktech.mia.utilities.MiaLocationManager;
import com.franktech.mia.utilities.PermissionManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.io.IOException;
import java.net.URL;

public class MapsActivity extends AbstractAppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {

        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        PermissionManager.requestPermissions(this, Permissions.LOCATION, new PermissionManager.onPermissionGranted() {
            @Override
            public void run() {
                if (locationManager != null) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MiaLocationManager.MIN_TIME,
                            MiaLocationManager.MIN_DISTANCE,
                            MiaLocationManager.getInstance(MapsActivity.this, googleMap).getLocationListener());
                }
            }
        });
    }



    private Bitmap getFacebookProfilePic(String faceId) {

        Bitmap bitmap = null;

        try {
            URL imageUrl = new URL("https://grapgh.facebook.com/" + faceId + "/picture?type=large");
            bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
