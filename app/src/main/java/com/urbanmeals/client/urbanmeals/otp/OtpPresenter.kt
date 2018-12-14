package com.urbanmeals.client.urbanmeals.otp

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.urbanmeals.client.urbanmeals.services.EndPoints
import org.json.JSONException
import org.json.JSONObject

class OtpPresenter(val context: Context, val view: Contract) {

    fun verify(otp: String, token: String) {

        val verifyRequest = object : StringRequest(Request.Method.POST, EndPoints().verify, Response.Listener { response ->
            verifyResponse(response)

        }, Response.ErrorListener {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["token"] = token
                params["otp"] = otp
                return params
            }
        }

        Volley.newRequestQueue(context).add(verifyRequest)

    }

    fun login(username: String, password: String) {
        val loginRequest = object : StringRequest(Request.Method.POST, EndPoints().login, Response.Listener {

            res ->
            run {
                try {
                    val response = JSONObject(res)
                    if (response["status"] as String == "success") {
                        val token = response.getString("sessionID")
                        view.onLoginSuccess(token)
                    }
                } catch (e: JSONException) {

                }
            }

        }, Response.ErrorListener {

        }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["login"] = username
                params["password"] = password
                return params
            }
        }

        Volley.newRequestQueue(context).add(loginRequest)

    }

    private fun verifyResponse(res: String) {
        try {
            val response = JSONObject(res)
            if (response["status"] as String == "success") {
                view.onOtpSuccess()
            } else {
                view.onOtpError()
            }
        } catch (e: JSONException) {
            view.onOtpError()
        }
    }

    interface Contract {
        fun onOtpSuccess()
        fun onOtpError()
        fun onLoginSuccess(token: String)
    }
}