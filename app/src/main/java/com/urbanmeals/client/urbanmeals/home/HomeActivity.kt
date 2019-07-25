package com.urbanmeals.client.urbanmeals.home

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.cleveroad.sy.cyclemenuwidget.CycleMenuWidget
import com.cleveroad.sy.cyclemenuwidget.OnMenuItemClickListener
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task

import com.urbanmeals.client.urbanmeals.R
import com.urbanmeals.client.urbanmeals.home.fragments.profile.ProfileFragment
import com.urbanmeals.client.urbanmeals.home.fragments.nearby.NearbyFragment


class HomeActivity : AppCompatActivity() {

    val LOCATION_PERMISSION = 12342
    val LOCATION_SETTING = 1343

    private lateinit var menuButton: CycleMenuWidget

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        Fresco.initialize(this)

        menuButton = findViewById(R.id.Home_MenuButton)
        menuButton.setMenuRes(R.menu.homemenu)

        //LocationProvider and locationRequest Initialization
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val permissionDialog = LocationPermissionDialog().apply {
            isCancelable = false
        }
        permissionDialog.show(supportFragmentManager, "Permission")


        supportFragmentManager.beginTransaction().add(R.id.HomeMainWindow, NearbyFragment()).commit()



        menuButton.setOnMenuItemClickListener(object : OnMenuItemClickListener {

            override fun onMenuItemClick(view: View?, itemPosition: Int) {
                when (view?.id) {
                    R.id.Home_NearbyMenuItem -> {
                        supportFragmentManager.beginTransaction().replace(R.id.HomeMainWindow, NearbyFragment()).addToBackStack(null).commit()
                        menuButton.close(true)
                    }
                    R.id.Home_ProfileMenuItem -> {
                        supportFragmentManager.beginTransaction().replace(R.id.HomeMainWindow, ProfileFragment()).addToBackStack(null).commit()
                        menuButton.close(true)
                    }
                }
            }

            override fun onMenuItemLongClick(view: View?, itemPosition: Int) {
                //Item Long Click
            }
        })

    }


    private fun enableLocation(){

    }

    private fun iskPermissionEnabled(): Boolean {
        val fine = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        val course = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED

        return fine && course
    }

    private fun isGPSEnabled() : Boolean {
        return false
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION)
    }

    private fun createLocationRequest() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())
        executeTask(task)
    }

    private fun executeTask(task: Task<LocationSettingsResponse>) {
        task.addOnSuccessListener {

        }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(this, LOCATION_SETTING)
                } catch (sendEx: IntentSender.SendIntentException) {
                    //do nothing
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_PERMISSION -> {
                if (permissions.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission is granted
                    Toast.makeText(this, "Permission is granted", Toast.LENGTH_LONG).show()
                    createLocationRequest()
                } else {
                    Toast.makeText(this, "Sorry this app cannot work without proper permission", Toast.LENGTH_LONG).show()
                    TODO("Add a custom UI for educating permission")
                }
            }
        }
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            LOCATION_SETTING -> {
                if (resultCode == Activity.RESULT_OK) {
                    //GPS has been enabled!
                } else {
                    TODO("GPS Have not been enabled, place a prompt here")
                }
            }
        }
        //super.onActivityResult(requestCode, resultCode, data)
    }
}