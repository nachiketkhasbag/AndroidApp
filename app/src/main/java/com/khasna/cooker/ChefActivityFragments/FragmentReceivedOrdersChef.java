package com.khasna.cooker.ChefActivityFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.khasna.cooker.ChefActivityFragments.Adapters.FragmentReceivedOrdersChefAdapter;
import com.khasna.cooker.Common.Interfaces;
import com.khasna.cooker.Common.ProcessDialogBox;
import com.khasna.cooker.Models.Collection;
import com.khasna.cooker.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by nachiket on 9/12/2017.
 */

public class FragmentReceivedOrdersChef extends Fragment implements FragmentReceivedOrdersChefAdapter.OnButtonClickListener{

    DatabaseReference mDatabaseRef;
    RecyclerView.Adapter mAdapter;
    ProcessDialogBox mProcessDialogBox;
    Collection mCollection;

    public FragmentReceivedOrdersChef() {
        mCollection = Collection.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ChefEntity.chefReceivedOrderItemsArrayList.clear();

        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_chef_view_received_orders, container, false);
        final RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.fragment_chef_view_received_orders_recycler_view);
        mProcessDialogBox = new ProcessDialogBox(getActivity());
        mProcessDialogBox.ShowDialogBox();

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        mAdapter = new FragmentReceivedOrdersChefAdapter(this);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("liveOrders").
                child(mCollection.mFireBaseFunctions.getuID());

        mCollection.mChefActivityFunctions.GetReceivedOrders(
                mDatabaseRef,
                new Interfaces.ReceivedOrdersInterface() {
                    @Override
                    public void ReadSuccessful() {
                        mProcessDialogBox.DismissDialogBox();
                        recyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void ReadFailed(String error) {
                        mProcessDialogBox.DismissDialogBox();
                        System.out.println("Error in reading item");
                        Toast.makeText(getContext(), "Error in reading current orders", Toast.LENGTH_LONG).show();
                        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                    }
                }
        );

        return mView;
    }

    @Override
    public void OnAcceptOrder( int itemPosition ) {

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("liveOrders")
                .child(mCollection.mFireBaseFunctions.getuID()).child(ChefEntity.chefReceivedOrderItemsArrayList.get(itemPosition).getItemKey())
                .child("status");
        mProcessDialogBox.ShowDialogBox();

        mCollection.mChefActivityFunctions.SetOrderStatus(mDatabaseRef,
                "Accepted",
                itemPosition,
                new Interfaces.OrderStatusInterface() {
            @Override
            public void OrderSetStatusSuccessful() {
                mProcessDialogBox.DismissDialogBox();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void OrderSetStatusFailed(String error) {
                mProcessDialogBox.DismissDialogBox();
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void OnDeclineOrder( int itemPosition ) {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("liveOrders")
                .child(mCollection.mFireBaseFunctions.getuID()).child(ChefEntity.chefReceivedOrderItemsArrayList.get(itemPosition).getItemKey())
                .child("status");
        mProcessDialogBox.ShowDialogBox();

        mCollection.mChefActivityFunctions.SetOrderStatus(mDatabaseRef, "Declined", itemPosition, new Interfaces.OrderStatusInterface() {
            @Override
            public void OrderSetStatusSuccessful() {
                mProcessDialogBox.DismissDialogBox();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void OrderSetStatusFailed(String error) {
                mProcessDialogBox.DismissDialogBox();
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
