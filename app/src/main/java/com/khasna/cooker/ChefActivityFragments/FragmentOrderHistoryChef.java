package com.khasna.cooker.ChefActivityFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.khasna.cooker.ChefActivityFragments.Adapters.FragmentOrderHistoryChefAdapter;
import com.khasna.cooker.ChefActivityFragments.ContainerClasses.OrderHistoryChefItemDetails;
import com.khasna.cooker.Common.Interfaces;
import com.khasna.cooker.Common.ProcessDialogBox;
import com.khasna.cooker.Models.Collection;
import com.khasna.cooker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FragmentOrderHistoryChef extends Fragment {


    View mView;
    DatabaseReference mDatabaseRef;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    FragmentManager mActiveFragmentManager;
    Fragment mActiveFragment;
    ProcessDialogBox processDialogBox;
    Collection mCollection;

    public FragmentOrderHistoryChef() {
        // Required empty public constructor
        mCollection = Collection.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

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

                Bundle bundle = new Bundle();
                bundle.putInt("position", position );
                mActiveFragment = new FragmentOrderHistoryEntryChef();
                mActiveFragment.setArguments(bundle);
                mActiveFragmentManager.beginTransaction().addToBackStack("OrderHistory").replace(R.id.chefs_page, mActiveFragment).commit();

//                mCollection.mDataBaseFunctions.ReadDataBase(
//                        itemDatabaseRef,
//                        "cookProfile/" + mCollection.mFireBaseFunctions.getuID() + "/orderHistory",
//                        new Interfaces.DataBaseReadInterface() {
//                            @Override
//                            public void ReadSucceeded(DataSnapshot dataSnapshot) {
//                                ChefEntity.arrayListOrderHistoryChefItemDetails.clear();
//                                for (DataSnapshot child : dataSnapshot.getChildren()) {
//                                    OrderHistoryChefItemDetails orderHistoryChefItemDetails = child.getValue(OrderHistoryChefItemDetails.class);
//                                    ChefEntity.arrayListOrderHistoryChefItemDetails.add(orderHistoryChefItemDetails);
//                                }
//                                Toast.makeText(getContext(), "Items found", Toast.LENGTH_SHORT).show();
//                                mActiveFragment = new FragmentOrderHistoryEntryChef();
//                                mActiveFragmentManager.beginTransaction().addToBackStack("OrderHistory").replace(R.id.chefs_page, mActiveFragment).commit();
//                                processDialogBox.DismissDialogBox();
//                            }
//
//                            @Override
//                            public void ReadFailed(DatabaseError databaseError) {
//                                Toast.makeText(getContext(), "Database call failed", Toast.LENGTH_LONG).show();
//                                processDialogBox.DismissDialogBox();
//                            }
//                        });
            }
        };

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new FragmentOrderHistoryChefAdapter(onClickListener);

        mCollection.mChefActivityFunctions.GetPendingItems(
                mDatabaseRef,
                new Interfaces.PendingItemsReadInterface() {
                    @Override
                    public void ReadSuccessful() {
                        processDialogBox.DismissDialogBox();
                        mRecyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void ReadFailed(String error) {
                        System.out.println("Error in reading item");
                        Toast.makeText(getContext(), "Error in reading order history", Toast.LENGTH_LONG).show();
                        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                        processDialogBox.DismissDialogBox();
                    }
                });

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        ChefEntity.arrayListOrderHistoryChefItem.clear();
    }
}
