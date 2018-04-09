package com.khasna.cooker.ChefActivityFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.khasna.cooker.ChefActivityFragments.ContainerClasses.ChefProfile;
import com.khasna.cooker.Common.FireBaseAuthClass;
import com.khasna.cooker.Common.ProcessDialogBox;
import com.khasna.cooker.R;
import com.khasna.cooker.Common.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by nachiket on 4/24/2017.
 */

public class FragmentUpdateAccountChef extends Fragment {
    View mView;
    DatabaseReference databaseRef;
    Button buttonUpdateChefProfile;
    Button buttonChefChangePassword;
    EditText editTextUpdateChefEmailAddress;
    EditText editTextUpdateChefFName;
    EditText editTextUpdateChefLName;
    EditText editTextUpdateAptNo;
    EditText editTextUpdateStreetAddress;
    EditText editTextUpdateCity;
    EditText editTextUpdateZipCode;
    EditText editTextUpdatePhoneNumber;
    Fragment mActiveFragment;
    FragmentManager mActiveFragmentManager;
    ValueEventListener valueEventListener;
    DatabaseReference mDataBaseRef;
    ChefProfile chefProfile;
    ProcessDialogBox processDialogBox;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_chef_update_account, container, false);

        mDataBaseRef = FirebaseDatabase.getInstance().getReference("cookProfile").
                child(UserInfo.getuID()).child("profile");
        mActiveFragmentManager          = getFragmentManager();
        buttonUpdateChefProfile = (Button)mView.findViewById(R.id.buttonUpdateChefProfile);
        buttonChefChangePassword        = (Button)mView.findViewById(R.id.buttonChefChangePassword);
        editTextUpdateChefEmailAddress  = (EditText)mView.findViewById(R.id.editTextUpdateChefEmailAddress);
        editTextUpdateChefFName         = (EditText)mView.findViewById(R.id.editTextUpdateChefFName);
        editTextUpdateChefLName         = (EditText)mView.findViewById(R.id.editTextUpdateChefLName);
        editTextUpdateStreetAddress     = (EditText)mView.findViewById(R.id.editTextUpdateStreetAddress);
        editTextUpdateAptNo             = (EditText)mView.findViewById(R.id.editTextUpdateAptNo);
        editTextUpdateCity              = (EditText)mView.findViewById(R.id.editTextUpdateCity);
        editTextUpdateZipCode           = (EditText)mView.findViewById(R.id.editTextUpdateZipCode);
        editTextUpdatePhoneNumber       = (EditText)mView.findViewById(R.id.editTextUpdatePhoneNumber);

        editTextUpdateChefFName.setText(ChefEntity.chefProfile.getfname());
        editTextUpdateChefLName.setText(ChefEntity.chefProfile.getlname());
        editTextUpdateStreetAddress.setText(ChefEntity.chefProfile.getAddress());
        editTextUpdateAptNo.setText(ChefEntity.chefProfile.getaptno());
        editTextUpdateCity.setText(ChefEntity.chefProfile.getCity());
        editTextUpdateZipCode.setText(ChefEntity.chefProfile.getZipcode());
        editTextUpdatePhoneNumber.setText(ChefEntity.chefProfile.getPhoneNO());

        processDialogBox = new ProcessDialogBox(getActivity());

        setKnownFields();

        buttonUpdateChefProfile.setOnClickListener(new View.OnClickListener(){
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
                            }
                            processDialogBox.DismissDialogBox();
                        }
                    };
                    FireBaseAuthClass.UpdateEmail(onCompleteListener, editTextUpdateChefEmailAddress.getText().toString());
                    return;
                }
                Toast.makeText(getContext(), errorCode, Toast.LENGTH_LONG).show();
            }
        });

        buttonChefChangePassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mActiveFragment = new FragmentResetPasswordChef();
                mActiveFragmentManager.beginTransaction().replace(R.id.chefs_page, mActiveFragment).commit();
            }
        });

        return mView;
    }

    public void setKnownFields(){
        String emailAddress = UserInfo.getEmailID();
        editTextUpdateChefEmailAddress.setText(emailAddress);
    }

    public String ValidateInput()
    {
        String emailAddress = editTextUpdateChefEmailAddress.getText().toString();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (!emailAddress.matches(emailPattern))
        {
            return "INVALID_EMAIL";
        }

        if(editTextUpdateStreetAddress.getText().toString().matches("") ||
                editTextUpdateCity.getText().toString().matches("") ||
                editTextUpdateZipCode.getText().toString().matches("") ||
                editTextUpdatePhoneNumber.getText().toString().matches("") )
        {
            return "Complete address and phone number are required";
        }

        return "VALID_INPUT";
    }

    public void UpdateNewFields()
    {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(editTextUpdateChefFName.getText().toString() + editTextUpdateChefLName.getText().toString()).build();
        FireBaseAuthClass.UpdateProfile(profileUpdates);

        chefProfile = new ChefProfile(editTextUpdateStreetAddress.getText().toString(),
                editTextUpdateAptNo.getText().toString(),
                editTextUpdateCity.getText().toString(),
                editTextUpdateChefFName.getText().toString(),
                editTextUpdateChefLName.getText().toString(),
                editTextUpdatePhoneNumber.getText().toString(),
                editTextUpdateZipCode.getText().toString());

        databaseRef.child("cookProfile").child(FireBaseAuthClass.GetFirebaseUser().getUid()).child("profile").
                setValue(chefProfile, new DatabaseReference.CompletionListener() {

            //Implies that the data has been committed
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null)
                {
                    ChefEntity.chefProfile = chefProfile;
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
        databaseRef = FirebaseDatabase.getInstance().getReference();
    }
}
