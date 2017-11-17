package com.franktech.mia.utilities;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.franktech.mia.Permissions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by franktech on 17/11/17.
 */

public class PermissionManager {

    private static Map<Integer, onPermissionGranted> permissionsCallback = new HashMap<>();

    public static void requestPermissions(Activity activity, Permissions permissions, onPermissionGranted callback) {

        if (!permissions.isPermissionGranted(activity)) {
            permissionsCallback.put(permissions.getRequestCode(), callback);
            ActivityCompat.requestPermissions(activity, permissions.getPermissions(), permissions.getRequestCode());
        }else {
            callback.run();
        }
    }


    public static void onPermissionGranted(int requestCode, String[] permissions, int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permissionsCallback.get(requestCode).run();
        } else {

        }
    }

    public interface onPermissionGranted{
        void run();
    }
}
