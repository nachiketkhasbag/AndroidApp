package com.khasna.cooker.GuestActivityFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.Toast;

import com.khasna.cooker.Common.Interfaces;
import com.khasna.cooker.Common.ProcessDialogBox;
import com.khasna.cooker.GuestActivityFragments.Adapters.ViewListedItemsGuestAdapter;
import com.khasna.cooker.Models.Collection;
import com.khasna.cooker.R;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by nachiket on 6/5/2017.
 */

public class ViewListedItemsGuest extends Fragment implements
        ViewListedItemsGuestAdapter.OnClickListener,
        Interfaces.ReadChefItemsInterface{

    View mView;

    private DatabaseReference mDataBaseRef;
    private Button buttonAddItemToCart;
    private SparseBooleanArray clickedItemPositions = new SparseBooleanArray();
    private int position;
    RecyclerView.LayoutManager mLayoutManager;
    ProcessDialogBox processDialogBox;
    Collection mCollection;
    ViewListedItemsGuestAdapter mAdapter;

    public ViewListedItemsGuest(){
        mCollection = Collection.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_guest_activity_view_listed_items, container, false);

        position = getArguments().getInt("position");

        final RecyclerView recyclerView = (RecyclerView)mView.findViewById(R.id.itemsListForGuest);
        mAdapter = new ViewListedItemsGuestAdapter(this);
        recyclerView.setAdapter(mAdapter);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        buttonAddItemToCart = (Button)mView.findViewById(R.id.buttonAddItemToCart);

        processDialogBox = new ProcessDialogBox(getActivity());
        processDialogBox.ShowDialogBox();

        mCollection.GetChefItems(position, this);

        buttonAddItemToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemsInCart = mCollection.GetCartItemArrayList().size();
                mCollection.AddItemToCart(clickedItemPositions, position);
                if(mCollection.GetCartItemArrayList().size() != itemsInCart)
                {
                    Toast.makeText(getContext(), "Item(s) added to cart", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(), "No item selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return mView;
    }

    @Override
    public void OnClick(View view, int position) {
        System.out.println("Item selected");
        CheckedTextView checkedTextView = (CheckedTextView)view.findViewById(R.id.text1);
        checkedTextView.toggle();
        clickedItemPositions.put(position, checkedTextView.isChecked());
    }

    @Override
    public void onStop() {
        super.onStop();
        mCollection.GetGuestItemArrayList().clear();
    }

    @Override
    public void ReadComplete() {
        mAdapter.notifyDataSetChanged();
        processDialogBox.DismissDialogBox();
    }

    @Override
    public void ReadFailed(String error) {
        System.out.println(error);
        mAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        processDialogBox.DismissDialogBox();
    }
}
