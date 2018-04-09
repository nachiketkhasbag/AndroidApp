package com.khasna.cooker.GuestActivityFragments;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khasna.cooker.GuestActivityFragments.Adapters.FragmentChefsListGuestAdapter;
import com.khasna.cooker.GuestActivityFragments.ContainerClasses.ChefsListForGuest;
import com.khasna.cooker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentChefsListGuest extends Fragment implements FragmentChefsListGuestAdapter.OnClickListener{

    private Fragment mActiveFragment;
    private View mView;
    private int orderDay;
    private TextView textViewOrderTime;
    RecyclerView.LayoutManager mLayoutManager;

    private ValueEventListener valueEventListenerChef;
    private DatabaseReference mDataBaseRefChefs;

    String dayOfTheWeek[] =
    {
            "", "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"
    };

    public FragmentChefsListGuest() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_cooks_list_for_guest, container, false);
        mDataBaseRefChefs = FirebaseDatabase.getInstance().getReference("cookProfile");

        textViewOrderTime = (TextView)mView.findViewById(R.id.textViewOrderTime);
        String[] ids = TimeZone.getAvailableIDs(-8 * 60 * 60 * 1000);

        SimpleTimeZone simpleTimeZone = new SimpleTimeZone(-8 * 60 * 60 * 1000, ids[0]);
        Calendar calendar = new GregorianCalendar(simpleTimeZone);
        Date trialTime = new Date();
        calendar.setTime(trialTime);
        orderDay = calendar.get(Calendar.DAY_OF_WEEK);
        if (orderDay == 7){
            orderDay = 1;
        }
        else
        {
            orderDay += 1;
        }

        textViewOrderTime.setText("Currently accepting orders for " + dayOfTheWeek[orderDay]);

        final RecyclerView mCooksList = (RecyclerView) mView.findViewById(R.id.CooksListForGuest);
        final FragmentChefsListGuestAdapter adapter = new FragmentChefsListGuestAdapter(this);

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
                        mCooksList.setAdapter(adapter);
                    }
                    catch (Exception e)
                    {
                        System.out.println("Error retrieving chef details");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.toString());
            }
        };
        mDataBaseRefChefs.addListenerForSingleValueEvent(valueEventListenerChef);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mCooksList.setLayoutManager(mLayoutManager);

        // Inflate the layout for this fragment
        return mView;
    }

    @Override
    public void OnClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack("CooksItems");

        mActiveFragment = new ViewListedItemsGuest();
        mActiveFragment.setArguments(bundle);

        transaction.replace(R.id.guest_page, mActiveFragment);
        transaction.commit();
    }
}
