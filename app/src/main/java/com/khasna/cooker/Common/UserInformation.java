package com.khasna.cooker.Common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.khasna.cooker.ChefActivityFragments.ChefActivity;
import com.khasna.cooker.ChefActivityFragments.ContainerClasses.ChefProfile;
import com.khasna.cooker.GuestActivityFragments.GuestActivity;
import com.khasna.cooker.GuestActivityFragments.ContainerClasses.GuestProfile;
import com.khasna.cooker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by nachiket on 5/2/2017.
 */

public class UserInformation extends AppCompatActivity {

    private DatabaseReference databaseRef;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText editTextFname;
    private EditText editTextLname;
    private EditText editTextStreetAddress;
    private EditText editTextCity;
    private EditText editTextZipCode;
    private EditText editTextAptNo;
    private EditText editTextPhoneNumber;

    private Button buttonSignUpChef;
    private Button buttonSignUpGuest;
    public static ChefProfile chefProfile;
    public static GuestProfile guestProfile;
    private String uId;

    private enum PROFILE
    {
        CHEF_PROFILE,
        GUEST_PROFILE,
        NONE
    }

    private PROFILE profile = PROFILE.NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_information);
        UserInfo.signOut();

        /*
         * This fucntion initilaizes the screen
         * It takes too much of time .(Maybe because of database initilaization)
         * The First and Last name is accessible from FirebaseAuthentication class.
         * Can update it accordingly
         */

        //initialize the ui
        editTextFname = (EditText)findViewById(R.id.editTextFname);
        editTextLname = (EditText)findViewById(R.id.editTextLname);
        editTextStreetAddress = (EditText)findViewById(R.id.editTextUpdateStreetAddress);
        editTextCity = (EditText)findViewById(R.id.editTextCity);
        editTextZipCode = (EditText)findViewById(R.id.editTextUpdateZipCode);
        editTextPhoneNumber = (EditText)findViewById(R.id.editTextPhoneNumber);
        editTextAptNo = (EditText)findViewById(R.id.editTextUpdateAptNo);
        buttonSignUpChef = (Button)findViewById(R.id.buttonSignUpChef);
        buttonSignUpGuest = (Button)findViewById(R.id.buttonSignUpGuest);

        buttonSignUpChef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile = PROFILE.CHEF_PROFILE;
                createOnclick();
                SignUpUserAndUpdateDB();
            }
        });

        buttonSignUpGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile = PROFILE.GUEST_PROFILE;
                createOnclick();
                SignUpUserAndUpdateDB();
            }
        });
    }

    //function is called when create button is pressed
    private void createOnclick(){
        /*
         * Fucntion called when create button is clicked
         * The parts to be done: Data Validation
         */
        String firstName = editTextFname.getText().toString();
        String lastName = editTextLname.getText().toString();
        String streetAddress = editTextStreetAddress.getText().toString();
        String apt = editTextAptNo.getText().toString();
        String zipCode = editTextZipCode.getText().toString();
        String city = editTextCity.getText().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();

        if (profile == PROFILE.CHEF_PROFILE)
        {
            chefProfile = new ChefProfile(streetAddress, apt, city,
                    firstName, lastName, phoneNumber, zipCode);
        }

        else if (profile == PROFILE.GUEST_PROFILE)
        {
            guestProfile = new GuestProfile(streetAddress, apt, city,
                    firstName, lastName, phoneNumber, zipCode);
        }

    }

    public void SignUpUserAndUpdateDB()
    {
        String errorCode = "NONE";

        if(profile == PROFILE.CHEF_PROFILE)
        {
            errorCode = ValidateInputForChef();
        }
        else if(profile == PROFILE.GUEST_PROFILE)
        {
            errorCode = ValidateInputForGuest();
        }

        if (errorCode.equals("VALID_INPUT"))
        {
            //fireBaseAuthClass.SignUpWithEmail(this, SignupActivity.editTextEmailText, SignupActivity.editTextPasswordText);
        }
        else
        {
            Toast.makeText(getApplicationContext(), errorCode, Toast.LENGTH_SHORT).show();
        }

    }

    private void UpdateDB(PROFILE profile){

        if( profile == PROFILE.CHEF_PROFILE )
        {
            databaseRef.child("cookProfile").child(UserInfo.getuID()).child("profile").setValue(chefProfile, new DatabaseReference.CompletionListener() {

                //Implies that the data has been committed
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError == null)
                    {
                        databaseRef.child("cookProfile").child(UserInfo.getuID()).child("isActive").setValue(false);
                        Intent startMainChefActivity = new Intent(getApplicationContext(), ChefActivity.class);
                        startActivity(startMainChefActivity);
                        finish();
                        System.out.println("Chef Data saved successfully.");
                        return;
                    }

                    Toast.makeText(getApplicationContext(),"There has been problem connecting to the server" +
                                    "Please try again in sometime ",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
        else
        {
            databaseRef.child("userProfile").child(UserInfo.getuID()).child("profile").setValue(guestProfile, new DatabaseReference.CompletionListener() {

                //Implies that the data has been committed
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError == null)
                    {

                        Intent startMainChefActivity = new Intent(getApplicationContext(), GuestActivity.class);
                        startActivity(startMainChefActivity);
                        finish();
                        System.out.println("Chef Data saved successfully.");

                        return;
                    }

                    Toast.makeText(getApplicationContext(),"There has been problem connecting to the server" +
                                    "Please try again in sometime ",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public String ValidateInputForChef()
    {
        if (editTextFname.getText().toString().isEmpty())
        {
            return "First name is required";
        }
        if (editTextStreetAddress.getText().toString().isEmpty() |
                editTextCity.getText().toString().isEmpty() |
                editTextZipCode.getText().toString().isEmpty())
        {
            return "Complete address with zipcode is required";
        }
        if (editTextPhoneNumber.getText().toString().isEmpty())
        {
            return "Phone number is required";
        }

        return "VALID_INPUT";
    }

    public String ValidateInputForGuest()
    {
        if (editTextFname.getText().toString().isEmpty())
        {
            return "First name is required";
        }

        if (editTextPhoneNumber.getText().toString().isEmpty())
        {
            return "Phone number is required";
        }

        return "VALID_INPUT";
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
//        fireBaseAuthClass.AddAuthStateListener(mAuthListener);
        databaseRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
