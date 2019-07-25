package com.urbanmeals.client.urbanmeals.otp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.urbanmeals.client.urbanmeals.R
import com.urbanmeals.client.urbanmeals.home.HomeActivity
import es.dmoral.toasty.Toasty
import java.util.ArrayList

class OtpActivity : AppCompatActivity(), OtpPresenter.Contract {


    private var numbers = ""
    private val textOutputView = ArrayList<TextView>()

    private lateinit var login: String
    private lateinit var password: String
    private lateinit var token: String

    private lateinit var presenter: OtpPresenter

    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_otp)
        textOutputView.add(findViewById(R.id.OTP_TextView1))
        textOutputView.add(findViewById(R.id.OTP_TextView2))
        textOutputView.add(findViewById(R.id.OTP_TextView3))
        textOutputView.add(findViewById(R.id.OTP_TextView4))

        login = intent.extras["login"] as String
        password = intent.extras["password"] as String
        token = intent.extras["token"] as String

        preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE)

        presenter = OtpPresenter(this, this)
    }

    override fun onBackPressed() {
            //This is method is empty to stop the user from going back
    }

    private fun writeTextView(num: String) {
        if (num == "b") {
            //Deleting a character.
            if (numbers.isNotEmpty()) {
                //print the blank character
                textOutputView[numbers.length - 1].text = ""
                //remove the last characters
                numbers = numbers.dropLast(1)
            }
        } else {
            if(numbers.length < 4) {
                numbers += num
                textOutputView[numbers.length - 1].text = numbers.last().toString()
                if (numbers.length == 4) {
                    //calling presenter and verify
                    presenter.verify(numbers, token)
                }
            }
        }

    }

    fun numButtonClick(view: View) {

        when (view.id) {
            R.id.OTP_buttonB -> //Back button is pressed
                writeTextView("b")
            R.id.OTP_buttonSkip -> {
                //Skip button is pressed
            }
            else -> {
                //casting the pressed button.
                val text = (view as Button).text.toString()
                writeTextView(text)
            }
        }
    }

    override fun onOtpSuccess() {
        Toasty.success(this, "Success", Toast.LENGTH_SHORT, true).show()
        presenter.login(login, password)
    }

    override fun onOtpError() {
        Toasty.error(this, "Please check the OTP Again", Toast.LENGTH_LONG, true).show()
    }

    override fun onLoginSuccess(token: String) {
        val editor = preferences.edit()
        editor.putString("token", token)
        editor.apply()

        val i = Intent(this, HomeActivity::class.java)
        startActivity(i)
    }

}