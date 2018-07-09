package com.urbanmeals.client.urbanmeals.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.urbanmeals.client.urbanmeals.R;

import java.util.ArrayList;

public class ProfilePhotoListAdapter extends RecyclerView.Adapter<ProfilePhotoListAdapter.ViewHolder> {

    private ArrayList<String> imageURLs;
    private Context context;


    public ProfilePhotoListAdapter(ArrayList<String> imageURLs) {
        this.imageURLs = imageURLs;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_profile_imagelist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String url = "http://urbanmeals.in" + imageURLs.get(position);
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.imageLoadingBar.setVisibility(View.GONE);
                holder.uploadedImage.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(context).add(imageRequest);
    }

    @Override
    public int getItemCount() {
        return imageURLs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView uploadedImage;
        public ProgressBar imageLoadingBar;
        public ViewHolder(View itemView) {
            super(itemView);
            uploadedImage = itemView.findViewById(R.id.Profile_UploadedImagesView);
            imageLoadingBar = itemView.findViewById(R.id.Profile_UploadedImageProgress);
        }
    }
}
