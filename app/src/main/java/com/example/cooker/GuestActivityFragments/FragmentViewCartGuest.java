package com.example.cooker.GuestActivityFragments;

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

import com.example.cooker.Common.CartItem;
import com.example.cooker.Common.CartItemAdapter;
import com.example.cooker.Common.Interfaces;
import com.example.cooker.Common.ProcessDialogBox;
import com.example.cooker.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by nachiket on 6/14/2017.
 */

public class FragmentViewCartGuest extends Fragment implements CartItemAdapter.OnCLickListener{

    RecyclerView listViewCartItems;
    TextView textViewTotalPrice;
    TextView textViewNumberOfItems;
    TextView textViewDollarSign3;
    TextView textViewTotalPriceLabel;
    TextView textViewNumberOfItemsLabel;
    TextView textViewEmptyCart;
    CartItemAdapter cartItemAdapter;
    View v;
    int clickCount;
    String time;
    private DatabaseReference mDataBaseRef;
    private Interfaces.CartDataUpload cartDataUpload;
    RecyclerView.LayoutManager mLayoutManager;

    ProcessDialogBox processDialogBox;

    public FragmentViewCartGuest() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_guest_cart_layout, container, false);

        processDialogBox = new ProcessDialogBox(getActivity());

        listViewCartItems               = (RecyclerView)v.findViewById(R.id.listViewCartItems);
        textViewDollarSign3             = (TextView)v.findViewById(R.id.textViewDollarSign3);
        textViewTotalPrice              = (TextView)v.findViewById(R.id.textViewTotalPrice);
        textViewTotalPriceLabel         = (TextView)v.findViewById(R.id.textViewTotalPriceLabel);
        textViewNumberOfItems           = (TextView)v.findViewById(R.id.textViewNumberOfItems);
        textViewNumberOfItemsLabel      = (TextView)v.findViewById(R.id.textViewNumberOfItemsLabel);
        textViewEmptyCart               = (TextView)v.findViewById(R.id.textViewEmptyCart);
        final Button buttonPlaceOrder   = (Button)v.findViewById(R.id.buttonPlaceOrder);

        listViewCartItems.setVisibility(View.VISIBLE);
        textViewDollarSign3.setVisibility(View.VISIBLE);
        textViewTotalPrice.setVisibility(View.VISIBLE);
        textViewTotalPriceLabel.setVisibility(View.VISIBLE);
        textViewNumberOfItems.setVisibility(View.VISIBLE);
        textViewNumberOfItemsLabel.setVisibility(View.VISIBLE);
        textViewEmptyCart.setVisibility(View.GONE);
        buttonPlaceOrder.setVisibility(View.VISIBLE);
        buttonPlaceOrder.setText("PLACE ORDER");

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        listViewCartItems.setLayoutManager(mLayoutManager);

        if (GuestEntity.cartItemArrayList.size() != 0)
        {
            textViewEmptyCart.setVisibility(View.GONE);
            cartItemAdapter = new CartItemAdapter(this);
            listViewCartItems.setAdapter(cartItemAdapter);
        }
        else
        {
            listViewCartItems.setVisibility(View.GONE);
            textViewDollarSign3.setVisibility(View.GONE);
            textViewTotalPrice.setVisibility(View.GONE);
            textViewTotalPriceLabel.setVisibility(View.GONE);
            textViewNumberOfItems.setVisibility(View.GONE);
            textViewNumberOfItemsLabel.setVisibility(View.GONE);
            textViewEmptyCart.setVisibility(View.VISIBLE);
            buttonPlaceOrder.setVisibility(View.GONE);
        }

        clickCount = 0;
        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCount++;
                if(clickCount == 1)
                {
                    buttonPlaceOrder.setText("PRESS AGAIN TO CONFIRM ORDER");
                }
                else
                {
                    System.out.println("Place Order!!");
                    cartDataUpload.DataUploadFinished();
                }
            }
        });

        cartDataUpload = new Interfaces.CartDataUpload() {
            @Override
            public void DataUploadFinished() {
                int index = 0;
                Date trialTime = new Date();
                time = trialTime.toString();
                while( index < GuestEntity.cartItemArrayList.size())
                {
                    CartItem cartItem = GuestEntity.cartItemArrayList.get(index);
                    String currentItemChefUid  = cartItem.getChefUid();
                    mDataBaseRef = FirebaseDatabase.getInstance().getReference().child("liveOrders").child(currentItemChefUid);
                    // setting the time
                    cartItem.setOrderTime(time);
                    String key = mDataBaseRef.push().getKey();
                    processDialogBox.ShowDialogBox();
                    mDataBaseRef.child(key).setValue(cartItem, new DatabaseReference.CompletionListener() {
                        //Implies that the data has been committed
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null)
                            {
                                System.out.println("Chef Data saved successfully.");
                                System.out.println("Place Order!!");
                            }
                            else
                            {
                                Toast.makeText(getContext(),"There has been problem connecting to the server" +
                                                "Please try again in sometime ",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    index++;
                }
                if (index != 0)
                {
                    System.out.println("Order Placed successfully!!");
                    Toast.makeText(getContext(), "Order placed! Please wait for order confirmation", Toast.LENGTH_SHORT).show();
//                    GuestEntity.cartItemArrayList.clear();
                    // TODO This is inly for testing
                    clickCount = 0;
                    buttonPlaceOrder.setText("PLACE ORDER");
                    processDialogBox.DismissDialogBox();
                }
            }
        };

        return v;
    }

    @Override
    public void RemoveOnClick(int itemPosition) {
        GuestEntity.cartItemArrayList.remove(itemPosition);
        cartItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void UpdateFields() {
        int numberOfItems = 0;

        TextView textViewTotalPrice = (TextView)v.findViewById(R.id.textViewTotalPrice);
        TextView textViewNumberOfItems = (TextView)v.findViewById(R.id.textViewNumberOfItems);
        TextView buttonPlaceOrder = (TextView)v.findViewById(R.id.buttonPlaceOrder);

        BigDecimal itemPrice;
        BigDecimal itemQuantity;
        BigDecimal itemValue;
        BigDecimal totalValue = new BigDecimal("0");

        for (Integer index = 0; index < GuestEntity.cartItemArrayList.size(); index++)
        {
            itemPrice = new BigDecimal(GuestEntity.cartItemArrayList.get(index).getPrice());
            itemQuantity = new BigDecimal(GuestEntity.cartItemArrayList.get(index).getItemQuantity());
            itemValue = itemPrice.multiply(itemQuantity);

            totalValue = totalValue.add(itemValue);
            numberOfItems += GuestEntity.cartItemArrayList.get(index).getItemQuantity();
        }

        textViewTotalPrice.setText(String.valueOf(totalValue));
        textViewNumberOfItems.setText(String.valueOf(numberOfItems));

        if(clickCount > 0)
        {
            clickCount--;
            buttonPlaceOrder.setText("PLACE ORDER");
        }
    }
}