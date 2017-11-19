package com.franktech.mia.utilities;

import com.franktech.mia.model.User;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tzlil on 19/11/17.
 */

public class FakeDataManager {
    public static Map<String, User> users = new HashMap<>();

    public static void setUsers(){
        double lat = 32.075888;
        double longt= 34.771872;
        LatLng latLng = new LatLng(lat - 0.0005, longt);
        users.put("100015974979753", new User("Avraham Frank", "100015974979753", new Date(1511021743), latLng, true));

        latLng = new LatLng(lat, longt - 0.0005);
        users.put("1020667303", new User("benjamin umi frank", "1020667303", new Date(1511121743), latLng, true));

        latLng = new LatLng(lat - 0.0005, longt - 0.0005);
        users.put("100002943197376", new User("Zvi Mendelson", "100002943197376", new Date(1511921743), latLng, true));

        latLng = new LatLng(lat + 0.0005, longt);
        users.put("544356333", new User("Avi Salomon", "544356333", new Date(1510021743), latLng, true));

        latLng = new LatLng(lat, longt + 0.0005);
        users.put("100004328658378", new User("Miki Balin", "100004328658378", new Date(1411121743), latLng, true));

        latLng = new LatLng(lat + 0.0005, longt + 0.0005);
        users.put("1397714808", new User("Ella Bar-Yaacov", "1397714808", new Date(1311921743), latLng, false));
    }
}
