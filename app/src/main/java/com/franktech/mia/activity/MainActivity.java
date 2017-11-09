package com.franktech.mia.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = SharedPrefUtil.getSharedPref(getApplicationContext());
        final SharedPreferences.Editor editor = sharedPreferences.edit();

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

    }

    private void setUserData(JSONObject object, SharedPreferences.Editor editor) throws JSONException {
        String email = object.getString("email");
        if(email != null) {
            editor.putString(SharedPrefUtil.EMAIL_KEY, email);
        }

        String gender = object.getString("gender");
        if(gender != null) {
            editor.putString(SharedPrefUtil.GENDER_KEY, gender);
        }

        String birthDay = object.getString("gender");
        if(birthDay != null) {
            editor.putString(SharedPrefUtil.BIRTH_DAY_KEY, gender);
        }

        sendData(object);
    }

    private void sendData(JSONObject object) {
        String url = "http://10.100.102.9:5000/user?d=" + object.toString();


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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callback.onActivityResult(requestCode, resultCode, data);

    }
}
