package com.khasna.cooker.Models;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khasna.cooker.Common.Interfaces;

/**
 * Created by nachiket on 5/2/2018.
 */

public class DataBaseFunctions<G extends Collection> {

    private G databaseFunctionsGeneric;
    private DatabaseReference mDatabaseReference;

    public DataBaseFunctions(G g) {
        databaseFunctionsGeneric = g;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("userProfile");
    }

    public <T> void TempWriteToDataBase(String uid, String extraParams, T object)
    {
        if (extraParams == null)
        {
            extraParams = "";
        }
        mDatabaseReference
                .child(uid)
                .child(extraParams).setValue(object);
    }

    public void TempReadChefDataBase(String extraParams, final Interfaces.DataBaseReadInterface dataBaseReadInterface)
    {
        if (extraParams == null)
        {
            extraParams = "";
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cookProfile");

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

    public void TempReadDataBase(String extraParams, final Interfaces.DataBaseReadInterface dataBaseReadInterface)
    {
        if (extraParams == null)
        {
            extraParams = "";
        }

        mDatabaseReference
                .child(databaseFunctionsGeneric.mFireBaseFunctions.getFireBaseUser().getUid())
                .child(extraParams).addListenerForSingleValueEvent(new ValueEventListener() {
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
}
