package com.urbanmeals.client.urbanmeals.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.urbanmeals.client.urbanmeals.R;
import com.urbanmeals.client.urbanmeals.adapters.HotelHeadFragmentAdapter;
import com.urbanmeals.client.urbanmeals.adapters.RatingFragmentAdapter;
import com.urbanmeals.client.urbanmeals.data.HotelProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HotelActivity extends AppCompatActivity {

    private String token;
    private TabLayout ratingTab;
    private ViewPager ratingPager;
    private TextView hotelPhoneView;
    private TextView hotelTimingView;
    private TextView hotelDescriptionView;
    private TextView hotelAddressView;
    private String hotelName, hotelCode;
    private Button menuButton;
    private Button directionButtonCard;
    private Button openCloseStatus;
    private ScrollView mainScrollView;
    private ProgressBar mainLoadingRing;
    private ViewPager headerViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);

        SharedPreferences sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);

        hotelName = getIntent().getExtras().getString("hotelName");
        hotelCode = getIntent().getExtras().getString("hotelCode");


        ratingTab = findViewById(R.id.Hotel_RatingTab);
        ratingPager = findViewById(R.id.Hotel_RatingPager);
        menuButton = findViewById(R.id.Hotel_MenuButton);
        hotelPhoneView = findViewById(R.id.Hotel_PhoneTextView);
        hotelTimingView = findViewById(R.id.Hotel_TimingView);
        hotelDescriptionView = findViewById(R.id.Hotel_HotelDescriptionView);
        hotelAddressView = findViewById(R.id.Hotel_AddressView);
        directionButtonCard = findViewById(R.id.Hotel_CardDirectionButton);
        mainScrollView = findViewById(R.id.Hotel_MailScrollView);
        mainLoadingRing = findViewById(R.id.Hotel_MainLoadingRing);
        openCloseStatus = findViewById(R.id.Hotel_OpenCloseStatus);
        headerViewPager = findViewById(R.id.Hotel_HeaderViewPager);



        RatingFragmentAdapter ratingAdapter = new RatingFragmentAdapter(getSupportFragmentManager(), hotelCode);
        ratingPager.setAdapter(ratingAdapter);
        ratingTab.setupWithViewPager(ratingPager);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DigitalMenuActivity.class);
                i.putExtra("hotelCode", hotelCode);
                i.putExtra("hotelName", hotelName);
                startActivity(i);
            }
        });
        Uri.Builder url = new Uri.Builder();
        url.scheme("http")
                .encodedAuthority("urbanmeals.in/api/1.0/restaurant/profile")
                .appendQueryParameter("hotelcode", String.valueOf(hotelCode))
                .appendQueryParameter("token", token)
                .build();

        StringRequest profileRequest = new StringRequest(Request.Method.GET, url.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String jsonResponse) {
                try {
                    JSONObject response = new JSONObject(jsonResponse);
                    String status = response.getString("status");
                    if (status.equals("success")) {

                        JSONObject result = response.getJSONObject("result");

                        HotelProfile profile = new HotelProfile();
                        profile.setName(result.getString("name"));
                        profile.setPlace(result.getString("place"));
                        profile.setPhone(result.getString("phone"));
                        JSONArray urljsonArray = result.getJSONArray("images");
                        ArrayList<String> imageList = new ArrayList<>();
                        for (int i = 0; i < urljsonArray.length(); i++) {
                            imageList.add(urljsonArray.getString(i));
                        }
                        profile.setImages(imageList);
                        profile.setLatitude(result.getDouble("latitude"));
                        profile.setLongitude(result.getDouble("longitude"));

                        String timing = result.getString("openingTime") + " to " + result.getString("closingTime");
                        String latitude = String.valueOf(profile.getLatitude());
                        String longitude = String.valueOf(profile.getLongitude());

                        hotelPhoneView.setText(profile.getPhone());
                        hotelTimingView.setText(timing);
                        hotelDescriptionView.setText(result.getString("description"));
                        hotelAddressView.setText(result.getString("address"));

                        hotelPhoneView.setOnClickListener(new callButtonClick(profile.getPhone()));

                        directionButtonCard.setOnClickListener(new directionButtonClick(latitude, longitude));

                        if(!result.getBoolean("isOpened")){
                            openCloseStatus.setText("CLOSED");
                            openCloseStatus.setBackgroundColor(Color.parseColor("#EE1E2C"));
                        }

                        mainScrollView.setForeground(null);
                        mainLoadingRing.setVisibility(View.GONE);

                        headerViewPager.setAdapter(new HotelHeadFragmentAdapter(getSupportFragmentManager(), profile));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(this).add(profileRequest);
    }

    private class callButtonClick implements View.OnClickListener {

        private String phone;

        public callButtonClick(String phone) {
            this.phone = phone;
        }


        @Override
        public void onClick(View v) {
            Intent i = new Intent(Intent.ACTION_CALL);
            i.setData(Uri.parse("tel:" + phone));
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            } else {
                startActivity(i);
            }
        }

    }

    private class directionButtonClick implements View.OnClickListener {

        private String latitude;
        private String longitude;

        public directionButtonClick(String latitude, String longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + latitude + "," + longitude));
            startActivity(i);
        }
    }

}
