package com.urbanmeals.client.urbanmeals.fragments;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.urbanmeals.client.urbanmeals.R;
import com.urbanmeals.client.urbanmeals.activities.HomeActivity;
import com.urbanmeals.client.urbanmeals.adapters.SuggestionMainListAdapter;
import com.urbanmeals.client.urbanmeals.data.SuggestionMainListItem;
import com.urbanmeals.client.urbanmeals.data.SuggestionSubListItem;
import com.urbanmeals.client.urbanmeals.interfaces.LocationReadyListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeSuggestionsFragment extends Fragment {


    String token;
    RecyclerView suggestionMainRecycler;

    ProgressBar mainLoader;

    public HomeSuggestionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_suggestions, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("credentials", getContext().MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        suggestionMainRecycler = view.findViewById(R.id.Suggestion_MainRecycler);
        mainLoader = view.findViewById(R.id.Suggestion_MainLoading);

        suggestionMainRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        ((HomeActivity)getActivity()).SetUpLocationListener(new LocationReadyListener() {
            @Override
            public void onResult(Location location) {
                Uri.Builder url = new Uri.Builder();
                url.scheme("http")
                        .encodedAuthority("urbanmeals.in/api/1.0/items/suggestion")
                        .appendQueryParameter("token", token)
                        .appendQueryParameter("lat", String.valueOf(location.getLatitude()))
                        .appendQueryParameter("lon", String.valueOf(location.getLongitude()))
                        .build();
                StringRequest suggestionRequest = new StringRequest(url.toString(), new Response.Listener<String>() {
/*
        Example Response:
        {
            "place": "Kalamassery",
            "item": [
                {
                    "name": "Chicken Biriyani",
                    "code": "QBrq6Qg4OZm",
                    "price": 110,
                    "hotel": "Biriyani Chembu"
                },
                {
                    "name": "Beef Biriyani",
                    "code": "VMW4KQ2k8G5",
                    "price": 90,
                    "hotel": "Biriyani Chembu"
                },
                {
                    "name": "Chicken Biriyani Half",
                    "code": "A50kR8Z4D9O",
                    "price": 70,
                    "hotel": "Biriyani Chembu"
                }
            ]
        }
                     */

                    @SuppressLint("NewApi")
                    @Override
                    public void onResponse(String jsonResponse) {
                        try {
                            JSONObject response = new JSONObject(jsonResponse);
                            String status = response.getString("status");
                            if(status.equals("success")){
                                //Please see the above response example to understand this.
                                //This is returns a ArrayList in ArrayList.
                                JSONArray mainList = response.getJSONArray("result");
                                ArrayList<SuggestionMainListItem> placeList = new ArrayList<>();
                                for(int i = 0; i < mainList.length(); i++){
                                    SuggestionMainListItem suggestionMainListItem = new SuggestionMainListItem();
                                    JSONObject item = mainList.getJSONObject(i);
                                    suggestionMainListItem.setPlace(item.getString("place"));

                                    JSONArray itemarray = item.getJSONArray("item");
                                    ArrayList<SuggestionSubListItem> subListItem = new ArrayList<>();
                                    for (int j = 0; j < itemarray.length(); j++){
                                        JSONObject meals = itemarray.getJSONObject(j);
                                        SuggestionSubListItem suggestionSubListItem = new SuggestionSubListItem();
                                        suggestionSubListItem.setName(meals.getString("name"));
                                        suggestionSubListItem.setItemCode(meals.getString("code"));
                                        suggestionSubListItem.setPrice(meals.getDouble("price"));
                                        suggestionSubListItem.setHotelName(meals.getString("hotel"));
                                        suggestionSubListItem.setItemCode(meals.getString("code"));
                                        subListItem.add(suggestionSubListItem);
                                    }
                                    suggestionMainListItem.setSuggestionSubList(subListItem);
                                    placeList.add(suggestionMainListItem);
                                    mainLoader.setVisibility(View.GONE);
                                }
                                suggestionMainRecycler.setAdapter(new SuggestionMainListAdapter(placeList));
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Unable to parse JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                Volley.newRequestQueue(getContext()).add(suggestionRequest);
            }
        });
    }
}
