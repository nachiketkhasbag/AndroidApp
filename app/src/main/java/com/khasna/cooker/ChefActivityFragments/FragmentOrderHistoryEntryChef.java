package com.khasna.cooker.ChefActivityFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khasna.cooker.ChefActivityFragments.Adapters.FragmentOrderHistoryEntryChefAdapter;
import com.khasna.cooker.R;

/**
 * Created by nachiket on 9/17/2017.
 */

public class FragmentOrderHistoryEntryChef extends Fragment {

    View mView;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    public FragmentOrderHistoryEntryChef() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_order_history_chef_order_details, container, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.order_history_order_details_recycler_view);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new FragmentOrderHistoryEntryChefAdapter();
        mRecyclerView.setAdapter(mAdapter);

        return mView;
    }
}
