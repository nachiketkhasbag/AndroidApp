package com.example.cooker.ChefActivityFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cooker.ChefActivityFragments.ContainerClasses.ChefItem;
import com.example.cooker.Common.Items;
import com.example.cooker.Common.ProcessDialogBox;
import com.example.cooker.R;
import com.example.cooker.Common.UserInfo;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by nachiket on 4/18/2017.
 */

public class FragmentAddNewItemsChef extends Fragment {
    View mView;
    Button buttonAddNewItem;
    EditText editTextItemName;
    EditText editTextItemDescription;
    EditText editTextItemIngredients;
    EditText editTextItemPrice;
    private DatabaseReference mDataBaseRef;
    ChefItem chefItem;
    ProcessDialogBox processDialogBox;

    public FragmentAddNewItemsChef() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_chef_add_new_item, container, false);
        buttonAddNewItem = (Button)mView.findViewById(R.id.buttonAddNewItem);
        editTextItemName = (EditText)mView.findViewById(R.id.editTextItemName);
        editTextItemDescription = (EditText)mView.findViewById(R.id.editTextItemDescription);
        editTextItemIngredients = (EditText)mView.findViewById(R.id.editTextItemIngredients);
        editTextItemPrice = (EditText)mView.findViewById(R.id.editTextItemPrice);
        mDataBaseRef = FirebaseDatabase.getInstance().getReference().child("cookProfile").child(UserInfo.getuID());

        processDialogBox = new ProcessDialogBox(getActivity());

        buttonAddNewItem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String errorCode = ValidateInput(editTextItemName.getText().toString(),
                        editTextItemPrice.getText().toString());
                if (errorCode.equals("INVALID_INPUT"))
                {
                    Toast.makeText(getContext(),"Invalid name or price",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                chefItem = new ChefItem(editTextItemName.getText().toString(), editTextItemDescription.getText().toString(),
                        editTextItemIngredients.getText().toString(), editTextItemPrice.getText().toString(), false);

                if(!checkForDuplicateNames(chefItem)) {
                    processDialogBox.ShowDialogBox();
                    mDataBaseRef.child("items").child(editTextItemName.getText().toString()).setValue(chefItem, new DatabaseReference.CompletionListener() {

                        //Implies that the data has been committed
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                System.out.println("Chef Data saved successfully.");
                                ChefEntity.ChefItemArrayList.add(chefItem);

                                Toast.makeText(getContext(), "Item added " + editTextItemName.getText().toString(),
                                        Toast.LENGTH_SHORT).show();
                                editTextItemName.setText("");
                                editTextItemDescription.setText("");
                                editTextItemIngredients.setText("");
                                editTextItemPrice.setText("");
                            } else {
                                Toast.makeText(getContext(), "There has been problem connecting to the server" +
                                                "Please try again in sometime ",
                                        Toast.LENGTH_LONG).show();
                            }
                            processDialogBox.DismissDialogBox();
                        }
                    });
                }
            }

            public boolean checkForDuplicateNames(Items inputItem){
                for (ChefItem item : ChefEntity.ChefItemArrayList)
                {
                    if (inputItem.getitemName().
                            equals(item.getitemName()))
                    {
                        Toast.makeText(getContext(),"Item already exists!",
                                Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                return false;
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
