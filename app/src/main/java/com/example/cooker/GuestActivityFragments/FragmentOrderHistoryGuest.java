package com.example.cooker.GuestActivityFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cooker.Common.ProcessDialogBox;
import com.example.cooker.Common.UserInfo;
import com.example.cooker.GuestActivityFragments.Adapters.FragmentOrderHistoryGuestAdapter;
import com.example.cooker.GuestActivityFragments.ContainerClasses.OrderHistoryGuestItem;
import com.example.cooker.GuestActivityFragments.ContainerClasses.OrderHistoryGuestItemDetails;
import com.example.cooker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOrderHistoryGuest extends Fragment{
    RecyclerView.LayoutManager mLayoutManager;
    DatabaseReference mDatabaseRef;
    ValueEventListener mValueEventListener;
    RecyclerView.Adapter mAdapter;
    RecyclerView orderHistoryLRecyclerView;
    FragmentManager mActiveFragmentManager;
    Fragment mActiveFragment;
    ProcessDialogBox processDialogBox;

    public FragmentOrderHistoryGuest() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View orderHistory = inflater.inflate(R.layout.fragment_order_history, container, false);

        orderHistoryLRecyclerView = (RecyclerView)orderHistory.findViewById(R.id.guest_order_history);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        orderHistoryLRecyclerView.setLayoutManager(mLayoutManager);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("userProfile").
                child(UserInfo.getuID()).child("orderHistory");
        mActiveFragmentManager = getFragmentManager();

        processDialogBox = new ProcessDialogBox(getActivity());
        processDialogBox.ShowDialogBox();

        FragmentOrderHistoryGuestAdapter.onClickListener onClickListener = new FragmentOrderHistoryGuestAdapter.onClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getContext(), "Item clicked", Toast.LENGTH_LONG).show();
                DatabaseReference itemDatabaseRef = FirebaseDatabase.getInstance().getReference("userProfile").
                        child(UserInfo.getuID()).child("orderHistory").
                        child(GuestEntity.orderHistoryGuestItemsArrayList.get(position).getChefName());

                processDialogBox.ShowDialogBox();

                ValueEventListener itemClickValueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GuestEntity.orderHistoryGuestItemDetails.clear();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Toast.makeText(getContext(), "Items found", Toast.LENGTH_LONG).show();
                            OrderHistoryGuestItemDetails orderHistoryGuestItemDetails = child.getValue(OrderHistoryGuestItemDetails.class);
                            GuestEntity.orderHistoryGuestItemDetails.add(orderHistoryGuestItemDetails);
                        }
                        mActiveFragment = new FragmentOrderHistoryEntryGuest();
                        mActiveFragmentManager.beginTransaction().addToBackStack("OrderHistory").replace(R.id.guest_page, mActiveFragment).commit();

                        processDialogBox.DismissDialogBox();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getContext(), "Database call failed", Toast.LENGTH_LONG).show();
                    }
                };
                itemDatabaseRef.addListenerForSingleValueEvent(itemClickValueEventListener);
            }
        };

        // specify an adapter
        mAdapter = new FragmentOrderHistoryGuestAdapter(onClickListener);

        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getItems(dataSnapshot);
                processDialogBox.DismissDialogBox();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error in reading item");
                Toast.makeText(getContext(), "Error in reading order history", Toast.LENGTH_LONG).show();
            }

            public void getItems(DataSnapshot dataSnapshot)
            {
                GuestEntity.orderHistoryGuestItemsArrayList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    GuestEntity.orderHistoryGuestItemsArrayList.add(child.getValue(OrderHistoryGuestItem.class));
                }
                orderHistoryLRecyclerView.setAdapter(mAdapter);
            }
        };
        mDatabaseRef.addListenerForSingleValueEvent(mValueEventListener);

        // Inflate the layout for this fragment
        return orderHistory;
    }
}
