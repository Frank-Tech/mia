package com.franktech.mia.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.franktech.mia.R;
import com.franktech.mia.utilities.FacebookInfo;
import com.franktech.mia.utilities.MiaLogger;
import com.franktech.mia.utilities.SharedPrefSingleton;
import com.franktech.mia.utilities.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AbstractAppCompatActivity {

    private static final Class TAG = MainActivity.class;

    private CallbackManager callback;

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
                                        MiaLogger.e(TAG, e.getMessage(), e);
                                    }
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString(FacebookInfo.FIELDS_KEY, FacebookInfo.FIELDS_VALUE);
                    request.setParameters(parameters);
                    request.executeAsync();


                    openMap();
                }

                @Override
                public void onCancel() {
                    MiaLogger.e(TAG, "onCancel");
                }

                @Override
                public void onError(FacebookException error) {
                    MiaLogger.e(TAG, error.getMessage(), error);
                }
            });

        }else{
            openMap();
        }
    }

    private void openMap() {
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void setUserData(JSONObject object) throws JSONException {

        for (String key : FacebookInfo.getInfoKeys()) {

            if (object.isNull(key)) continue;

            String val = object.getString(key);

            if (val == null) continue;

            prefUtil.putString(key, val);

            sendData(object);
        }
    }

    private void sendData(JSONObject object) {
        try {

            object.put("mid", prefUtil.getString(SharedPrefSingleton.UUID_KEY, null));

            String query = URLEncoder.encode(object.toString(), "utf-8");
            String url = getString(R.string.user_url) + query;


            VolleySingleton.getInstance().request(this, url, new VolleySingleton.VolleyCallback() {
                @Override
                public void onSuccess(String response) {
                    MiaLogger.d(TAG, response);
                }

                @Override
                public void onFailed(VolleyError error) {
                    MiaLogger.e(TAG, error.getMessage(), error);

                }
            });

        } catch (UnsupportedEncodingException | JSONException e) {
           MiaLogger.e(TAG, e.getMessage(), e);
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
