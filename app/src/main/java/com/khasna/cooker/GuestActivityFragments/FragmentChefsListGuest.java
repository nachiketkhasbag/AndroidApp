package com.khasna.cooker.GuestActivityFragments;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khasna.cooker.Common.Interfaces;
import com.khasna.cooker.GuestActivityFragments.Adapters.FragmentChefsListGuestAdapter;
import com.khasna.cooker.Models.Collection;
import com.khasna.cooker.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    RecyclerView.LayoutManager mLayoutManager;

    private DatabaseReference mDataBaseRefChefs;
    private Collection mCollection;

//    String dayOfTheWeek[] =
//    {
//            "", "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"
//    };

    public FragmentChefsListGuest() {
        // Required empty public constructor
        mCollection = Collection.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_cooks_list_for_guest, container, false);
        mDataBaseRefChefs = FirebaseDatabase.getInstance().getReference("cookProfile");

//        textViewOrderTime = (TextView)mView.findViewById(R.id.textViewOrderTime);
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

        final RecyclerView mCooksList = (RecyclerView) mView.findViewById(R.id.CooksListForGuest);
        final FragmentChefsListGuestAdapter adapter = new FragmentChefsListGuestAdapter(this);

        mCollection.mGuestActivityFunctions.GetActiveChefs(
                mDataBaseRefChefs,
                new Interfaces.ReadActiveChefsInterface() {
                    @Override
                    public void ReadComplete() {
                        mCooksList.setAdapter(adapter);
                    }

                    @Override
                    public void ReadFailed(String error) {
                        System.out.println(error);
                    }
                }
        );

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
