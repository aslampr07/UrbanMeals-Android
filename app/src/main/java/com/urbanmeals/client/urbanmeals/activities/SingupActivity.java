package com.urbanmeals.client.urbanmeals.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    Button SignUpButton;

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
                } else {
                }
            }
        });

    }

    public void SignUpButtonClick(View v){
        Intent i = new Intent(this, OtpActivity.class);
        startActivity(i);

    }

    public void SignUpClick(View v) {
        //Check the validation here.
        if (true) {
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
                            SignUpButton.setText(R.string.signup);
                            JSONArray item = response.getJSONArray("type");
                            for (int i = 0; i < item.length(); i++) {
                                switch (item.getInt(i)) {
                                    case 101:
                                        break;
                                    case 104:
                                        break;
                                    case 105:
                                        break;
                                    case 106:
                                        break;
                                    case 107:
                                        break;
                                    case 102:
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
