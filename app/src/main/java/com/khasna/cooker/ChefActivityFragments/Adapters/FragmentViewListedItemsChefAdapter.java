package com.khasna.cooker.ChefActivityFragments.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.khasna.cooker.ChefActivityFragments.ChefEntity;
import com.khasna.cooker.ChefActivityFragments.FragmentViewListedItemsChef;
import com.khasna.cooker.R;

/**
 * Created by nachiket on 4/26/2017.
 */

public class FragmentViewListedItemsChefAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    Switch mSwitchItemAvailable;
    FragmentViewListedItemsChef fragmentViewListedItemsChef = new FragmentViewListedItemsChef();

    public FragmentViewListedItemsChefAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //1
    @Override
    public int getCount() {
        return ChefEntity.ChefItemArrayList.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return ChefEntity.ChefItemArrayList.get(position).getitemName();
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get view for row item

        View itemDetails = mInflater.inflate(R.layout.chef_item_layout, parent, false);

        TextView textViewItemName = (TextView) itemDetails.findViewById(R.id.textViewChefNameLabel);
        TextView textViewItemDescription = (TextView) itemDetails.findViewById(R.id.textViewChefAddressLabel);
        TextView textViewItemIngredients = (TextView) itemDetails.findViewById(R.id.textViewItemOrderedLabel);
        TextView textViewItemPrice = (TextView) itemDetails.findViewById(R.id.textViewItemPriceLabel);
        mSwitchItemAvailable = (Switch) itemDetails.findViewById(R.id.switchItemAvailable);

        textViewItemName.setText(ChefEntity.ChefItemArrayList.get(position).getitemName());
        textViewItemDescription.setText(ChefEntity.ChefItemArrayList.get(position).getitemDescription());
        textViewItemIngredients.setText(ChefEntity.ChefItemArrayList.get(position).getItemIngredients());
        textViewItemPrice.setText(ChefEntity.ChefItemArrayList.get(position).getItemPrice());
        mSwitchItemAvailable.setChecked(ChefEntity.ChefItemArrayList.get(position).getIsAvailable());

        SetSwitchText(mSwitchItemAvailable, ChefEntity.ChefItemArrayList.get(position).getIsAvailable());

        mSwitchItemAvailable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fragmentViewListedItemsChef.UpdateItemState(ChefEntity.ChefItemArrayList.get(position).getitemName(), isChecked);
                ChefEntity.ChefItemArrayList.get(position).setIsAvailable(isChecked);
                SetSwitchText(buttonView, isChecked);
            }
        });

        return itemDetails;
    }

    public void SetSwitchText(CompoundButton buttonView, boolean isChecked)
    {
        if(isChecked)
        {
            buttonView.setText(((Switch)buttonView).getTextOn());
        }
        else
        {
            buttonView.setText(((Switch)buttonView).getTextOff());
        }
    }
}
