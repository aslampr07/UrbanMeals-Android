package com.urbanmeals.client.urbanmeals.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.urbanmeals.client.urbanmeals.R;
import com.urbanmeals.client.urbanmeals.data.MealReviewItem;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MealReviewListAdapter extends RecyclerView.Adapter<MealReviewListAdapter.ViewHolder> {


    private ArrayList<MealReviewItem> reviewList;

    public MealReviewListAdapter(ArrayList<MealReviewItem> reviewList) {
        this.reviewList = reviewList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_meal_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.usernameView.setText(reviewList.get(position).getUserName());
        holder.body.setText(reviewList.get(position).getReviewBody());

        float taste = reviewList.get(position).getTaste().floatValue();
        float presentation = reviewList.get(position).getPresentation().floatValue();
        float quantity = reviewList.get(position).getPresentation().floatValue();

        ArrayList<BarEntry> entry = new ArrayList<>();
        entry.add(new BarEntry(1, taste));
        entry.add(new BarEntry(2, presentation));
        entry.add(new BarEntry(3, quantity));

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#22c552"));
        colors.add(Color.parseColor("#faea01"));
        colors.add(Color.parseColor("#d72f2f"));

        BarDataSet data = new BarDataSet(entry, "");
        data.setColors(colors);


        holder.ratingChart.setData(new BarData(data));

    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView usernameView;
        private TextView body;
        private BarChart ratingChart;
        private ViewHolder(View itemView) {
            super(itemView);
            usernameView = itemView.findViewById(R.id.Meal_Review_UserName);
            body = itemView.findViewById(R.id.Meal_Review_Body);
            ratingChart = itemView.findViewById(R.id.Meal_IndividualRatingGraph);

            ratingChart.setTouchEnabled(false);
            ratingChart.getAxisLeft().setDrawGridLines(false);
            ratingChart.getAxisLeft().setDrawLabels(false);
            ratingChart.getAxisLeft().setDrawAxisLine(false);
            ratingChart.getAxisLeft().setAxisMinimum(0);
            ratingChart.getAxisLeft().setAxisMaximum(5);
            ratingChart.getAxisRight().setDrawGridLines(false);
            ratingChart.getAxisRight().setDrawLabels(false);
            ratingChart.getAxisRight().setDrawAxisLine(false);
            ratingChart.getAxisRight().setAxisMinimum(0);
            ratingChart.getAxisRight().setAxisMaximum(5);

            ratingChart.getXAxis().setEnabled(false);
            Description description = new Description();
            description.setText("");
            ratingChart.setDescription(description);
            ratingChart.getLegend().setEnabled(false);


        }
    }

}
