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
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.khasna.cooker.Common.Interfaces;
import com.khasna.cooker.Common.MainActivity;
import com.khasna.cooker.Common.ProcessDialogBox;
import com.khasna.cooker.Models.Collection;
import com.khasna.cooker.R;
import com.google.firebase.database.DatabaseReference;


public class ChefActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference mDataBaseRef;
    Collection mCollection;

    public ChefActivity() {

        mCollection = Collection.getInstance();

        mDataBaseRef = FirebaseDatabase.getInstance().getReference("cookProfile").
                child(mCollection.mFireBaseFunctions.getuID());

        if (mCollection.mFireBaseFunctions.getEmailID() == null)
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

        mCollection.mChefActivityFunctions.PushToken(mDataBaseRef);

        final ProcessDialogBox processDialogBox = new ProcessDialogBox(this);
        processDialogBox.ShowDialogBox();

        mCollection.mChefActivityFunctions.GetAllChefData(
                mDataBaseRef,
                new Interfaces.ReadChefDataInterface() {
                    @Override
                    public void ReadComplete(String message) {
                        if (message.equals("start"))
                        {
                            processDialogBox.DismissDialogBox();
                            Fragment mActiveFragment;
                            FragmentManager supportFragmentManager = getSupportFragmentManager();
                            mActiveFragment = new FragmentViewListedItemsChef();
                            supportFragmentManager.beginTransaction().replace(R.id.chefs_page, mActiveFragment).commitNow();
                            setTitle(R.string.viewListedItems);
                        }
                        else
                        {
                            Fragment mActiveFragment;
                            FragmentManager supportFragmentManager = getSupportFragmentManager();
                            mActiveFragment = new FragmentReceivedOrdersChef();
                            mActiveFragment.setArguments(extras);
                            supportFragmentManager.beginTransaction().replace(R.id.chefs_page, mActiveFragment).commit();
                            setTitle(R.string.receivedOrders);
                        }
                    }

                    @Override
                    public void ReadFailed(DatabaseError databaseError) {
                        processDialogBox.DismissDialogBox();
                        Toast.makeText(getApplicationContext(), databaseError.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                });

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
                mCollection.mFireBaseFunctions.signOut();
                mCollection.mChefActivityFunctions.CleanObjects(mDataBaseRef);
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
        textViewUserName.setText(mCollection.mFireBaseFunctions.getDisplayName());
        textViewUserEmail.setText(mCollection.mFireBaseFunctions.getEmailID());
    }
}
