package com.urbanmeals.client.urbanmeals.login

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.Window
import android.widget.*
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton

import com.urbanmeals.client.urbanmeals.R
import com.urbanmeals.client.urbanmeals.activities.HomeActivity

class LoginActivity : AppCompatActivity() {


    internal var URL = "http://urbanmeals.in/api/1.0/account/login"

    private lateinit var userNameBox: EditText
    private lateinit var passwordBox: EditText
    private lateinit var loginButton: CircularProgressButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        //For setting up the Spannable text on the signup button.
        userNameBox = findViewById(R.id.Login_usernameInput)
        passwordBox = findViewById(R.id.Login_passwordInput)
        loginButton = findViewById(R.id.Login_loginButton)
    }

    fun loginButtonClick(v: View) {
        loginButton.startAnimation()
        
    }
}
