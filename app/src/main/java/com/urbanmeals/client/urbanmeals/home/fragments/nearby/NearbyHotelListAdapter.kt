package com.urbanmeals.client.urbanmeals.home.fragments.nearby

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.toolbox.ImageLoader
import com.facebook.drawee.view.SimpleDraweeView
import com.urbanmeals.client.urbanmeals.R

class NearbyHotelListAdapter(val list: ArrayList<HotelItem>) : RecyclerView.Adapter<NearbyHotelListAdapter.ViewHolder>() {

    private lateinit var imageLoader: ImageLoader

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.layout_nearbyhotellistdp, p0, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.dpImage.setImageURI("http://urbanmeals.in${list[p1].imageUrl}")
    }

    override fun getItemCount() = list.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val dpImage = view.findViewById<SimpleDraweeView>(R.id.Nearby_HotelDP)
    }
}