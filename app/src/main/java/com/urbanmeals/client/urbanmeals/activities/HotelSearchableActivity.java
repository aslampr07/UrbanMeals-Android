package com.urbanmeals.client.urbanmeals.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.SearchRecentSuggestions;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.urbanmeals.client.urbanmeals.HotelSearchSuggestionProvider;
import com.urbanmeals.client.urbanmeals.R;
import com.urbanmeals.client.urbanmeals.adapters.HotelSearchResultAdapter;
import com.urbanmeals.client.urbanmeals.data.HotelSearchResultItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HotelSearchableActivity extends AppCompatActivity {

    private RecyclerView searchResultList;
    private String token;
    private EditText searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_searchable);

        searchResultList = findViewById(R.id.Search_Recycler);
        searchBox = findViewById(R.id.Search_SearchView);

        searchResultList.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //To save the icon_search suggestions.
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, HotelSearchSuggestionProvider.AUTHORITY, HotelSearchSuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);
            search(query);

        }

        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                search(v.getText().toString());
                return true;
            }
        });
    }

    private void search(String query) {

        Uri.Builder url = new Uri.Builder();
        url.scheme("http")
                .encodedAuthority("urbanmeals.in/api/1.0/restaurant/icon_search/suggestion")
                .appendQueryParameter("query", query)
                .appendQueryParameter("token", token)
                .build();

        StringRequest searchQueryRequest = new StringRequest(url.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String jsonResponse) {
                try {
                    JSONObject response = new JSONObject(jsonResponse);
                    if (response.getString("status").equals("success")) {
                        JSONArray searchResult = response.getJSONArray("result");
                        ArrayList<HotelSearchResultItem> resultList = new ArrayList<>();
                        for (int i = 0; i < searchResult.length(); i++) {
                            HotelSearchResultItem item = new HotelSearchResultItem();
                            JSONObject jsonObject = searchResult.getJSONObject(i);
                            item.setHotelName(jsonObject.getString("name"));
                            item.setPlace(jsonObject.getString("place"));
                            item.setHotelCode(jsonObject.getString("code"));
                            resultList.add(item);
                        }
                        searchResultList.setAdapter(new HotelSearchResultAdapter(resultList));
                    }
                } catch (JSONException e) {
                    Toast.makeText(HotelSearchableActivity.this, "Something not Right", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HotelSearchableActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(searchQueryRequest);
    }
}

