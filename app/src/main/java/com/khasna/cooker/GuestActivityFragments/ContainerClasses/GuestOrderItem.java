package com.khasna.cooker.GuestActivityFragments.ContainerClasses;

/**
 * Created by nachiket on 9/4/2017.
 */

public class GuestOrderItem {

    String chefName;
    String chefAddress;
    String itemName;
    int numberOfItems;

    public GuestOrderItem() {
    }

    public GuestOrderItem(String chefName, String chefAddress, String itemName, int numberOfItems) {
        this.chefName = chefName;
        this.chefAddress = chefAddress;
        this.itemName = itemName;
        this.numberOfItems = numberOfItems;
    }

    public String getChefName() {
        return chefName;
    }

    public void setChefName(String chefName) {
        this.chefName = chefName;
    }

    public String getChefAddress() {
        return chefAddress;
    }

    public void setChefAddress(String chefAddress) {
        this.chefAddress = chefAddress;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }
}
