package com.urbanmeals.client.urbanmeals.Dialogues;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.urbanmeals.client.urbanmeals.R;

import java.util.HashMap;
import java.util.Map;


public class MealRatingDialogue extends Dialog {


    SeekBar tasteSeek;
    SeekBar presentationSeek;
    SeekBar quantitySeek;
    EditText reviewEdit;
    Button submitButton;
    String token;

    public MealRatingDialogue(@NonNull final Context context, final String itemCode) {
        super(context);
        setContentView(R.layout.dialogue_meal_rating);

        tasteSeek = findViewById(R.id.Meal_TasteSeek);
        presentationSeek = findViewById(R.id.Meal_PresentationSeek);
        quantitySeek = findViewById(R.id.Meal_QuantitySeek);
        reviewEdit = findViewById(R.id.Meal_ReviewEdit);
        submitButton = findViewById(R.id.Meal_RatingSubmitButton);
        SharedPreferences sharedPreferences = context.getSharedPreferences("credentials", context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);

        tasteSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float p = (progress / 25.0f) + 1;
                Log.v("UrbanMeals", String.valueOf(p));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri.Builder url = new Uri.Builder();
                url.scheme("http")
                        .encodedAuthority("urbanmeals.in/api/1.0/items/rate")
                        .appendQueryParameter("token", token)
                        .build();
                StringRequest submitRate = new StringRequest(Request.Method.POST, url.toString(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        cancel();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }

                    @Override
                    protected Map<String, String> getParams() {
                        float taste = (tasteSeek.getProgress() / 25.00f) + 1;
                        float presentation = (presentationSeek.getProgress() / 25.00f) + 1;
                        float quantity = (quantitySeek.getProgress() / 25.00f) + 1;
                        String body = reviewEdit.getText().toString();
                        Map<String, String> params = new HashMap<>();
                        params.put("itemcode", itemCode);
                        params.put("taste", String.valueOf(taste));
                        params.put("presentation", String.valueOf(presentation));
                        params.put("quantity", String.valueOf(quantity));
                        params.put("body", body);
                        return params;
                    }
                };

                Volley.newRequestQueue(context).add(submitRate);
            }
        });


    }
}
