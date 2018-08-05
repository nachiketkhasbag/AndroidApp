package com.khasna.cooker.Models;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.khasna.cooker.Common.DebugClass;
import com.khasna.cooker.Common.Interfaces;

/**
 * Created by nachiket on 4/26/2018.
 */

public class FireBaseFunctions<G extends Collection> {

    private G fireBaseFunctionsGeneric;
    private FirebaseUser mFireBaseUser;

    FireBaseFunctions(G g){
        fireBaseFunctionsGeneric = g;
    }

    private static final String className = "FireBaseFunctions";

    public void WaitForUserLogin(Interfaces.AppUserInterface userInterface)
    {
        Interfaces.AppUserInterface mInterfaces;

        mInterfaces = userInterface;

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null)
        {
            // User is signed in
            System.out.print("Signed in");
            DebugClass.DebugPrint(className, "onAuthStateChanged:valid input from user");
            mFireBaseUser = auth.getCurrentUser();
            mInterfaces.UserSignedIn();
        }
        else
        {
            // User is signed out
            System.out.print("Signed out");
            DebugClass.DebugPrint(className, "onAuthStateChanged:User signed out");
            mInterfaces.UserSignedOut();
        }
    }

    protected FirebaseUser getFireBaseUser()
    {
        return mFireBaseUser;
    }

    public void signOut(Context context, final Interfaces.SignOutInterface signOutInterface)
    {
        AuthUI.getInstance().signOut(context).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    signOutInterface.TaskComplete();
                }
                else
                {
                    signOutInterface.TaskFailed(task.getException().toString());
                }
            }
        });

        if(DebugClass.IsDelete())
        {
            AuthUI.getInstance().delete(context);
        }
        DebugClass.DebugPrint(className, "DestroyCurrentUser:User destroyed");
    }

    public void UpdateProfile(String newFname, String newLname)
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth == null || mAuth.getCurrentUser() == null)
        {
            System.out.print("Cannot complete process. PLease try again later");
            System.exit(0);
            return;
        }

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newFname + newLname).build();
        mAuth.getCurrentUser().updateProfile(profileUpdates);
    }
}
