package com.urbanmeals.client.urbanmeals.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.TooltipCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.urbanmeals.client.urbanmeals.R;
import com.urbanmeals.client.urbanmeals.activities.EditProfileActivity;
import com.urbanmeals.client.urbanmeals.adapters.ProfilePhotoListAdapter;
import com.urbanmeals.client.urbanmeals.data.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeProfileFragment extends Fragment {


    private String token;
    private Profile profile;
    private TextView nameView;
    private TextView photoCountView;
    private TextView reviewCountView;
    private TextView bioView;
    private TextView websiteView;
    private RecyclerView imageListRecycler;
    private ImageView verificationBadge;
    private LinearLayout noMediaNotice;
    private Button editProfileButton;
    private ProgressBar mainLoading;
    private ScrollView mainScroll;
    private CircleImageView proPicViewer;

    public HomeProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_profile, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("credentials", getContext().MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        nameView = view.findViewById(R.id.Profile_ProfileNameView);
        photoCountView = view.findViewById(R.id.Profile_PhotoCountView);
        reviewCountView = view.findViewById(R.id.Profile_ReviewCountView);
        imageListRecycler = view.findViewById(R.id.Profile_PhotoViewerRecycler);
        verificationBadge = view.findViewById(R.id.Profile_VerfiedBloggerBadge);
        noMediaNotice = view.findViewById(R.id.Profile_NoMediaNotice);
        editProfileButton = view.findViewById(R.id.Profile_EditProfileButton);
        mainLoading = view.findViewById(R.id.Profile_MainLoading);
        mainScroll = view.findViewById(R.id.Profile_MainScrollView);
        bioView = view.findViewById(R.id.Profile_BioView);
        websiteView = view.findViewById(R.id.Profile_WebsiteView);
        proPicViewer = view.findViewById(R.id.Profile_DPViewer);

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), EditProfileActivity.class);
                startActivity(i);
            }
        });

        TooltipCompat.setTooltipText(verificationBadge, "Urban Meals Verified Blogger");

        imageListRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3));

        if (profile == null) {
            Uri.Builder url = new Uri.Builder();
            url.scheme("http")
                    .encodedAuthority("urbanmeals.in/api/1.0/profile/")
                    .appendQueryParameter("token", token)
                    .build();
            StringRequest profileRequest = new StringRequest(Request.Method.GET, url.toString(), new Response.Listener<String>() {
                @Override
                public void onResponse(String JsonResponse) {
                    profile = new Profile();
                    try {
                        JSONObject response = new JSONObject(JsonResponse);
                        profile.setName(response.getString("firstName") + " " + response.getString("lastName"));
                        nameView.setText(profile.getName());

                        JSONObject count = response.getJSONObject("count");
                        profile.setPhotoCount(count.getString("photos"));
                        profile.setReviewCount(count.getString("reviews"));
                        profile.setDpUrl(response.getString("displayPicture"));
                        photoCountView.setText(profile.getPhotoCount());
                        reviewCountView.setText(profile.getReviewCount());

                        JSONArray imageURLs = response.getJSONArray("images");
                        //To set the no media Notice
                        if (imageURLs.length() > 0) {
                            noMediaNotice.setVisibility(View.GONE);
                        }
                        ArrayList<String> images = new ArrayList<>();
                        for (int i = 0; i < imageURLs.length(); i++) {
                            images.add(imageURLs.getString(i));
                        }
                        profile.setImages(images);
                        imageListRecycler.setAdapter(new ProfilePhotoListAdapter(profile.getImages()));

                        profile.setBlogger((response.getString("blogger").equals("Y")));

                        if (profile.getBlogger()) {
                            verificationBadge.setVisibility(View.VISIBLE);
                        } else {
                            verificationBadge.setVisibility(View.GONE);
                        }

                        profile.setBio(response.getString("bio"));
                        profile.setWebsite(response.getString("website"));

                        if (!profile.getBio().equals("")) {
                            bioView.setVisibility(View.VISIBLE);
                            bioView.setText(profile.getBio());
                        }

                        if (!profile.getWebsite().equals("")) {
                            websiteView.setVisibility(View.VISIBLE);
                            websiteView.setText(profile.getWebsite());
                        }

                        String url = "http://urbanmeals.in" + profile.getDpUrl();

                        ImageRequest dpRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                proPicViewer.setImageBitmap(response);
                            }
                        }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), "dp loading failed", Toast.LENGTH_SHORT).show();
                            }
                        });

                        Volley.newRequestQueue(getContext()).add(dpRequest);
                        mainLoading.setVisibility(View.GONE);
                        mainScroll.setForeground(null);

                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), "Unable to Parse JSON", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            Volley.newRequestQueue(getContext()).add(profileRequest);
        } else {
            nameView.setText(profile.getName());
            photoCountView.setText(profile.getPhotoCount());
            reviewCountView.setText(profile.getReviewCount());
            imageListRecycler.setAdapter(new ProfilePhotoListAdapter(profile.getImages()));

            if (profile.getImages().size() > 0) {
                noMediaNotice.setVisibility(View.GONE);
            }

            if (profile.getBlogger()) {
                verificationBadge.setVisibility(View.VISIBLE);
            } else {
                verificationBadge.setVisibility(View.GONE);
            }
            if (!profile.getBio().equals("")) {
                bioView.setVisibility(View.VISIBLE);
                bioView.setText(profile.getBio());
            }

            if (!profile.getWebsite().equals("")) {
                websiteView.setVisibility(View.VISIBLE);
                websiteView.setText(profile.getWebsite());
            }

            mainLoading.setVisibility(View.GONE);
            mainScroll.setForeground(null);
        }
    }
}
