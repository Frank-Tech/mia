package com.franktech.mia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.franktech.mia.MiaApplication;
import com.franktech.mia.R;
import com.franktech.mia.utilities.FacebookInfo;
import com.franktech.mia.utilities.SharedPreSingleton;
import com.franktech.mia.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

public class MainActivity extends AbstractAppCompatActivity {

    private CallbackManager callback;
    private SharedPreSingleton prefUtil = SharedPreSingleton.getInstance(getApplicationContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isLoggedIn()) {

            callback = CallbackManager.Factory.create();

            LoginButton loginButton = findViewById(R.id.open_with_facebook);
            loginButton.setReadPermissions(FacebookInfo.getPermissions());

            loginButton.registerCallback(callback, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {

                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    try {
                                        setUserData(object);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString(FacebookInfo.FIELDS_KEY, FacebookInfo.FIELDS_VALUE);
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

    private void setUserData(JSONObject object) throws JSONException {

        for (String key : FacebookInfo.getInfoKeys()) {

            if (object.isNull(key)) continue;

            String val = object.getString(key);

            if (val == null) continue;

            prefUtil.putString(key, val);

        }
    }

    private void sendData(JSONObject object) {
        try {

            object.put("mid", prefUtil.getString(SharedPreSingleton.UUID_KEY, null));

            String query = URLEncoder.encode(object.toString(), "utf-8");
//            String url = "http://10.100.102.9:5000/user?d=" + query;
            String url = "http://52.14.253.14:5000/user?d=" + query;


            VolleySingleton.getInstance(this).request(url, new VolleySingleton.VolleyCallback() {
                @Override
                public void onSuccess(String response) {

                }

                @Override
                public void onFailed(VolleyError error) {

                }
            });

        } catch (UnsupportedEncodingException | JSONException e) {
            e.printStackTrace();
        }
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
