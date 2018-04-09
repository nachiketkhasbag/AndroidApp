package com.khasna.cooker.ChefActivityFragments.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khasna.cooker.ChefActivityFragments.ChefEntity;
import com.khasna.cooker.R;

import java.math.BigDecimal;


/**
 * Created by nachiket on 9/9/2017.
 */

public class FragmentOrderHistoryChefAdapter extends RecyclerView.Adapter<FragmentOrderHistoryChefAdapter.ViewHolder> {

    public interface onClickListener{
        void onClick(int position);
    }

    onClickListener mOnClickListener;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        public TextView textViewGuestName;
        public TextView textViewGuestPhoneNumber;
        public TextView textViewItemName;
        public TextView textViewItemQuantity;
        public TextView textViewOrderTime;
        public TextView textViewPrice;
        public TextView textViewStatus;

        public ViewHolder(View v) {
            super(v);
            textViewGuestName = (TextView)v.findViewById(R.id.textViewGuestName);
            textViewGuestPhoneNumber = (TextView)v.findViewById(R.id.textViewGuestPhoneNumber);
            textViewItemName = (TextView)v.findViewById(R.id.textViewItemName);
            textViewItemQuantity = (TextView)v.findViewById(R.id.textViewItemQuantity);
            textViewOrderTime = (TextView)v.findViewById(R.id.textViewOrderTime);
            textViewPrice = (TextView)v.findViewById(R.id.textViewPrice);
            textViewStatus = (TextView)v.findViewById(R.id.textViewStatus);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FragmentOrderHistoryChefAdapter(onClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    @Override
    public FragmentOrderHistoryChefAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_order_history_chef_item, parent, false);

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textViewGuestName.setText(ChefEntity.arrayListOrderHistoryChefItem.get(position).getGuestName());
        String orderCount = String.format("%s", ChefEntity.arrayListOrderHistoryChefItem.get(position).getItemQuantity()) + " " + "Order(s)";

        holder.textViewGuestPhoneNumber.setText(ChefEntity.arrayListOrderHistoryChefItem.get(position).getGuestPhoneNumber());
        holder.textViewItemName.setText(ChefEntity.arrayListOrderHistoryChefItem.get(position).getItemName());
        holder.textViewItemQuantity.setText(orderCount);
        holder.textViewOrderTime.setText(ChefEntity.arrayListOrderHistoryChefItem.get(position).getOrderTime());

        BigDecimal itemCount = new BigDecimal(ChefEntity.arrayListOrderHistoryChefItem.get(position).getItemQuantity());
        BigDecimal itemPrice = new BigDecimal(ChefEntity.arrayListOrderHistoryChefItem.get(position).getItemPrice());
        BigDecimal totalPrice;
        totalPrice = itemCount.multiply(itemPrice);

        holder.textViewPrice.setText("Total price  = $" + totalPrice);
        holder.textViewStatus.setText("Status:" + ChefEntity.arrayListOrderHistoryChefItem.get(position).getStatus());

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
        return ChefEntity.arrayListOrderHistoryChefItem.size();
    }
}
