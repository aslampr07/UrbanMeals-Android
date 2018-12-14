package com.urbanmeals.client.urbanmeals.signup

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.urbanmeals.client.urbanmeals.services.EndPoints
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SignUpPresenter(val context: Context, val view: Contract) {

    fun signUp(firstName: String, lastName: String, email: String, phone: String, password: String) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty()
                || password.isEmpty()) {
            if (firstName.isEmpty()) {
                view.onFirstNameError("First Name is empty")
            }
            if (lastName.isEmpty()) {
                view.onLastNameError("Last Name is empty")
            }
            if (email.isEmpty()) {
                view.onEmailError("Email is empty")
            }
            if (phone.isEmpty()) {
                view.onPhoneError("Phone is empty")
            }
            if (password.isEmpty()) {
                view.onPasswordError("Password is empty")
            }
        } else {
            val registerRequest = object : StringRequest(Request.Method.POST, EndPoints().signUp, Response.Listener { response ->
                run {
                    val otp = validateResponse(response)
                    if (otp.isNotEmpty()) {
                        view.onSuccess(email, password, otp)
                    }
                }
            }, Response.ErrorListener {

            }) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["firstname"] = firstName
                    params["lastname"] = lastName
                    params["email"] = email
                    params["phone"] = "+91$phone"
                    params["password"] = password
                    return params
                }
            }

            Volley.newRequestQueue(context).add(registerRequest)
        }

    }

    private fun validateResponse(res: String): String {
        try {
            val response = JSONObject(res)
            if (response["status"].toString() == "error") {
                val errors = response["type"] as JSONArray
                for (i in 0 until errors.length()) {
                    when (errors.getInt(i)) {
                        101 -> view.onPhoneError("Account with this phone number already exist")
                        102 -> view.onPhoneError("Please check the phone number")
                        103 -> view.onPasswordError("Please enter more than 6 character")
                        104 -> view.onFirstNameError("Name can only contain alphabets")
                        105 -> view.onLastNameError("Name can only contain alphabets")
                        106 -> view.onEmailError("Account with this email already exist")
                        107 -> view.onEmailError("Please check your email")
                    }
                }
                return ""
            } else if (response["status"].toString() == "success") {
                val OTPtoken = response["token"] as String
                return OTPtoken
            }
        } catch (e: JSONException) {

        }
        return ""
    }

    interface Contract {
        fun onFirstNameError(error: String)
        fun onLastNameError(error: String)
        fun onEmailError(error: String)
        fun onPhoneError(error: String)
        fun onPasswordError(error: String)
        fun onSuccess(login: String, password: String, otpToken: String)
    }
}