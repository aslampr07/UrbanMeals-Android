package com.urbanmeals.client.urbanmeals.signup

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import com.urbanmeals.client.urbanmeals.R
import com.urbanmeals.client.urbanmeals.otp.OtpActivity

class SignUpActivity : AppCompatActivity(), SignUpPresenter.Contract {


    private lateinit var presenter: SignUpPresenter

    private lateinit var firstNameLayout: TextInputLayout
    private lateinit var lastNameLayout: TextInputLayout
    private lateinit var emailLayout: TextInputLayout
    private lateinit var phoneLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout

    private lateinit var firstNameInput: EditText
    private lateinit var lastNameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var phoneInput: EditText
    private lateinit var passwordInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        firstNameLayout = findViewById(R.id.SignUp_FirstNameLayout)
        lastNameLayout = findViewById(R.id.Signup_LastNameLayout)
        emailLayout = findViewById(R.id.Signup_EmailLayout)
        phoneLayout = findViewById(R.id.SingUp_PhoneLayout)
        passwordLayout = findViewById(R.id.Signup_PasswordLayout)

        firstNameInput = findViewById(R.id.SignUp_FirstNameInput)
        lastNameInput = findViewById(R.id.SignUp_LastNameInput)
        emailInput = findViewById(R.id.SignUp_EmailInput)
        phoneInput = findViewById(R.id.SignUp_PhoneInput)
        passwordInput = findViewById(R.id.SignUp_PasswordInput)

        presenter = SignUpPresenter(this, this)
    }

    fun signUpButtonClick(view: View) {
        val firstName = firstNameInput.text.toString()
        val lastName = lastNameInput.text.toString()
        val email = emailInput.text.toString()
        val phone = phoneInput.text.toString()
        val password = passwordInput.text.toString()

        presenter.signUp(firstName, lastName, email, phone, password)
    }

    override fun onFirstNameError(error: String) {
        firstNameLayout.error = error
    }

    override fun onLastNameError(error: String) {
        lastNameLayout.error = error
    }

    override fun onEmailError(error: String) {
        emailLayout.error = error
    }

    override fun onPhoneError(error: String) {
        phoneLayout.error = error
    }

    override fun onPasswordError(error: String) {
        passwordLayout.error = error
    }

    override fun onSuccess(login: String, password: String, otpToken: String) {
        val i = Intent(this, OtpActivity::class.java)
        i.apply {
            putExtra("login", login)
            putExtra("password", password)
            putExtra("token", otpToken)
        }
        startActivity(i)
    }
}