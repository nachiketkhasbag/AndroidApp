package com.khasna.cooker.Models;

import android.util.SparseBooleanArray;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.khasna.cooker.Common.CartItem;
import com.khasna.cooker.Common.DebugClass;
import com.khasna.cooker.Common.Interfaces;
import com.khasna.cooker.GuestActivityFragments.ContainerClasses.ChefsListForGuest;
import com.khasna.cooker.GuestActivityFragments.ContainerClasses.GuestItem;
import com.khasna.cooker.GuestActivityFragments.ContainerClasses.GuestProfile;
import com.khasna.cooker.GuestActivityFragments.ContainerClasses.OrderHistoryGuestItem;
import com.khasna.cooker.GuestActivityFragments.GuestEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by nachiket on 5/10/2018.
 */

public class GuestActivityFunctions<G extends Collection> {

    G mGuestActivityFunctionsGeneric;
    private int mClickCount = 0;

    public GuestActivityFunctions(G g) {
        mGuestActivityFunctionsGeneric = g;
    }

    public void PushToken(DatabaseReference databaseReference)
    {
        String token = FirebaseInstanceId.getInstance().getToken();
        DebugClass.DebugPrint("GuestActivity", "PushToken:New push token");
        mGuestActivityFunctionsGeneric.mDataBaseFunctions.WriteToDataBase(databaseReference, "token", token);
    }

    public void GetAllGuestData(
            DatabaseReference databaseReference,
            final Interfaces.ReadGuestDataInterface readGuestDataInterface)
    {
        mGuestActivityFunctionsGeneric.mDataBaseFunctions.ReadDataBase(
                databaseReference,
                null,
                new Interfaces.DataBaseReadInterface() {
                    @Override
                    public void ReadSucceeded(DataSnapshot dataSnapshot) {
                        GuestEntity.guestProfile = getGuestProfile(dataSnapshot);
                        readGuestDataInterface.ReadComplete();
                    }

                    @Override
                    public void ReadFailed(DatabaseError databaseError) {
                        readGuestDataInterface.ReadFailed(databaseError);
                    }

                    private GuestProfile getGuestProfile(DataSnapshot dataSnapshot )
                    {
                        DataSnapshot snapshot= dataSnapshot.child("profile");
                        GuestProfile guestProfile = snapshot.getValue(GuestProfile.class);
                        if (guestProfile == null)
                        {
                            guestProfile = new GuestProfile();
                        }
                        System.out.println("Profile detected");
                        return guestProfile;
                    }
                }
        );
    }

    public void CleanObjects( DatabaseReference databaseReference)
    {
        GuestEntity.chefsListForGuestArrayList.clear();
        GuestEntity.guestProfile = null;
        GuestEntity.cartItemArrayList.clear();
        GuestEntity.guestItemArrayList.clear();
        GuestEntity.orderHistoryGuestItemDetails.clear();
        GuestEntity.orderHistoryGuestItemsArrayList.clear();

        mGuestActivityFunctionsGeneric.mDataBaseFunctions.WriteToDataBase(
                databaseReference,
                "token/",
                "");
    }

