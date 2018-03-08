package com.example.cooker.ChefActivityFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.cooker.ChefActivityFragments.Adapters.FragmentViewListedItemsChefAdapter;
import com.example.cooker.Common.ProcessDialogBox;
import com.example.cooker.R;
import com.example.cooker.Common.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by nachiket on 4/18/2017.
 */

public class FragmentViewListedItemsChef extends Fragment {

    private Switch mSwitch;
    private static DatabaseReference mDatabaseRef;

    public FragmentViewListedItemsChef() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chef_view_listed_items, container, false);

        mSwitch = (Switch)view.findViewById(R.id.activeSwitch);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        final ProcessDialogBox mProcessDialogBox = new ProcessDialogBox(getActivity());
        mProcessDialogBox.ShowDialogBox();

        mDatabaseRef.child("cookProfile").child(UserInfo.getuID()).
                child("isActive").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean isActive = (Boolean)dataSnapshot.getValue();
                if (isActive == null){
                    isActive = false;
                }
                mSwitch.setChecked(isActive);
                SetSwitchText(isActive);
                mProcessDialogBox.DismissDialogBox();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ListView itemListView = (ListView) view.findViewById(R.id.itemsList);
        FragmentViewListedItemsChefAdapter adapter = new FragmentViewListedItemsChefAdapter(getContext());
        itemListView.setAdapter(adapter);
        return view;
    }

    public void SetSwitchText(Boolean isActive)
    {
        if(isActive)
        {
            mSwitch.setText(mSwitch.getTextOn());
        }
        else
        {
            mSwitch.setText(mSwitch.getTextOff());
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && ChefEntity.chefProfile != null )
                {
                    if (ChefEntity.chefProfile.getAddress().matches("") ||
                            ChefEntity.chefProfile.getPhoneNO().matches("") ||
                            ChefEntity.chefProfile.getCity().matches("") ||
                            ChefEntity.chefProfile.getZipcode().matches(""))
                    {
                        Toast.makeText(getContext(),"Address and phone number not set. Please go to Update Account and set required fields ",
                                Toast.LENGTH_LONG).show();
                        buttonView.setChecked(false);
                        return;
                    }
                }
                else if (ChefEntity.chefProfile == null)
                {
                    Toast.makeText(getContext(),"Profile not set. Please go to Update Account",
                            Toast.LENGTH_LONG).show();
                    buttonView.setChecked(false);
                    return;
                }

                mDatabaseRef.child("cookProfile").child(UserInfo.getuID()).
                        child("isActive").setValue(mSwitch.isChecked());
                SetSwitchText(isChecked);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void UpdateItemState(String itemName, Boolean isAvailable)
    {
        //itemListCollectionIsAvailable.set(position, isAvailable);
        mDatabaseRef.child("cookProfile").child(UserInfo.getuID()).child("items").
                child(itemName).child("isAvailable").setValue(isAvailable);

    }
}
