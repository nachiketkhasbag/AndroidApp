package com.khasna.cooker.ChefActivityFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.khasna.cooker.Common.Interfaces;
import com.khasna.cooker.Models.Collection;
import com.khasna.cooker.R;


/**
 * Created by nachiket on 4/27/2017.
 */

public class FragmentResetPasswordChef extends Fragment {
    View mView;
    EditText editTextCurrentPassword;
    EditText editTextNewPassword;
    EditText editTextConfirmPassword;
    Button buttonChefResetPassword;
    Collection mCollection;

    public FragmentResetPasswordChef() {
        mCollection = Collection.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_chef_reset_password, container, false);

        editTextCurrentPassword = (EditText)mView.findViewById(R.id.editTextCurrentPassword);
        editTextNewPassword = (EditText)mView.findViewById(R.id.editTextNewPassword);
        editTextConfirmPassword = (EditText)mView.findViewById(R.id.editTextConfirmPassword);
        buttonChefResetPassword = (Button)mView.findViewById(R.id.buttonChefResetPassword);

        buttonChefResetPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                mCollection.mFireBaseFunctions.ChangePassword(
                        editTextCurrentPassword.getText().toString(),
                        editTextNewPassword.getText().toString(),
                        editTextConfirmPassword.getText().toString(),
                        new Interfaces.UpdatePasswordInterface() {
                            @Override
                            public void UpdatePasswordSuccessful() {
                                Toast.makeText(getContext(), " Password reset successful" , Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void UpdatePasswordFailed(String error) {
                                Toast.makeText(getContext(), error , Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        return mView;
    }
}