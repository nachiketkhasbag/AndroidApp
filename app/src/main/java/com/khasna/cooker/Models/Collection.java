package com.khasna.cooker.Models;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.SparseBooleanArray;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.khasna.cooker.Common.CartItem;
import com.khasna.cooker.Common.Interfaces;
import com.khasna.cooker.GuestActivityFragments.ContainerClasses.ChefsListForGuest;
import com.khasna.cooker.GuestActivityFragments.ContainerClasses.GuestItem;
import com.khasna.cooker.GuestActivityFragments.ContainerClasses.GuestProfile;
import com.khasna.cooker.GuestActivityFragments.ContainerClasses.OrderHistoryGuestItem;
import com.khasna.cooker.GuestActivityFragments.ContainerClasses.OrderHistoryGuestItemDetails;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by nachiket on 4/27/2018.
 */

public class Collection{

    private static Collection single_instance = null;

    private int mClickCount = 0;

    private GuestProfile mGuestProfile;
    private ArrayList<ChefsListForGuest> mChefsListForGuestArrayList = new ArrayList<>();
    private ArrayList<GuestItem> mGuestItemArrayList = new ArrayList<>();
    private ArrayList<CartItem> mCartItemArrayList = new ArrayList<>();
    private ArrayList<OrderHistoryGuestItem> mOrderHistoryGuestItemsArrayList = new ArrayList<>();
    private ArrayList<OrderHistoryGuestItemDetails> mOrderHistoryGuestItemDetails = new ArrayList<>();

    protected FireBaseFunctions mFireBaseFunctions;
    protected DataBaseFunctions mDataBaseFunctions;
    protected FireBaseStorageFunctions mFireBaseStorageFunctions;
    protected Context mContext;

    private Collection(Context context) {
        mFireBaseFunctions = new FireBaseFunctions<>(this);
        mFireBaseStorageFunctions = new FireBaseStorageFunctions<>(this);
        mContext = context;
    }

    // static method to create instance of Singleton class
    public static Collection getInstance()
    {
        if (single_instance == null)
            single_instance = new Collection(null);

        return single_instance;
    }

    public static Collection getInstance(Context context)
    {
        if (single_instance == null)
            single_instance = new Collection(context);

        return single_instance;
    }

    public ArrayList<OrderHistoryGuestItemDetails> GetOrderHistoryGuestItemDetails() {
        return mOrderHistoryGuestItemDetails;
    }

    public void SetOrderHistoryGuestItemDetails(ArrayList<OrderHistoryGuestItemDetails> mOrderHistoryGuestItemDetails) {
        this.mOrderHistoryGuestItemDetails = mOrderHistoryGuestItemDetails;
    }

    public ArrayList<OrderHistoryGuestItem> GetOrderHistoryGuestItemsArrayList() {
        return mOrderHistoryGuestItemsArrayList;
    }

    public void SetOrderHistoryGuestItemsArrayList(ArrayList<OrderHistoryGuestItem> mOrderHistoryGuestItemsArrayList) {
        this.mOrderHistoryGuestItemsArrayList = mOrderHistoryGuestItemsArrayList;
    }

    public ArrayList<CartItem> GetCartItemArrayList() {
        return mCartItemArrayList;
    }

    public void SetCartItemArrayList(ArrayList<CartItem> mCartItemArrayList) {
        this.mCartItemArrayList = mCartItemArrayList;
    }

    public ArrayList<ChefsListForGuest> GetChefsListForGuest()
    {
        return mChefsListForGuestArrayList;
    }

    public ArrayList<GuestItem> GetGuestItemArrayList() {
        return mGuestItemArrayList;
    }

    public void SetGuestItemArrayList(ArrayList<GuestItem> mGuestItemArrayList) {
        this.mGuestItemArrayList = mGuestItemArrayList;
    }

    public GuestProfile GetGuestProfile()
    {
        return mGuestProfile;
    }

    public FirebaseUser GetFireBaseUser()
    {
        return mFireBaseFunctions.getFireBaseUser();
    }

    public void WaitForUserLogin(Interfaces.AppUserInterface userInterface)
    {
        mFireBaseFunctions.WaitForUserLogin(userInterface);
    }

    public void InitDatabase()
    {
        mDataBaseFunctions = new DataBaseFunctions<>(this);
    }

