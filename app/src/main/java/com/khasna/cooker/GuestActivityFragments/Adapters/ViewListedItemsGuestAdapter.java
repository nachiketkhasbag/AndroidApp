package com.khasna.cooker.GuestActivityFragments.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khasna.cooker.GuestActivityFragments.GuestEntity;
import com.khasna.cooker.R;

/**
 * Created by nachiket on 6/7/2017.
 */

public class ViewListedItemsGuestAdapter extends RecyclerView.Adapter<ViewListedItemsGuestAdapter.ViewHolder> {

    public interface OnClickListener{
        void OnClick(View view, int position);
    }
    private OnClickListener mOnClickListener;
    private GuestEntity mGuestEntity;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        private TextView textViewItemNameForGuest;
        private TextView textViewItemDescriptionForGuest;
        private TextView textViewItemContentsForGuest;
        private TextView textViewItemPriceForGuest;
        private ViewHolder(View v) {
            super(v);
            textViewItemNameForGuest        = (TextView) v.findViewById(R.id.textViewItemNameForGuest);
            textViewItemDescriptionForGuest = (TextView) v.findViewById(R.id.textViewItemDescriptionForGuest);
            textViewItemContentsForGuest    = (TextView) v.findViewById(R.id.textViewItemContentsForGuest);
            textViewItemPriceForGuest       = (TextView) v.findViewById(R.id.textViewItemPriceForGuest);
        }
    }

    public ViewListedItemsGuestAdapter(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
        mGuestEntity = GuestEntity.getInstance();
    }

    @Override
    public ViewListedItemsGuestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.guest_item_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewListedItemsGuestAdapter.ViewHolder vh = new ViewListedItemsGuestAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewListedItemsGuestAdapter.ViewHolder holder, int position) {
        holder.textViewItemNameForGuest.setText(mGuestEntity.getGuestItemArrayList().get(position).getitemName());
        holder.textViewItemDescriptionForGuest.setText(mGuestEntity.getGuestItemArrayList().get(position).getitemDescription());
        holder.textViewItemContentsForGuest.setText(mGuestEntity.getGuestItemArrayList().get(position).getItemIngredients());
        holder.textViewItemPriceForGuest.setText(mGuestEntity.getGuestItemArrayList().get(position).getItemPrice());
        final int itemPosition = holder.getAdapterPosition();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListener.OnClick(v, itemPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGuestEntity.getGuestItemArrayList().size();
    }

}
