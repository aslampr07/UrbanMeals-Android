package com.urbanmeals.client.urbanmeals.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.urbanmeals.client.urbanmeals.R;
import com.urbanmeals.client.urbanmeals.adapters.MealReviewListAdapter;
import com.urbanmeals.client.urbanmeals.data.MealReviewItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MealReviewFragment extends Fragment {

    private boolean isCritic;
    private RecyclerView mealReviewRecycler;
    private Context context;

    public MealReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meal_reviews, container, false);
        context = getContext();
        mealReviewRecycler = view.findViewById(R.id.Meal_ReviewList);

        mealReviewRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("credentials", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        isCritic = getArguments().getBoolean("isCritic");
        String itemCode = getArguments().getString("itemcode");
        String path;
        if(isCritic){
            path = "urbanmeals.in/api/1.0/items/reviews/critic";
        }
        else {
            path = "urbanmeals.in/api/1.0/items/reviews/user";
        }

        Uri.Builder url = new Uri.Builder();
        url.scheme("http")
                .encodedAuthority(path)
                .appendQueryParameter("token",token)
                .appendQueryParameter("itemcode",itemCode)
                .build();

        StringRequest reviewRequest = new StringRequest(Request.Method.GET, url.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String jsonResponse) {
                try {
                    JSONObject response = new JSONObject(jsonResponse);
                    String status = response.getString("status");
                    if(status.equals("success")){
                        ArrayList<MealReviewItem> reviewList = new ArrayList<>();
                        JSONArray reviews = response.getJSONArray("result");
                        for(int i = 0; i < reviews.length(); i++){
                            MealReviewItem reviewItem = new MealReviewItem();
                            JSONObject item = reviews.getJSONObject(i);
                            reviewItem.setUserName(item.getString("firstname"), item.getString("lastname"));
                            reviewItem.setTaste(item.getDouble("taste"));
                            reviewItem.setPresentation(item.getDouble("presentation"));
                            reviewItem.setPresentation(item.getDouble("quantity"));
                            reviewItem.setReviewBody(item.getString("body"));
                            reviewList.add(reviewItem);
                        }
                        mealReviewRecycler.setAdapter(new MealReviewListAdapter(reviewList));
                    }
                }
                catch (JSONException e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(getContext()).add(reviewRequest);
        return view;

    }
}
