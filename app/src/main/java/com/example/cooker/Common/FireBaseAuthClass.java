package com.example.cooker.Common;

import android.app.Activity;
import android.app.Application;
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

    public static void SignInWithEmail(final Activity activity, String emailId, String password)
    {
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        FirebaseAuth mAuth = getCurrentInstance();

        progressDialog.setMax(100); // Progress Dialog Max Value
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("Processing"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Horizontal
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

        mAuth.signInWithEmailAndPassword(emailId,password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            System.out.print("Sign in failed");
                            Toast.makeText(activity.getApplicationContext(),
                                    task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            System.out.print("Sign in successful");
                            Toast.makeText(activity.getApplicationContext(),
                                    "Signed in", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void SignUpWithEmail(final Activity activity, String emailId, String password)
    {
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        FirebaseAuth mAuth = getCurrentInstance();

        progressDialog.setMax(100); // Progress Dialog Max Value
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("Processing"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Horizontal
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

        mAuth.createUserWithEmailAndPassword(emailId, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressDialog.dismiss();

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful())
                        {
                            Toast.makeText(activity.getApplicationContext(), "Signup failed", Toast.LENGTH_SHORT).show();
                            Toast.makeText(activity.getApplicationContext(), "Please click on Forgot Password" +
                                    "if you already have an account", Toast.LENGTH_SHORT).show();

                            System.out.print("SignUp failed");
                        }
                        else
                        {
                            Toast.makeText(activity.getApplicationContext(),
                                    "Signup succeeded", Toast.LENGTH_SHORT).show();
                            System.out.print("SignUp succeeded");
                        }
                    }
                });
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

    public static FirebaseUser GetFirebaseUser()
    {
        FirebaseAuth mAuth = getCurrentInstance();
        return mAuth.getCurrentUser();
    }

    public static void SendResetPassword(final Context context, String password)
    {
        FirebaseAuth mAuth = getCurrentInstance();
        mAuth.getCurrentUser().updatePassword(password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context.getApplicationContext(), "Password changed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
//            Toast.makeText(getActivity().getApplicationContext(), "Cannot complete process. PLease try again later",
//                    Toast.LENGTH_SHORT).show();

            System.exit(0);
        }

        return mAuth;
    }
}
