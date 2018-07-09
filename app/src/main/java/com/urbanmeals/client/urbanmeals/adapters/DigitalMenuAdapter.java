package com.urbanmeals.client.urbanmeals.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.urbanmeals.client.urbanmeals.R;
import com.urbanmeals.client.urbanmeals.activities.MealActivity;
import com.urbanmeals.client.urbanmeals.data.DigitalMenuItem;
import com.urbanmeals.client.urbanmeals.tools.PieGraphTool;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by aslampr07 on 18/3/18.
 */

public class DigitalMenuAdapter extends RecyclerView.Adapter<DigitalMenuAdapter.ViewHolder> {

    private ArrayList<DigitalMenuItem> Items;
    private Context context;

    public DigitalMenuAdapter(ArrayList<DigitalMenuItem> Items) {
        this.Items = Items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_digitalmenu_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ItemName.setText(Items.get(position).getName());
        holder.ItemPrice.setText(String.format("₹ %s", String.valueOf(Items.get(position).getPrice())));
        Double rating =  Items.get(position).getRating();
        holder.ratingChart.setData(PieGraphTool.SetPieGraphData(rating.floatValue(), 5.00f, holder.ItemName.getResources().getColor(R.color.urbanMealsRed)));
        holder.ratingChart.setCenterText(new DecimalFormat("#.#").format(rating));
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView ItemName;
        private TextView ItemPrice;
        private PieChart ratingChart;

        private ViewHolder(View itemView) {
            super(itemView);

            ItemName = itemView.findViewById(R.id.DigitalMenuItemName);
            ItemPrice = itemView.findViewById(R.id.DigitalMenuItemPrice);
            ratingChart = itemView.findViewById(R.id.DigitalMenu_RatingChart);

            ratingChart.getDescription().setEnabled(false);
            ratingChart.getLegend().setEnabled(false);
            ratingChart.setHoleRadius(70);
            ratingChart.setTouchEnabled(false);
            ratingChart.setNoDataText("");
            ratingChart.setCenterTextSize(8);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, MealActivity.class);
                    i.putExtra("itemCode", Items.get(getLayoutPosition()).getCode());
                    context.startActivity(i);
                }
            });
        }
    }
}
