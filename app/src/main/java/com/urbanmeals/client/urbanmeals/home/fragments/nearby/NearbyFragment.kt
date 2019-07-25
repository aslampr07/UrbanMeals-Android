package com.urbanmeals.client.urbanmeals.home.fragments.nearby


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextSwitcher
import android.widget.TextView
import com.ramotion.cardslider.CardSliderLayoutManager
import com.ramotion.cardslider.CardSnapHelper

import com.urbanmeals.client.urbanmeals.R
import com.urbanmeals.client.urbanmeals.StartActivity
import com.urbanmeals.client.urbanmeals.activities.DigitalMenuActivity

class NearbyFragment : Fragment(), NearbyPresenter.Contract {


    private lateinit var mainRecyclerView: RecyclerView
    private lateinit var hotelName: TextView
    private lateinit var hotelPlace: TextSwitcher
    private lateinit var hotelDescription: TextView
    private lateinit var hotelDistance: TextView
    private lateinit var hotelRating: TextView
    private lateinit var menuButton: ImageButton
    private lateinit var directionButton: ImageButton

    private lateinit var presenter: NearbyPresenter
    private lateinit var layoutManager: CardSliderLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        presenter = NearbyPresenter(context, this)
        val view = inflater.inflate(R.layout.fragment_nearby, container, false)

        mainRecyclerView = view.findViewById(R.id.Nearby_MainRecyler)
        hotelName = view.findViewById(R.id.Nearby_HotelNameLabel)
        hotelPlace = view.findViewById(R.id.Nearby_PlaceLabel)
        hotelDescription = view.findViewById(R.id.Nearby_HotelDescription)
        hotelDistance = view.findViewById(R.id.Nearby_DistanceLabel)
        hotelRating = view.findViewById(R.id.Nearby_HotelRatingLabel)
        menuButton = view.findViewById(R.id.Nearby_MenuButton)
        directionButton = view.findViewById(R.id.Nearby_NavigationButton)

        layoutManager = CardSliderLayoutManager(100, 500, 30f)

        mainRecyclerView.layoutManager = layoutManager
        CardSnapHelper().attachToRecyclerView(mainRecyclerView)

        presenter.getInitialList(10f, 76f)

        hotelPlace.outAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
        hotelPlace.inAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)

        mainRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                //if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    presenter.getHotelItem(layoutManager.activeCardPosition)
                //}
            }
        })
        return view
    }


    override fun onSuccess(list: ArrayList<HotelItem>) {
        mainRecyclerView.adapter = NearbyHotelListAdapter(list)
    }

    override fun onHotelItemReceived(hotel: HotelItem) {
        hotelName.text = hotel.name
        hotelPlace.setText(hotel.place)
        hotelDescription.text = hotel.description
        hotelDistance.text = "${hotel.distance} KM"
        hotelRating.text = hotel.rating.toString()

        menuButton.setOnClickListener {
            val i = Intent(context, DigitalMenuActivity::class.java)
            i.putExtra("hotelCode", hotel.code)
            context?.startActivity(i)
        }

        directionButton.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=${hotel.lat},${hotel.lon}"))
            startActivity(i)
        }
    }
}
