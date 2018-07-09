package com.urbanmeals.client.urbanmeals.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.urbanmeals.client.urbanmeals.R;
import com.urbanmeals.client.urbanmeals.activities.DigitalMenuActivity;
import com.urbanmeals.client.urbanmeals.activities.HotelActivity;
import com.urbanmeals.client.urbanmeals.data.HotelSearchResultItem;

import java.util.ArrayList;

public class HotelSearchResultAdapter extends RecyclerView.Adapter<HotelSearchResultAdapter.ViewHolder> {


    private ArrayList<HotelSearchResultItem> list;
    private Context context;

    public HotelSearchResultAdapter(ArrayList<HotelSearchResultItem> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_result_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.hotelNameView.setText(list.get(position).getHotelName());
        holder.hotelPlaceView.setText(list.get(position).getPlace());

        holder.menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DigitalMenuActivity.class);
                i.putExtra("hotelCode", list.get(position).getHotelCode());
                i.putExtra("hotelName", list.get(position).getHotelName());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView hotelNameView;
        private TextView hotelPlaceView;
        private Button menuButton;

        private ViewHolder(View itemView) {
            super(itemView);

            hotelNameView = itemView.findViewById(R.id.Search_HotelNameView);
            hotelPlaceView = itemView.findViewById(R.id.Search_HotelPlaceView);
            menuButton = itemView.findViewById(R.id.Search_MenuButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, HotelActivity.class);
                    i.putExtra("hotelCode", list.get(getLayoutPosition()).getHotelCode());
                    i.putExtra("hotelName", list.get(getLayoutPosition()).getHotelName());
                    context.startActivity(i);
                }
            });
        }
    }
}
