package com.urbanmeals.client.urbanmeals.login

import android.app.VoiceInteractor
import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.urbanmeals.client.urbanmeals.services.EndPoints
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class LoginPresenter(val view: Contract, val context: Context) {


    fun login(username: String, password: String) {

        if (username.isEmpty() || password.isEmpty()) {
            if(username.isEmpty())
                view.onUserNameEmpty()
            if(password.isEmpty())
                view.onPasswordEmpty()
        }
        else {
            val loginRequest = object : StringRequest(Request.Method.POST, EndPoints().login, Response.Listener {

                response ->
                validateResponse(response)

            }, Response.ErrorListener {
                view.onNetworkError()
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
    }

    private fun validateResponse(r: String) {
        try {
            val response = JSONObject(r)
            if (response["status"] == "success") {
                val token = response.getString("sessionID")
                view.onSuccess(token)
            } else {
                val errors = response["type"] as JSONArray
                for (i in 0 until errors.length()) {
                    when (errors[i] as Int) {
                        111 -> view.onUserNameError()
                        112 -> view.onPasswordError()
                    }
                }
            }
        } catch (e: JSONException) {

        }
    }

    interface Contract {
        fun onUserNameError()
        fun onPasswordError()
        fun onUserNameEmpty()
        fun onPasswordEmpty()
        fun onSuccess(token: String)
        fun onNetworkError()
    }
}