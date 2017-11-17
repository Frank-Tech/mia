package com.franktech.mia;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by franktech on 17/11/17.
 */

public enum Permissions {
    LOCATION(96313, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,  Manifest.permission.ACCESS_FINE_LOCATION});

    private int requestCode;
    private String[] permissions;

    Permissions(int requestCode, String[] permissions) {
        this.requestCode = requestCode;
        this.permissions = permissions;
    }

    public String[] getPermissions() {
        return permissions;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public boolean isPermissionGranted(Context context){
        for(String permission : getPermissions()){
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }
}
