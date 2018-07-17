package com.urbanmeals.client.urbanmeals;


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


/**
 * A simple {@link Fragment} subclass.
 */
public class PromotionBannerFragment extends Fragment {

    private String hotelCode;
    private ImageView bannerImage;

    public PromotionBannerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_promotion_banner, container, false);

        bannerImage = view.findViewById(R.id.Promotion_BannerImage);

        String url = "http://urbanmeals.in" + getArguments().getString("bannerURL");

        ImageRequest bannerRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                bannerImage.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Failed to Load the Promotions", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(getContext()).add(bannerRequest);

        return view;
    }

}
