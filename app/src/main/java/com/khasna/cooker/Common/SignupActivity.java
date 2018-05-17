package com.khasna.cooker.Common;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.khasna.cooker.ChefActivityFragments.ChefActivity;
import com.khasna.cooker.ChefActivityFragments.ChefEntity;
import com.khasna.cooker.ChefActivityFragments.ContainerClasses.ChefProfile;
import com.khasna.cooker.GuestActivityFragments.ContainerClasses.GuestProfile;
import com.khasna.cooker.GuestActivityFragments.GuestActivity;
import com.khasna.cooker.GuestActivityFragments.GuestEntity;
import com.khasna.cooker.Models.Collection;
import com.khasna.cooker.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by nachiket on 4/2/2017.
 */

public class SignupActivity extends AppCompatActivity{

    private static final String TAG = "SignUpActivity";
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextConfirmPassword;

    String editTextEmailText;
    String editTextPasswordText;
    String editTextConfirmPasswordText;

    Button buttonSignUpAsGuest;
    Button buttonSignUpAsChef;
    Collection mCollection;
    ProgressDialog progressDialog;
    private DatabaseReference databaseRef;

    private static int MINIMUM_PASSWORD_LENGTH = 5;
    private static final String className = "SignupActivity";

    public enum USER_TYPE
    {
        CHEF,
        GUEST
    }

    USER_TYPE userType;

    public SignupActivity() {
        mCollection = Collection.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DebugClass.DebugPrint(className, "onCreate:Activity created");
        setContentView(R.layout.activity_signup);

        editTextEmail = (EditText)findViewById(R.id.editTextEmail);                     // Textfield email
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);               // Textfield password
        editTextConfirmPassword = (EditText)findViewById(R.id.editTextConfirmPassword); // Textfield confirm password

        buttonSignUpAsGuest = (Button)findViewById(R.id.buttonSignUpAsGuest);       // SignUpGuest button listener
        buttonSignUpAsChef = (Button)findViewById(R.id.buttonSignUpAsChef);       // SignUpGuest button listener

        progressDialog = new ProgressDialog(this);

        progressDialog.setMax(100); // Progress Dialog Max Value
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("Processing"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Horizontal
        progressDialog.setCancelable(false);

        buttonSignUpAsGuest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String errorCode = ValidateInput();

                if (errorCode.compareTo("VALID_INPUT") == 0)
                {
                    progressDialog.show(); // Display Progress Dialog
                    DebugClass.DebugPrint(className, "OnClickSignUpGuest:valid input from user");
                    userType = USER_TYPE.GUEST;
                    SignUp();
                }
                else
                {
                    DebugClass.DebugPrint(className, "OnClickSignUp:invalid input from user");
                    Toast.makeText(getApplicationContext(), errorCode, Toast.LENGTH_LONG).show();
                }
            }
        });

        buttonSignUpAsChef.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String errorCode = ValidateInput();

                if (errorCode.compareTo("VALID_INPUT") == 0)
                {
                    progressDialog.show(); // Display Progress Dialog
                    DebugClass.DebugPrint(className, "OnClickSignUpChef:valid input from user");
                    userType = USER_TYPE.CHEF;
                    SignUp();
                }
                else
                {
                    DebugClass.DebugPrint(className, "OnClickSignUpChef:invalid input from user");
                    Toast.makeText(getApplicationContext(), errorCode, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void SignUp()
    {
        editTextEmailText = editTextEmail.getText().toString();
        editTextPasswordText = editTextPassword.getText().toString();
        mCollection.mFireBaseFunctions.SignUpWithEmail(userType, editTextEmailText, editTextPasswordText,
                new Interfaces.SignUpUserInterface() {
                    @Override
                    public void SignUpComplete() {
                        Toast.makeText(getApplicationContext(),
                                "Signup succeeded", Toast.LENGTH_SHORT).show();
                        System.out.print("SignUp succeeded");
                        progressDialog.dismiss();
                        if (userType == USER_TYPE.CHEF)
                        {
                            ChefEntity.chefProfile = new ChefProfile();

                            UpdateDB(ChefEntity.chefProfile);

                            // Take directly to homepage
                            Intent i = new Intent(getApplicationContext(), ChefActivity.class);
                            startActivity(i);

                            // close this activity
                            finish();
                        }
                        else
                        {
                            GuestEntity.guestProfile = new GuestProfile();

                            UpdateDB(GuestEntity.guestProfile);

                            // Take directly to homepage
                            Intent i = new Intent(getApplicationContext(), GuestActivity.class);
                            startActivity(i);

                            // close this activity
                            finish();
                        }
                    }

                    @Override
                    public void SignUpFailed(String error) {
                        Toast.makeText(getApplicationContext(), "Signup failed", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Please click on Forgot Password" +
                                "if you already have an account", Toast.LENGTH_SHORT).show();

                        System.out.print("SignUp failed");
                        progressDialog.dismiss();
                    }
                });
    }

    public String ValidateInput()
    {
        editTextEmailText = editTextEmail.getText().toString();
        editTextPasswordText = editTextPassword.getText().toString();
        editTextConfirmPasswordText = editTextConfirmPassword.getText().toString();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if ((!editTextEmailText.matches(emailPattern)) ||
                editTextEmailText.isEmpty() )
        {
            return "INVALID_EMAIL";
        }
        if (editTextPasswordText.isEmpty())
        {
            return "INVALID_PASSWORD. Password cannot be blank";
        }
        if (editTextPasswordText.length() < MINIMUM_PASSWORD_LENGTH)
        {
            return "INVALID_PASSWORD. Minimum lanegth required:5";
        }
        if (editTextPasswordText.compareTo(editTextConfirmPasswordText) != 0)
        {
            return "Password does not match. Please check password.";
        }

        return "VALID_INPUT";
    }

    public void UpdateDB( ChefProfile chefProfile )
    {
        databaseRef.child("cookProfile").child(mCollection.mFireBaseFunctions.getuID()).child("profile").setValue(chefProfile, new DatabaseReference.CompletionListener() {

            //Implies that the data has been committed
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null)
                {
                    databaseRef.child("cookProfile").child(mCollection.mFireBaseFunctions.getuID()).child("isActive").setValue(false);
                    System.out.println("Chef Data saved successfully.");
                    DebugClass.DebugPrint(className, "UpdateDB:Update cookprofile db");
                    return;
                }

                Toast.makeText(getApplicationContext(),"There has been problem connecting to the server" +
                                "Please try again in sometime ",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
    public void UpdateDB( GuestProfile guestProfile )
    {
        databaseRef.child("userProfile").child(mCollection.mFireBaseFunctions.getuID()).child("profile").setValue(guestProfile, new DatabaseReference.CompletionListener() {

            //Implies that the data has been committed
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null)
                {
                    System.out.println("Chef Data saved successfully.");
                    DebugClass.DebugPrint(className, "UpdateDB:Update guestprofile db");
                    return;
                }

                Toast.makeText(getApplicationContext(),"There has been problem connecting to the server" +
                                "Please try again in sometime ",
                        Toast.LENGTH_LONG).show();
            }
        });
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
        databaseRef = FirebaseDatabase.getInstance().getReference();
    }
}
