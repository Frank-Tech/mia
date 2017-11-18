package com.franktech.mia.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.franktech.mia.model.MiaAsyncTask;
import com.franktech.mia.utilities.PermissionManager;

/**
 * Created by franktech on 17/11/17.
 */

abstract public class AbstractAppCompatActivity extends AppCompatActivity {
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.onPermissionGranted(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MiaAsyncTask.cancel();
    }
}
