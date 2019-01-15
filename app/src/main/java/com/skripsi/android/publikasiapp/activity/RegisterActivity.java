package com.skripsi.android.publikasiapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


import com.skripsi.android.publikasiapp.R;
import com.skripsi.android.publikasiapp.helper.SessionManager;
import com.skripsi.android.publikasiapp.app.AppController;
import com.skripsi.android.publikasiapp.app.AppConfig;
import com.skripsi.android.publikasiapp.helper.UserPreferences;


/**
 * Created by Harits on 2/26/2018.
 */

public class RegisterActivity extends Activity{
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private Spinner inputPekerjaan;
    private Spinner inputPendidikanTerakhir;
    private EditText inputPassword;
    private ArrayAdapter<CharSequence> adapterPekerjaan;
    private ArrayAdapter<CharSequence> adapterPTerakhir;
    private ProgressDialog pDialog;
    private SessionManager session;
    private UserPreferences userPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        inputPekerjaan = (Spinner)findViewById(R.id.pekerjaan);
        inputPendidikanTerakhir = (Spinner)findViewById(R.id.pendidikan_terakhir);
        adapterPekerjaan = ArrayAdapter.createFromResource(this,R.array.pekerjaan, android.R.layout.simple_spinner_item);
        adapterPekerjaan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterPTerakhir = ArrayAdapter.createFromResource(this,R.array.pendidikan_terakhir, android.R.layout.simple_spinner_item);
        adapterPTerakhir.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        inputPekerjaan.setAdapter(adapterPekerjaan);
        inputPendidikanTerakhir.setAdapter(adapterPTerakhir);


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());


        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String pekerjaan = inputPekerjaan.getSelectedItem().toString();
                String pendidikan_terakhir = inputPendidikanTerakhir.getSelectedItem().toString();


                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !pekerjaan.isEmpty() && !pendidikan_terakhir.isEmpty()) {
                    Log.d(TAG, "onClick: register");
                    registerUser(name, email, password, pekerjaan, pendidikan_terakhir);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String name, final String email,
                              final String password, final String pekerjaan, final String pendidikan_terakhir) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Log.d(TAG, "onResponse: tak eror");
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");
                        String pekerjaan = user.getString("pekerjaan");
                        String pendidikan_terakhir = user.getString("pendidikan_terakhir");

                        // Inserting row in users table
//                        userPreferences.setUserPref(name, email, pekerjaan, pendidikan_terakhir);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("pekerjaan", pekerjaan);
                params.put("pendidikan_terakhir", pendidikan_terakhir);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
