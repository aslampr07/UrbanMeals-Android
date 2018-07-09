package com.urbanmeals.client.urbanmeals.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.urbanmeals.client.urbanmeals.R;
import com.yalantis.ucrop.UCrop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class EditProfileActivity extends AppCompatActivity {

    private static final int VIEWGALLERY = 3421;
    private ImageButton cancelButton;
    private ImageButton saveButton;
    private EditText firstNameEdit;
    private EditText lastNameEdit;
    private EditText websiteEdit;
    private EditText bioEdit;
    private String token;
    private CircleImageView dpImageView;
    private Button changeImageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        SharedPreferences sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);

        cancelButton = findViewById(R.id.Edit_Profile_CancelEditButton);
        saveButton = findViewById(R.id.Edit_Profile_SaveEditButton);
        firstNameEdit = findViewById(R.id.Edit_Profile_FirstNameEdit);
        lastNameEdit = findViewById(R.id.Edit_Profile_LastNameEdit);
        websiteEdit = findViewById(R.id.Edit_Profile_WebsiteEdit);
        bioEdit = findViewById(R.id.Edit_Profile_BioEdit);
        dpImageView = findViewById(R.id.Edit_Profile_DPImage);
        changeImageButton = findViewById(R.id.Edit_Profile_ChangePhotoButton);


        Uri.Builder url = new Uri.Builder();
        url.scheme("http")
                .encodedAuthority("urbanmeals.in/api/1.0/profile/")
                .appendQueryParameter("token", token)
                .build();
        StringRequest setFieldRequest = new StringRequest(url.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String jsonResponse) {
                try {
                    JSONObject response = new JSONObject(jsonResponse);
                    firstNameEdit.setText(response.getString("firstName"));
                    lastNameEdit.setText(response.getString("lastName"));
                    websiteEdit.setText(response.getString("website"));
                    bioEdit.setText(response.getString("bio"));

                    String url = "http://urbanmeals.in" + response.getString("displayPicture");

                    ImageRequest dpRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            dpImageView.setImageBitmap(response);
                        }
                    }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(EditProfileActivity.this, "dp loading failed", Toast.LENGTH_SHORT).show();
                        }
                    });

                    Volley.newRequestQueue(EditProfileActivity.this).add(dpRequest);

                } catch (JSONException e) {
                    Toast.makeText(EditProfileActivity.this, "Something not right", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(setFieldRequest);


    }

    public void editPageButtonClick(View v) {

        if (cancelButton.getId() == v.getId()) {
            finish();
        } else if (saveButton.getId() == v.getId()) {
            Uri.Builder url = new Uri.Builder();
            url.scheme("http")
                    .encodedAuthority("urbanmeals.in/api/1.0/profile/edit")
                    .appendQueryParameter("token", token);
            StringRequest editRequest = new StringRequest(Request.Method.POST, url.toString(), new Response.Listener<String>() {
                @Override
                public void onResponse(String jsonResponse) {
                    try {
                        JSONObject response = new JSONObject(jsonResponse);
                        if (response.getString("status").equals("success")) {
                            Toast.makeText(EditProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(EditProfileActivity.this, "Something Bad happened", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(EditProfileActivity.this, "Something not right, Try again", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("firstname", firstNameEdit.getText().toString());
                    params.put("lastname", lastNameEdit.getText().toString());
                    params.put("website", websiteEdit.getText().toString());
                    params.put("bio", bioEdit.getText().toString());
                    return params;
                }
            };
            Volley.newRequestQueue(this).add(editRequest);
        } else if (changeImageButton.getId() == v.getId()) {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, VIEWGALLERY);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        //When the gallery is fired and an image is selected.
        if (requestCode == VIEWGALLERY && resultCode == RESULT_OK) {
            UCrop.of(data.getData(), Uri.fromFile(new File(getCacheDir(), "dp.jpg")))
                    .withAspectRatio(1, 1)
                    .start(EditProfileActivity.this);
        }
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri uri = UCrop.getOutput(data);
            try {
                final android.app.AlertDialog progressDialog = new SpotsDialog.Builder()
                        .setContext(EditProfileActivity.this)
                        .setMessage("Processing")
                        .build();
                progressDialog.show();
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                byte[] bytes = outputStream.toByteArray();
                bitmap.recycle();

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("picture", "dp.jpg", RequestBody.create(MediaType.parse("image/jpeg"), bytes))
                        .build();

                HttpUrl url = new HttpUrl.Builder()
                        .scheme("http")
                        .host("urbanmeals.in")
                        .addPathSegments("api/1.0/profile/dp/update")
                        .addEncodedQueryParameter("token", token)
                        .build();
                final okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                client.newCall(request).enqueue(new Callback() {
                    Handler mainLoop = new Handler(getMainLooper());

                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.v("urbanmeals", "something not right");
                    }

                    @Override
                    public void onResponse(Call call, final okhttp3.Response response) throws IOException {
                        mainLoop.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject report = new JSONObject(response.body().string());
                                    if (report.getString("status").equals("success")) {
                                        String url = "http://urbanmeals.in" + report.getString("url");
                                        ImageRequest dpRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
                                            @Override
                                            public void onResponse(Bitmap response) {
                                                dpImageView.setImageBitmap(response);
                                                progressDialog.cancel();
                                            }
                                        }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(EditProfileActivity.this, "dp loading failed", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        Volley.newRequestQueue(EditProfileActivity.this).add(dpRequest);
                                    } else {

                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(EditProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    Toast.makeText(EditProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

            } catch (FileNotFoundException e) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
