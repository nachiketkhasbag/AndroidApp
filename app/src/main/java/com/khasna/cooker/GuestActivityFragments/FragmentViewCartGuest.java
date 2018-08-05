package com.khasna.cooker.GuestActivityFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.khasna.cooker.Common.CartItemAdapter;
import com.khasna.cooker.Common.Interfaces;
import com.khasna.cooker.Common.ProcessDialogBox;
import com.khasna.cooker.Models.Collection;
import com.khasna.cooker.R;

import java.math.BigDecimal;

/**
 * Created by nachiket on 6/14/2017.
 */

public class FragmentViewCartGuest extends Fragment implements
        CartItemAdapter.OnCLickListener,
        Interfaces.DataUploadInterface{

    RecyclerView listViewCartItems;
    TextView textViewTotalPrice;
    TextView textViewNumberOfItems;
    TextView textViewDollarSign3;
    TextView textViewTotalPriceLabel;
    TextView textViewNumberOfItemsLabel;
    Button buttonPlaceOrder;
    CartItemAdapter cartItemAdapter;
    View v;
    int clickCount;
    RecyclerView.LayoutManager mLayoutManager;

    ProcessDialogBox processDialogBox;
    Collection mCollection;

    public FragmentViewCartGuest() {
        mCollection = Collection.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_guest_cart_layout, container, false);

        processDialogBox = new ProcessDialogBox(getActivity());

        listViewCartItems               = v.findViewById(R.id.listViewCartItems);
        textViewDollarSign3             = v.findViewById(R.id.textViewDollarSign3);
        textViewTotalPrice              = v.findViewById(R.id.textViewTotalPrice);
        textViewTotalPriceLabel         = v.findViewById(R.id.textViewTotalPriceLabel);
        textViewNumberOfItems           = v.findViewById(R.id.textViewNumberOfItems);
        textViewNumberOfItemsLabel      = v.findViewById(R.id.textViewNumberOfItemsLabel);
        buttonPlaceOrder                = v.findViewById(R.id.buttonPlaceOrder);

        buttonPlaceOrder.setText("PLACE ORDER");

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        listViewCartItems.setLayoutManager(mLayoutManager);

        if (mCollection.GetCartItemArrayList().size() != 0)
        {
            cartItemAdapter = new CartItemAdapter(this);
            listViewCartItems.setAdapter(cartItemAdapter);
        }

        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCollection.IsClickCount2())
                {
                    buttonPlaceOrder.setText("PRESS AGAIN TO CONFIRM ORDER");
                }
                else if(mCollection.IsGuestProfileCheckPass())
                {
                    System.out.println("Guest information missing!!");
                    Toast.makeText( getContext(),"Please enter a name and phone number before you place an order ",
                            Toast.LENGTH_LONG ).show();
                }
                else
                {
                    System.out.println("Place Order!!");
                    processDialogBox.ShowDialogBox();
                    mCollection.DataUpload(FragmentViewCartGuest.this);
                }
            }
        });

        return v;
    }

    @Override
    public void RemoveOnClick(int itemPosition) {
        mCollection.GetCartItemArrayList().remove(itemPosition);
        cartItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void UpdateFields() {
        TextView buttonPlaceOrder = (TextView)v.findViewById(R.id.buttonPlaceOrder);

        clickCount = 0;
        buttonPlaceOrder.setText("PLACE ORDER");
        UpdateCartAndUI();
    }

    public void UpdateCartAndUI()
    {
        int numberOfItems = 0;

        TextView textViewTotalPrice = (TextView)v.findViewById(R.id.textViewTotalPrice);
        TextView textViewNumberOfItems = (TextView)v.findViewById(R.id.textViewNumberOfItems);

        BigDecimal itemPrice;
        BigDecimal totalValue = new BigDecimal("0");

        for (Integer index = 0; index < mCollection.GetCartItemArrayList().size(); index++)
        {
            itemPrice = new BigDecimal(mCollection.GetCartItemArrayList().get(index).getTotalPrice());

            totalValue = totalValue.add(itemPrice);
            numberOfItems += mCollection.GetCartItemArrayList().get(index).getItemQuantity();
        }

        textViewTotalPrice.setText(String.valueOf(totalValue));
        textViewNumberOfItems.setText(String.valueOf(numberOfItems));
    }

    @Override
    public void UploadComplete() {
        System.out.println("Order Placed successfully!!");
        Toast.makeText(getContext(), "Order placed! Please wait for order confirmation", Toast.LENGTH_SHORT).show();
        cartItemAdapter.notifyDataSetChanged();
        UpdateCartAndUI();
        buttonPlaceOrder.setText("PLACE ORDER");
        processDialogBox.DismissDialogBox();
    }

    @Override
    public void UploadFailed(String error) {
        processDialogBox.DismissDialogBox();
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }
}