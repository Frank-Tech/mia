package com.franktech.mia.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.franktech.mia.R;
import com.franktech.mia.SharedPrefUtil;
import com.franktech.mia.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = SharedPrefUtil.getSharedPref(getApplicationContext());
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        if (isLoggedIn() == false) {
            callback = CallbackManager.Factory.create();

            LoginButton loginButton = (LoginButton) findViewById(R.id.open_with_facebook);
            loginButton.setReadPermissions(Arrays.asList(
                    "public_profile", "email", "user_birthday", "user_friends", "user_about_me"));

            loginButton.registerCallback(callback, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {

                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    try {
                                        setUserData(object, editor);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,link,email,gender,birthday, age_range");
                    request.setParameters(parameters);
                    request.executeAsync();


                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onCancel() {
                    Log.e(getClass().getName(), "onCancel");
                }

                @Override
                public void onError(FacebookException error) {
                    Log.e(getClass().getName(), error.getMessage(), error);
                }
            });

        }else{
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
        }
    }

    private void setUserData(JSONObject object, SharedPreferences.Editor editor) throws JSONException {
        String facebookId =  object.getString("id");
        if(facebookId != null) {
            editor.putString(SharedPrefUtil.FACEBOOK_ID, facebookId);
        }

        String email = object.getString("email");
        if(email != null) {

            editor.putString(SharedPrefUtil.EMAIL_KEY, email);
        }

        String gender = object.getString("gender");
        if(gender != null) {
            editor.putString(SharedPrefUtil.GENDER_KEY, gender);
        }

        if(object.isNull("birthday") == false){
            String birthDay = object.getString("birthday");
            if(birthDay != null) {
                editor.putString(SharedPrefUtil.BIRTH_DAY_KEY, gender);
            }
        }

        editor.apply();
        sendData(object);
    }

    private void sendData(JSONObject object) {
        try {
            object.put("mid", Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
            String query = URLEncoder.encode(object.toString(), "utf-8");
//            String url = "http://10.100.102.9:5000/user?d=" + query;
            String url = "http://52.14.253.14:5000/user?d=" + query;


            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            System.out.println(response);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Something went wrong!");
                    error.printStackTrace();

                }
            });

            VolleySingleton.getInstance(this).addRequestToQueue(stringRequest);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, object,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        System.out.println(response);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });

    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callback.onActivityResult(requestCode, resultCode, data);

    }
}
