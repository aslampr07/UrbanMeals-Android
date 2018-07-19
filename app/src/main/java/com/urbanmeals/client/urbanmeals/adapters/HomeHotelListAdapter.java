package com.urbanmeals.client.urbanmeals.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.urbanmeals.client.urbanmeals.R;
import com.urbanmeals.client.urbanmeals.activities.DigitalMenuActivity;
import com.urbanmeals.client.urbanmeals.activities.HotelActivity;
import com.urbanmeals.client.urbanmeals.data.HomeHotelItem;

import java.util.ArrayList;

/**
 * Created by aslampr07 on 5/3/18.
 */

public class HomeHotelListAdapter extends RecyclerView.Adapter<HomeHotelListAdapter.ViewHolder> {

    private ArrayList<HomeHotelItem> hotelList;
    private Context context;

    public HomeHotelListAdapter(ArrayList<HomeHotelItem> hotelList) {
        this.hotelList = hotelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_homecardview, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.hotelName.setText(hotelList.get(position).getHotelName());
        holder.distance.setText(String.valueOf(hotelList.get(position).getDistance()) + " KM");
        if (!hotelList.get(position).getOpened()) {
            holder.statusImage.setImageResource(R.drawable.icon_close);
        } else {
            holder.statusImage.setImageResource(R.drawable.icon_open);

        }
        if (hotelList.get(position).getIsVeg().equals("V")) {
            holder.hotelType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_veg));
        } else if (hotelList.get(position).getIsVeg().equals("N")) {
            holder.hotelType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_nonveg));
        }

        Double rating = hotelList.get(position).getRating();
        ArrayList<PieEntry> Entrylist = new ArrayList<>();
        Entrylist.add(new PieEntry(rating.floatValue(), 0));
        Entrylist.add(new PieEntry(5f - rating.floatValue(), 1));
        PieDataSet dataSet = new PieDataSet(Entrylist, "");
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(context.getResources().getColor(R.color.urbanMealsRed));
        colors.add(Color.parseColor("#f3f4f7"));
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);
        PieData data = new PieData(dataSet);
        holder.ratingChart.setData(data);
        holder.ratingChart.setCenterText(String.valueOf(rating));
        holder.ratingChart.animateY(500);

        holder.ViewMenuButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DigitalMenuActivity.class);
                i.putExtra("hotelCode", hotelList.get(position).getHotelCode());
                i.putExtra("hotelName", hotelList.get(position).getHotelName());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView hotelName;
        public TextView distance;
        public ImageView statusImage;
        public ImageView hotelType;
        public Button ViewMenuButton;
        public PieChart ratingChart;

        private ArrayList<HomeHotelItem> hotelItems;

        public ViewHolder(View itemView) {
            super(itemView);
            hotelName = itemView.findViewById(R.id.HotelName);
            distance = itemView.findViewById(R.id.Distance);
            statusImage = itemView.findViewById(R.id.HomeCard_HotelStatus);
            hotelType = itemView.findViewById(R.id.hotelType);
            ViewMenuButton = itemView.findViewById(R.id.HomeViewMenuButton);
            ratingChart = itemView.findViewById(R.id.RatingGraph);
            ratingChart.getDescription().setEnabled(false);
            ratingChart.getLegend().setEnabled(false);
            ratingChart.setHoleRadius(75);
            ratingChart.setCenterTextSize(13);
            ratingChart.setTouchEnabled(false);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, HotelActivity.class);
                    i.putExtra("hotelCode", hotelList.get(getLayoutPosition()).getHotelCode());
                    i.putExtra("hotelName", hotelList.get(getLayoutPosition()).getHotelName());
                    context.startActivity(i);
                }
            });
        }
    }
}
