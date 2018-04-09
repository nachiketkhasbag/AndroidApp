package com.khasna.cooker.Common;

import com.khasna.cooker.ChefActivityFragments.ContainerClasses.ChefOrderItem;
import com.khasna.cooker.GuestActivityFragments.ContainerClasses.GuestOrderItem;

/**
 * Created by nachiket on 7/3/2017.
 */

public class CurrentOrder {

    ChefOrderItem chefOrderItem;
    GuestOrderItem guestOrderItem;

    public CurrentOrder() {
    }

    public CurrentOrder(String chefName, String chefAddress,
                 String guestName, String guestPhoneNumber,
                 String itemName, int numberOfItems)
    {
        chefOrderItem = new ChefOrderItem(guestName, guestPhoneNumber, itemName, numberOfItems);
        guestOrderItem = new GuestOrderItem(chefName, chefAddress, itemName, numberOfItems );
    }

    public GuestOrderItem getGuestOrderItem() {
        return guestOrderItem;
    }

    public ChefOrderItem getChefOrderItem() {
        return chefOrderItem;
    }
}
