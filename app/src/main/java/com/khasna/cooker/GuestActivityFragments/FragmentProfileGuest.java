package com.khasna.cooker.GuestActivityFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.khasna.cooker.R;

/**
 * Created by nachiket on 4/11/2017.
 */

public class FragmentProfileGuest extends Fragment {

    public FragmentProfileGuest() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View FragmentGuestProfile = inflater.inflate(R.layout.guest_profile_layout, container, false);

        EditText fullName = (EditText)FragmentGuestProfile.findViewById(R.id.editTextGuestFullName);
        EditText phoneNumber = (EditText)FragmentGuestProfile.findViewById(R.id.editTextGuestPhoneNumber);
        EditText homeAddress = (EditText)FragmentGuestProfile.findViewById(R.id.editTextGuestHomeAddress);
        Button updateProfile = (Button)FragmentGuestProfile.findViewById(R.id.buttonGuestUpdateProfile);

        return FragmentGuestProfile;
    }
}
