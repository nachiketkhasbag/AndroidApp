package com.khasna.cooker.ChefActivityFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.khasna.cooker.Common.FireBaseAuthClass;
import com.khasna.cooker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;


/**
 * Created by nachiket on 4/27/2017.
 */

public class FragmentResetPasswordChef extends Fragment {
    View mView;
    EditText editTextCurrentPassword;
    EditText editTextNewPassword;
    EditText editTextConfirmPassword;
    Button buttonChefResetPassword;
    String errorCode;
    String errorMsg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_chef_reset_password, container, false);

        errorCode = "NONE";
        errorMsg = "NONE";
        editTextCurrentPassword = (EditText)mView.findViewById(R.id.editTextCurrentPassword);
        editTextNewPassword = (EditText)mView.findViewById(R.id.editTextNewPassword);
        editTextConfirmPassword = (EditText)mView.findViewById(R.id.editTextConfirmPassword);
        buttonChefResetPassword = (Button)mView.findViewById(R.id.buttonChefResetPassword);

        buttonChefResetPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                errorCode = ValidateAndResetPassword(editTextCurrentPassword.getText().toString());

                switch (errorCode)
                {
                    case "CURRENT_PASSWORD_BLANK":
                        Toast.makeText(getContext(), errorMsg , Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
        return mView;
    }

    public String ValidateAndResetPassword(String editTextCurrentPassword)
    {
        errorCode = "NONE";

        if (editTextCurrentPassword.isEmpty())
        {
            errorCode = "CURRENT_PASSWORD_BLANK";
            errorMsg = "Current password cannot be blank";
            return errorCode;
        }

        AuthCredential credential = EmailAuthProvider
                .getCredential(FireBaseAuthClass.GetFirebaseUser().getEmail(),
                        editTextCurrentPassword);

        FireBaseAuthClass.GetFirebaseUser().reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            errorCode = ValidateInput(editTextNewPassword.getText().toString(),
                                    editTextConfirmPassword.getText().toString());
                            switch (errorCode)
                            {
                                case "PASSWORD_CANNOT_BE_BLANK":
                                    Toast.makeText(getContext(), errorMsg , Toast.LENGTH_SHORT).show();
                                    break;
                                case "PASSWORDS_DO_NOT_MATCH":
                                    Toast.makeText(getContext(), errorMsg , Toast.LENGTH_SHORT).show();
                                    break;
                                case "PASSWORDS_MATCH":
                                    FireBaseAuthClass.SendResetPassword(getContext(), editTextConfirmPassword.getText().toString());
                                    break;
                                default:
                                    break;
                            }
                        }
                        else
                        {
                            errorCode = "CURRENT_PASSWORD_DOES_NOT_MATCH";
                            errorMsg = task.getException().getMessage();
                            Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return errorCode;
    }

    public String ValidateInput(String editTextNewPassword,
                                String editTextConfirmPassword)
    {
        if (editTextNewPassword.isEmpty() |
                editTextConfirmPassword.isEmpty() )
        {
            errorCode = "PASSWORD_CANNOT_BE_BLANK";
            errorMsg = "Password cannot be blank";
            return errorCode;
        }
        else if (!editTextNewPassword.equals(editTextConfirmPassword)){
            errorCode = "PASSWORDS_DO_NOT_MATCH";
            errorMsg = "Passwords do not match";
            return errorCode;
        }
        else if (editTextNewPassword.equals(editTextConfirmPassword)){
            return "PASSWORDS_MATCH";
        }
        return "UNKNOWN_ERROR";
    }
}