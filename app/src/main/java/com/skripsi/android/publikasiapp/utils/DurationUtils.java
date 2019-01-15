package com.skripsi.android.publikasiapp.utils;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.skripsi.android.publikasiapp.app.AppConfig;
import com.skripsi.android.publikasiapp.app.AppController;

import java.util.HashMap;
import java.util.Map;

public class DurationUtils {
    private static final String TAG = "ActivityUtils";

    public static void addDurationToServer(final String email, final String id, final Integer duration){

        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, AppConfig.URL_DURATION, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Berhasil menambahkan Durasi: " + response);

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "gagal menambahkan Durasi"+ error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email",email);
                params.put("id",id);
                params.put("duration",Integer.toString(duration));

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

}
