package com.urbanmeals.client.urbanmeals.home.fragments.profile

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.drawee.view.SimpleDraweeView
import com.urbanmeals.client.urbanmeals.R


class ProfilePhotoListAdapter(private val list: ArrayList<String>) : RecyclerView.Adapter<ProfilePhotoListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.layout_profile_imagelist_item, p0, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.imageView.setImageURI(list[p1])
    }

    override fun getItemCount() = list.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imageView = view.findViewById<SimpleDraweeView>(R.id.Profile_UploadedImagesView)
    }

}
