package com.urbanmeals.client.urbanmeals.Dialogues;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.urbanmeals.client.urbanmeals.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aslampr07 on 24/3/18.
 */

public class HotelRatingDialogue extends Dialog {

    private String hotelCode;
    private String token;
    private ImageView EmojiView;
    private SeekBar ratingBar;

    public HotelRatingDialogue(@NonNull final Context context, final String hotelCode) {
        super(context);
        this.hotelCode = hotelCode;
        setContentView(R.layout.dialogue_hotel_rating);

        SharedPreferences sharedPreferences = context.getSharedPreferences("credentials", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);


        Button submitButton = findViewById(R.id.Rating_SubmitButton);
        EmojiView = findViewById(R.id.Rating_EmojiView);

        final int[] Emojis = {
                R.drawable.emoji001,
                R.drawable.emoji002,
                R.drawable.emoji003,
                R.drawable.emoji004,
                R.drawable.emoji005,
                R.drawable.emoji006,
                R.drawable.emoji007,
                R.drawable.emoji008,
                R.drawable.emoji009,
                R.drawable.emoji010,
                R.drawable.emoji011,
                R.drawable.emoji012,
                R.drawable.emoji013,
                R.drawable.emoji014,
                R.drawable.emoji015,
                R.drawable.emoji016,
                R.drawable.emoji017,
                R.drawable.emoji018,
                R.drawable.emoji019,
                R.drawable.emoji020,
                R.drawable.emoji021,
                R.drawable.emoji022,
                R.drawable.emoji023,
                R.drawable.emoji024,
                R.drawable.emoji025,
                R.drawable.emoji026,
                R.drawable.emoji027,
                R.drawable.emoji028,
                R.drawable.emoji029,
                R.drawable.emoji030,
                R.drawable.emoji031,
                R.drawable.emoji032,
                R.drawable.emoji033,
                R.drawable.emoji034,
                R.drawable.emoji035,
                R.drawable.emoji036,
                R.drawable.emoji037,
                R.drawable.emoji038,
                R.drawable.emoji039,
                R.drawable.emoji040,
                R.drawable.emoji041,
                R.drawable.emoji042,
                R.drawable.emoji043,
                R.drawable.emoji044,
                R.drawable.emoji045,
        };
        ratingBar = findViewById(R.id.Rating_SeekBar);
        //final TextView ratingLabel = findViewById(R.id.Rating_ProgressView);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri.Builder url = new Uri.Builder();
                url.scheme("http")
                        .encodedAuthority("urbanmeals.in/api/1.0/restaurant/rating")
                        .appendQueryParameter("token", token)
                        .build();

                StringRequest submitRating = new StringRequest(Request.Method.POST, url.toString(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        cancel();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Network Error!", Toast.LENGTH_LONG).show();
                    }
                }) {

                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }

                    @Override
                    protected Map<String, String> getParams() {
                        float rating = (ratingBar.getProgress() / 25.00f) + 1;
                        Map<String, String> params = new HashMap<>();
                        params.put("rating", String.valueOf(rating));
                        params.put("hotelcode", hotelCode);
                        return params;
                    }
                };
                Volley.newRequestQueue(context).add(submitRating);
            }
        });
        ratingBar.setProgress(50);
        EmojiView.setImageResource(Emojis[22]);
        ratingBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int index = (int) (progress / 2.2727);
                EmojiView.setImageResource(Emojis[index]);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
