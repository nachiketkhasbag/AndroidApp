package com.khasna.cooker.GuestActivityFragments.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khasna.cooker.GuestActivityFragments.ContainerClasses.OrderHistoryGuestItem;
import com.khasna.cooker.GuestActivityFragments.GuestEntity;
import com.khasna.cooker.R;

/**
 * Created by nachiket on 9/23/2017.
 */

public class FragmentOrderHistoryGuestAdapter extends RecyclerView.Adapter<FragmentOrderHistoryGuestAdapter.ViewHolder> {
    public interface onClickListener{
        void onClick(int position);
    }

    FragmentOrderHistoryGuestAdapter.onClickListener mOnClickListener;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        public TextView textViewItemName;
        public TextView Quantity;
        public TextView ChefName;
        public TextView textViewItemPrice;

        public ViewHolder(View v) {
            super(v);
            textViewItemName = (TextView)v.findViewById(R.id.textViewItemName);
            Quantity = (TextView)v.findViewById(R.id.Quantity);
            ChefName = (TextView)v.findViewById(R.id.ChefName);
            textViewItemPrice = (TextView)v.findViewById(R.id.textViewItemPrice);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FragmentOrderHistoryGuestAdapter(FragmentOrderHistoryGuestAdapter.onClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    @Override
    public FragmentOrderHistoryGuestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_order_history_guest_item, parent, false);

        // set the view's size, margins, paddings and layout parameters
        FragmentOrderHistoryGuestAdapter.ViewHolder vh = new FragmentOrderHistoryGuestAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(FragmentOrderHistoryGuestAdapter.ViewHolder holder, int position) {
        OrderHistoryGuestItem orderItem = GuestEntity.orderHistoryGuestItemsArrayList.get(position);
        holder.textViewItemName.setText(orderItem.getItemName());
        holder.Quantity.setText(String.valueOf(orderItem.getItemQuantity()));
        holder.ChefName.setText(orderItem.getChefName());
        holder.textViewItemPrice.setText(orderItem.getPrice());

        final int itemPosition = holder.getAdapterPosition();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListener.onClick(itemPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return GuestEntity.orderHistoryGuestItemsArrayList.size();
    }
}
