package com.khasna.cooker.ChefActivityFragments;

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

import com.khasna.cooker.ChefActivityFragments.Adapters.FragmentViewListedItemsChefAdapter;
import com.khasna.cooker.Common.Interfaces;
import com.khasna.cooker.Common.ProcessDialogBox;
import com.khasna.cooker.Models.Collection;
import com.khasna.cooker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by nachiket on 4/18/2017.
 */

public class FragmentViewListedItemsChef extends Fragment {

    private Switch mSwitch;
    private static DatabaseReference mDatabaseRef;
    private Collection mCollection;

    public FragmentViewListedItemsChef() {
        mCollection = Collection.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chef_view_listed_items, container, false);

        mSwitch = (Switch)view.findViewById(R.id.activeSwitch);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        final ProcessDialogBox mProcessDialogBox = new ProcessDialogBox(getActivity());
        mProcessDialogBox.ShowDialogBox();

        mCollection.mDataBaseFunctions.ReadDataBase(
                mDatabaseRef,
                "cookProfile/" + mCollection.mFireBaseFunctions.getuID() + "/isActive",
                new Interfaces.DataBaseReadInterface() {
                    @Override
                    public void ReadSucceeded(DataSnapshot dataSnapshot) {
                        Boolean isActive = (Boolean)dataSnapshot.getValue();
                        if (isActive == null){
                            isActive = false;
                        }
                        mSwitch.setChecked(isActive);
                        SetSwitchText(isActive);
                        mProcessDialogBox.DismissDialogBox();
                    }

                    @Override
                    public void ReadFailed(DatabaseError databaseError) {
                        mProcessDialogBox.DismissDialogBox();
                        Toast.makeText(getContext(),"Read failed" + databaseError.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }
        );

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
                if (isChecked && ChefEntity.chefProfile == null )
                {
                    Toast.makeText(getContext(),"Profile is empty",
                            Toast.LENGTH_LONG).show();
                    mCollection.mChefActivityFunctions.SetChefStatus(mDatabaseRef, false);
                    SetSwitchText(false);
                    buttonView.setChecked(false);
                    mSwitch.setChecked(false);
                    return;
                }
                else if (isChecked && (ChefEntity.chefProfile.getAddress().matches("") ||
                        ChefEntity.chefProfile.getPhoneNO().matches("") ||
                        ChefEntity.chefProfile.getCity().matches("") ||
                        ChefEntity.chefProfile.getfname().matches("") ||
                        ChefEntity.chefProfile.getZipcode().matches("") ||
                        ChefEntity.chefProfile.getlname().matches("")))
                {
                    Toast.makeText(getContext(),"Profile not set. Please go to Update Account",
                            Toast.LENGTH_LONG).show();
                    mCollection.mChefActivityFunctions.SetChefStatus(mDatabaseRef, false);
                    SetSwitchText(false);
                    buttonView.setChecked(false);
                    mSwitch.setChecked(false);
                    return;
                }

                mCollection.mChefActivityFunctions.SetChefStatus(mDatabaseRef, mSwitch.isChecked());
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
        mDatabaseRef.child("cookProfile").child(mCollection.mFireBaseFunctions.getuID()).child("items").
                child(itemName).child("isAvailable").setValue(isAvailable);

    }
}
