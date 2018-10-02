package com.urbanmeals.client.urbanmeals.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley
import com.urbanmeals.client.urbanmeals.R
import com.urbanmeals.client.urbanmeals.activities.HotelActivity
import com.urbanmeals.client.urbanmeals.data.PromotionBanner
import de.hdodenhof.circleimageview.CircleImageView

class SuggestionHotelIconListAdapter(private val list: List<PromotionBanner>): RecyclerView.Adapter<SuggestionHotelIconListAdapter.ViewHolder>() {


    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_suggestion_hotel_icon_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val url = "http://urbanmeals.in/${list[position].url}"

        val iconRequest = ImageRequest(url, Response.Listener<Bitmap> {
            image -> holder.icon.setImageBitmap(image)
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ALPHA_8, Response.ErrorListener {
          error -> Toast.makeText(holder.icon.context, error.toString(), Toast.LENGTH_LONG).show()
        })

        holder.iconLabel.text = list[position].name

        holder.icon.setOnClickListener{
            val i = Intent(context, HotelActivity::class.java)
            i.putExtra("hotelCode", list[position].hotelCode)
            i.putExtra("hotelName", list[position].name)
            context?.startActivity(i)
        }

        Volley.newRequestQueue(context).add(iconRequest)
    }


    override fun getItemCount(): Int = list.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val icon = view.findViewById<CircleImageView>(R.id.Suggestion_hotelIconImageView)!!
        val iconLabel = view.findViewById<TextView>(R.id.Suggestion_hotelIconLabel)!!
    }
}