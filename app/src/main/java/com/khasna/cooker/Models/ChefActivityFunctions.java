package com.khasna.cooker.Models;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.khasna.cooker.ChefActivityFragments.ChefEntity;
import com.khasna.cooker.ChefActivityFragments.ContainerClasses.ChefItem;
import com.khasna.cooker.ChefActivityFragments.ContainerClasses.ChefProfile;
import com.khasna.cooker.ChefActivityFragments.ContainerClasses.ChefReceivedOrderItem;
import com.khasna.cooker.ChefActivityFragments.ContainerClasses.OrderHistoryChefItem;
import com.khasna.cooker.ChefActivityFragments.FragmentAddNewItemsChef;
import com.khasna.cooker.Common.DebugClass;
import com.khasna.cooker.Common.Interfaces;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by nachiket on 5/2/2018.
 */

public class ChefActivityFunctions<G extends Collection> {

    G mChefActivityFunctionsGeneric;

    public ChefActivityFunctions(G chefActivityFunctionsGeneric) {
        mChefActivityFunctionsGeneric = chefActivityFunctionsGeneric;
    }

    public void GetAllChefData(
            DatabaseReference databaseReference,
            final Interfaces.ReadChefDataInterface readChefDataInterface)
    {

        mChefActivityFunctionsGeneric.mDataBaseFunctions.ReadDataBase(
                databaseReference,
                "",
                new Interfaces.DataBaseReadInterface() {
            @Override
            public void ReadSucceeded(DataSnapshot dataSnapshot) {
                getExistingItems(dataSnapshot);
//                Fragment mActiveFragment;

                ChefEntity.chefProfile = getChefProfile(dataSnapshot);
                String message = "start";

                readChefDataInterface.ReadComplete("start");
            }

            @Override
            public void ReadFailed(DatabaseError databaseError) {
                readChefDataInterface.ReadFailed(databaseError);
            }

            private void getExistingItems(DataSnapshot dataSnapshot){
                ChefEntity.ChefItemArrayList.clear();
                for (DataSnapshot child : dataSnapshot.child("items").getChildren()) {
                    ChefItem chefItem = child.getValue(ChefItem.class);
                    ChefEntity.ChefItemArrayList.add(chefItem);
                    System.out.println("Data detected");
                }
            }

            private ChefProfile getChefProfile(DataSnapshot dataSnapshot){
                DataSnapshot snapshot= dataSnapshot.child("profile");
                ChefProfile chefProfile = snapshot.getValue(ChefProfile.class);
                if (chefProfile == null)
                {
                    chefProfile = new ChefProfile();
                }
                System.out.println("Profile detected");
                return chefProfile;
            }
        });
    }

    public void PushToken(DatabaseReference databaseReference)
    {
        String token = FirebaseInstanceId.getInstance().getToken();
        DebugClass.DebugPrint("ChefActivity", "PushToken:New push token");
        mChefActivityFunctionsGeneric.mDataBaseFunctions.WriteToDataBase(databaseReference, "token", token);
    }

    public void CleanObjects(DatabaseReference databaseReference)
    {
        ChefEntity.ChefItemArrayList.clear();
        ChefEntity.chefProfile = null;
        ChefEntity.chefReceivedOrderItemsArrayList.clear();
        ChefEntity.arrayListOrderHistoryChefItem.clear();
        ChefEntity.arrayListOrderHistoryChefItemDetails.clear();

        mChefActivityFunctionsGeneric.mDataBaseFunctions.WriteToDataBase(databaseReference,"isActive", false);
        mChefActivityFunctionsGeneric.mDataBaseFunctions.WriteToDataBase(databaseReference,"token", "");
    }

    public void AddNewItem(
            FragmentAddNewItemsChef fragmentAddNewItemsChef,
            DatabaseReference databaseReference,
            final Interfaces.AddNewItemInterface addNewItemInterface)
    {
        final ChefItem chefItem = new ChefItem(
                fragmentAddNewItemsChef.editTextItemName.getText().toString(),
                fragmentAddNewItemsChef.editTextItemDescription.getText().toString(),
                fragmentAddNewItemsChef.editTextItemIngredients.getText().toString(),
                fragmentAddNewItemsChef.editTextItemPrice.getText().toString(),
                false);

        if (CheckForDuplicateNames(chefItem))
        {
            addNewItemInterface.ItemAddFailed("Item already exists");
            return;
        }

        mChefActivityFunctionsGeneric.mDataBaseFunctions.WriteToDataBase(
                databaseReference,
                "items/" + fragmentAddNewItemsChef.editTextItemName.getText().toString(),
                chefItem,
                new Interfaces.DataBaseWriteInterface() {
            @Override
            public void WriteSucceeded() {
                addNewItemInterface.ItemAdded(chefItem);
            }

            @Override
            public void WriteFailed(DatabaseError databaseError) {
                addNewItemInterface.ItemAddFailed("There has been problem connecting to the server" +
                        "Please try again in sometime ");
                addNewItemInterface.ItemAddFailed(databaseError.toString());
            }
        });
    }

