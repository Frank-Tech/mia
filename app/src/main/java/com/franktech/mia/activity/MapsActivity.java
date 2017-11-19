package com.franktech.mia.activity;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.franktech.mia.Permissions;
import com.franktech.mia.R;
import com.franktech.mia.utilities.NearUserManager;
import com.franktech.mia.utilities.PermissionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AbstractAppCompatActivity implements OnMapReadyCallback {

    public static final float MIN_DISTANCE = 0;
    public static final long MIN_TIME = 5;

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

                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        LatLng ownerLatlng = new LatLng(location.getLatitude(), location.getLongitude());
                        setMyMarker(ownerLatlng, googleMap);
                    }

                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME,
                                MIN_DISTANCE,
                                new LocationListener() {
                                    @Override
                                    public void onLocationChanged(Location location) {
                                        LatLng ownerLatlng = new LatLng(location.getLatitude(), location.getLongitude());
                                        setMyMarker(ownerLatlng, googleMap);
                                        NearUserManager.getInstance(getApplicationContext(), googleMap).OnNearUsersUpdate(MapsActivity.this, location);
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
                                });
                }
            }
        });
    }

    private void setMyMarker(LatLng ownerLatlng, GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(ownerLatlng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ownerLatlng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));
    }


}
