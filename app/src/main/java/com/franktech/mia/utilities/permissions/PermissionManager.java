package com.franktech.mia.utilities.permissions;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by franktech on 17/11/17.
 */

public class PermissionManager {

    private static Map<Integer, onPermissionGranted> permissionsCallback = new HashMap<>();

    public static void requestPermissions(Activity activity, Permissions permissions, onPermissionGranted callback) {

        boolean closeApp = false;

        for(String p : permissions.getPermissions()){

            if(!permissions.isPermissionGranted(activity) &&
                    !activity.shouldShowRequestPermissionRationale(p) &&
                    permissions.isAskedBefore(activity)){

                closeApp = true;
            }
        }

        if(closeApp){
           activity.finish();
        }else if (!permissions.isPermissionGranted(activity)) {
            permissions.setAskedBefore(activity);
            permissionsCallback.put(permissions.getRequestCode(), callback);
            ActivityCompat.requestPermissions(activity, permissions.getPermissions(), permissions.getRequestCode());
        }else {
            callback.run();
        }
    }


    public static void onPermissionGranted(Activity activity, int requestCode, String[] permissions, int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permissionsCallback.get(requestCode).run();
        } else {

            for(Permissions p : Permissions.values()) {
                if(Arrays.equals(p.getPermissions(), permissions)){
                    requestPermissions(activity, p, permissionsCallback.get(requestCode));
                }
            }
        }
    }

    public interface onPermissionGranted{
        void run();
    }
}
