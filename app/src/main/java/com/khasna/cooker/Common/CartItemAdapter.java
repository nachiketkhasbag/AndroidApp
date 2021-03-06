package com.khasna.cooker.Common;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.khasna.cooker.Models.Collection;
import com.khasna.cooker.R;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by nachiket on 6/12/2017.
 */

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder>{

    private OnCLickListener mOnCLickListener;
    private SparseArray<String> orderTimeOptions = new SparseArray<>();
    private Collection mCollection;

    public interface OnCLickListener{
        void RemoveOnClick(int itemPosition);
        void UpdateFields();
        void RefreshLayout();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        public TextView textViewCartItemName;
        public TextView textViewCartChefName;
        public TextView textViewChefAddress;
        public TextView textViewItemPriceForGuest;
        public TextView textViewRemoveItemLink;
        public Button buttonIncrease;
        public Button buttonDecrease;
        public TextView textViewCartItemAdapterQuantity;
        public ArrayAdapter adapter;
        public RadioButton radioButton0;
        public RadioButton radioButton1;
        public RadioButton radioButton2;

        public ViewHolder(View v) {
            super(v);
            textViewCartItemName        = (TextView) v.findViewById(R.id.textViewCartItemName);
            textViewCartChefName        = (TextView) v.findViewById(R.id.textViewCartChefName);
            textViewChefAddress         = (TextView) v.findViewById(R.id.textViewChefAddress);
            textViewItemPriceForGuest   = (TextView) v.findViewById(R.id.textViewItemPriceForGuest);
            textViewRemoveItemLink      = (TextView) v.findViewById(R.id.textViewRemoveItemLink);
            radioButton0 = (RadioButton) v.findViewById(R.id.radioButton0);
            radioButton1 = (RadioButton) v.findViewById(R.id.radioButton1);
            radioButton2 = (RadioButton) v.findViewById(R.id.radioButton2);
            buttonIncrease              = (Button) v.findViewById(R.id.buttonIncrease);
            buttonDecrease              = (Button) v.findViewById(R.id.buttonDecrease);
            textViewCartItemAdapterQuantity              = (TextView) v.findViewById(R.id.textViewCartItemAdapterQuantity);

            adapter                     = ArrayAdapter.createFromResource(v.getContext(), R.array.quantity, android.R.layout.simple_spinner_item);
            radioButton0.setChecked(true);
        }
    }

    public CartItemAdapter(OnCLickListener onCLickListener) {
        mOnCLickListener = onCLickListener;
        mCollection = Collection.getInstance();
    }

    @Override
    public CartItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        CartItemAdapter.ViewHolder vh = new CartItemAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CartItemAdapter.ViewHolder holder, int position) {
        final int itemPosition = position;
        holder.textViewCartItemName.setText(mCollection.GetCartItemArrayList().get(position).getItemName());
        holder.textViewCartChefName.setText(mCollection.GetCartItemArrayList().get(position).getChefName());
        holder.textViewChefAddress.setText(mCollection.GetCartItemArrayList().get(position).getChefAddress());
        holder.textViewItemPriceForGuest.setText(String.valueOf(mCollection.GetCartItemArrayList().get(position).getItemPrice()));
        AdjustDatesForRadioButtons();
        holder.radioButton0.setText(orderTimeOptions.get(0));
        holder.radioButton1.setText(orderTimeOptions.get(1));
        holder.radioButton2.setText(orderTimeOptions.get(2));
        holder.textViewCartItemAdapterQuantity.setText(String.format("%s", mCollection.GetCartItemArrayList().get(position).getItemQuantity()));
        SetRadioButton(holder, itemPosition);
        mOnCLickListener.UpdateFields();

        holder.buttonIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = mCollection.GetCartItemArrayList().get(itemPosition).getItemQuantity();
                quantity++;
                mCollection.GetCartItemArrayList().get(itemPosition).setItemQuantity(quantity);
                UpdateTotal(itemPosition);
                mOnCLickListener.RefreshLayout();
            }
        });

        holder.buttonDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = mCollection.GetCartItemArrayList().get(itemPosition).getItemQuantity();
                if(quantity > 1){
                    quantity--;
                    mCollection.GetCartItemArrayList().get(itemPosition).setItemQuantity(quantity);
                    UpdateTotal(itemPosition);
                    mOnCLickListener.RefreshLayout();
                }
            }
        });

        holder.textViewRemoveItemLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCLickListener.RemoveOnClick(itemPosition);
                mOnCLickListener.UpdateFields();
                mOnCLickListener.RefreshLayout();
            }
        });

        holder.radioButton0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCollection.GetCartItemArrayList().get(itemPosition).setPickUpTime(buttonView.getText().toString());
                    mOnCLickListener.UpdateFields();
                    mOnCLickListener.RefreshLayout();
                }
            }
        });

        holder.radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCollection.GetCartItemArrayList().get(itemPosition).setPickUpTime(buttonView.getText().toString());
                    mOnCLickListener.UpdateFields();
                    mOnCLickListener.RefreshLayout();
                }
            }
        });

        holder.radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCollection.GetCartItemArrayList().get(itemPosition).setPickUpTime(buttonView.getText().toString());
                    mOnCLickListener.UpdateFields();
                    mOnCLickListener.RefreshLayout();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCollection.GetCartItemArrayList().size();
    }

    private void UpdateTotal(int itemPosition)
    {
        BigDecimal itemValue = new BigDecimal(mCollection.GetCartItemArrayList().get(itemPosition).getItemPrice());
        BigDecimal itemQuantity = new BigDecimal(mCollection.GetCartItemArrayList().get(itemPosition).getItemQuantity());

        itemValue = itemValue.multiply(itemQuantity);
        mCollection.GetCartItemArrayList().get(itemPosition).setTotalPrice(String.valueOf(itemValue));
    }
    private void SetRadioButton(CartItemAdapter.ViewHolder holder, int position)
    {
        String pickupTIme = mCollection.GetCartItemArrayList().get(position).getPickUpTime();

        if( holder.radioButton1.getText().equals(pickupTIme))
        {
            holder.radioButton1.setChecked(true);
            mCollection.GetCartItemArrayList().get(position).setPickUpTime(orderTimeOptions.get(1));
        }
        else if( holder.radioButton2.getText().equals(pickupTIme))
        {
            holder.radioButton2.setChecked(true);
            mCollection.GetCartItemArrayList().get(position).setPickUpTime(orderTimeOptions.get(2));
        }
        else {
            holder.radioButton0.setChecked(true);
            mCollection.GetCartItemArrayList().get(position).setPickUpTime(orderTimeOptions.get(0));
        }
    }

    private void AdjustDatesForRadioButtons()
    {
        Calendar now = Calendar.getInstance();
        Date calendarDateTime = now.getTime();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int lastHourLunch = 10;
        int lastHourDinner = 18;

        if (hour < lastHourLunch)
        {
            SimpleDateFormat sdfTemp = new SimpleDateFormat("EEE, dd MMM ");
            String option0 = sdfTemp.format(calendarDateTime);
            option0 = option0 + "10 am - 11 am";

            orderTimeOptions.put(0, option0);

            String option1 = sdfTemp.format(calendarDateTime);
            option1 = option1 + "6 pm - 8 pm";

            orderTimeOptions.put(1, option1);

            now.add(Calendar.DATE, 1);
            Date calendarTemp = now.getTime();
            String option2 = sdfTemp.format(calendarTemp);
            option2 = option2 + "10 am - 11 am";

            orderTimeOptions.put(2, option2);

        }
        else if( hour < lastHourDinner )
        {
            SimpleDateFormat sdfTemp = new SimpleDateFormat("EEE, dd MMM ");
            String option0 = sdfTemp.format(calendarDateTime);
            option0 = option0 + "6 pm - 8 pm";

            orderTimeOptions.put(0, option0);

            now.add(Calendar.DATE, 1);
            Date calendarTemp = now.getTime();
            String option1 = sdfTemp.format(calendarTemp);
            option1 = option1 + "10 am - 11 am";

            orderTimeOptions.put(1, option1);

            String option2 = sdfTemp.format(calendarTemp);
            option2 = option2 + "6 pm - 8 pm";

            orderTimeOptions.put(2, option2);
        }
        else
        {
            SimpleDateFormat sdfTemp = new SimpleDateFormat("EEE, dd MMM ");

            now.add(Calendar.DATE, 1);
            Date calendarTemp = now.getTime();

            String option0 = sdfTemp.format(calendarTemp);
            option0 = option0 + "10 am - 11 am";

            orderTimeOptions.put(0, option0);

            String option1 = sdfTemp.format(calendarTemp);
            option1 = option1 + "6 pm - 8 pm";

            orderTimeOptions.put(1, option1);

            now.add(Calendar.DATE, 1);
            calendarTemp = now.getTime();
            String option2 = sdfTemp.format(calendarTemp);
            option2 = option2 + "10 am - 11 am";

            orderTimeOptions.put(2, option2);
        }
    }
}
