package com.example.cooker.ChefActivityFragments.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cooker.ChefActivityFragments.ChefEntity;
import com.example.cooker.R;


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
        public TextView textViewOrderDate;
        public TextView textViewOrderCount;

        public ViewHolder(View v) {
            super(v);
            textViewOrderDate = (TextView)v.findViewById(R.id.textViewOrderDate);
            textViewOrderCount = (TextView)v.findViewById(R.id.textViewOrderCount);
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
        holder.textViewOrderDate.setText(ChefEntity.arrayListOrderHistoryChefItem.get(position).getOrderDate());
        String orderCount = ChefEntity.arrayListOrderHistoryChefItem.get(position).getOrderCount() + " " + "Items";
        holder.textViewOrderCount.setText(orderCount);
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
