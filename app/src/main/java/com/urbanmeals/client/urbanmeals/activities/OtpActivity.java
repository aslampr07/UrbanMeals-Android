package com.urbanmeals.client.urbanmeals.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.urbanmeals.client.urbanmeals.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.OffsetTime;
import java.util.HashMap;
import java.util.Map;

public class OtpActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
                /*String URL = "http://urbanmeals.in/api/1.0/account/login";
                StringRequest signInRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String JsonResponse) {
                        try {
                            JSONObject response = new JSONObject(JsonResponse);
                            String status = response.getString("status");
                            if (status.equals("success")) {
                                SharedPreferences tokenPreference = getSharedPreferences("credentials", MODE_PRIVATE);
                                SharedPreferences.Editor editor = tokenPreference.edit();
                                editor.putString("token", response.getString("sessionID"));
                                editor.apply();
                                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(i);
                                finish();
                            }
                        } catch (JSONException e) {

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("login", getIntent().getExtras().getString("email"));
                        params.put("password", getIntent().getExtras().getString("password"));
                        return params;
                    }
                };
                Volley.newRequestQueue(OtpActivity.this).add(signInRequest);
            }*/
    }

    @Override
    public void onBackPressed() {
        //This is to handle the back button press.
        //There is no code here because there is no going back from the OTP Page (The user is trapped!!).
    }

    public void ButtonClick(View v) {/*
        final Editable otp = otpInput.getText();
        final String token = getIntent().getExtras().getString("otpToken");
        final String email = getIntent().getExtras().getString("email");
        final String password = getIntent().getExtras().getString("password");

        String url = "http://urbanmeals.in/api/1.0/account/verify/phone";
        StringRequest otpRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String jsonResponse) {
                try {
                    JSONObject response = new JSONObject(jsonResponse);
                    if (response.getString("status").equals("success")) {
                        //Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        //startActivity(i);
                        //finish();
                        String URL = "http://urbanmeals.in/api/1.0/account/login";
                        StringRequest signInRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String JsonResponse) {
                                try {
                                    JSONObject response = new JSONObject(JsonResponse);
                                    String status = response.getString("status");
                                    if (status.equals("success")) {
                                        SharedPreferences tokenPreference = getSharedPreferences("credentials", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = tokenPreference.edit();
                                        editor.putString("token", response.getString("sessionID"));
                                        editor.apply();
                                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                } catch (JSONException e) {

                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                            @Override
                            public String getBodyContentType() {
                                return "application/x-www-form-urlencoded; charset=UTF-8";
                            }

                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("login", email);
                                params.put("password", password);
                                return params;
                            }
                        };
                        Volley.newRequestQueue(getApplicationContext()).add(signInRequest);
                    } else {

                    }
                } catch (JSONException e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OtpActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("token", token);
                parameters.put("otp", otp.toString());
                return parameters;
            }
        };
        Volley.newRequestQueue(this).add(otpRequest);*/
    }
}

