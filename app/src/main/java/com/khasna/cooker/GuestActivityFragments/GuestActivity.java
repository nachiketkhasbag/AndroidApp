package com.khasna.cooker.GuestActivityFragments;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;

import android.support.v4.view.GravityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.khasna.cooker.Common.Interfaces;
import com.khasna.cooker.Common.MainActivity;
import com.khasna.cooker.Common.ProcessDialogBox;
import com.khasna.cooker.Models.Collection;
import com.khasna.cooker.R;
import com.google.firebase.database.DatabaseError;

/**
 * Created by nachiket on 4/6/2017.
 */

public class GuestActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,Interfaces.DataBaseReadInterface{

    Toolbar mToolbar;
    Fragment mActiveFragment;
    FragmentManager mActiveFragmentManager;
    TextView mTextViewUserName;
    TextView mTextViewUserEmail;
    ProcessDialogBox processDialogBox;
    NavigationView navigationView;
    Collection mCollection;

    public GuestActivity() {
        mCollection = Collection.getInstance(this);

        mCollection.InitDatabase();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);
        mActiveFragmentManager = getSupportFragmentManager();

        mCollection.PushToken();

        processDialogBox = new ProcessDialogBox(this);
        processDialogBox.ShowDialogBox();

        setupDrawer();
    }

    private void setupDrawer() {

        mToolbar = findViewById(R.id.toolbarGuest);
        setSupportActionBar(mToolbar);
        setTitle(R.string.app_name);

        DrawerLayout drawer = findViewById(R.id.guest_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        setupNavMenu();

        View header = navigationView.getHeaderView(0);
        mTextViewUserName = header.findViewById(R.id.userNameGuest);
        mTextViewUserEmail = header.findViewById(R.id.userEmailGuest);
        mTextViewUserName.setText(mCollection.GetFireBaseUser().getDisplayName());
        mTextViewUserEmail.setText(mCollection.GetFireBaseUser().getEmail());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.guest, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id)
        {
            case R.id.action_cart:
                mActiveFragment = new FragmentViewCartGuest();
                FragmentTransaction viewCartTransaction = getSupportFragmentManager().beginTransaction();
                viewCartTransaction.replace(R.id.guest_page, mActiveFragment);
                viewCartTransaction.commit();
                setTitle(R.string.viewCart);
                navigationView.getMenu().getItem(2).setChecked(true);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();

        mActiveFragmentManager = getSupportFragmentManager();
        processDialogBox.DismissDialogBox();

        switch(id)
        {
            case R.id.viewChefs:
                mActiveFragment = new FragmentChefsListGuest();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.guest_page, mActiveFragment);
                transaction.commit();
                setTitle(R.string.viewChefs);
                break;

            case R.id.updateAccount:
                mActiveFragment = new FragmentAccountSettingsGuest();
                FragmentTransaction updateAccountTransaction = getSupportFragmentManager().beginTransaction();
                updateAccountTransaction.replace(R.id.guest_page, mActiveFragment);
                updateAccountTransaction.commit();
                setTitle(R.string.updateAccount);
                break;

            case R.id.viewCart:
                mActiveFragment = new FragmentViewCartGuest();
                FragmentTransaction viewCartTransaction = getSupportFragmentManager().beginTransaction();
                viewCartTransaction.replace(R.id.guest_page, mActiveFragment);
                viewCartTransaction.commit();
                setTitle(R.string.viewCart);
                break;

            case R.id.orderHistory:
                mActiveFragment = new FragmentOrderHistoryGuest();
                FragmentTransaction viewOrderHistoryTransaction = getSupportFragmentManager().beginTransaction();
                viewOrderHistoryTransaction.replace(R.id.guest_page, mActiveFragment);
                viewOrderHistoryTransaction.commit();
                setTitle(R.string.orderHistory);
                break;

            case R.id.signOut:
                mCollection.SignOut(new Interfaces.SignOutInterface() {
                    @Override
                    public void TaskComplete() {
                        mCollection.CleanObjects();
                        Intent signOut = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(signOut);

                        // close this activity
                        finish();
                    }

                    @Override
                    public void TaskFailed(String error) {
                        Toast.makeText(getApplicationContext(), error,
                                Toast.LENGTH_LONG).show();
                    }
                });

                break;
        }

        DrawerLayout drawer = findViewById(R.id.guest_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onBackPressed() {
        // Disable back button for now

        mActiveFragmentManager = getSupportFragmentManager();
        if(mActiveFragmentManager.getBackStackEntryCount() > 0 )
        {
            mActiveFragment = new FragmentChefsListGuest();
            FragmentTransaction transaction = mActiveFragmentManager.beginTransaction();
            transaction.replace(R.id.guest_page, mActiveFragment);
            transaction.commit();
            setTitle(R.string.viewChefs);
            navigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCollection.CleanObjects();
    }

    @Override
    public void ReadSucceeded(DataSnapshot dataSnapshot) {
        processDialogBox.DismissDialogBox();
        mActiveFragment = new FragmentChefsListGuest();
        FragmentTransaction transaction = mActiveFragmentManager.beginTransaction();
        transaction.replace(R.id.guest_page, mActiveFragment);
        transaction.commit();
        setTitle(R.string.viewChefs);
    }

    @Override
    public void ReadFailed(DatabaseError databaseError) {
        processDialogBox.DismissDialogBox();
        System.out.println(databaseError.toString());
        Toast.makeText(getApplicationContext(), databaseError.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCollection.FillGuestProfile(this);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    void setupNavMenu()
    {
        navigationView = findViewById(R.id.nav_view_guest);
        navigationView.setNavigationItemSelectedListener(GuestActivity.this);
        navigationView.getMenu().getItem(0).setChecked(true);
    }
}
