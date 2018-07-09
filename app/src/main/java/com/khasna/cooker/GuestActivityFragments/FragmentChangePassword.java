package com.khasna.cooker.GuestActivityFragments;


import android.os.Bundle;
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
 * A simple {@link Fragment} subclass.
 */
public class FragmentChangePassword extends Fragment {

    Collection mCollection;
    View mView;
    EditText editTextCurrentPassword;
    EditText editTextNewPassword;
    EditText editTextConfirmPassword;
    Button buttonResetPassword;

    public FragmentChangePassword() {
        mCollection = Collection.getInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_change_password, container, false);

        editTextCurrentPassword = (EditText)mView.findViewById(R.id.editTextCurrentPassword);
        editTextNewPassword = (EditText)mView.findViewById(R.id.editTextNewPassword);
        editTextConfirmPassword = (EditText)mView.findViewById(R.id.editTextConfirmPassword);
        buttonResetPassword = (Button)mView.findViewById(R.id.buttonResetPassword);

        buttonResetPassword.setOnClickListener(new View.OnClickListener(){
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
