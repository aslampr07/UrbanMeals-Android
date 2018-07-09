package com.urbanmeals.client.urbanmeals.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.urbanmeals.client.urbanmeals.R;
import com.urbanmeals.client.urbanmeals.activities.MealActivity;
import com.urbanmeals.client.urbanmeals.data.SuggestionSubListItem;

import java.util.ArrayList;
import java.util.Random;

public class SuggestionSubListAdapter extends RecyclerView.Adapter<SuggestionSubListAdapter.ViewHolder> {

    ArrayList<SuggestionSubListItem> list;
    Context context;

    private int[] thump = {
            R.drawable.meal01,
            R.drawable.meal02,
            R.drawable.meal03,
            R.drawable.meal04,
            R.drawable.meal05,
            R.drawable.meal06
    };

    public SuggestionSubListAdapter(ArrayList<SuggestionSubListItem> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_suggestions_sublist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemName.setText(list.get(position).getName());
        holder.hotelName.setText(list.get(position).getHotelName());
        String price = "₹ " + list.get(position).getPrice().intValue();
        holder.itemPrice.setText(price);

        //Temporary code.
        Random r = new Random();
        int x = r.nextInt(6);
        holder.itemImage.setImageResource(thump[x]);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView itemImage;
        public TextView itemName;
        public TextView hotelName;
        public TextView itemPrice;

        public ViewHolder(View itemView) {
            super(itemView);


            itemImage = itemView.findViewById(R.id.Suggestion_SubListItemImage);
            itemName = itemView.findViewById(R.id.Suggestion_SubListItemName);
            hotelName = itemView.findViewById(R.id.Suggestion_SubListHotelName);
            itemPrice = itemView.findViewById(R.id.Suggestion_SubListItemPrice);

            Random r = new Random();
            int x = r.nextInt(6);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, MealActivity.class);
                    i.putExtra("itemCode", list.get(getAdapterPosition()).getItemCode());
                    context.startActivity(i);
                }
            });
        }
    }

}
