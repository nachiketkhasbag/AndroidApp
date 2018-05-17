package com.khasna.cooker.ChefActivityFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.khasna.cooker.ChefActivityFragments.ContainerClasses.ChefItem;
import com.khasna.cooker.Models.Collection;
import com.khasna.cooker.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by nachiket on 4/21/2017.
 */

public class FragmentDeleteItemChef extends Fragment {

    View mView;
    ListView mItemListView;
    SparseBooleanArray clickedItemPositions;
    Button buttonDeleteItem;
    private DatabaseReference mDataBaseRef;
    private ArrayList<String> itemName;
    private Collection mCollection;

    public FragmentDeleteItemChef() {
        mCollection = Collection.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_chef_delete_items, container, false);

        mDataBaseRef = FirebaseDatabase.getInstance().getReference().child("cookProfile").child(mCollection.mFireBaseFunctions.getuID());
        itemName = new ArrayList<>();
        for (ChefItem item : ChefEntity.ChefItemArrayList)
        {
            itemName.add(item.getitemName());
        }

        buttonDeleteItem = (Button)mView.findViewById(R.id.buttonDeleteItem);

        mItemListView = (ListView) mView.findViewById(R.id.itemsDeleteList);
        final ArrayAdapter<String> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_multiple_choice,
                itemName);
        mItemListView.setAdapter(adapter);
        clickedItemPositions = null;

        mItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickedItemPositions = mItemListView.getCheckedItemPositions();
            }
        });

        buttonDeleteItem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(clickedItemPositions != null) {
                    for (int index = clickedItemPositions.size() - 1; index >= 0; index--) {
                        // Get the checked status of the current item
                        boolean checked = clickedItemPositions.valueAt(index);

                        if (checked) {
                            // If the current item is checked
                            int key = clickedItemPositions.keyAt(index);
                            mCollection.mChefActivityFunctions.RemoveItemFromList(
                                    mDataBaseRef,
                                    key);
                            mItemListView.setItemChecked(key, false);
                            mItemListView.removeViewInLayout(mItemListView.getChildAt(key));
                            Toast.makeText(getContext(),"Items removed",Toast.LENGTH_LONG).show();
                        }
                    }
                }

                FragmentDeleteItemChef fragment = (FragmentDeleteItemChef)
                        getFragmentManager().findFragmentById(R.id.chefs_page);

                getFragmentManager().beginTransaction()
                        .detach(fragment)
                        .attach(fragment)
                        .commit();
        }
        });

        return mView;
    }
}
