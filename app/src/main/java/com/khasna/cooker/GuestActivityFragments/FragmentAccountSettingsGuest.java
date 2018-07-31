package com.khasna.cooker.GuestActivityFragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.khasna.cooker.Common.FireBaseAuthClass;
import com.khasna.cooker.Common.ProcessDialogBox;
import com.khasna.cooker.GuestActivityFragments.ContainerClasses.GuestProfile;
import com.khasna.cooker.Models.Collection;
import com.khasna.cooker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAccountSettingsGuest extends Fragment {

    DatabaseReference mDataBaseRef;
    FragmentManager mActiveFragmentManager;
    ProcessDialogBox processDialogBox;
    View updateGuestAccount;
    EditText editTextUpdateGuestFirstName;
    EditText editTextUpdateGuestPhoneNumber;
    EditText editTextUpdateGuestLastName;
    EditText editTextUpdateGuestEmailAddress;
    Button buttonReUpdateGuestProfile;
    Button buttonGuestResetPassword;
    GuestProfile guestProfile;
    Collection mCollection;
    Fragment mActiveFragment;
    GuestEntity mGuestEntity;

    public FragmentAccountSettingsGuest() {
        mCollection = Collection.getInstance();
        mGuestEntity = GuestEntity.getInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        updateGuestAccount = inflater.inflate(R.layout.fragment_guest_account_settings, container, false);

        mDataBaseRef = FirebaseDatabase.getInstance().getReference("userProfile").
                child(mGuestEntity.getFirebaseUser().getUid()).child("profile");
        mActiveFragmentManager              = getFragmentManager();
        editTextUpdateGuestFirstName        = (EditText)updateGuestAccount.findViewById(R.id.editTextUpdateGuestFirstName);
        editTextUpdateGuestPhoneNumber         = (EditText)updateGuestAccount.findViewById(R.id.editTextUpdateGuestPhoneNumber);
        editTextUpdateGuestLastName        = (EditText)updateGuestAccount.findViewById(R.id.editTextUpdateGuestLastName);
        editTextUpdateGuestEmailAddress    = (EditText)updateGuestAccount.findViewById(R.id.editTextUpdateGuestEmailAddress);
        buttonReUpdateGuestProfile           = (Button)updateGuestAccount.findViewById(R.id.buttonReUpdateGuestProfile);
        buttonGuestResetPassword             = (Button)updateGuestAccount.findViewById(R.id.buttonGuestResetPassword);

        processDialogBox = new ProcessDialogBox(getActivity());

        editTextUpdateGuestEmailAddress.setEnabled(false);

        setKnownFields();

        buttonGuestResetPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mActiveFragment = new FragmentChangePassword();
                mActiveFragmentManager.beginTransaction().replace(R.id.guest_page, mActiveFragment).commit();
            }
        });

        buttonReUpdateGuestProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String errorCode = ValidateInput();
                if (errorCode.equals("VALID_INPUT")){
                    processDialogBox.ShowDialogBox();

                    OnCompleteListener onCompleteListener = new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                UpdateNewFields();
                                Toast.makeText(getContext(), "Information updated", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(getContext(),
                                        "This email address is already in use", Toast.LENGTH_LONG).show();
                                return;
                            }
                            processDialogBox.DismissDialogBox();
                        }
                    };
                    FireBaseAuthClass.UpdateEmail(onCompleteListener, editTextUpdateGuestEmailAddress.getText().toString());
                    return;
                }
                Toast.makeText(getContext(), errorCode, Toast.LENGTH_LONG).show();
            }
        });

        // Inflate the layout for this fragment
        return updateGuestAccount;
    }

    public void setKnownFields(){
        editTextUpdateGuestFirstName.setText(mGuestEntity.getGuestProfile().getfname());
        editTextUpdateGuestLastName.setText(mGuestEntity.getGuestProfile().getlname());
        editTextUpdateGuestPhoneNumber.setText(mGuestEntity.getGuestProfile().getPhoneNumber());
        editTextUpdateGuestEmailAddress.setText(mGuestEntity.getFirebaseUser().getEmail());
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

    public void UpdateNewFields()
    {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(editTextUpdateGuestFirstName.getText().toString() + editTextUpdateGuestLastName.getText().toString()).build();
        FireBaseAuthClass.UpdateProfile(profileUpdates);

        guestProfile = new GuestProfile("",
                "",
                "",
                editTextUpdateGuestFirstName.getText().toString(),
                editTextUpdateGuestLastName.getText().toString(),
                editTextUpdateGuestPhoneNumber.getText().toString(),
                "");

        mDataBaseRef.child("userProfile").child(mGuestEntity.getFirebaseUser().getUid()).child("profile").
                setValue(guestProfile, new DatabaseReference.CompletionListener() {

                    //Implies that the data has been committed
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null)
                        {
                            mGuestEntity.setGuestProfile(guestProfile);
                            Toast.makeText(getContext(), "Information updated", Toast.LENGTH_LONG).show();
                            return;
                        }

                        Toast.makeText(getContext(),"There has been problem connecting to the server" +
                                        "Please try again in sometime ",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mDataBaseRef = FirebaseDatabase.getInstance().getReference();
    }
}
