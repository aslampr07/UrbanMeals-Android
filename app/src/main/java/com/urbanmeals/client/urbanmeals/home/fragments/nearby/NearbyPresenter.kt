package com.urbanmeals.client.urbanmeals.home.fragments.nearby

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.urbanmeals.client.urbanmeals.services.EndPoints
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class NearbyPresenter(private val context: Context?, val view: Contract) {

    private lateinit var list : ArrayList<HotelItem>

    fun getInitialList(lat: Float, lon: Float) {

        val url = Uri.Builder().apply {
            scheme("http")
            encodedAuthority(EndPoints().nearbyList)
            appendQueryParameter("ver", "2.0")
            appendQueryParameter("lat", lat.toString())
            appendQueryParameter("lon", lon.toString())
            appendQueryParameter("start", "0")
            appendQueryParameter("end", "30")
        }.build()

        val listRequest = StringRequest(url.toString(), Response.Listener { response ->
            buildList(response)

        }, Response.ErrorListener {
            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
        })
        Volley.newRequestQueue(context).add(listRequest)
    }

    fun getHotelItem(position: Int){
        view.onHotelItemReceived(list[position])
    }

    private fun buildList(res: String) {
        try {
            val response = JSONObject(res)
            if (response["success"] as Boolean) {
                val resultList = response["result"] as JSONArray
                list = ArrayList()
                for (i in 0 until resultList.length()) {
                    val jsonItem = resultList[i] as JSONObject
                    val item = HotelItem(
                            jsonItem["name"] as String,
                            jsonItem["code"] as String,
                            jsonItem["url"] as String,
                            jsonItem["distance"] as Double,
                            jsonItem["place"] as String,
                            String.format("%.2f", jsonItem.getDouble("rating")), //This is a hack, when 0 is in jSon it is converted to integer
                            jsonItem["phone"] as String,
                            jsonItem["latitude"] as Double,
                            jsonItem["longitude"] as Double,
                            jsonItem["description"] as String
                    )
                    list.add(item)
                }
                view.onSuccess(list)
            } else {
                TODO("Implement the error code")
            }
        } catch (e: JSONException) {
            TODO("Implement the exception for the JSON")
        }
    }

    interface Contract {
        fun onSuccess(list: ArrayList<HotelItem>)
        fun onHotelItemReceived(hotel: HotelItem)
    }
}