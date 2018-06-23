package com.khasna.cooker.Common;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.khasna.cooker.ChefActivityFragments.ChefActivity;
import com.khasna.cooker.ChefActivityFragments.ChefEntity;
import com.khasna.cooker.ChefActivityFragments.ContainerClasses.ChefProfile;
import com.khasna.cooker.GuestActivityFragments.ContainerClasses.GuestProfile;
import com.khasna.cooker.GuestActivityFragments.GuestActivity;
import com.khasna.cooker.GuestActivityFragments.GuestEntity;
import com.khasna.cooker.Models.Collection;
import com.khasna.cooker.R;

public class MainActivity extends AppCompatActivity implements
        com.khasna.cooker.Common.Interfaces.UserInterface,
        Interfaces.AppUserInterface,
        Interfaces.AppUserLocatorInterface{
    private static final String TAG = "LoginActivity";
    private static final String className = "MainActivity";

    //declareing the mCallbackManager
//    CallbackManager mCallbackManager;
//    LoginButton mFbLoginButton;
    Button mLoginButton;
    Button mSignUpButton;
    TextView mForgotPassword;
    TextView mTextViewPP;
    TextView mTextViewTC;
    Collection mCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DebugClass.DebugPrint(className, "onCreate:Activity created");

        mCollection = Collection.getInstance();
        mCollection.mFireBaseFunctions.WaitForUserLogin(this);

        mLoginButton = (Button)findViewById(R.id.loginButton);   // Login button listener
        mSignUpButton = (Button)findViewById(R.id.signupButton); // Signup button listener
//        mFbLoginButton = (LoginButton)findViewById(R.id.facebookLogin);   //declaring and instantiate the loin button
        mForgotPassword = (TextView)findViewById(R.id.forgotPasswordView);
        mTextViewPP = (TextView)findViewById(R.id.textViewPP);
        mTextViewTC = (TextView)findViewById(R.id.textViewTC);

        mLoginButton.setVisibility(View.GONE);
        mSignUpButton.setVisibility(View.GONE);
//        mFbLoginButton.setVisibility(View.GONE);
//        mFbLoginButton.setClickable(false);
        mForgotPassword.setVisibility(View.GONE);

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

        mTextViewPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/cookerptcd/home"));
                startActivity(intent);
            }
        });

        mTextViewTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/cookerptcd/home"));
                startActivity(intent);
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
            try {
                DebugClass.DebugPrint(className, "login:valid input from user");
                mCollection.mFireBaseFunctions.SignInWithEmail(this, email.getText().toString(), password.getText().toString());
            }
            catch(Exception e) {
                Toast.makeText(getApplicationContext(),"Wrong Credentials. Make sure you are signed in on your phone.",Toast.LENGTH_LONG).show();
            }
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
        mCollection.mDataBaseFunctions.LocateUser(this);
    }

    @Override
    public void UserSignedOut() {
        mLoginButton.setVisibility(View.VISIBLE);
        mSignUpButton.setVisibility(View.VISIBLE);
//        mFbLoginButton.setVisibility(View.GONE);
        mForgotPassword.setVisibility(View.VISIBLE);
    }

//    protected void startWelcome(boolean Facebook){
//        if (Facebook)
//        {
//            //extract the name using the file
//            // Dummy comment
//        }
//        Intent intent=new Intent(this,WelcomeActivity.class);
//        startActivity(intent);
//    }

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

    @Override
    public void UserIsChef() {
        ChefEntity.chefProfile = new ChefProfile();

        Bundle bundle = new Bundle();
        bundle.putString("source", "MainActivity");

        Intent i = new Intent(getApplicationContext(), ChefActivity.class);
        i.putExtras(bundle);
        startActivity(i);

        // close this activity
        finish();
    }

    @Override
    public void UserIsGuest() {
        GuestEntity.guestProfile = new GuestProfile();

        Intent i = new Intent(getApplicationContext(), GuestActivity.class);
        startActivity(i);

        // close this activity
        finish();
    }

    @Override
    public void ForceUserSignOut() {
        //Toast.makeText(android.content.Context.getApplicationContext(), "THERE HAS BEEN A SERIOUS PROBLEM. PLEASE REPORT THIS", Toast.LENGTH_LONG).show();
        System.out.print("!!!!!!!!!!!!!!!!THERE HAS BEEN A SERIOUS PROBLEM. NEEDS DEBUG!!!!!!!!!!!!!!!!!!!!!!");
        mLoginButton.setVisibility(View.VISIBLE);
        mSignUpButton.setVisibility(View.VISIBLE);
//        mFbLoginButton.setVisibility(View.GONE);
        mForgotPassword.setVisibility(View.VISIBLE);
        mCollection.mFireBaseFunctions.signOut();
    }
}