    public void SetChefStatus(DatabaseReference databaseReference, boolean isActive)
    {
        mChefActivityFunctionsGeneric.mDataBaseFunctions.WriteToDataBase(
                databaseReference,
                "cookProfile/" + mChefActivityFunctionsGeneric.mFireBaseFunctions.getuID() + "/isActive",
                isActive );
    }

    public void RemoveItemFromList(
            DatabaseReference databaseReference,
            int key )
    {
        RemoveFromDataBase(databaseReference, ChefEntity.ChefItemArrayList.get(key).getitemName());
        ChefEntity.ChefItemArrayList.remove(key);
    }

    public void GetPendingItems(DatabaseReference databaseReference, final Interfaces.PendingItemsReadInterface pendingItemsReadInterface)
    {
        mChefActivityFunctionsGeneric.mDataBaseFunctions.ReadDataBase(
                databaseReference,
                "cookProfile/" + mChefActivityFunctionsGeneric.mFireBaseFunctions.getuID() + "/orderHistory",
                new Interfaces.DataBaseReadInterface() {
                    @Override
                    public void ReadSucceeded(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            OrderHistoryChefItem orderHistoryChefItem = child.getValue(OrderHistoryChefItem.class);
                            if (orderHistoryChefItem != null) {
                                if (!orderHistoryChefItem.getStatus().equals("pending")) {
                                    ChefEntity.arrayListOrderHistoryChefItem.add(orderHistoryChefItem);
                                }

                                Collections.sort(ChefEntity.arrayListOrderHistoryChefItem, new Comparator<OrderHistoryChefItem>() {
                                    @Override
                                    public int compare(OrderHistoryChefItem o1, OrderHistoryChefItem o2) {
                                        Date date1;
                                        Date date2;
                                        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy hh:mm:ss a");
                                        try {
                                            date1 = sdf.parse(o1.getOrderTime());
                                            date2 = sdf.parse(o2.getOrderTime());

                                            if( date1.compareTo(date2) <= 0 )
                                            {
                                                return 1;
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        return -1;
                                    }
                                });
                            }
                        }
                        pendingItemsReadInterface.ReadSuccessful();
                    }

                    @Override
                    public void ReadFailed(DatabaseError dataBaseError) {
                        pendingItemsReadInterface.ReadFailed(dataBaseError.toString());
                    }
                }
        );
    }

    public void GetReceivedOrders(DatabaseReference databaseReference, final Interfaces.ReceivedOrdersInterface receivedOrdersInterface)
    {
        mChefActivityFunctionsGeneric.mDataBaseFunctions.ReadDataBase(
                databaseReference,
                null,
                new Interfaces.DataBaseReadInterface() {
            @Override
            public void ReadSucceeded(DataSnapshot dataSnapshot) {
                getItems(dataSnapshot);
                receivedOrdersInterface.ReadSuccessful();
            }

            @Override
            public void ReadFailed(DatabaseError databaseError) {
                receivedOrdersInterface.ReadFailed(databaseError.toString());
            }

            private void getItems(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    ChefReceivedOrderItem chefCurrentOrderItem = child.getValue(ChefReceivedOrderItem.class);
                    chefCurrentOrderItem.setItemKey(child.getKey());
                    ChefEntity.chefReceivedOrderItemsArrayList.add(chefCurrentOrderItem);
                }
            }
        });
    }

    public void SetOrderStatus(
            DatabaseReference databaseReference,
            String status,
            final int itemPosition,
            final Interfaces.OrderStatusInterface orderStatusInterface)
    {
        mChefActivityFunctionsGeneric.mDataBaseFunctions.WriteToDataBase(
                databaseReference,
                null,
                status,
                new Interfaces.DataBaseWriteInterface() {
                    @Override
                    public void WriteSucceeded() {
                        orderStatusInterface.OrderSetStatusSuccessful();
                        ChefEntity.chefReceivedOrderItemsArrayList.remove(itemPosition);
                    }

                    @Override
                    public void WriteFailed(DatabaseError databaseError) {
                        orderStatusInterface.OrderSetStatusFailed(databaseError.toString());
                    }
                }
        );
    }

    private void RemoveFromDataBase(DatabaseReference databaseReference, String itemName)
    {
        databaseReference.child("items").child(itemName).removeValue();
    }

    private boolean CheckForDuplicateNames(ChefItem inputItem){
        for (ChefItem item : ChefEntity.ChefItemArrayList)
        {
            if (inputItem.getitemName().
                    equals(item.getitemName()))
            {
                return true;
            }
        }
        return false;
    }
}
