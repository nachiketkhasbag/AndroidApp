package com.khasna.cooker.GuestActivityFragments.ContainerClasses;

import com.khasna.cooker.Common.Items;

/**
 * Created by nachiket on 8/30/2017.
 */

public class GuestItem extends Items {

    public GuestItem() {
    }

    public GuestItem(String itemName, String itemDescription, String itemIngredients, String itemPrice, Boolean isAvailable) {
        super(itemName, itemDescription, itemIngredients, itemPrice, isAvailable);
    }
}
