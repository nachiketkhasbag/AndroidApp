package com.khasna.cooker.Common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * Created by nachiket on 5/6/2017.
 */

public class FireBaseAuthClass{

    public FireBaseAuthClass()
    {
    }

    public static void SignOut()
    {
        FirebaseAuth mAuth = getCurrentInstance();

        if (DebugClass.IsDelete())
        {
            mAuth.getCurrentUser().delete();
        }
        mAuth.signOut();
    }

    public static void AddAuthStateListener(FirebaseAuth.AuthStateListener mAuthListener)
    {
        FirebaseAuth mAuth = getCurrentInstance();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public static void RemoveAuthStateListener(FirebaseAuth.AuthStateListener mAuthListener)
    {
        FirebaseAuth mAuth = getCurrentInstance();
        mAuth.removeAuthStateListener(mAuthListener);
    }


    public static void UpdateEmail( OnCompleteListener onCompleteListener, String emailId )
    {
        FirebaseAuth mAuth = getCurrentInstance();
        mAuth.getCurrentUser().updateEmail(emailId).addOnCompleteListener(onCompleteListener);
    }

    public static void UpdateProfile(UserProfileChangeRequest userProfileChangeRequest)
    {
        FirebaseAuth mAuth = getCurrentInstance();
        mAuth.getCurrentUser().updateProfile(userProfileChangeRequest);
    }

    private static FirebaseAuth getCurrentInstance()
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth == null)
        {
            System.out.print("Cannot complete process. PLease try again later");
            System.exit(0);
        }

        return mAuth;
    }
}
