package com.khasna.cooker.Models;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.khasna.cooker.Common.DebugClass;
import com.khasna.cooker.Common.FireBaseAuthClass;
import com.khasna.cooker.Common.Interfaces;
import com.khasna.cooker.Common.SignupActivity;

/**
 * Created by nachiket on 4/26/2018.
 */

public class FireBaseFunctions<G extends Collection> {

    private String emailID;
    private String uID;
    private String displayName;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private G fireBaseFunctionsGeneric;
    private Interfaces.AppUserInterface mInterfaces;

    FireBaseFunctions(G g){
        fireBaseFunctionsGeneric = g;
    }

    private static final String className = "FireBaseFunctions";

    public void WaitForUserLogin(Interfaces.AppUserInterface userInterface)
    {
        mInterfaces = userInterface;

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
                    mInterfaces.UserSignedIn();
                } else {
                    // User is signed out
                    System.out.print("Signed out");
                    DebugClass.DebugPrint(className, "onAuthStateChanged:User signed out");
                    mInterfaces.UserSignedOut();
                }
            }
        };
        FireBaseAuthClass.AddAuthStateListener(mAuthListener);
    }

    public String getEmailID() {
        return emailID;
    }

    public String getuID() {
        return uID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void signOut()
    {
        FireBaseAuthClass.SignOut();
        FireBaseAuthClass.RemoveAuthStateListener(mAuthListener);
        DestroyCurrentUser();
    }

    public void SignInWithEmail(final Activity activity, String emailId, String password)
    {
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

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

    public void UpdateEmail(String emailId, final Interfaces.UpdateEmailInterface updateEmailInterface)
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null){
            mAuth.getCurrentUser().updateEmail(emailId).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        updateEmailInterface.UpdateEmailComplete();
                    }
                    else{
                        updateEmailInterface.UpdateEmailFailed(task.getException().toString());
                    }
                }
            });
        }
    }

    public void UpdateProfileName(String firstName, String lastName){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null){
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(firstName + lastName).build();
            mAuth.getCurrentUser().updateProfile(profileUpdates);
        }
    }

    public void ChangePassword(
            final String currentPassword,
            final String newPassword,
            final String confirmNewPassword,
            final Interfaces.UpdatePasswordInterface updatePasswordInterface)
    {
        String errorCode;

        if (currentPassword.isEmpty())
        {
            errorCode = "CURRENT_PASSWORD_BLANK";
            updatePasswordInterface.UpdatePasswordFailed(errorCode);
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(
                getEmailID(),
                currentPassword);

        FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            String error = ValidateInput(
                                    newPassword,
                                    confirmNewPassword);
                            if (error.equals("PASSWORDS_MATCH")){
                                SendResetPassword(confirmNewPassword, updatePasswordInterface);
                            }
                            else{
                                updatePasswordInterface.UpdatePasswordFailed(error);
                            }
                        }
                        else
                        {
                            updatePasswordInterface.UpdatePasswordFailed(task.getException().toString());
                        }
                    }
                });
    }

    public void SignUpWithEmail(
            String userName,
            String password,
            final Interfaces.SignUpUserInterface signUpUserInterface)
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(userName, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful())
                {
                    signUpUserInterface.SignUpFailed(task.getException().toString());
                }
                else
                {
                    signUpUserInterface.SignUpComplete();
                }
            }
        });
    }

    private void SendResetPassword(String newPass, final Interfaces.UpdatePasswordInterface updatePasswordInterface)
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.getCurrentUser().updatePassword(newPass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            updatePasswordInterface.UpdatePasswordSuccessful();
                        }
                        else{
                            updatePasswordInterface.UpdatePasswordFailed(task.getException().toString());
                        }
                    }
                });
    }

    private String ValidateInput(String editTextNewPassword,
                                String editTextConfirmPassword)
    {
        if (editTextNewPassword.isEmpty() |
                editTextConfirmPassword.isEmpty() )
        {
            return "PASSWORD_CANNOT_BE_BLANK";
        }
        else if (!editTextNewPassword.equals(editTextConfirmPassword)){
            return "PASSWORDS_DO_NOT_MATCH";
        }
        else if (editTextNewPassword.equals(editTextConfirmPassword)){
            return "PASSWORDS_MATCH";
        }
        return "UNKNOWN_ERROR";
    }

    private void DestroyCurrentUser()
    {
        DebugClass.DebugPrint(className, "DestroyCurrentUser:User destroyed");
        emailID = "";
        uID = "";
        displayName = "";
    }
}
