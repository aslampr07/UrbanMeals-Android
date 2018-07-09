package com.urbanmeals.client.urbanmeals.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.urbanmeals.client.urbanmeals.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {


    String URL = "http://urbanmeals.in/api/1.0/account/login";

    EditText userNameBox;
    EditText passwordBox;
    ProgressBar progressBar;
    Button loginButton;

    SharedPreferences tokenPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        tokenPreference = getSharedPreferences("credentials", MODE_PRIVATE);
        if (tokenPreference.contains("token")) {
            Intent i = new Intent(this, HomeActivity.class);
            //Intent i = new Intent(this, OtpActivity.class);

            startActivity(i);
            finish();
        }
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


        //For setting up the Spannable text on the signup button.
        Button signUpButton = findViewById(R.id.SignUpJumpButton);
        userNameBox = findViewById(R.id.userNameBox);
        passwordBox = findViewById(R.id.passwordBox);
        progressBar = findViewById(R.id.loginProgressBar);
        loginButton = findViewById(R.id.LoginButton);

        SpannableString loginText = new SpannableString(getString(R.string.signupText));
        loginText.setSpan(new ForegroundColorSpan(Color.WHITE), 23, 29, 0);
        signUpButton.setText(loginText, TextView.BufferType.SPANNABLE);

    }

    public void ButtonClicks(View v){
        if(v.getId() == R.id.SignUpJumpButton){
            Intent i = new Intent(this, SingupActivity.class);
            startActivity(i);

        }
        if(v.getId() == R.id.LoginButton){
            progressBar.setVisibility(View.VISIBLE);
            loginButton.setText("");
            StringRequest signinrequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String JsonResponse) {
                    try {
                        JSONObject response = new JSONObject(JsonResponse);
                        String status = response.getString("status");
                        if (status.equals("error")) {
                            progressBar.setVisibility(View.GONE);
                            loginButton.setText(getText(R.string.login));
                            JSONArray errors = response.getJSONArray("type");
                            if ((int) errors.get(0) == 111) {
                                userNameBox.setError("Email or Phone does not exist");
                            }
                            if ((int) errors.get(0) == 112) {
                                passwordBox.setError("Password is incorrect");
                            }
                        } else if (status.equals("success")) {
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

                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("login", userNameBox.getText().toString());
                    params.put("password", passwordBox.getText().toString());
                    return params;
                }
            };
            Volley.newRequestQueue(getApplicationContext()).add(signinrequest);
        }
    }
}
