package com.example.cooker.GuestActivityFragments;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.cooker.Common.MainActivity;
import com.example.cooker.Common.ProcessDialogBox;
import com.example.cooker.GuestActivityFragments.ContainerClasses.ChefsListForGuest;
import com.example.cooker.GuestActivityFragments.ContainerClasses.GuestProfile;
import com.example.cooker.R;
import com.example.cooker.Common.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by nachiket on 4/6/2017.
 */

public class GuestActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    Toolbar mToolbar;
    Fragment mActiveFragment;
    FragmentManager mActiveFragmentManager;
    TextView mTextViewUserName;
    TextView mTextViewUserEmail;
    ProcessDialogBox processDialogBox;

    private ValueEventListener valueEventListenerChef;
    private ValueEventListener valueEventListenerGuest;
    private DatabaseReference mDataBaseRefChefs;
    private DatabaseReference mDataBaseRefGuest;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);
        mActiveFragmentManager = getSupportFragmentManager();
        mDataBaseRefChefs = FirebaseDatabase.getInstance().getReference("cookProfile");
        mDataBaseRefGuest = FirebaseDatabase.getInstance().getReference("userProfile").child(UserInfo.getuID());

        processDialogBox = new ProcessDialogBox(this);
        processDialogBox.ShowDialogBox();

        valueEventListenerGuest = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GuestEntity.guestProfile = getGuestProfile(dataSnapshot);
                processDialogBox.DismissDialogBox();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.toString());
            }

            private GuestProfile getGuestProfile(DataSnapshot dataSnapshot )
            {
                DataSnapshot snapshot= dataSnapshot.child("profile");
                GuestProfile guestProfile = snapshot.getValue(GuestProfile.class);
                if (guestProfile == null)
                {
                    guestProfile = new GuestProfile();
                }
                System.out.println("Profile detected");
                return guestProfile;
            }
        };

        valueEventListenerChef = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GuestEntity.chefsListForGuestArrayList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    try{
                        if(dataSnapshot.child(child.getKey()).child("isActive").getValue().toString().matches("true") ) {
                            ChefsListForGuest chefsListForGuest = child.child("profile").getValue(ChefsListForGuest.class);
                            chefsListForGuest.SetUnknownFields(dataSnapshot.child(child.getKey()).getKey());

                            GuestEntity.chefsListForGuestArrayList.add(chefsListForGuest);
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println("Error retrieving chef details");
                    }
                }
                processDialogBox.DismissDialogBox();

                mActiveFragment = new FragmentChefsListGuest();
                FragmentTransaction transaction = mActiveFragmentManager.beginTransaction();
                transaction.replace(R.id.guest_page, mActiveFragment);
                transaction.commit();
                setTitle(R.string.viewChefs);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.toString());
            }
        };

        setupDrawer();
    }

    private void setupDrawer() {

        mToolbar = (Toolbar) findViewById(R.id.toolbarGuest);
        setSupportActionBar(mToolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.guest_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_guest);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        mTextViewUserName = (TextView)header.findViewById(R.id.userNameGuest);
        mTextViewUserEmail = (TextView)header.findViewById(R.id.userEmailGuest);
        mTextViewUserName.setText(UserInfo.getDisplayName());
        mTextViewUserEmail.setText(UserInfo.getEmailID());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        mActiveFragmentManager = getSupportFragmentManager();
        if(mActiveFragmentManager.getBackStackEntryCount() > 0 )
        {
            mActiveFragmentManager.popBackStack();
        }

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
                UserInfo.signOut();
                CleanObjects();

                Intent signOut = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(signOut);

                // close this activity
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.guest_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void CleanObjects()
    {
        GuestEntity.chefsListForGuestArrayList.clear();
        GuestEntity.guestProfile = null;
        GuestEntity.cartItemArrayList.clear();
        GuestEntity.guestItemArrayList.clear();
        GuestEntity.orderHistoryGuestItemDetails.clear();
        GuestEntity.orderHistoryGuestItemsArrayList.clear();
    }

    @Override
    public void onBackPressed() {
        mActiveFragmentManager = getSupportFragmentManager();
        if(mActiveFragmentManager.getBackStackEntryCount() > 0 )
        {
            setTitle(R.string.viewChefs);
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        GuestEntity.chefsListForGuestArrayList.clear();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDataBaseRefGuest.addListenerForSingleValueEvent(valueEventListenerGuest);
        mDataBaseRefChefs.addListenerForSingleValueEvent(valueEventListenerChef);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
