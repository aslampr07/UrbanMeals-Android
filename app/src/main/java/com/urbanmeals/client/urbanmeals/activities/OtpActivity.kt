package com.urbanmeals.client.urbanmeals.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.urbanmeals.client.urbanmeals.R
import java.util.ArrayList

class OtpActivity : AppCompatActivity() {

    private var numbers = ""
    private val textOutputView = ArrayList<TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        textOutputView.add(findViewById(R.id.OTP_TextView1))
        textOutputView.add(findViewById(R.id.OTP_TextView2))
        textOutputView.add(findViewById(R.id.OTP_TextView3))
        textOutputView.add(findViewById(R.id.OTP_TextView4))
    }

    override fun onBackPressed() {

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
                    Toast.makeText(this, "Getting the result", Toast.LENGTH_SHORT).show()
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
}