    public void GetActiveChefs(
            DatabaseReference databaseReference,
            final Interfaces.ReadActiveChefsInterface readActiveChefsInterface)
    {
        mGuestActivityFunctionsGeneric.mDataBaseFunctions.ReadDataBase(
                databaseReference,
                null,
                new Interfaces.DataBaseReadInterface() {
                    @Override
                    public void ReadSucceeded(DataSnapshot dataSnapshot) {
                        GuestEntity.chefsListForGuestArrayList.clear();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            try{
                                if(dataSnapshot.child(child.getKey()).child("isActive").getValue().toString().matches("true") ) {
                                    ChefsListForGuest chefsListForGuest = child.child("profile").getValue(ChefsListForGuest.class);
                                    chefsListForGuest.SetUnknownFields(dataSnapshot.child(child.getKey()).getKey());

                                    GuestEntity.chefsListForGuestArrayList.add(chefsListForGuest);
                                }
                            }
                            catch (Exception e)
                            {
                                System.out.println("Error retrieving chef details");
                            }
                        }
                        readActiveChefsInterface.ReadComplete();
                    }

                    @Override
                    public void ReadFailed(DatabaseError databaseError) {
                        readActiveChefsInterface.ReadFailed(databaseError.toString());
                    }
                }
        );
    }

    public void GetChefItems(
            DatabaseReference databaseReference,
            final Interfaces.ReadChefItemsInterface readChefItemsInterface)
    {
        mGuestActivityFunctionsGeneric.mDataBaseFunctions.ReadDataBase(
                databaseReference,
                null,
                new Interfaces.DataBaseReadInterface() {
                    @Override
                    public void ReadSucceeded(DataSnapshot dataSnapshot) {
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
                        }
                        readChefItemsInterface.ReadComplete();
                    }

                    @Override
                    public void ReadFailed(DatabaseError databaseError) {
                        readChefItemsInterface.ReadFailed(databaseError.toString());
                    }
                }
        );
    }

    public void AddItemToCart(SparseBooleanArray sparseBooleanArray, int position)
    {
        for(int index = 0; index < sparseBooleanArray.size(); index++){
            // Get the checked status of the current item
            boolean checked = sparseBooleanArray.valueAt(index);

            if(checked){
                // If the current item is checked
                int key = sparseBooleanArray.keyAt(index);
                System.out.println("Item selected to add to cart");

                CartItem cartItem = new CartItem(
                        GuestEntity.chefsListForGuestArrayList.get(position).getFullName(),
                        GuestEntity.chefsListForGuestArrayList.get(position).getuID(),
                        GuestEntity.guestProfile.getfname() + " " + GuestEntity.guestProfile.getlname(),
                        mGuestActivityFunctionsGeneric.mFireBaseFunctions.getuID(),
                        GuestEntity.chefsListForGuestArrayList.get(position).getFullAddress(),
                        1,
                        GuestEntity.guestItemArrayList.get(key).getitemName(),
                        GuestEntity.guestProfile.getPhoneNumber(),
                        GuestEntity.guestItemArrayList.get(key).getItemPrice(),
                        //R.string.dinnerPickUpTime,
                        "",
                        String.valueOf(GuestEntity.guestItemArrayList.get(key).getItemPrice()),
                        GuestEntity.chefsListForGuestArrayList.get(position).getPhoneNO(),
                        GuestEntity.guestProfile.getAddress() + " " + GuestEntity.guestProfile.getaptno() +
                                " " + GuestEntity.guestProfile.getCity() + " " + GuestEntity.guestProfile.getZipcode()
                );

                GuestEntity.cartItemArrayList.add(cartItem);
            }
        }
    }

    public boolean IsGuestProfileCheckPass()
    {
        return (GuestEntity.guestProfile.getfname().isEmpty() || GuestEntity.guestProfile.getPhoneNumber().isEmpty());
    }

    public void DataUpload(final Interfaces.DataUploadInterface dataUploadInterface)
    {
        int index = 0;
        Date trialTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy hh:mm:ss a");
        sdf.format(trialTime);
        String time = sdf.format(trialTime);

        while( index < GuestEntity.cartItemArrayList.size())
        {
            CartItem cartItem = GuestEntity.cartItemArrayList.get(index);
            String currentItemChefUid  = cartItem.getChefUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("liveOrders").child(currentItemChefUid);
            // setting the time
            cartItem.setOrderTime(time);
            String key = databaseReference.push().getKey();
            databaseReference.child(key).setValue(cartItem, new DatabaseReference.CompletionListener() {
                //Implies that the data has been committed
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError == null)
                    {
                        System.out.println("Chef Data saved successfully.");
                        System.out.println("Place Order!!");
                    }
                    else
                    {
                        dataUploadInterface.UploadFailed(databaseError.toString());
                    }
                }
            });
            index++;
        }
        GuestEntity.cartItemArrayList.clear();
        dataUploadInterface.UploadComplete();
    }

    public boolean IsClickCount2()
    {
        mClickCount++;

        if (mClickCount == 2)
        {
            mClickCount = 0;
            return false;
        }
        return true;
    }

    public void ReadGuestHistoryData(
            DatabaseReference databaseReference,
            final Interfaces.ReadGuestHistoryInterface readGuestHistoryInterface )
    {
        mGuestActivityFunctionsGeneric.mDataBaseFunctions.ReadDataBase(
                databaseReference,
                null,
                new Interfaces.DataBaseReadInterface() {
                    @Override
                    public void ReadSucceeded(DataSnapshot dataSnapshot) {
                        getItems(dataSnapshot);
                        readGuestHistoryInterface.ReadComplete();
                    }

                    @Override
                    public void ReadFailed(DatabaseError databaseError) {
                        readGuestHistoryInterface.ReadFailed(databaseError.toString());
                    }

                    public void getItems(DataSnapshot dataSnapshot)
                    {
                        GuestEntity.orderHistoryGuestItemsArrayList.clear();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            GuestEntity.orderHistoryGuestItemsArrayList.add(child.getValue(OrderHistoryGuestItem.class));
                        }

                        Collections.sort(GuestEntity.orderHistoryGuestItemsArrayList, new Comparator<OrderHistoryGuestItem>() {
                            @Override
                            public int compare(OrderHistoryGuestItem o1, OrderHistoryGuestItem o2) {
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
        );
    }
}
