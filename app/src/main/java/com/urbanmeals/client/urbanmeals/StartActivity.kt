package com.urbanmeals.client.urbanmeals

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.urbanmeals.client.urbanmeals.activities.LoginActivity
import com.urbanmeals.client.urbanmeals.activities.SingupActivity
import kotlin.math.log

class StartActivity : AppCompatActivity() {


    private lateinit var loginButton: Button
    private lateinit var logo: ImageView
    private lateinit var logoText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)


        loginButton = findViewById(R.id.Start_LoginUpButton)
        logo = findViewById(R.id.Start_Logo)
        logoText = findViewById(R.id.Start_LogoText)
    }

    fun startButtonsClick(view: View){
        when(view.id){
            R.id.Start_LoginUpButton -> {
                val i = Intent(this, LoginActivity::class.java)
                val pairLogo = android.support.v4.util.Pair.create<View, String>(logo, "logoTransition")
                val pairLogoText = android.support.v4.util.Pair.create<View, String>(logoText, "logoTextTransition")
                val pairButton = android.support.v4.util.Pair.create<View, String>(loginButton, "loginButtonTransition")

                val option = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairLogo, pairLogoText, pairButton)
                startActivity(i, option.toBundle())
            }
            R.id.Start_SignUpButton -> {
                val i = Intent(this, SingupActivity::class.java)
                startActivity(i)
            }
        }
    }
}