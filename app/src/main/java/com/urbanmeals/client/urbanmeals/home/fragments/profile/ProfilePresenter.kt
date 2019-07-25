package com.urbanmeals.client.urbanmeals.home.fragments.profile

import android.content.Context
import android.net.Uri
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.urbanmeals.client.urbanmeals.services.EndPoints
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ProfilePresenter(private val token: String?, private val context: Context?, private val view: Contract) {


    fun getProfile(){

        val url = Uri.Builder().apply {
            scheme("http")
            encodedAuthority(EndPoints().profile)
            appendQueryParameter("token", token)
        }.toString()

        val profileRequest = StringRequest(url, Response.Listener {
            response -> validateResponse(response)
        }, Response.ErrorListener {

        })
        Volley.newRequestQueue(context).add(profileRequest)
    }

    private fun validateResponse(res: String){
        try {
            val response = JSONObject(res)

            if(response.getString("status") == "success"){
                val result = response["result"] as JSONObject

                val count = result["count"] as JSONObject
                view.onLabelSuccess("${result["firstName"]} ${result["lastName"]}", count.getString("photos"), count.getString("reviews"))

                //IS Blogger checking
                if(result["blogger"] as String == "Y"){
                    view.isBlogger(true)
                }

                //Setting the bio
                if(result.getString("bio").isNotEmpty()){
                    view.onBioSuccess("About ${result["firstName"]}", result["bio"] as String)
                }

                //Setting the DP
                val url = "http://urbanmeals.in${result.getString("displayPicture")}"
                view.setImage(url)

                //Setting the image list
                val imageList = ArrayList<String>()
                val images = result["images"] as JSONArray
                for(i in 0 until images.length()){
                    val url = "http://urbanmeals.in${images.getString(i)}"
                    imageList.add(url)
                }
                //call the callback only when there is image in the imageList
                if(imageList.size > 0)
                    view.onImagesLoaded(imageList)

            }
            else{
                TODO("CHECK THE UI")
            }
        }
        catch (e: JSONException){
            TODO("IMPLEMENT THIS")
        }
    }

    interface Contract{
        fun onLabelSuccess(name: String, photoCount: String, reviewCount: String)
        fun isBlogger(blogger: Boolean)
        fun onBioSuccess(name: String, bio: String)
        fun setImage(url: String)
        fun onImagesLoaded(list: ArrayList<String>)
    }

}