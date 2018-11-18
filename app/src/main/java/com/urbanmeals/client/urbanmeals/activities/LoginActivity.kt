package com.urbanmeals.client.urbanmeals.activities

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.transition.Fade
import android.view.View
import android.view.Window
import android.widget.*
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.urbanmeals.client.urbanmeals.R

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.HashMap

class LoginActivity : AppCompatActivity() {


    internal var URL = "http://urbanmeals.in/api/1.0/account/login"

    internal lateinit var userNameBox: EditText
    internal lateinit var passwordBox: EditText
    internal lateinit var loginButton: CircularProgressButton

    internal lateinit var tokenPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tokenPreference = getSharedPreferences("credentials", MODE_PRIVATE)
        if (tokenPreference.contains("token")) {
            val i = Intent(this, HomeActivity::class.java)
            //Intent i = new Intent(this, OtpActivity.class);

            startActivity(i)
            finish()
        }
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_login)


        //For setting up the Spannable text on the signup button.
        userNameBox = findViewById(R.id.Login_usernameInput)
        passwordBox = findViewById(R.id.Login_passwordInput)
        loginButton = findViewById(R.id.Login_loginButton)

        val loginText = SpannableString(getString(R.string.signupText))
        loginText.setSpan(ForegroundColorSpan(Color.WHITE), 23, 29, 0)

    }

    fun loginButtonClick(v: View) {
       /* if (v.id == R.id.Login_loginButton) {
            loginButton.text = ""
            val signinrequest = object : StringRequest(Request.Method.POST, URL, Response.Listener { JsonResponse ->
                try {
                    val response = JSONObject(JsonResponse)
                    val status = response.getString("status")
                    if (status == "error") {
                        loginButton.text = getText(R.string.login)
                        val errors = response.getJSONArray("type")
                        if (errors.get(0) as Int == 111) {
                            userNameBox.error = "Email or Phone does not exist"
                        }
                        if (errors.get(0) as Int == 112) {
                            passwordBox.error = "Password is incorrect"
                        }
                    } else if (status == "success") {
                        val editor = tokenPreference.edit()
                        editor.putString("token", response.getString("sessionID"))
                        editor.apply()
                        val i = Intent(applicationContext, HomeActivity::class.java)
                        startActivity(i)
                        finish()
                    }
                } catch (e: JSONException) {

                }
            }, Response.ErrorListener { }) {
                override fun getBodyContentType(): String {
                    return "application/x-www-form-urlencoded; charset=UTF-8"
                }

                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["login"] = userNameBox.text.toString()
                    params["password"] = passwordBox.text.toString()
                    return params
                }
            }
            Volley.newRequestQueue(applicationContext).add(signinrequest)
        }*/
        loginButton.startAnimation()
    }
}
