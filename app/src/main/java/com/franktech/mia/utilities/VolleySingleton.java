package com.franktech.mia.utilities;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.franktech.mia.R;

/**
 * Created by tzlilswimmer on 09/11/2017.
 */

public class VolleySingleton extends Volley{

    private static VolleySingleton instance;

    private VolleySingleton() {
    }

    public static VolleySingleton getInstance()
    {
        //if no instance is initialized yet then create new instance
        //else return stored instance
        if (instance == null)
        {
            instance = new VolleySingleton();
        }
        return instance;
    }

    private void addRequestToQueue(Context context, StringRequest stringRequest){
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(context).add(stringRequest);
    }

    private void addRequestToQueue(Context context, JsonObjectRequest jsonObjectRequest){
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    public void request(Context context, String url, final VolleyCallback callback) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getString(R.string.base_url) + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailed(error);
            }
        });

        addRequestToQueue(context, stringRequest);
    }

    public interface VolleyCallback{
        void onSuccess(String response);
        void onFailed(VolleyError error);
    }

}
