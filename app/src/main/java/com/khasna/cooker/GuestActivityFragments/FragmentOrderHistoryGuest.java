package com.khasna.cooker.GuestActivityFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.khasna.cooker.Common.Interfaces;
import com.khasna.cooker.Common.ProcessDialogBox;
import com.khasna.cooker.GuestActivityFragments.Adapters.FragmentOrderHistoryGuestAdapter;
import com.khasna.cooker.Models.Collection;
import com.khasna.cooker.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOrderHistoryGuest extends Fragment{
    RecyclerView.LayoutManager mLayoutManager;
    DatabaseReference mDatabaseRef;
    RecyclerView.Adapter mAdapter;
    RecyclerView orderHistoryLRecyclerView;
    FragmentManager mActiveFragmentManager;
    Fragment mActiveFragment;
    ProcessDialogBox processDialogBox;
    Collection mCollection;
    GuestEntity mGuestEntity;

    public FragmentOrderHistoryGuest() {
        // Required empty public constructor
        mCollection = Collection.getInstance();
        mGuestEntity = GuestEntity.getInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View orderHistory = inflater.inflate(R.layout.fragment_order_history, container, false);

        orderHistoryLRecyclerView = (RecyclerView)orderHistory.findViewById(R.id.guest_order_history);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        orderHistoryLRecyclerView.setLayoutManager(mLayoutManager);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("userProfile").
                child(mGuestEntity.getFirebaseUser().getUid()).child("orderHistory");
        mActiveFragmentManager = getFragmentManager();

        processDialogBox = new ProcessDialogBox(getActivity());
        processDialogBox.ShowDialogBox();

        FragmentOrderHistoryGuestAdapter.onClickListener onClickListener = new FragmentOrderHistoryGuestAdapter.onClickListener() {
            @Override
            public void onClick(final int position) {
//                Toast.makeText(getContext(), "Item clicked", Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                mActiveFragment = new FragmentOrderHistoryEntryGuest();
                mActiveFragment.setArguments(bundle);
                mActiveFragmentManager.beginTransaction().addToBackStack("OrderHistory").replace(R.id.guest_page, mActiveFragment).commit();
            }
        };

        // specify an adapter
        mAdapter = new FragmentOrderHistoryGuestAdapter(onClickListener);

        mCollection.mGuestActivityFunctions.ReadGuestHistoryData(
                mDatabaseRef,
                new Interfaces.ReadGuestHistoryInterface() {
                    @Override
                    public void ReadComplete() {
                        processDialogBox.DismissDialogBox();
                        orderHistoryLRecyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void ReadFailed(String error) {
                        System.out.println("Error in reading item");
                        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                        processDialogBox.DismissDialogBox();
                    }
                }
        );

        // Inflate the layout for this fragment
        return orderHistory;
    }
}
