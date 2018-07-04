package com.khasna.cooker.ChefActivityFragments;

import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
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
import com.khasna.cooker.Common.Interfaces;
import com.khasna.cooker.Common.LocationHandler;
import com.khasna.cooker.Common.LocationResultReciever;
import com.khasna.cooker.Common.ProcessDialogBox;
import com.khasna.cooker.Models.Collection;
import com.khasna.cooker.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by nachiket on 4/24/2017.
 */

public class FragmentUpdateAccountChef extends Fragment {
    View mView;
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
    DatabaseReference mDataBaseRef;
    ChefProfile chefProfile;
    ProcessDialogBox processDialogBox;
    Collection mCollection;

    public FragmentUpdateAccountChef() {
        mCollection = Collection.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_chef_update_account, container, false);

        mDataBaseRef = FirebaseDatabase.getInstance().getReference();

        mActiveFragmentManager          = getFragmentManager();
        buttonUpdateChefProfile         = (Button)mView.findViewById(R.id.buttonUpdateChefProfile);
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

        editTextUpdateChefEmailAddress.setText(mCollection.mFireBaseFunctions.getEmailID());

        buttonUpdateChefProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String errorCode = ValidateInput();
                if (errorCode.equals("VALID_INPUT")){
                    processDialogBox.ShowDialogBox();

                    mCollection.mFireBaseFunctions.UpdateEmail(
                            editTextUpdateChefEmailAddress.getText().toString(),
                            new Interfaces.UpdateEmailInterface() {
                                @Override
                                public void UpdateEmailComplete() {
                                    UpdateNewFields();
                                    Toast.makeText(getContext(), "Information updated", Toast.LENGTH_LONG).show();
                                    processDialogBox.DismissDialogBox();
                                }

                                @Override
                                public void UpdateEmailFailed(String error) {
                                    Toast.makeText(getContext(),error, Toast.LENGTH_LONG).show();
                                    processDialogBox.DismissDialogBox();
                                }
                            });
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
        mCollection.mFireBaseFunctions.UpdateProfileName(
                editTextUpdateChefFName.getText().toString(),
                editTextUpdateChefLName.getText().toString()
        );

        chefProfile = new ChefProfile(editTextUpdateStreetAddress.getText().toString(),
                editTextUpdateAptNo.getText().toString(),
                editTextUpdateCity.getText().toString(),
                editTextUpdateChefFName.getText().toString(),
                editTextUpdateChefLName.getText().toString(),
                editTextUpdatePhoneNumber.getText().toString(),
                editTextUpdateZipCode.getText().toString());

        LocationResultReciever resultReciever =new LocationResultReciever(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);
                if(resultCode==1){
                    System.out.println("There is a valid address ");
                    Address address = resultData.getParcelable("address");
                    Double longitude =  address.getLongitude();
                    Double latitude = address.getLatitude();
                    chefProfile.setLatitude(latitude);
                    chefProfile.setLongitude(longitude);
                    mCollection.mDataBaseFunctions.UpdateProfileAddress(
                            mDataBaseRef,
                            chefProfile,
                            new Interfaces.UpdateProfileInterface() {
                                @Override
                                public void UpdateProfileComplete() {
                                    ChefEntity.chefProfile = chefProfile;
                                    Toast.makeText(getContext(), "Information updated", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void UpdateProfileFailed(String error) {
                                    Toast.makeText(getContext(),"There has been problem connecting to the server" +
                                                    "Please try again in sometime ",
                                            Toast.LENGTH_LONG).show();
                                    Toast.makeText(getContext(),error, Toast.LENGTH_LONG).show();
                                }
                            });
                }
                else{
                    Toast.makeText(getContext(), "There was a problem with the address verification." +
                            "Please check your address. ", Toast.LENGTH_LONG).show();
                }
            }
        };
        LocationHandler locationHandler= new LocationHandler(chefProfile,this.getContext(),resultReciever);


    }
}
