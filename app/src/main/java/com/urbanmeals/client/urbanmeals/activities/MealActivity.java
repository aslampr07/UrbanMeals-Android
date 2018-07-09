package com.urbanmeals.client.urbanmeals.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.urbanmeals.client.urbanmeals.Dialogues.MealRatingDialogue;
import com.urbanmeals.client.urbanmeals.R;
import com.urbanmeals.client.urbanmeals.adapters.MealPhotoThumbnailAdapter;
import com.urbanmeals.client.urbanmeals.adapters.MealReviewFragmentAdapter;
import com.urbanmeals.client.urbanmeals.data.MealProfileThumbnails;
import com.urbanmeals.client.urbanmeals.tools.PieGraphTool;
import com.yalantis.ucrop.UCrop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class MealActivity extends AppCompatActivity {

    private static final int VIEWGALLERY = 123;
    Button ratingDialogueOpenButton;
    ImageButton addImageButton;
    TabLayout reviewTab;
    ViewPager reviewViewPager;
    TextView mealNameView;
    TextView hotelNameView;
    TextView priceView;
    PieChart tasteChart;
    PieChart presentationChart;
    PieChart quantityChart;
    RecyclerView photoRecycler;
    ProgressBar mainProgressRing;
    ScrollView mainScrollView;
    private String itemCode;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);
        SharedPreferences sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);
        itemCode = getIntent().getExtras().getString("itemCode");

        reviewTab = findViewById(R.id.Meal_ReviewTab);
        reviewViewPager = findViewById(R.id.Meal_RatingViewpager);
        mealNameView = findViewById(R.id.Meal_MealName);
        hotelNameView = findViewById(R.id.Meal_HotelName);
        priceView = findViewById(R.id.Meal_PriceView);
        tasteChart = findViewById(R.id.Meal_TasteGraph);
        presentationChart = findViewById(R.id.Meal_PresentationGraph);
        quantityChart = findViewById(R.id.Meal_QuantityGraph);
        addImageButton = findViewById(R.id.Meal_AddPhotoButton);
        ratingDialogueOpenButton = findViewById(R.id.Meal_SubmitRatingButton);
        photoRecycler = findViewById(R.id.Meal_PhotoList);
        mainProgressRing = findViewById(R.id.Meal_MainLoadingRing);
        mainScrollView = findViewById(R.id.Meal_MainScrollView);

        tasteChart.getDescription().setEnabled(false);
        tasteChart.getLegend().setEnabled(false);
        tasteChart.setHoleRadius(70);
        tasteChart.setCenterTextSize(13);
        tasteChart.setTouchEnabled(false);
        tasteChart.setNoDataText("");
        presentationChart.getDescription().setEnabled(false);
        presentationChart.getLegend().setEnabled(false);
        presentationChart.setHoleRadius(70);
        presentationChart.setCenterTextSize(13);
        presentationChart.setTouchEnabled(false);
        presentationChart.setNoDataText("");
        quantityChart.getDescription().setEnabled(false);
        quantityChart.getLegend().setEnabled(false);
        quantityChart.setHoleRadius(70);
        quantityChart.setCenterTextSize(13);
        quantityChart.setTouchEnabled(false);
        quantityChart.setNoDataText("");

        //Setting up the Photo RecyclerView

        photoRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //Setting up the tab for the Reviews
        reviewViewPager.setAdapter(new MealReviewFragmentAdapter(getSupportFragmentManager(), itemCode));
        reviewTab.setupWithViewPager(reviewViewPager);


        Uri.Builder url = new Uri.Builder();
        url.scheme("http")
                .encodedAuthority("urbanmeals.in/api/1.0/items/meal")
                .appendQueryParameter("token", token)
                .appendQueryParameter("itemcode", itemCode)
                .build();
        StringRequest mealProfileRequest = new StringRequest(Request.Method.GET, url.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String JsonResponse) {
                try {
                    JSONObject response = new JSONObject(JsonResponse);
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        JSONObject result = response.getJSONObject("result");
                        mealNameView.setText(result.getString("name"));
                        hotelNameView.setText(result.getString("hotel"));

                        JSONArray prices = result.getJSONArray("price");
                        String price = "";
                        for (int i = 0; i < prices.length(); i++) {
                            String description = prices.getJSONObject(i).getString("description");
                            Double amount = prices.getJSONObject(i).getDouble("amount");

                            price = price + "₹ " + amount + " " + description + " ";
                        }
                        priceView.setText(price);
                        mainProgressRing.setVisibility(View.GONE);
                        mainScrollView.setForeground(null);
                    }
                } catch (JSONException e) {
                    Toast.makeText(MealActivity.this, "Error Parsing Json", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MealActivity.this, error.getStackTrace().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(mealProfileRequest);
        setRating();
        setThumbnails();
    }

    public void ButtonClick(View view) {
        if (view.getId() == ratingDialogueOpenButton.getId()) {
            MealRatingDialogue mealRatingDialogue = new MealRatingDialogue(this, itemCode);
            mealRatingDialogue.show();
        }
        if (view.getId() == addImageButton.getId()) {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, VIEWGALLERY);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VIEWGALLERY && resultCode == RESULT_OK) {
            UCrop.Options cropOptions = new UCrop.Options();
            cropOptions.setCompressionFormat(Bitmap.CompressFormat.JPEG);
            cropOptions.setFreeStyleCropEnabled(true);
            UCrop.of(data.getData(), Uri.fromFile(new File(getCacheDir(), "item.jpg")))
                    .withOptions(cropOptions)
                    .start(MealActivity.this);
        }
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(UCrop.getOutput(data));
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bytes = stream.toByteArray();
                bitmap.recycle();
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("image", "photo.jpg", RequestBody.create(MediaType.parse("image/jpeg"), bytes))
                        .build();
                HttpUrl url = new HttpUrl.Builder()
                        .scheme("http")
                        .host("urbanmeals.in")
                        .addPathSegments("api/1.0/items/photo/upload")
                        .addEncodedQueryParameter("token", token)
                        .addEncodedQueryParameter("itemcode", itemCode)
                        .build();
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                Toast.makeText(this, "Uploading Image", Toast.LENGTH_SHORT).show();
                client.newCall(request).enqueue(new Callback() {
                    //To change the thread to main thread on the responses.
                    Handler mainHandler = new Handler(getMainLooper());

                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.v("urbanmeals", e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MealActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                                setThumbnails();
                            }
                        });
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void setRating() {
        Uri.Builder url = new Uri.Builder();
        url.scheme("http")
                .encodedAuthority("urbanmeals.in/api/1.0/items/rating")
                .appendQueryParameter("token", token)
                .appendQueryParameter("itemcode", itemCode)
                .build();
        StringRequest ratingRequest = new StringRequest(Request.Method.GET, url.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String jsonResponse) {
                try {
                    JSONObject response = new JSONObject(jsonResponse);
                    String status = response.getString("status");
                    if (status.equals("success")) {


                        Double tRating = response.getDouble("taste");
                        Double pRating = response.getDouble("presentation");
                        Double qRating = response.getDouble("quantity");

                        PieData data = PieGraphTool.SetPieGraphData(tRating.floatValue(), 5f, Color.parseColor("#22c552"));
                        tasteChart.setData(data);
                        tasteChart.setCenterText(String.format("%.2f", tRating));
                        tasteChart.animateX(1000);

                        data = PieGraphTool.SetPieGraphData(pRating.floatValue(), 5f, Color.parseColor("#faea01"));
                        presentationChart.setData(data);
                        presentationChart.setCenterText(String.format("%.2f", pRating));
                        presentationChart.animateX(1000);

                        data = PieGraphTool.SetPieGraphData(qRating.floatValue(), 5f, Color.parseColor("#d72f2f"));
                        quantityChart.setData(data);
                        quantityChart.setCenterText(String.format("%.2f", qRating));
                        quantityChart.animateX(1000);


                    } else {
                        Toast.makeText(MealActivity.this, "Something happened our end! Oops", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(MealActivity.this, "Error Parsing JSON", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MealActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(MealActivity.this).add(ratingRequest);
    }

    public void setThumbnails() {
        Uri.Builder url = new Uri.Builder();
        url.scheme("http")
                .encodedAuthority("urbanmeals.in/api/1.0/items/thumbnails")
                .appendQueryParameter("token", token)
                .appendQueryParameter("itemcode", itemCode)
                .build();
        StringRequest thumbnailRequest = new StringRequest(Request.Method.GET, url.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String JsonResponse) {
                try {
                    JSONObject response = new JSONObject(JsonResponse);
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        ArrayList<MealProfileThumbnails> thumbnailList = new ArrayList<>();
                        JSONArray itemList = response.getJSONArray("result");
                        for (int i = 0; i < itemList.length(); i++) {
                            JSONObject item = itemList.getJSONObject(i);
                            MealProfileThumbnails thumbnail = new MealProfileThumbnails(item.getString("url"), item.getString("code"));
                            thumbnailList.add(thumbnail);
                        }
                        photoRecycler.setAdapter(new MealPhotoThumbnailAdapter(thumbnailList));
                    }

                } catch (JSONException e) {
                    Toast.makeText(MealActivity.this, "Cannot Parse JSON", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MealActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(this).add(thumbnailRequest);

    }
}