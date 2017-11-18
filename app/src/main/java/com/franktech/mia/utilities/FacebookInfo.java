package com.franktech.mia.utilities;

import java.util.Arrays;
import java.util.List;

/**
 * Created by franktech on 17/11/17.
 */

public class FacebookInfo {

    public static final String FIELDS_KEY = "fields";
    public static final String FIELDS_VALUE = "id, name, link, email, gender, birthday, age_range";
    public static final String EMAIL_KEY = "email";


    private static String[] infoKeys = new String[]{
            EMAIL_KEY, "gender", "birth_day", "id"
    };

    private static String[] permissions = new String[]{
            "public_profile", "email", "user_birthday", "user_friends", "user_about_me"
    };

    public static List<String> getInfoKeys() {
        return Arrays.asList(infoKeys);
    }

    public static List<String> getPermissions() {
        return Arrays.asList(permissions);
    }
}
