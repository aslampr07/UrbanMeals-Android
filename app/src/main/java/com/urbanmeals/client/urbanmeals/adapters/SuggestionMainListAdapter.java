package com.urbanmeals.client.urbanmeals.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.urbanmeals.client.urbanmeals.R;
import com.urbanmeals.client.urbanmeals.data.SuggestionMainListItem;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SuggestionMainListAdapter extends RecyclerView.Adapter<SuggestionMainListAdapter.ViewHolder> {

    private ArrayList<SuggestionMainListItem> list;
    private Context context;

    public SuggestionMainListAdapter(ArrayList<SuggestionMainListItem> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_suggestions_main_items, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.placeView.setText(list.get(position).getPlace());

        holder.subList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.subList.setAdapter(new SuggestionSubListAdapter(list.get(position).getSuggestionSubList()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView placeView;
        public RecyclerView subList;

        public ViewHolder(View itemView) {
            super(itemView);

            placeView = itemView.findViewById(R.id.Suggestion_PlaceName);
            subList = itemView.findViewById(R.id.Suggestion_SubRecycler);
        }
    }
}
