package com.example.cooker.ChefActivityFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cooker.ChefActivityFragments.Adapters.FragmentOrderHistoryChefAdapter;
import com.example.cooker.ChefActivityFragments.ContainerClasses.OrderHistoryChefItem;
import com.example.cooker.ChefActivityFragments.ContainerClasses.OrderHistoryChefItemDetails;
import com.example.cooker.Common.ProcessDialogBox;
import com.example.cooker.R;
import com.example.cooker.Common.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentOrderHistoryChef extends Fragment {


    View mView;
    DatabaseReference mDatabaseRef;
    ValueEventListener mValueEventListener;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    FragmentManager mActiveFragmentManager;
    Fragment mActiveFragment;
    ProcessDialogBox processDialogBox;

    public FragmentOrderHistoryChef() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_order_history_chef, container, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.order_history_recycler_view);
        mActiveFragmentManager = getFragmentManager();

        processDialogBox = new ProcessDialogBox(getActivity());
        processDialogBox.ShowDialogBox();

        FragmentOrderHistoryChefAdapter.onClickListener onClickListener = new FragmentOrderHistoryChefAdapter.onClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getContext(), "Item clicked", Toast.LENGTH_LONG).show();
                DatabaseReference itemDatabaseRef = FirebaseDatabase.getInstance().getReference("cookProfile").
                        child(UserInfo.getuID()).child("PastOrders").
                        child(ChefEntity.arrayListOrderHistoryChefItem.get(position).getOrderDate());
                processDialogBox.ShowDialogBox();

                ValueEventListener itemClickValueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ChefEntity.arrayListOrderHistoryChefItemDetails.clear();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Toast.makeText(getContext(), "Items found", Toast.LENGTH_LONG).show();
                            OrderHistoryChefItemDetails orderHistoryChefItemDetails = child.getValue(OrderHistoryChefItemDetails.class);
                            ChefEntity.arrayListOrderHistoryChefItemDetails.add(orderHistoryChefItemDetails);
                        }
                        mActiveFragment = new FragmentOrderHistoryEntryChef();
                        mActiveFragmentManager.beginTransaction().addToBackStack("OrderHistory").replace(R.id.chefs_page, mActiveFragment).commit();
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

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new FragmentOrderHistoryChefAdapter(onClickListener);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("cookProfile").
                child(UserInfo.getuID()).child("PastOrders");

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
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    OrderHistoryChefItem orderHistoryChefItem = new OrderHistoryChefItem(child.getKey(),
                            String.format("%s", child.getChildrenCount()));
                    ChefEntity.arrayListOrderHistoryChefItem.add(orderHistoryChefItem);
                }
                mRecyclerView.setAdapter(mAdapter);
            }
        };
        mDatabaseRef.addListenerForSingleValueEvent(mValueEventListener);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        ChefEntity.arrayListOrderHistoryChefItem.clear();
    }
}
