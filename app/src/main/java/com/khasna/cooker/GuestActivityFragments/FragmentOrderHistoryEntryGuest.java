package com.khasna.cooker.GuestActivityFragments;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khasna.cooker.Models.Collection;
import com.khasna.cooker.R;

/**
 * Created by nachiket on 9/23/2017.
 */

public class FragmentOrderHistoryEntryGuest extends Fragment {
    View mView;
    int position;

    public TextView textViewSnippetChefName;
    public TextView textViewSnippetChefAddress;
    public TextView textViewSnippetItemName;
    public TextView textViewSnippetOrderTime;
    public TextView textViewSnippetPickupTime;
    public TextView textViewSnippetTotalPrice;
    public TextView textViewSnippetChefPhoneNumber;
    public TextView textViewSnippetStatus;
    private Collection mCollection;

    public FragmentOrderHistoryEntryGuest() {
        mCollection = Collection.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_order_history_guest_order_details, container, false);

        textViewSnippetChefName = mView.findViewById(R.id.textViewSnippetChefName);
        textViewSnippetChefAddress = mView.findViewById(R.id.textViewSnippetChefAddress);
        textViewSnippetItemName = mView.findViewById(R.id.textViewSnippetItemName);
        textViewSnippetOrderTime = mView.findViewById(R.id.textViewSnippetOrderTime);
        textViewSnippetPickupTime = mView.findViewById(R.id.textViewSnippetPickupTime);
        textViewSnippetTotalPrice = mView.findViewById(R.id.textViewSnippetTotalPrice);
        textViewSnippetChefPhoneNumber = mView.findViewById(R.id.textViewSnippetChefPhoneNumber);
        textViewSnippetStatus = mView.findViewById(R.id.textViewSnippetStatus);

        position = getArguments().getInt("position");
        textViewSnippetChefName.setText(mCollection.GetOrderHistoryGuestItemsArrayList().get(position).getChefName());
        textViewSnippetChefAddress.setText(mCollection.GetOrderHistoryGuestItemsArrayList().get(position).getChefAddress());

        String itemQuantityAndName = String.format("%s",mCollection.GetOrderHistoryGuestItemsArrayList().get(position).getItemQuantity()) +
                " " + mCollection.GetOrderHistoryGuestItemsArrayList().get(position).getItemName();
        textViewSnippetItemName.setText(itemQuantityAndName);

        textViewSnippetOrderTime.setText(mCollection.GetOrderHistoryGuestItemsArrayList().get(position).getOrderTime());
        textViewSnippetTotalPrice.setText(mCollection.GetOrderHistoryGuestItemsArrayList().get(position).getTotalPrice());
        textViewSnippetPickupTime.setText(mCollection.GetOrderHistoryGuestItemsArrayList().get(position).getPickUpTime());
        textViewSnippetChefPhoneNumber.setText(mCollection.GetOrderHistoryGuestItemsArrayList().get(position).getChefPhoneNumber());
        textViewSnippetStatus.setText(mCollection.GetOrderHistoryGuestItemsArrayList().get(position).getStatus());

        return mView;
    }
}
