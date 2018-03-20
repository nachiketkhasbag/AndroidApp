package com.example.cooker.ChefActivityFragments.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.cooker.ChefActivityFragments.ChefEntity;
import com.example.cooker.R;

/**
 * Created by nachiket on 9/12/2017.
 */

public class FragmentReceivedOrdersChefAdapter extends RecyclerView.Adapter<FragmentReceivedOrdersChefAdapter.ViewHolder> {

    public interface OnButtonClickListener {
        void OnAcceptOrder( int position );
        void OnDeclineOrder( int position );
    }
    public OnButtonClickListener mOnButtonClickListener;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        public TextView mTextViewGuestName;
        public TextView mTextViewGuestPhoneNumber;
        public TextView mTextViewItemName;
        public TextView mTextViewItemQuantity;
        public TextView mTextViewPickUpTime;
        public Button mButtonAcceptOrder;
        public Button mButtonDeclineOrder;
        public String mItemKey;

        public ViewHolder(View v) {
            super(v);
            mTextViewGuestName = (TextView)v.findViewById(R.id.textViewGuestName);
            mTextViewGuestPhoneNumber = (TextView)v.findViewById(R.id.textViewGuestPhoneNumber);
            mTextViewItemName = (TextView)v.findViewById(R.id.textViewItemName);
            mTextViewItemQuantity = (TextView)v.findViewById(R.id.textViewItemQuantity);
            mTextViewPickUpTime = (TextView)v.findViewById(R.id.textViewPickUpTime);
            mButtonAcceptOrder = (Button)v.findViewById(R.id.buttonAcceptOrder);
            mButtonDeclineOrder = (Button)v.findViewById(R.id.buttonDeclineOrder);
        }
    }

    public FragmentReceivedOrdersChefAdapter( OnButtonClickListener onButtonClickListener ) {
        mOnButtonClickListener = onButtonClickListener;
    }

    @Override
    public FragmentReceivedOrdersChefAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_chef_view_current_orders_item_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        FragmentReceivedOrdersChefAdapter.ViewHolder vh = new FragmentReceivedOrdersChefAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextViewGuestName.setText(ChefEntity.chefReceivedOrderItemsArrayList.get(position).getGuestName());
        holder.mTextViewGuestPhoneNumber.setText(ChefEntity.chefReceivedOrderItemsArrayList.get(position).getGuestPhoneNumber());
        holder.mTextViewItemName.setText(ChefEntity.chefReceivedOrderItemsArrayList.get(position).getItemName());
        holder.mTextViewPickUpTime.setText(ChefEntity.chefReceivedOrderItemsArrayList.get(position).getOrderPickupTime());
        holder.mTextViewItemQuantity.setText(String.format("%s", ChefEntity.chefReceivedOrderItemsArrayList.get(position).getItemQuantity()));
        holder.mItemKey = ChefEntity.chefReceivedOrderItemsArrayList.get(position).getItemKey();
        final int itemPosition = holder.getAdapterPosition();

        holder.mButtonAcceptOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnButtonClickListener.OnAcceptOrder(itemPosition);
            }
        });

        holder.mButtonDeclineOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnButtonClickListener.OnDeclineOrder(itemPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ChefEntity.chefReceivedOrderItemsArrayList.size();
    }
}
