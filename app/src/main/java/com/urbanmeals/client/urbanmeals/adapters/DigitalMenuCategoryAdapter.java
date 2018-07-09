package com.urbanmeals.client.urbanmeals.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.urbanmeals.client.urbanmeals.R;
import com.urbanmeals.client.urbanmeals.data.DigitalMenuCategoryItem;
import com.urbanmeals.client.urbanmeals.data.DigitalMenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by aslampr07 on 15/3/18.
 */

public class DigitalMenuCategoryAdapter extends RecyclerView.Adapter<DigitalMenuCategoryAdapter.ViewHolder> {

    private ArrayList<DigitalMenuCategoryItem> Items;
    private Context context;
    private String hotelCode;
    private RecyclerView menuRecycler;

    public DigitalMenuCategoryAdapter(ArrayList<DigitalMenuCategoryItem> items, String hotelCode, RecyclerView menuRecycler) {
        this.Items = items;
        this.hotelCode = hotelCode;
        this.menuRecycler = menuRecycler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_digitalmenu_category_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, context, parent, hotelCode, Items);
        viewHolder.setMenuRecycler(menuRecycler);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.CategoryName.setText(Items.get(position).getCategoryName());

        String url = "http://urbanmeals.in" + Items.get(position).getCategoryImage();
        ImageRequest navIconRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.CategoryImage.setImageBitmap(response);
            }
        }, 80, 80, ImageView.ScaleType.CENTER, Bitmap.Config.ALPHA_8, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        Volley.newRequestQueue(context).add(navIconRequest);

        if (position == 0) {
            View v = (View) holder.CategoryImage.getParent();
            holder.onClick(v);
        }
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView CategoryName;
        public ImageView CategoryImage;

        private Context context;
        private ViewGroup parent;
        private String hotelCode;
        private ArrayList<DigitalMenuCategoryItem> Items;
        private RecyclerView menuRecycler;

        public ViewHolder(View itemView, Context context, ViewGroup parent, String hotelCode, ArrayList<DigitalMenuCategoryItem> Items) {
            super(itemView);
            this.context = context;
            this.parent = parent;
            this.hotelCode = hotelCode;
            this.Items = Items;
            itemView.setOnClickListener(this);
            CategoryName = itemView.findViewById(R.id.DigitalMenuCategoryName);
            CategoryImage = itemView.findViewById(R.id.DigitalMenuCategoryImage);
        }

        public void setMenuRecycler(RecyclerView menuRecycler) {
            this.menuRecycler = menuRecycler;
        }

        @Override
        public void onClick(View v) {
            //Setting the selection status bar color at the bottom
            View selectionStatus = v.findViewById(R.id.digitalmenu_selection_status);
            selectionStatus.setBackgroundResource(R.color.urbanMealsRed);
            for (int i = 0; i < parent.getChildCount(); i++) {
                if (getAdapterPosition() != i) {
                    View item = parent.getChildAt(i);
                    item.findViewById(R.id.digitalmenu_selection_status).setBackgroundResource(R.color.white);
                }
            }

            Uri.Builder url = new Uri.Builder();
            url.scheme("http")
                    .encodedAuthority("urbanmeals.in/api/1.0/items/menu")
                    .appendQueryParameter("hotelcode", hotelCode)
                    .appendQueryParameter("catcode", Items.get(getAdapterPosition()).getCategoryCode())
                    .build();

            StringRequest menuItemRequest = new StringRequest(Request.Method.GET, url.toString(), new Response.Listener<String>() {
                @Override
                public void onResponse(String JsonResponse) {
                    try {
                        JSONObject response = new JSONObject(JsonResponse);
                        if ("success".equals(response.getString("status"))) {
                            ArrayList<DigitalMenuItem> menuItems = new ArrayList<>();
                            JSONArray lists = response.getJSONArray("result");
                            for (int i = 0; i < lists.length(); i++) {
                                JSONObject jsonObject = lists.optJSONObject(i);
                                DigitalMenuItem item = new DigitalMenuItem(jsonObject.getString("name"), (float) jsonObject.getDouble("price"), jsonObject.getString("code"));
                                item.setRating(jsonObject.getDouble("rating"));
                                menuItems.add(item);
                            }
                            menuRecycler.setAdapter(new DigitalMenuAdapter(menuItems));
                        }
                    } catch (JSONException e) {
                        Toast.makeText(context, "Error parsing Json!", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            Volley.newRequestQueue(context).add(menuItemRequest);
        }

    }
}
