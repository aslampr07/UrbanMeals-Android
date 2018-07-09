package com.urbanmeals.client.urbanmeals.fragments;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.urbanmeals.client.urbanmeals.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class HotelHeaderPictureFragment extends Fragment {


    private ImageView hotelImageView;

    public HotelHeaderPictureFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hotel_header_picture, container, false);

        hotelImageView = view.findViewById(R.id.Hotel_HeaderImageView);

        String url = "http://urbanmeals.in" + getArguments().getString("url");

        ImageRequest hotelImageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                hotelImageView.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(getContext()).add(hotelImageRequest);

        return view;
    }

}
