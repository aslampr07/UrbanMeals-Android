package com.urbanmeals.client.urbanmeals.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton
import com.urbanmeals.client.urbanmeals.R
import com.urbanmeals.client.urbanmeals.activities.HomeActivity

class LoginActivity : AppCompatActivity(), LoginPresenter.Contract {


    private lateinit var userNameBox: EditText
    private lateinit var passwordBox: EditText
    private lateinit var loginButton: CircularProgressButton

    private lateinit var presenter: LoginPresenter

    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userNameBox = findViewById(R.id.Login_usernameInput)
        passwordBox = findViewById(R.id.Login_passwordInput)
        loginButton = findViewById(R.id.Login_loginButton)

        preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE)

        presenter = LoginPresenter(this, this)
    }

    //ButtonClick event method, Event listener is added in XML
    fun loginButtonClick(v: View) {
        //Starting the animation
        loginButton.startAnimation()

        val userName = userNameBox.text.toString()
        val password = passwordBox.text.toString()

        //called the presenter method
        presenter.login(userName, password)
    }

    //Implemented methods of the LoginContract
    override fun onUserNameError() {
        loginButton.stopAnimation()
        Toast.makeText(this, "Username error", Toast.LENGTH_SHORT).show()
    }

    override fun onUserNameEmpty() {
        loginButton.revertAnimation()
        Toast.makeText(this, "Username is empty", Toast.LENGTH_SHORT).show()
    }

    override fun onPasswordEmpty() {
        loginButton.revertAnimation()
        Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT).show()
    }

    override fun onPasswordError() {
        loginButton.revertAnimation()
        Toast.makeText(this, "Password error", Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess(token: String) {
        Log.v("urbanmeals", token)
        val editor = preferences.edit()
        editor.putString("token", token)
        editor.apply()
        val i = Intent(this, HomeActivity::class.java)
        startActivity(i)
    }

    override fun onNetworkError() {
        loginButton.stopAnimation()
    }

}
