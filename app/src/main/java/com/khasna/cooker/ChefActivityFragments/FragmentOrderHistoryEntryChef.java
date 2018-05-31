package com.khasna.cooker.ChefActivityFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khasna.cooker.ChefActivityFragments.Adapters.FragmentOrderHistoryEntryChefAdapter;
import com.khasna.cooker.R;

/**
 * Created by nachiket on 9/17/2017.
 */

public class FragmentOrderHistoryEntryChef extends Fragment {

    View mView;
    TextView textViewGuestName;
    TextView textViewGuestAddress;
    TextView textViewItemOrdered;
    TextView textViewItemQuantity;
    TextView textViewOrderTime;
    TextView textViewPickUpTime;
    TextView textViewPrice;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    public FragmentOrderHistoryEntryChef() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_order_history_chef_order_details, container, false);
//        mRecyclerView = (RecyclerView) mView.findViewById(R.id.order_history_order_details_recycler_view);

        textViewGuestName = mView.findViewById(R.id.textViewGuestName);
        textViewGuestAddress = mView.findViewById(R.id.textViewGuestAddress);
        textViewItemOrdered = mView.findViewById(R.id.textViewItemOrdered);
        textViewItemQuantity = mView.findViewById(R.id.textViewItemQuantity);
        textViewOrderTime = mView.findViewById(R.id.textViewOrderTime);
        textViewPickUpTime = mView.findViewById(R.id.textViewPickUpTime);
        textViewPrice = mView.findViewById(R.id.textViewPrice);

        int position = getArguments().getInt("position");
        textViewGuestName.setText(ChefEntity.arrayListOrderHistoryChefItem.get(position).getGuestName());
//        textViewGuestAddress.setText(ChefEntity.arrayListOrderHistoryChefItem.get(position).getGuestAddress());
        textViewGuestAddress.setText("");
        textViewItemOrdered.setText(ChefEntity.arrayListOrderHistoryChefItem.get(position).getItemName());
        textViewItemQuantity.setText(String.format("%s", ChefEntity.arrayListOrderHistoryChefItem.get(position).getItemQuantity()));
        textViewOrderTime.setText(ChefEntity.arrayListOrderHistoryChefItem.get(position).getOrderTime());
        textViewPickUpTime.setText(ChefEntity.arrayListOrderHistoryChefItem.get(position).getPickUpTime());
        textViewPrice.setText(ChefEntity.arrayListOrderHistoryChefItem.get(position).getTotalPrice());

        // use a linear layout manager
//        mLayoutManager = new LinearLayoutManager(getContext());
//        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
//        mAdapter = new FragmentOrderHistoryEntryChefAdapter();
//        mRecyclerView.setAdapter(mAdapter);

        return mView;
    }
}
