package com.urbanmeals.client.urbanmeals.fragments;


import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.urbanmeals.client.urbanmeals.Dialogues.HotelRatingDialogue;
import com.urbanmeals.client.urbanmeals.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class OverallRatingFragment extends Fragment {


    private PieChart overallPie;

    public OverallRatingFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overall_rating, container, false);

        Button rateButton = view.findViewById(R.id.Overall_SubmitButton);
        overallPie = view.findViewById(R.id.Overall_PieChart);
        overallPie.getDescription().setEnabled(false);
        overallPie.getLegend().setEnabled(false);
        overallPie.setHoleRadius(70);
        overallPie.setCenterTextSize(25);
        overallPie.setTouchEnabled(false);

        Bundle bundle = getArguments();
        final String hotelCode = bundle.getString("hotelCode");

        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HotelRatingDialogue ratingDialogue = new HotelRatingDialogue(getContext(), hotelCode);
                ratingDialogue.show();
            }
        });


        Uri.Builder url = new Uri.Builder();
        url.scheme("http")
                .encodedAuthority("urbanmeals.in/api/1.0/restaurant/rating")
                .appendQueryParameter("hotelcode", getArguments().getString("hotelCode"))
                .build();

        StringRequest ratingRequest = new StringRequest(Request.Method.GET, url.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String jsonResponse) {
                try {
                    JSONObject response = new JSONObject(jsonResponse);
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        Double rating = response.getDouble("rating");
                        ArrayList<PieEntry> Entrylist = new ArrayList<>();
                        Entrylist.add(new PieEntry(rating.floatValue(), 0));
                        Entrylist.add(new PieEntry(5f - rating.floatValue(), 1));
                        PieDataSet dataSet = new PieDataSet(Entrylist, "");
                        ArrayList<Integer> colors = new ArrayList<>();
                        colors.add(getResources().getColor(R.color.urbanMealsRed));
                        colors.add(Color.parseColor("#f3f4f7"));
                        dataSet.setColors(colors);
                        dataSet.setDrawValues(false);
                        PieData data = new PieData(dataSet);
                        overallPie.setData(data);
                        overallPie.setCenterText(String.valueOf(rating));
                        overallPie.animateX(1000);
                    }

                } catch (JSONException e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(getContext()).add(ratingRequest);
        return view;
    }

}
