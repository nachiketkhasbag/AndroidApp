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
import com.khasna.cooker.ChefActivityFragments.ContainerClasses.ChefReceivedOrderItem;
import com.khasna.cooker.Common.ProcessDialogBox;
import com.khasna.cooker.Common.UserInfo;
import com.khasna.cooker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by nachiket on 9/12/2017.
 */

public class FragmentReceivedOrdersChef extends Fragment implements FragmentReceivedOrdersChefAdapter.OnButtonClickListener{

    DatabaseReference mDatabaseRef;
    RecyclerView.Adapter mAdapter;
    ProcessDialogBox mProcessDialogBox;

    public FragmentReceivedOrdersChef() {
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
                child(UserInfo.getuID());
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getItems(dataSnapshot);
                mProcessDialogBox.DismissDialogBox();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error in reading item");
                Toast.makeText(getContext(), "Error in reading current orders", Toast.LENGTH_LONG).show();
            }

            private void getItems(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    ChefReceivedOrderItem chefCurrentOrderItem = child.getValue(ChefReceivedOrderItem.class);
                    chefCurrentOrderItem.setItemKey(child.getKey());
                    ChefEntity.chefReceivedOrderItemsArrayList.add(chefCurrentOrderItem);
                }
                recyclerView.setAdapter(mAdapter);
            }
        };

        mDatabaseRef.addListenerForSingleValueEvent(valueEventListener);

        return mView;
    }

    @Override
    public void OnAcceptOrder( int itemPosition ) {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("liveOrders")
                .child(UserInfo.getuID()).child(ChefEntity.chefReceivedOrderItemsArrayList.get(itemPosition).getItemKey())
                .child("status");
        mProcessDialogBox.ShowDialogBox();

        mDatabaseRef.setValue("Accepted").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mProcessDialogBox.DismissDialogBox();
            }
        });
        ChefEntity.chefReceivedOrderItemsArrayList.remove(itemPosition);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnDeclineOrder( int itemPosition ) {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("liveOrders")
                .child(UserInfo.getuID()).child(ChefEntity.chefReceivedOrderItemsArrayList.get(itemPosition).getItemKey())
                .child("status");
        mProcessDialogBox.ShowDialogBox();

        mDatabaseRef.setValue("Declined").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mProcessDialogBox.DismissDialogBox();
            }
        });
        ChefEntity.chefReceivedOrderItemsArrayList.remove(itemPosition);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
