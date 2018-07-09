package com.urbanmeals.client.urbanmeals.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.urbanmeals.client.urbanmeals.R;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class HotelHeaderFragment extends Fragment {

    private TextView nameView;
    private TextView placeView;
    private Button callButton;
    private Button directionButton;

    public HotelHeaderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hotel_header, container, false);

        nameView = view.findViewById(R.id.Hotel_HeaderName);
        placeView = view.findViewById(R.id.Hotel_HeaderPlace);
        callButton = view.findViewById(R.id.Hotel_HeaderCallButton);
        directionButton = view.findViewById(R.id.Hotel_HeaderDirectionButton);

        nameView.setText(getArguments().getString("name"));
        placeView.setText(getArguments().getString("place"));

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        final String latitude = String.valueOf(getArguments().getDouble("latitude"));
        final String longitude = String.valueOf(getArguments().getDouble("longitude"));

        directionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + latitude + "," + longitude));
                startActivity(i);
            }
        });

        return view;
    }


}
