package com.example.cooker.GuestActivityFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.cooker.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAccountSettingsGuest extends Fragment {


    public FragmentAccountSettingsGuest() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View updateGuestAccount = inflater.inflate(R.layout.fragment_guest_account_settings, container, false);

        EditText updateFullName = (EditText)updateGuestAccount.findViewById(R.id.editTextUpdateGuestFullName);
        EditText updatePhoneNumber = (EditText)updateGuestAccount.findViewById(R.id.editTextUpdateGuestPhoneNumber);
        EditText updateHomeAddress = (EditText)updateGuestAccount.findViewById(R.id.editTextUpdateGuestHomeAddress);
        EditText updateEmailAddress = (EditText)updateGuestAccount.findViewById(R.id.editTextUpdateGuestEmailAddress);
        Button updateProfile = (Button)updateGuestAccount.findViewById(R.id.buttonReUpdateGuestProfile);

        // Inflate the layout for this fragment
        return updateGuestAccount;
    }

}
