package com.example.cooker.Common;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * Created by nachiket on 7/3/2017.
 */

public class UserInfo{
    private static String emailID = "";
    private static String uID = "";
    private static String displayName = "";
    private static FirebaseAuth.AuthStateListener mAuthListener;

    private static final String className = "UserInfo";

    public static void AuthenticateUser(final Interfaces.UserInterface userInterface)
    {
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    final FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        System.out.print("Signed in");
                        DebugClass.DebugPrint(className, "onAuthStateChanged:valid input from user");
                        emailID = user.getEmail();
                        uID = user.getUid();
                        displayName = user.getDisplayName();
                        userInterface.UserSignedIn();
                    } else {
                        // User is signed out
                        System.out.print("Signed out");
                        DebugClass.DebugPrint(className, "onAuthStateChanged:User signed out");
                        userInterface.UserSignedOut();
                    }
                }
            };
            FireBaseAuthClass.AddAuthStateListener(mAuthListener);
    }

    public static String getEmailID() {
        return emailID;
    }

    public static String getuID() {
        return uID;
    }

    public static String getDisplayName() {
        return displayName;
    }

    public static void signOut()
    {
        FireBaseAuthClass.SignOut();
        FireBaseAuthClass.RemoveAuthStateListener(mAuthListener);
        DestroyCurrentUser();
    }

    private static void DestroyCurrentUser()
    {
        DebugClass.DebugPrint(className, "DestroyCurrentUser:User destroyed");
        emailID = "";
        uID = "";
        displayName = "";
    }
}
