package com.urbanmeals.client.urbanmeals.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.urbanmeals.client.urbanmeals.R;
import com.urbanmeals.client.urbanmeals.data.MealProfileThumbnails;

import java.util.ArrayList;

public class MealPhotoThumbnailAdapter extends RecyclerView.Adapter<MealPhotoThumbnailAdapter.ViewHolder> {

    private ArrayList<MealProfileThumbnails> thumbnailList;
    private Context context;

    public MealPhotoThumbnailAdapter(ArrayList<MealProfileThumbnails> thumbnailList) {
        this.thumbnailList = thumbnailList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_meal_thumbnail_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String url = "http://urbanmeals.in" + thumbnailList.get(position).getThumbnailURL();
        ImageRequest thumbnailRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.thumbnailPreview.setImageBitmap(response);
            }
        }, 161, 161, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(context).add(thumbnailRequest);
    }

    @Override
    public int getItemCount() {
        return thumbnailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView thumbnailPreview;
        public ViewHolder(View itemView) {
            super(itemView);
            thumbnailPreview = itemView.findViewById(R.id.Meal_ThumbNail);
        }
    }
}
