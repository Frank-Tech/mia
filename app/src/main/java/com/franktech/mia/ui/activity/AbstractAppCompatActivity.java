package com.franktech.mia.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.franktech.mia.event.LikeEvent;
import com.franktech.mia.utilities.SharedPrefSingleton;
import com.franktech.mia.utilities.permissions.PermissionManager;
import com.squareup.otto.Bus;

/**
 * Created by franktech on 17/11/17.
 */

abstract public class AbstractAppCompatActivity extends AppCompatActivity {

    private Bus bus;
    protected SharedPrefSingleton prefUtil;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefUtil = SharedPrefSingleton.getInstance(getApplicationContext());
        bus = new Bus();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bus.register(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.onPermissionGranted(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onStop() {
        super.onStop();
        bus.unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onEvent(LikeEvent event){

    }
}
