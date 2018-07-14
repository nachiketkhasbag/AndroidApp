package com.khasna.cooker.Models;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
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

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null)
        {
            // User is signed in
            System.out.print("Signed in");
            DebugClass.DebugPrint(className, "onAuthStateChanged:valid input from user");
            emailID = auth.getCurrentUser().getEmail();
            uID = auth.getUid();
            displayName = auth.getCurrentUser().getDisplayName();
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

    public String getEmailID() {
        return emailID;
    }

    public String getuID() {
        return uID;
    }

    public String getDisplayName() {
        return displayName;
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
        DestroyCurrentUser();
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
