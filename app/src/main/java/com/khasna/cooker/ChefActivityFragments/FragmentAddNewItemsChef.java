package com.khasna.cooker.ChefActivityFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.khasna.cooker.ChefActivityFragments.ContainerClasses.ChefItem;
import com.khasna.cooker.Common.Interfaces;
import com.khasna.cooker.Common.ProcessDialogBox;
import com.khasna.cooker.Models.Collection;
import com.khasna.cooker.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by nachiket on 4/18/2017.
 */

public class FragmentAddNewItemsChef extends Fragment {
    View mView;
    Button buttonAddNewItem;
    public EditText editTextItemName;
    public EditText editTextItemDescription;
    public EditText editTextItemIngredients;
    public EditText editTextItemPrice;
    private DatabaseReference mDataBaseRef;
    ProcessDialogBox processDialogBox;
    Collection mCollection;

    public FragmentAddNewItemsChef() {
        mCollection = Collection.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_chef_add_new_item, container, false);
        buttonAddNewItem = (Button)mView.findViewById(R.id.buttonAddNewItem);
        editTextItemName = (EditText)mView.findViewById(R.id.textViewChefName);
        editTextItemDescription = (EditText)mView.findViewById(R.id.textViewChefAddress);
        editTextItemIngredients = (EditText)mView.findViewById(R.id.textViewItemOrdered);
        editTextItemPrice = (EditText)mView.findViewById(R.id.textViewPrice);
        mDataBaseRef = FirebaseDatabase.getInstance().getReference().child("cookProfile").child(mCollection.mFireBaseFunctions.getuID());

        processDialogBox = new ProcessDialogBox(getActivity());

        buttonAddNewItem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String errorCode = ValidateInput(editTextItemName.getText().toString(),
                        editTextItemPrice.getText().toString());
                processDialogBox.ShowDialogBox();
                if (errorCode.equals("INVALID_INPUT"))
                {
                    processDialogBox.DismissDialogBox();
                    Toast.makeText(getContext(),"Invalid name or price",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                mCollection.mChefActivityFunctions.AddNewItem(
                        FragmentAddNewItemsChef.this,
                        mDataBaseRef,
                        new Interfaces.AddNewItemInterface() {
                    @Override
                    public void ItemAdded(ChefItem chefItem) {
                        processDialogBox.DismissDialogBox();
                        System.out.println("Chef Data saved successfully.");
                        ChefEntity.ChefItemArrayList.add(chefItem);

                        Toast.makeText(getContext(), "Item added " + editTextItemName.getText().toString(),
                                Toast.LENGTH_SHORT).show();
                        editTextItemName.setText("");
                        editTextItemDescription.setText("");
                        editTextItemIngredients.setText("");
                        editTextItemPrice.setText("");
                    }

                    @Override
                    public void ItemAddFailed(String error) {
                        processDialogBox.DismissDialogBox();
                        Toast.makeText(getContext(), error,
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        return mView;
    }

    public String ValidateInput(String editTextItemName, String editTextItemPrice )
    {
        String textPattern = "[a-zA-Z]";

        if (editTextItemName.equals("") |
            editTextItemPrice.equals("") |
            editTextItemPrice.matches(textPattern) )
        {
            return "INVALID_INPUT";
        }

        return "VALID_INPUT";
    }
}
