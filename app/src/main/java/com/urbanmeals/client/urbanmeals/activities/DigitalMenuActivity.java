package com.urbanmeals.client.urbanmeals.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.urbanmeals.client.urbanmeals.R;
import com.urbanmeals.client.urbanmeals.adapters.DigitalMenuCategoryAdapter;
import com.urbanmeals.client.urbanmeals.data.DigitalMenuCategoryItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DigitalMenuActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView categoryRecyclerView;
    private RecyclerView digitalMenuRecyclerView;
    private String hotelCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_menu);

        //Received the hotelCode from the previous page.
        hotelCode = getIntent().getExtras().getString("hotelCode");


        toolbar = findViewById(R.id.digitalMenuAppBar);
        categoryRecyclerView = findViewById(R.id.DigitalMenuBottomNavigation);
        digitalMenuRecyclerView = findViewById(R.id.DigitalMenuList);


        digitalMenuRecyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        digitalMenuRecyclerView.animate();

        //Setting the actionbar.
        setActionBar(toolbar);
        toolbar.setTitle(getIntent().getExtras().getString("hotelName"));
        getActionBar().setDisplayHomeAsUpEnabled(true);


        final ArrayList<DigitalMenuCategoryItem> DigitalMenuCategoryList = new ArrayList<>();
        final DigitalMenuCategoryAdapter adapter = new DigitalMenuCategoryAdapter(DigitalMenuCategoryList, hotelCode, digitalMenuRecyclerView);

        Uri.Builder url = new Uri.Builder();
        url.scheme("http")
                .encodedAuthority("urbanmeals.in/api/1.0/items/digitalmenu/categories")
                .appendQueryParameter("hotelcode", hotelCode)
                .build();

        StringRequest getCategoryRequest = new StringRequest(Request.Method.GET, url.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String jsonResponse) {
                try {
                    JSONObject response = new JSONObject(jsonResponse);
                    JSONArray resultItems = response.getJSONArray("result");

                    for (int i = 0; i < resultItems.length(); i++) {
                        JSONObject jsonItem = resultItems.getJSONObject(i);
                        DigitalMenuCategoryItem item = new DigitalMenuCategoryItem(jsonItem.getString("name"), jsonItem.getString("imageURL"));
                        item.setCategoryCode(jsonItem.getString("code"));
                        DigitalMenuCategoryList.add(item);

                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }

        });

        Volley.newRequestQueue(this).add(getCategoryRequest);
        //Set the layout manager for the botton navigationbar;
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
