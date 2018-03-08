package com.example.cooker.Common;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cooker.GuestActivityFragments.GuestEntity;
import com.example.cooker.R;


/**
 * Created by nachiket on 6/12/2017.
 */

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder>{

    private OnCLickListener mOnCLickListener;

    public interface OnCLickListener{
        void RemoveOnClick(int itemPosition);
        void UpdateFields();
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
        public Spinner spinner;
        public ArrayAdapter adapter;
        public RadioButton radioButtonLunch;
        public RadioButton radioButtonDinner;

        public ViewHolder(View v) {
            super(v);
            textViewCartItemName        = (TextView) v.findViewById(R.id.textViewCartItemName);
            textViewCartChefName        = (TextView) v.findViewById(R.id.textViewCartChefName);
            textViewChefAddress         = (TextView) v.findViewById(R.id.textViewChefAddress);
            textViewItemPriceForGuest   = (TextView) v.findViewById(R.id.textViewItemPriceForGuest);
            textViewRemoveItemLink      = (TextView) v.findViewById(R.id.textViewRemoveItemLink);
            radioButtonLunch            = (RadioButton) v.findViewById(R.id.radioButtonLunch);
            radioButtonDinner           = (RadioButton) v.findViewById(R.id.radioButtonDinner);
            spinner                     = (Spinner) v.findViewById(R.id.spinner);

            adapter                     = ArrayAdapter.createFromResource(v.getContext(), R.array.quantity, android.R.layout.simple_spinner_item);
            spinner.setAdapter(adapter);
            radioButtonDinner.setChecked(true);
        }
    }

    public CartItemAdapter(OnCLickListener onCLickListener) {
        mOnCLickListener = onCLickListener;
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
        holder.textViewCartItemName.setText(GuestEntity.cartItemArrayList.get(position).getItemName());
        holder.textViewCartChefName.setText(GuestEntity.cartItemArrayList.get(position).getChefName());
        holder.textViewChefAddress.setText(GuestEntity.cartItemArrayList.get(position).getChefAddress());
        holder.textViewItemPriceForGuest.setText(String.valueOf(GuestEntity.cartItemArrayList.get(position).getPrice()));
        holder.spinner.setSelection(GuestEntity.cartItemArrayList.get(position).getItemQuantity() - 1);

        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GuestEntity.cartItemArrayList.get(itemPosition).setItemQuantity(position+1);
                mOnCLickListener.UpdateFields();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setSelection(0);
            }
        });

        holder.textViewRemoveItemLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCLickListener.RemoveOnClick(itemPosition);
                mOnCLickListener.UpdateFields();
            }
        });

        holder.radioButtonLunch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    GuestEntity.cartItemArrayList.get(itemPosition).setPickUpTime(buttonView.getText().toString());
                }
            }
        });

        holder.radioButtonDinner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    GuestEntity.cartItemArrayList.get(itemPosition).setPickUpTime(buttonView.getText().toString());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return GuestEntity.cartItemArrayList.size();
    }
}
