package com.khasna.cooker.Common;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.ResultReceiver;

import com.khasna.cooker.ChefActivityFragments.ContainerClasses.ChefProfile;

/**
 * Created by Sourabh on 10/06/2018.
 */

public class LocationHandler {
    public LocationHandler(ChefProfile chefProfile, Context context,ResultReceiver reciever) {
        Intent intent = new Intent(context,AddressResolverService.class);
        intent.putExtra("intent_reciever",reciever);
        intent.putExtra("ADDRESS",chefProfile.getAddress());
        intent.putExtra("city",chefProfile.getCity());
        if(chefProfile.getaptno()!=null)
            intent.putExtra("apt",chefProfile.getaptno());
        intent.putExtra("zipcode",chefProfile.getZipcode());
        context.startService(intent);
    }

}
