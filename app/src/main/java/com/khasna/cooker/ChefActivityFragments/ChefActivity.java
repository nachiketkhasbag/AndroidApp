package com.khasna.cooker.ChefActivityFragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.khasna.cooker.ChefActivityFragments.ContainerClasses.ChefItem;
import com.khasna.cooker.ChefActivityFragments.ContainerClasses.ChefProfile;
import com.khasna.cooker.Common.DebugClass;
import com.khasna.cooker.Common.MainActivity;
import com.khasna.cooker.Common.ProcessDialogBox;
import com.khasna.cooker.R;
import com.khasna.cooker.Common.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class ChefActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference mDataBaseRef;

    public ChefActivity() {

        mDataBaseRef = FirebaseDatabase.getInstance().getReference("cookProfile").
                child(UserInfo.getuID());

        if (UserInfo.getEmailID() == null)
        {
            System.out.print("!!!!!!!!!!!!!ALERT - SHOULDN'T COME HERE!!!!!!!!!!!!!");
            System.out.print("!!!!!!!!!!!!!PLEASE DEBUG THIS!!!!!!!!!!!!!");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle extras = getIntent().getExtras();

        setContentView(R.layout.activity_chef);

        PushToken();

        final ProcessDialogBox processDialogBox = new ProcessDialogBox(this);
        processDialogBox.ShowDialogBox();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getExistingItems(dataSnapshot);
                processDialogBox.DismissDialogBox();
                Fragment mActiveFragment;

                ChefEntity.chefProfile = getChefProfile(dataSnapshot);
                String message = "start";
                FragmentManager supportFragmentManager = getSupportFragmentManager();

                if( extras != null )
                {
                    message = extras.getString("source");
                }
                if (message.equals("notification")) {
                    mActiveFragment = new FragmentReceivedOrdersChef();
                    mActiveFragment.setArguments(extras);
                    supportFragmentManager.beginTransaction().replace(R.id.chefs_page, mActiveFragment).commit();
                    setTitle(R.string.receivedOrders);
                } else {
                    mActiveFragment = new FragmentViewListedItemsChef();
                    supportFragmentManager.beginTransaction().replace(R.id.chefs_page, mActiveFragment).commitNow();
                    setTitle(R.string.viewListedItems);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.toString());
            }

            public void getExistingItems(DataSnapshot dataSnapshot){
                ChefEntity.ChefItemArrayList.clear();
                for (DataSnapshot child : dataSnapshot.child("items").getChildren()) {
                    ChefItem chefItem = child.getValue(ChefItem.class);
                    ChefEntity.ChefItemArrayList.add(chefItem);
                    System.out.println("Data detected");
                }
            }

            public ChefProfile getChefProfile(DataSnapshot dataSnapshot){
                DataSnapshot snapshot= dataSnapshot.child("profile");
                ChefProfile chefProfile = snapshot.getValue(ChefProfile.class);
                if (chefProfile == null)
                {
                    chefProfile = new ChefProfile();
                }
                System.out.println("Profile detected");
                return chefProfile;
            }
        };

        mDataBaseRef.addListenerForSingleValueEvent(valueEventListener);

        setupDrawerAndToolbar();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            getSupportFragmentManager().popBackStack();
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment mActiveFragment;
        FragmentManager supportFragmentManager = getSupportFragmentManager();

        switch(id)
        {
            case R.id.viewListedItems:
                mActiveFragment = new FragmentViewListedItemsChef();
                supportFragmentManager.beginTransaction().replace(R.id.chefs_page, mActiveFragment).commit();
                setTitle(R.string.viewListedItems);
                break;
            case R.id.addNewItem:
                mActiveFragment = new FragmentAddNewItemsChef();
                supportFragmentManager.beginTransaction().replace(R.id.chefs_page, mActiveFragment).commit();
                setTitle(R.string.addNewItem);
                break;
            case R.id.deleteItem:
                mActiveFragment = new FragmentDeleteItemChef();
                supportFragmentManager.beginTransaction().replace(R.id.chefs_page, mActiveFragment).commit();
                setTitle(R.string.deleteItem);
                break;
            case R.id.updateAccount:
                mActiveFragment = new FragmentUpdateAccountChef();
                supportFragmentManager.beginTransaction().replace(R.id.chefs_page, mActiveFragment).commit();
                setTitle(R.string.updateAccount);
                break;
            case R.id.receivedOrders:
                mActiveFragment = new FragmentReceivedOrdersChef();
                supportFragmentManager.beginTransaction().replace(R.id.chefs_page, mActiveFragment).commit();
                setTitle(R.string.receivedOrders);
                break;
            case R.id.orderHistory:
                mActiveFragment = new FragmentOrderHistoryChef();
                supportFragmentManager.beginTransaction().replace(R.id.chefs_page, mActiveFragment).commit();
                setTitle(R.string.orderHistory);
                break;
            case R.id.signOut:
                UserInfo.signOut();
                CleanObjects();
                ChefEntity.ChefItemArrayList.clear();
                Intent signOut = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(signOut);

                // close this activity
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void CleanObjects()
    {
        ChefEntity.ChefItemArrayList.clear();
        ChefEntity.chefProfile = null;
        ChefEntity.chefReceivedOrderItemsArrayList.clear();
        ChefEntity.arrayListOrderHistoryChefItem.clear();
        ChefEntity.arrayListOrderHistoryChefItemDetails.clear();

        mDataBaseRef.child("isActive").setValue(false);

        mDataBaseRef.child("token").setValue("");
    }

    public void setupDrawerAndToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView textViewUserName = (TextView)header.findViewById(R.id.userName);
        TextView textViewUserEmail = (TextView)header.findViewById(R.id.userEmail);
        textViewUserName.setText(UserInfo.getDisplayName());
        textViewUserEmail.setText(UserInfo.getEmailID());
    }

    private void PushToken()
    {
        String token = FirebaseInstanceId.getInstance().getToken();
        DebugClass.DebugPrint("ChefActivity", "PushToken:New push token");
        mDataBaseRef.child("token").setValue(token);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
