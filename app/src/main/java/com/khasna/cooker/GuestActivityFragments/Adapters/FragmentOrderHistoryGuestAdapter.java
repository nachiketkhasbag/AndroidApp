package com.khasna.cooker.GuestActivityFragments.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khasna.cooker.GuestActivityFragments.ContainerClasses.OrderHistoryGuestItem;
import com.khasna.cooker.Models.Collection;
import com.khasna.cooker.R;

import java.util.regex.Pattern;

/**
 * Created by nachiket on 9/23/2017.
 */

public class FragmentOrderHistoryGuestAdapter extends RecyclerView.Adapter<FragmentOrderHistoryGuestAdapter.ViewHolder> {
    public interface onClickListener{
        void onClick(int position);
    }

    private FragmentOrderHistoryGuestAdapter.onClickListener mOnClickListener;
    private Collection mCollection;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        public TextView textViewItemName;
        public TextView textViewQuantity;
        public TextView textViewChefName;
        public TextView textViewPrice;
        public TextView textViewStatus;
        public TextView textViewOrderDate;

        public ViewHolder(View v) {
            super(v);
            textViewItemName = (TextView)v.findViewById(R.id.textViewItemName);
            textViewQuantity = (TextView)v.findViewById(R.id.textViewQuantity);
            textViewChefName = (TextView)v.findViewById(R.id.textViewChefName);
            textViewPrice = (TextView)v.findViewById(R.id.textViewPrice);
            textViewStatus = (TextView)v.findViewById(R.id.textViewStatus);
            textViewOrderDate = (TextView)v.findViewById(R.id.textViewOrderDate);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FragmentOrderHistoryGuestAdapter(FragmentOrderHistoryGuestAdapter.onClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
        mCollection = Collection.getInstance();
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
        OrderHistoryGuestItem orderItem = mCollection.GetOrderHistoryGuestItemsArrayList().get(position);
        holder.textViewItemName.setText(orderItem.getItemName());
        String quantity = "Number of items: " + String.valueOf(orderItem.getItemQuantity());
        holder.textViewQuantity.setText(quantity);
        holder.textViewChefName.setText(orderItem.getChefName());
        holder.textViewPrice.setText(orderItem.getTotalPrice());
        holder.textViewStatus.setText(orderItem.getStatus());

        Pattern pattern = Pattern.compile("\\s");

        String date = orderItem.getOrderTime();
        String[] date1 = pattern.split(date);

        String displayDate = date1[0] + " " + date1[1] + " " + date1[2] + " " + date1[3];

        holder.textViewOrderDate.setText(displayDate);

        final int itemPosition = holder.getAdapterPosition();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mOnClickListener.onClick(itemPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCollection.GetOrderHistoryGuestItemsArrayList().size();
    }
}
