package com.khasna.cooker.Common;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.firebase.ui.auth.AuthUI;
import com.khasna.cooker.GuestActivityFragments.ContainerClasses.GuestProfile;
import com.khasna.cooker.GuestActivityFragments.GuestActivity;
import com.khasna.cooker.GuestActivityFragments.GuestEntity;
import com.khasna.cooker.Models.Collection;
import com.khasna.cooker.R;
import java.util.Arrays;
import java.util.List;
public class MainActivity extends AppCompatActivity implements
        com.khasna.cooker.Common.Interfaces.UserInterface,
        Interfaces.AppUserInterface{
    private static final String TAG = "LoginActivity";
    private static final String className = "MainActivity";
    private static final int RC_SIGN_IN = 9001;

    // Choose authentication providers
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.GoogleBuilder().build(),
            new AuthUI.IdpConfig.EmailBuilder().build());

    Collection mCollection;
    ProcessDialogBox processDialogBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        DebugClass.DebugPrint(className, "onCreate:Activity created");

        processDialogBox = new ProcessDialogBox(this);
        processDialogBox.ShowDialogBox();

        mCollection = Collection.getInstance();
        mCollection.mFireBaseFunctions.WaitForUserLogin(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                UserSignedIn();
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                System.out.print("Sign in failed");
            }
        }
    }

    @Override
    public void UserSignedIn() {

        processDialogBox.DismissDialogBox();

        GuestEntity.guestProfile = new GuestProfile();

        Intent i = new Intent(getApplicationContext(), GuestActivity.class);
        startActivity(i);

        // close this activity
        finish();
    }

    @Override
    public void UserSignedOut() {
        processDialogBox.DismissDialogBox();

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.drawable.logobigred)
                        .setTheme(R.style.CookerAppTheme)
                        .setTosAndPrivacyPolicyUrls("https://sites.google.com/view/cookerptcd/home", "https://sites.google.com/view/cookerptcd/home")
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }
}