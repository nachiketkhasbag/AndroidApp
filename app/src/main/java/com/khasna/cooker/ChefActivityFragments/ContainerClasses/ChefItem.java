package com.khasna.cooker.ChefActivityFragments.ContainerClasses;

import com.khasna.cooker.Common.Items;

/**
 * Created by nachiket on 8/21/2017.
 */

public class ChefItem extends Items {

    public ChefItem(){

    }

    public ChefItem(String itemName, String itemDescription, String itemIngredients, String itemPrice, boolean isAvailable) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemIngredients = itemIngredients;
        this.itemPrice = itemPrice;
        this.isAvailable = isAvailable;
    }
}
