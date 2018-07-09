package com.urbanmeals.client.urbanmeals.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.urbanmeals.client.urbanmeals.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SingupActivity extends AppCompatActivity {

    EditText emailInput;
    EditText phoneInput;
    EditText passwordInput;
    EditText firstNameInput;
    EditText lastNameInput;
    TextInputLayout passwordLayout;
    TextInputLayout phoneNumberLayout;
    TextInputLayout firstNameLayout;
    TextInputLayout lastNameLayout;
    TextInputLayout emailLayout;
    Button SignUpButton;
    ProgressBar SignUpProgress;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        firstNameInput = findViewById(R.id.SignUp_FirstNameInput);
        lastNameInput = findViewById(R.id.SignUp_LastNameInput);
        emailInput = findViewById(R.id.SignUp_EmailInput);
        phoneInput = findViewById(R.id.SignUp_PhoneInput);
        passwordInput = findViewById(R.id.SignUp_PasswordInput);
        SignUpButton = findViewById(R.id.SignUpButton);
        SignUpProgress = findViewById(R.id.singupProgressBar);
        passwordLayout = findViewById(R.id.SignUp_PasswordLayout);
        coordinatorLayout = findViewById(R.id.SignUp_coordinatorLayout);
        phoneNumberLayout = findViewById(R.id.SignUp_PhoneNumberLayout);
        firstNameLayout = findViewById(R.id.SignUp_FirstNameLayout);
        lastNameLayout = findViewById(R.id.SignUp_LastNameLayout);
        emailLayout = findViewById(R.id.SignUp_EmailLayout);

        //To validate the emailInput ID at the client.
        emailInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!Patterns.EMAIL_ADDRESS.matcher(emailInput.getText()).matches()) {
                        emailInput.setError("Invalid Email");
                    }
                }
            }
        });

        //Phone number validation.
        phoneInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!Patterns.PHONE.matcher(phoneInput.getText()).matches()) {
                        phoneInput.setError("Phone is invalid");
                    }
                }
            }
        });

        //passwordInput validation.
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 7) {
                    passwordLayout.setErrorEnabled(true);
                    passwordLayout.setError("Too Short!");
                } else {
                    passwordLayout.setError(null);
                    passwordLayout.setErrorEnabled(false);
                }
            }
        });

    }


    public void SignUpClick(View v) {
        //Check the validation here.
        if (true) {
            SignUpProgress.setVisibility(View.VISIBLE);
            SignUpButton.setText("");
            String url = "http://urbanmeals.in/api/1.0/account/register";
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String jsonResponse) {
                    try {
                        JSONObject response = new JSONObject(jsonResponse);
                        String status = response.getString("status");

                        //Change to OTP activity only on success.
                        if (status.equals("success")) {
                            Intent i = new Intent(getApplicationContext(), OtpActivity.class);
                            i.putExtra("otpToken", response.getString("token"));
                            i.putExtra("email", emailInput.getText().toString().trim());
                            i.putExtra("password", passwordInput.getText().toString());
                            finishAffinity();
                            startActivity(i);
                            finish();
                        }
                        //The Errors are handled here.
                        else {
                            SignUpProgress.setVisibility(View.GONE);
                            SignUpButton.setText(R.string.signup);
                            JSONArray item = response.getJSONArray("type");
                            for (int i = 0; i < item.length(); i++) {
                                switch (item.getInt(i)) {
                                    case 101:
                                        phoneNumberLayout.setError("This number already exist");
                                        break;
                                    case 104:
                                        firstNameLayout.setError("First name contains illegal characters");
                                        break;
                                    case 105:
                                        lastNameLayout.setError("Last name contain illegal character");
                                        break;
                                    case 106:
                                        emailLayout.setError("This E-mail already exist");
                                        break;
                                    case 107:
                                        emailLayout.setError("Invalid Email");
                                        break;
                                    case 102:
                                        phoneNumberLayout.setError("Invalid Phone number");
                                    default:
                                        continue;
                                }
                            }
                        }
                    } catch (JSONException e) {

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Snackbar.make(coordinatorLayout, "Cannot connect to network", Snackbar.LENGTH_LONG).show();
                    SignUpProgress.setVisibility(View.GONE);
                    SignUpButton.setText(R.string.signup);
                }
            }) {

                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("firstname", firstNameInput.getText().toString().trim());
                    params.put("lastname", lastNameInput.getText().toString().trim());
                    params.put("email", emailInput.getText().toString().trim());
                    params.put("phone", ("+91" + phoneInput.getText().toString()).trim());
                    params.put("password", passwordInput.getText().toString());
                    return params;
                }
            };
            Volley.newRequestQueue(this).add(request);
        }
    }
}
