package com.khasna.cooker.GuestActivityFragments;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khasna.cooker.R;

/**
 * Created by nachiket on 9/23/2017.
 */

public class FragmentOrderHistoryEntryGuest extends Fragment {
    View mView;
    int position;

    public TextView textViewChefName;
    public TextView textViewChefAddress;
    public TextView textViewItemOrdered;
    public TextView textViewItemQuantity;
    public TextView textViewOrderTime;
    public TextView textViewPickupTime;
    public TextView textViewPrice;

    public FragmentOrderHistoryEntryGuest() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_order_history_guest_order_details, container, false);

        textViewChefName = mView.findViewById(R.id.textViewChefName);
        textViewChefAddress = mView.findViewById(R.id.textViewChefAddress);
        textViewItemOrdered = mView.findViewById(R.id.textViewItemOrdered);
        textViewItemQuantity = mView.findViewById(R.id.textViewItemQuantity);
        textViewOrderTime = mView.findViewById(R.id.textViewOrderTime);
        textViewPickupTime = mView.findViewById(R.id.textViewPickUpTime);
        textViewPrice = mView.findViewById(R.id.textViewPrice);

        position = getArguments().getInt("position");
        textViewChefName.setText(GuestEntity.orderHistoryGuestItemsArrayList.get(position).getChefName());
        textViewChefAddress.setText(GuestEntity.orderHistoryGuestItemsArrayList.get(position).getChefAddress());
        textViewItemOrdered.setText(GuestEntity.orderHistoryGuestItemsArrayList.get(position).getItemName());
        textViewItemQuantity.setText(String.format("%s",GuestEntity.orderHistoryGuestItemsArrayList.get(position).getItemQuantity()));
        textViewOrderTime.setText(GuestEntity.orderHistoryGuestItemsArrayList.get(position).getOrderTime());
        textViewPrice.setText(GuestEntity.orderHistoryGuestItemsArrayList.get(position).getTotalPrice());
        textViewPickupTime.setText(GuestEntity.orderHistoryGuestItemsArrayList.get(position).getPickUpTime());

        return mView;
    }
}
