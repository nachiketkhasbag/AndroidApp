package com.khasna.cooker.Models;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khasna.cooker.ChefActivityFragments.ContainerClasses.ChefProfile;
import com.khasna.cooker.Common.Interfaces;

/**
 * Created by nachiket on 5/2/2018.
 */

public class DataBaseFunctions<G extends Collection> {

    private DatabaseReference mDataBaseRef;
    private ValueEventListener mValueEventListener;
    private G databaseFunctionsGeneric;
    private Interfaces.AppUserLocatorInterface mAppUserLocatorInterface;

    public DataBaseFunctions(G g) {
        databaseFunctionsGeneric = g;
    }

    public void LocateUser(Interfaces.AppUserLocatorInterface appUserLocatorInterface) {
        mDataBaseRef = FirebaseDatabase.getInstance().getReference();
        mAppUserLocatorInterface = appUserLocatorInterface;

        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("cookProfile").child(databaseFunctionsGeneric.mFireBaseFunctions.getuID()).exists() &&
                        dataSnapshot.child("userProfile").child(databaseFunctionsGeneric.mFireBaseFunctions.getuID()).exists()) {
                    mAppUserLocatorInterface.ForceUserSignOut();
                } else if (dataSnapshot.child("cookProfile").child(databaseFunctionsGeneric.mFireBaseFunctions.getuID()).exists()) {
                    mAppUserLocatorInterface.UserIsChef();
                } else if (dataSnapshot.child("userProfile").child(databaseFunctionsGeneric.mFireBaseFunctions.getuID()).exists()) {
                    mAppUserLocatorInterface.UserIsGuest();
                } else {
                    // TODO: THIS IS TEMPORARY. THIS SHOULD NOT BE PRESENT
                    // TODO: ALL UNKNOWN USERS DIRECTED TO GUEST ACTIVITY
                    //mAppUserLocatorInterface.UserIsGuest();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.toString());
            }
        };
        mDataBaseRef.addListenerForSingleValueEvent(mValueEventListener);
    }

    public <T> void WriteToDataBase(DatabaseReference databaseReference, String extraParams, T object)
    {
        if (extraParams == null)
        {
            extraParams = "";
        }
        databaseReference.child(extraParams).setValue(object);
    }

    public <T> void WriteToDataBase(DatabaseReference databaseReference, String extraParams, T object, final Interfaces.DataBaseWriteInterface dataBaseWriteInterface)
    {
        if (extraParams == null)
        {
            extraParams = "";
        }

        databaseReference.child(extraParams).setValue(object, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null){
                    dataBaseWriteInterface.WriteSucceeded();
                }
                else{
                    dataBaseWriteInterface.WriteFailed(databaseError);
                }
            }
        });
    }

    public void ReadDataBase(DatabaseReference databaseReference, String extraParams, final Interfaces.DataBaseReadInterface dataBaseReadInterface)
    {
        if (extraParams == null)
        {
            extraParams = "";
        }

        databaseReference.child(extraParams).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataBaseReadInterface.ReadSucceeded(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dataBaseReadInterface.ReadFailed(databaseError);
            }
        });
    }

    public void UpdateProfileAddress(DatabaseReference databaseReference, ChefProfile chefProfile, final Interfaces.UpdateProfileInterface updateProfileInterface)
    {
        WriteToDataBase(
                databaseReference,
                "cookProfile/" + databaseFunctionsGeneric.mFireBaseFunctions.getuID() + "/profile",
                chefProfile,
                new Interfaces.DataBaseWriteInterface() {
                    @Override
                    public void WriteSucceeded() {
                        updateProfileInterface.UpdateProfileComplete();
                    }

                    @Override
                    public void WriteFailed(DatabaseError databaseError) {
                        updateProfileInterface.UpdateProfileFailed(databaseError.toString());
                    }
                });
    }
}
