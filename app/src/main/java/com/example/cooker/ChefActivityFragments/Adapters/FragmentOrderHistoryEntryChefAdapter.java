package com.example.cooker.ChefActivityFragments.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cooker.ChefActivityFragments.ChefEntity;
import com.example.cooker.R;

/**
 * Created by nachiket on 9/17/2017.
 */

public class FragmentOrderHistoryEntryChefAdapter extends RecyclerView.Adapter<FragmentOrderHistoryEntryChefAdapter.ViewHolder> {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        public TextView textViewOrderHistoryGuestName;
        public TextView textViewOrderHistoryGuestPhoneNumber;
        public TextView textViewOrderHistoryItemName;
        public TextView textViewOrderHistoryItemQuantity;

        public ViewHolder(View v) {
            super(v);
            textViewOrderHistoryGuestName = (TextView)v.findViewById(R.id.textViewOrderHistoryGuestName);
            textViewOrderHistoryGuestPhoneNumber = (TextView)v.findViewById(R.id.textViewOrderHistoryGuestPhoneNumber);
            textViewOrderHistoryItemName = (TextView)v.findViewById(R.id.textViewOrderHistoryItemName);
            textViewOrderHistoryItemQuantity = (TextView)v.findViewById(R.id.textViewOrderHistoryItemQuantity);
        }
    }

    public FragmentOrderHistoryEntryChefAdapter() {
    }

    @Override
    public FragmentOrderHistoryEntryChefAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_order_history_chef_order_details_item, parent, false);

        // set the view's size, margins, paddings and layout parameters
        FragmentOrderHistoryEntryChefAdapter.ViewHolder vh = new FragmentOrderHistoryEntryChefAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(FragmentOrderHistoryEntryChefAdapter.ViewHolder holder, int position) {
        holder.textViewOrderHistoryGuestName.setText(ChefEntity.arrayListOrderHistoryChefItemDetails.get(position).getGuestName());
        holder.textViewOrderHistoryGuestPhoneNumber.setText(ChefEntity.arrayListOrderHistoryChefItemDetails.get(position).getGuestPhoneNumber());
        holder.textViewOrderHistoryItemName.setText(ChefEntity.arrayListOrderHistoryChefItemDetails.get(position).getItemName());
        holder.textViewOrderHistoryItemQuantity.setText(String.format("%s", ChefEntity.arrayListOrderHistoryChefItemDetails.get(position).getNumberOfItems()));
    }

    @Override
    public int getItemCount() {
        return ChefEntity.arrayListOrderHistoryChefItemDetails.size();
    }
}
