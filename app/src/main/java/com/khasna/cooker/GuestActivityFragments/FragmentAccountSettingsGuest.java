package com.khasna.cooker.GuestActivityFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.khasna.cooker.Models.Collection;
import com.khasna.cooker.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAccountSettingsGuest extends Fragment {

    FragmentManager mActiveFragmentManager;
    View updateGuestAccount;
    EditText editTextUpdateGuestFirstName;
    EditText editTextUpdateGuestPhoneNumber;
    EditText editTextUpdateGuestLastName;
    EditText editTextUpdateGuestEmailAddress;
    Button buttonReUpdateGuestProfile;
//    Button buttonGuestResetPassword;
    Collection mCollection;

    public FragmentAccountSettingsGuest() {
        mCollection = Collection.getInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        updateGuestAccount = inflater.inflate(R.layout.fragment_guest_account_settings, container, false);

        mActiveFragmentManager              = getFragmentManager();
        editTextUpdateGuestFirstName        = (EditText)updateGuestAccount.findViewById(R.id.editTextUpdateGuestFirstName);
        editTextUpdateGuestPhoneNumber         = (EditText)updateGuestAccount.findViewById(R.id.editTextUpdateGuestPhoneNumber);
        editTextUpdateGuestLastName        = (EditText)updateGuestAccount.findViewById(R.id.editTextUpdateGuestLastName);
        editTextUpdateGuestEmailAddress    = (EditText)updateGuestAccount.findViewById(R.id.editTextUpdateGuestEmailAddress);
        buttonReUpdateGuestProfile           = (Button)updateGuestAccount.findViewById(R.id.buttonReUpdateGuestProfile);
//        buttonGuestResetPassword             = (Button)updateGuestAccount.findViewById(R.id.buttonGuestResetPassword);

        editTextUpdateGuestEmailAddress.setEnabled(false);

        setKnownFields();

        buttonReUpdateGuestProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String errorCode = ValidateInput();
                if (errorCode.equals("VALID_INPUT")){
                    mCollection.UpdateProfile(editTextUpdateGuestFirstName.getText().toString(),
                            editTextUpdateGuestLastName.getText().toString(),
                            editTextUpdateGuestPhoneNumber.getText().toString());
                    Toast.makeText(getContext(), "Information updated", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getContext(), errorCode, Toast.LENGTH_LONG).show();
                }
            }
        });

        // Inflate the layout for this fragment
        return updateGuestAccount;
    }

    public void setKnownFields(){
        editTextUpdateGuestFirstName.setText(mCollection.GetGuestProfile().getfname());
        editTextUpdateGuestLastName.setText(mCollection.GetGuestProfile().getlname());
        editTextUpdateGuestPhoneNumber.setText(mCollection.GetGuestProfile().getPhoneNumber());
        editTextUpdateGuestEmailAddress.setText(mCollection.GetFireBaseUser().getEmail());
    }

    public String ValidateInput()
    {
        String emailAddress = editTextUpdateGuestEmailAddress.getText().toString();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (!emailAddress.matches(emailPattern))
        {
            return "INVALID_EMAIL";
        }

        if(editTextUpdateGuestFirstName.getText().toString().matches("") ||
                editTextUpdateGuestLastName.getText().toString().matches("") ||
                editTextUpdateGuestPhoneNumber.getText().toString().matches("") )
        {
            return "All information is required";
        }

        return "VALID_INPUT";
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
