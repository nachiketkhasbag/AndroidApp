package com.khasna.cooker.Common;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.Locale;

/**
 * Created by Sourabh on 01/07/2018.
 */

public class AddressResolverService extends IntentService {

    public AddressResolverService() {
        super("AddressResolverService");

    }

    public AddressResolverService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent==null)
            return;
        Geocoder geocode = new Geocoder(this, Locale.getDefault());
        String address = intent.getStringExtra("ADDRESS");
        String apt = intent.getStringExtra("apt");
        String city =intent.getStringExtra("city");
        String zipcode = intent.getStringExtra("zipcode");
        final String  finalAddress = address+apt+city+zipcode;
        ResultReceiver receiver= intent.getParcelableExtra("intent_reciever");
        try {
            Bundle resultBundle = new Bundle();
            List<Address> addresses = null;
            addresses = geocode.getFromLocationName(finalAddress, 10);
            if(addresses.size()==0){
                System.out.println("There was a error");
                resultBundle.putString("error","No address found");
                receiver.send(0,resultBundle);
            }
            else{
                resultBundle.putParcelable("address",addresses.get(0));
                receiver.send(1,resultBundle);
            }
        }
        catch(Exception e){
            System.out.println("There was a exception");
        }


    }
}