    public void FillGuestProfile(final Interfaces.DataBaseReadInterface dataBaseReadInterface)
    {
        mDataBaseFunctions.TempReadDataBase(null,
                new Interfaces.DataBaseReadInterface() {
                    @Override
                    public void ReadSucceeded(DataSnapshot dataSnapshot) {
                        mGuestProfile = getGuestProfile(dataSnapshot);
                        dataBaseReadInterface.ReadSucceeded(dataSnapshot);
                    }

                    @Override
                    public void ReadFailed(DatabaseError databaseError) {

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
                });
    }

    public void PushToken()
    {
        String token = FirebaseInstanceId.getInstance().getToken();
        mDataBaseFunctions.TempWriteToDataBase(GetFireBaseUser().getUid(), "token", token);
    }

    public void SignOut(Interfaces.SignOutInterface signOutInterface)
    {
        mFireBaseFunctions.signOut(mContext,signOutInterface);
    }

    public void CleanObjects()
    {
        mGuestProfile = null;
        mOrderHistoryGuestItemDetails.clear();
        mOrderHistoryGuestItemsArrayList.clear();
    }

    public void DeleteLocalFiles(Activity activity)
    {
        File myDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (myDir != null && myDir.exists()) {
            if (myDir.isDirectory()) {
                String[] children = myDir.list();
                for (String child: children) {
                    new File(myDir, child).delete();
                }
            }
        }
        else{
            System.out.print("Files don't exist");
        }
    }

    public void GetActiveChefs(final Interfaces.DataBaseReadInterface readActiveChefsInterface)
    {
        if(!mChefsListForGuestArrayList.isEmpty())
        {
            readActiveChefsInterface.ReadSucceeded(null);
        }

        mDataBaseFunctions.TempReadChefDataBase(null, new Interfaces.DataBaseReadInterface() {
            @Override
            public void ReadSucceeded(DataSnapshot dataSnapshot) {
                mChefsListForGuestArrayList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    try{
                        if(dataSnapshot.child(child.getKey()).child("isActive").getValue().toString().matches("true") ) {
                            ChefsListForGuest chefsListForGuest = child.child("profile").getValue(ChefsListForGuest.class);
                            chefsListForGuest.SetUnknownFields(dataSnapshot.child(child.getKey()).getKey());

                            mChefsListForGuestArrayList.add(chefsListForGuest);
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println("Error retrieving chef details");
                    }
                }
                readActiveChefsInterface.ReadSucceeded(dataSnapshot);
            }

            @Override
            public void ReadFailed(DatabaseError databaseError) {
                readActiveChefsInterface.ReadFailed(databaseError);
            }
        });
    }

    public void DownloadDP(final Activity activity, final Interfaces.DownloadDP downloadDP)
    {
        if(!mChefsListForGuestArrayList.isEmpty())
        {
            mFireBaseStorageFunctions.DownloadDP(
                    activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    downloadDP
            );
        }
    }

    public void GetChefItems(int position,
            final Interfaces.ReadChefItemsInterface readChefItemsInterface)
    {
        String uId = GetChefsListForGuest().get(position).getuID();

        mDataBaseFunctions.TempReadChefDataBase(uId, new Interfaces.DataBaseReadInterface() {
            @Override
            public void ReadSucceeded(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.child("items").getChildren()) {
                    try{
                        GuestItem guestItem = child.getValue(GuestItem.class);

                        if(guestItem.getIsAvailable()) {
                            mGuestItemArrayList.add(guestItem);
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
        });
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
                        GetChefsListForGuest().get(position).getFullName(),
                        GetChefsListForGuest().get(position).getuID(),
                        GetGuestProfile().getfname() + " " + GetGuestProfile().getlname(),
                        GetFireBaseUser().getUid(),
                        GetChefsListForGuest().get(position).getFullAddress(),
                        1,
                        GetGuestItemArrayList().get(key).getitemName(),
                        GetGuestProfile().getPhoneNumber(),
                        GetGuestItemArrayList().get(key).getItemPrice(),
                        "",
                        String.valueOf(GetGuestItemArrayList().get(key).getItemPrice()),
                        GetChefsListForGuest().get(position).getPhoneNO(),
                        GetGuestProfile().getAddress() + " " + GetGuestProfile().getaptno() +
                                " " + GetGuestProfile().getCity() + " " + GetGuestProfile().getZipcode()
                );
                GetCartItemArrayList().add(cartItem);
            }
        }
    }

    public boolean IsGuestProfileCheckPass()
    {
        return (GetGuestProfile().getfname().isEmpty() || GetGuestProfile().getPhoneNumber().isEmpty());
    }

    public boolean IsClickCount2()
    {
        if(!GetCartItemArrayList().isEmpty())
        {
            mClickCount++;

            if (mClickCount == 2)
            {
                mClickCount = 0;
                return false;
            }
        }
        return true;
    }

    public void DataUpload(final Interfaces.DataUploadInterface dataUploadInterface)
    {
        int index = 0;
        Date trialTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy hh:mm:ss a");
        sdf.format(trialTime);
        String time = sdf.format(trialTime);

        while( index < GetCartItemArrayList().size())
        {
            CartItem cartItem = GetCartItemArrayList().get(index);
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
                        dataUploadInterface.UploadComplete();
                    }
                    else
                    {
                        dataUploadInterface.UploadFailed(databaseError.toString());
                    }
                }
            });
            index++;
        }
        GetCartItemArrayList().clear();
        mClickCount = 0;
    }

    public void ReadGuestHistoryData( final Interfaces.DataBaseReadInterface dataBaseReadInterface )
    {
        mDataBaseFunctions.TempReadDataBase("orderHistory",
                new Interfaces.DataBaseReadInterface() {
                    @Override
                    public void ReadSucceeded(DataSnapshot dataSnapshot) {
                        getItems(dataSnapshot);
                        dataBaseReadInterface.ReadSucceeded(dataSnapshot);
                    }

                    @Override
                    public void ReadFailed(DatabaseError databaseError) {
                        dataBaseReadInterface.ReadFailed(databaseError);
                    }

                    public void getItems(DataSnapshot dataSnapshot)
                    {
                        GetOrderHistoryGuestItemsArrayList().clear();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            GetOrderHistoryGuestItemsArrayList().add(child.getValue(OrderHistoryGuestItem.class));
                        }

                        Collections.sort(GetOrderHistoryGuestItemsArrayList(), new Comparator<OrderHistoryGuestItem>() {
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
                });
    }

    public void UpdateProfile(String newFname, String newLname, String newPhoneNumber)
    {
        mFireBaseFunctions.UpdateProfile(newFname,newLname);

        mGuestProfile = new GuestProfile("",
                "",
                "",
                newFname,
                newLname,
                newPhoneNumber,
                "");

        mDataBaseFunctions.TempWriteToDataBase(GetFireBaseUser().getUid(), "profile", mGuestProfile);
    }
}