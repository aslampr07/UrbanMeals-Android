package com.urbanmeals.client.urbanmeals.home

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.urbanmeals.client.urbanmeals.R

class LocationPermissionDialog : DialogFragment() {

    private lateinit var okbutton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.dialog_location_permission, container)

        okbutton = view.findViewById(R.id.Dialog_okbutton)
        okbutton.setOnClickListener {
            dismiss()
        }

        return view
    }

}