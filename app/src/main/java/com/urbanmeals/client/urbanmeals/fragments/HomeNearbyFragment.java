package com.urbanmeals.client.urbanmeals.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.urbanmeals.client.urbanmeals.R;
import com.urbanmeals.client.urbanmeals.activities.HomeActivity;
import com.urbanmeals.client.urbanmeals.adapters.HomeHotelListAdapter;
import com.urbanmeals.client.urbanmeals.data.HomeHotelItem;
import com.urbanmeals.client.urbanmeals.interfaces.CardListReadyListener;
import com.urbanmeals.client.urbanmeals.interfaces.LocationReadyListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */


public class HomeNearbyFragment extends Fragment {


    private String token;
    private RecyclerView hotelListRecycler;
    private HomeHotelListAdapter hotelListAdapter;
    private ProgressBar mainLoading;

    public HomeNearbyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("credentials", getContext().MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_nearby, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hotelListRecycler = view.findViewById(R.id.Home_HotelListRecycler);
        mainLoading = view.findViewById(R.id.Home_NearbyLoading);
        hotelListRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (hotelListAdapter == null) {
            setCardReadyListener(new CardListReadyListener() {
                @Override
                public void onCompleted(HomeHotelListAdapter adapter) {
                    mainLoading.setVisibility(View.GONE);
                    hotelListAdapter = adapter;
                    hotelListRecycler.setAdapter(adapter);
                }

                @Override
                public void onError(VolleyError error) {
                    Toast.makeText(getActivity(), "Network Error, Please restart the app", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            mainLoading.setVisibility(View.GONE);
            hotelListRecycler.setAdapter(hotelListAdapter);
        }
    }

    private void setCardReadyListener(final CardListReadyListener listener) {
        //LocationReadyListener is a custom listener, No need to google it.
        ((HomeActivity) getActivity()).SetUpLocationListener(new LocationReadyListener() {
            @Override
            public void onResult(Location location) {
                Uri.Builder url = new Uri.Builder();
                url.scheme("http")
                        .encodedAuthority("urbanmeals.in/api/1.0/restaurant/nearby")
                        .appendQueryParameter("lat", String.valueOf(location.getLatitude()))
                        .appendQueryParameter("lon", String.valueOf(location.getLongitude()))
                        .appendQueryParameter("count", String.valueOf(30))
                        .appendQueryParameter("token", token)
                        .build();
                StringRequest hotelListRequest = new StringRequest(Request.Method.GET, url.toString(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String jsonResponse) {
                        try {
                            JSONObject response = new JSONObject(jsonResponse);
                            JSONArray result = response.getJSONArray("result");
                            ArrayList<HomeHotelItem> hotelList = new ArrayList<HomeHotelItem>();

                            //For publishing a warning if the app is not in kochi.
                            if (result.getJSONObject(0).getDouble("distance") > 10) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Oops!")
                                        .setMessage("Looks like you are not in Kochi, We are coming to your location soon!");
                                AlertDialog alert = builder.create();
                                alert.show();
                            }

                            for (int i = 0; i < result.length(); i++) {
                                JSONObject jsonItem = result.getJSONObject(i);
                                HomeHotelItem item = new HomeHotelItem(
                                        jsonItem.getString("name"),
                                        jsonItem.getBoolean("opened"),
                                        jsonItem.getDouble("rating"),
                                        (float) jsonItem.getDouble("distance"),
                                        jsonItem.getString("type"),
                                        jsonItem.getString("code"));
                                hotelList.add(item);
                            }
                            listener.onCompleted(new HomeHotelListAdapter(hotelList));
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), "Unable to parse JSON", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError(error);
                    }
                });
                Volley.newRequestQueue(getActivity()).add(hotelListRequest);
            }
        });
    }

}
