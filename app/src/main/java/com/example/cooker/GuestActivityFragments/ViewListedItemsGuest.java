package com.example.cooker.GuestActivityFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.Toast;

import com.example.cooker.Common.CartItem;
import com.example.cooker.Common.ProcessDialogBox;
import com.example.cooker.Common.UserInfo;
import com.example.cooker.GuestActivityFragments.Adapters.ViewListedItemsGuestAdapter;
import com.example.cooker.GuestActivityFragments.ContainerClasses.GuestItem;
import com.example.cooker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by nachiket on 6/5/2017.
 */

public class ViewListedItemsGuest extends Fragment implements ViewListedItemsGuestAdapter.OnClickListener {

    View mView;

    private ValueEventListener valueEventListener;
    private DatabaseReference mDataBaseRef;
    private Button buttonAddItemToCart;
    private SparseBooleanArray clickedItemPositions = new SparseBooleanArray();
    private int position;
    RecyclerView.LayoutManager mLayoutManager;
    ProcessDialogBox processDialogBox;

    public ViewListedItemsGuest(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_guest_activity_view_listed_items, container, false);

        position = getArguments().getInt("position");
        String uId = GuestEntity.chefsListForGuestArrayList.get(position).getuID();
        mDataBaseRef = FirebaseDatabase.getInstance().getReference("cookProfile").child(uId);

        final RecyclerView recyclerView = (RecyclerView)mView.findViewById(R.id.itemsListForGuest);
        final ViewListedItemsGuestAdapter adapter = new ViewListedItemsGuestAdapter(this);
        recyclerView.setAdapter(adapter);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        buttonAddItemToCart = (Button)mView.findViewById(R.id.buttonAddItemToCart);

        processDialogBox = new ProcessDialogBox(getActivity());
        processDialogBox.ShowDialogBox();

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.child("items").getChildren()) {
                    try{
                        GuestItem guestItem = child.getValue(GuestItem.class);

                        if(guestItem.getIsAvailable()) {
                            GuestEntity.guestItemArrayList.add(guestItem);
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println("Error in reading item");
                    }
                    adapter.notifyDataSetChanged();
                }
                processDialogBox.DismissDialogBox();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.toString());
            }
        };

        mDataBaseRef.addListenerForSingleValueEvent(valueEventListener);

        adapter.notifyDataSetChanged();

        buttonAddItemToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean toast = false;
                for(int index = 0; index < clickedItemPositions.size(); index++){
                    // Get the checked status of the current item
                    boolean checked = clickedItemPositions.valueAt(index);

                    if(checked){
                        // If the current item is checked
                        int key = clickedItemPositions.keyAt(index);
                        System.out.println("Item selected to add to cart");

                        CartItem cartItem = new CartItem(
                                GuestEntity.chefsListForGuestArrayList.get(position).getFullName(),
                                GuestEntity.chefsListForGuestArrayList.get(position).getuID(),
                                GuestEntity.guestProfile.getfname() + " " + GuestEntity.guestProfile.getlname(),
                                UserInfo.getuID(),
                                GuestEntity.chefsListForGuestArrayList.get(position).getFullAddress(),
                                1,
                                GuestEntity.guestItemArrayList.get(key).getitemName(),
                                GuestEntity.guestProfile.getPhoneNumber(),
                                GuestEntity.guestItemArrayList.get(key).getItemPrice(),
                                getString(R.string.dinnerPickUpTime)
                        );

                        GuestEntity.cartItemArrayList.add(cartItem);
                        toast = true;
                    }
                }
                if(toast)
                {
                    Toast.makeText(getContext(), "Item(s) added to cart", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(), "No item selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return mView;
    }

    @Override
    public void OnClick(View view, int position) {
        System.out.println("Item selected");
        CheckedTextView checkedTextView = (CheckedTextView)view.findViewById(R.id.text1);
        checkedTextView.toggle();
        clickedItemPositions.put(position, checkedTextView.isChecked());
    }

    @Override
    public void onStop() {
        super.onStop();
        GuestEntity.guestItemArrayList.clear();
    }
}
