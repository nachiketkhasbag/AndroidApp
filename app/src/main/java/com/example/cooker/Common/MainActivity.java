package com.example.cooker.Common;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cooker.ChefActivityFragments.ChefActivity;
import com.example.cooker.ChefActivityFragments.ChefEntity;
import com.example.cooker.ChefActivityFragments.ContainerClasses.ChefProfile;
import com.example.cooker.GuestActivityFragments.ContainerClasses.GuestProfile;
import com.example.cooker.GuestActivityFragments.GuestActivity;
import com.example.cooker.GuestActivityFragments.GuestEntity;
import com.example.cooker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements com.example.cooker.Common.Interfaces.UserInterface{
    private static final String TAG = "LoginActivity";
    private static final String className = "MainActivity";

    //declareing the mCallbackManager
//    CallbackManager mCallbackManager;
//    LoginButton mFbLoginButton;
    Button mLoginButton;
    Button mSignUpButton;
    TextView mForgotPassword;

    private DatabaseReference mDataBaseRef;
    private ValueEventListener mValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DebugClass.DebugPrint(className, "onCreate:Activity created");

        mLoginButton = (Button)findViewById(R.id.loginButton);   // Login button listener
        mSignUpButton = (Button)findViewById(R.id.signupButton); // Signup button listener
//        mFbLoginButton = (LoginButton)findViewById(R.id.facebookLogin);   //declaring and instantiate the loin button
        mForgotPassword = (TextView)findViewById(R.id.forgotPasswordView);

        mLoginButton.setVisibility(View.GONE);
        mSignUpButton.setVisibility(View.GONE);
//        mFbLoginButton.setVisibility(View.GONE);
//        mFbLoginButton.setClickable(false);
        mForgotPassword.setVisibility(View.GONE);

        UserInfo.AuthenticateUser(this);

        mForgotPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Forgot password activity
                Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });


        // Facebook button listener
//        mCallbackManager =CallbackManager.Factory.create();
//
//        //register the callbackmanager with the button.
//        mFbLoginButton.registerCallback(mCallbackManager,new FacebookCallback< LoginResult>(){
//            @Override
//            public void onSuccess(LoginResult result){
//                //create a new activity
//                startWelcome(true);             //true indicates that the user has signed in using Facebook
//            }
//
//            @Override
//            public void onCancel(){
//                //display message
//            }
//            @Override
//            public void onError(FacebookException fbe){
//
//            }
//        });
//        mFbLoginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                List<String> list=new ArrayList<>();
//                list.add("email");
//                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this,list);
//            }
//        });
    }

    public void login() {
        Log.d(TAG, "Login");
        final EditText email = ( EditText)findViewById(R.id.email);
        final EditText password = ( EditText)findViewById(R.id.password);

        String errorCode = ValidateInput();

        if (errorCode.compareTo("VALID_INPUT") == 0)
        {
            DebugClass.DebugPrint(className, "login:valid input from user");
            FireBaseAuthClass.SignInWithEmail(this, email.getText().toString(), password.getText().toString() );
        }
        else
        {
            DebugClass.DebugPrint(className, "login:invalid input from user");
            Toast.makeText(getApplicationContext(), errorCode, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
//        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void UserSignedIn() {

        mDataBaseRef = FirebaseDatabase.getInstance().getReference();
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("cookProfile").child(UserInfo.getuID()).exists() &&
                        dataSnapshot.child("userProfile").child(UserInfo.getuID()).exists())
                {
                    //Toast.makeText(android.content.Context.getApplicationContext(), "THERE HAS BEEN A SERIOUS PROBLEM. PLEASE REPORT THIS", Toast.LENGTH_LONG).show();
                    System.out.print("!!!!!!!!!!!!!!!!THERE HAS BEEN A SERIOUS PROBLEM. NEEDS DEBUG!!!!!!!!!!!!!!!!!!!!!!");
                    Toast.makeText(getApplicationContext(),"Serious problem with account. Please contact customer service. Signing out...", Toast.LENGTH_LONG).show();
                    UserInfo.signOut();
                }
                else if(dataSnapshot.child("cookProfile").child(UserInfo.getuID()).exists())
                {
                    PushToken("cookProfile", UserInfo.getuID());
                    ChefEntity.chefProfile = new ChefProfile();

                    Bundle bundle = new Bundle();
                    bundle.putString("source", "MainActivity");

                    Intent i = new Intent(getApplicationContext(), ChefActivity.class);
                    i.putExtras(bundle);
                    startActivity(i);

                    // close this activity
                    finish();
                }
                else if(dataSnapshot.child("userProfile").child(UserInfo.getuID()).exists())
                {
                    PushToken("userProfile", UserInfo.getuID());
                    GuestEntity.guestProfile = new GuestProfile();

                    Intent i = new Intent(getApplicationContext(), GuestActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
                else
                {
                    // TODO: THIS IS TEMPORARY. THIS SHOULD NOT BE PRESENT
                    // TODO: ALL UNKNOWN USERS DIRECTED TO GUEST ACTIVITY
                    PushToken("userProfile", UserInfo.getuID());
                    GuestEntity.guestProfile = new GuestProfile();

                    Intent i = new Intent(getApplicationContext(), GuestActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.toString());
            }
        };
        mDataBaseRef.addListenerForSingleValueEvent(mValueEventListener);
    }

    @Override
    public void UserSignedOut() {
        mLoginButton.setVisibility(View.VISIBLE);
        mSignUpButton.setVisibility(View.VISIBLE);
//        mFbLoginButton.setVisibility(View.GONE);
        mForgotPassword.setVisibility(View.VISIBLE);
    }

    protected void startWelcome(boolean Facebook){
        if (Facebook)
        {
            //extract the name using the file
            // Dummy comment
        }
        Intent intent=new Intent(this,WelcomeActivity.class);
        startActivity(intent);
    }

    public String ValidateInput()
    {
        EditText emailID = (EditText)findViewById(R.id.email); // Textfield email
        EditText password = (EditText)findViewById(R.id.password); // Textfield password

        String validateEmail = emailID.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String validatePass = password.getText().toString();

        if ((!validateEmail.matches(emailPattern)) ||
                validateEmail.isEmpty() )
        {
            return "INVALID_EMAIL";
        }
        if (validatePass.isEmpty())
        {
            return "PASSWORD cannot be blank";
        }

        return "VALID_INPUT";
    }

    private void PushToken( String profileType, String uID )
    {
        final String token = FirebaseInstanceId.getInstance().getToken();
        DebugClass.DebugPrint(className, "PushToken:New push token");
        switch (profileType){
            case "cookProfile":
                mDataBaseRef.child("cookProfile").child(uID).child("token").setValue(token);
                break;

            case "userProfile":
                mDataBaseRef.child("userProfile").child(uID).child("token").setValue(token);
                break;
        }
    }
}