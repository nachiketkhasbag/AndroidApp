package com.khasna.cooker.GuestActivityFragments.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khasna.cooker.GuestActivityFragments.GuestEntity;
import com.khasna.cooker.R;

/**
 * Created by nachiket on 9/23/2017.
 */

public class FragmentOrderHistoryEntryGuestAdapter extends RecyclerView.Adapter<FragmentOrderHistoryEntryGuestAdapter.ViewHolder>{

    GuestEntity mGuestEntity;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        public TextView textViewOrderHistoryChefName;
        public TextView textViewOrderHistoryChefPhoneNumber;
        public TextView textViewOrderHistoryItemName;
        public TextView textViewOrderHistoryItemQuantity;

        public ViewHolder(View v) {
            super(v);
            textViewOrderHistoryChefName = (TextView)v.findViewById(R.id.textViewOrderHistoryChefName);
            textViewOrderHistoryChefPhoneNumber = (TextView)v.findViewById(R.id.textViewOrderHistoryChefPhoneNumber);
            textViewOrderHistoryItemName = (TextView)v.findViewById(R.id.textViewOrderHistoryItemName);
            textViewOrderHistoryItemQuantity = (TextView)v.findViewById(R.id.textViewOrderHistoryItemQuantity);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FragmentOrderHistoryEntryGuestAdapter() {
        mGuestEntity = GuestEntity.getInstance();
    }

    @Override
    public FragmentOrderHistoryEntryGuestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_order_history_guest_order_details_item, parent, false);

        // set the view's size, margins, paddings and layout parameters
        FragmentOrderHistoryEntryGuestAdapter.ViewHolder vh = new FragmentOrderHistoryEntryGuestAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(FragmentOrderHistoryEntryGuestAdapter.ViewHolder holder, int position) {
        holder.textViewOrderHistoryChefName.setText(mGuestEntity.getOrderHistoryGuestItemDetails().get(position).getChefName());
        holder.textViewOrderHistoryChefPhoneNumber.setText(mGuestEntity.getOrderHistoryGuestItemDetails().get(position).getChefPhoneNumber());
        holder.textViewOrderHistoryItemName.setText(mGuestEntity.getOrderHistoryGuestItemDetails().get(position).getItemName());
        holder.textViewOrderHistoryItemQuantity.setText(String.format("%s", mGuestEntity.getOrderHistoryGuestItemDetails().get(position).getNumberOfItems()));
    }

    @Override
    public int getItemCount() {
        return mGuestEntity.getOrderHistoryGuestItemDetails().size();
    }
}
