package com.khasna.cooker.GuestActivityFragments.ContainerClasses;

/**
 * Created by nachiket on 9/23/2017.
 */

public class OrderHistoryGuestItemDetails {
    String chefName;
    String chefPhoneNumber;
    String itemName;
    long numberOfItems;

    public OrderHistoryGuestItemDetails() {
    }

    public OrderHistoryGuestItemDetails(String chefName, String chefPhoneNumber, String itemName, long numberOfItems) {
        this.chefName = chefName;
        this.chefPhoneNumber = chefPhoneNumber;
        this.itemName = itemName;
        this.numberOfItems = numberOfItems;
    }

    public String getChefName() {
        return chefName;
    }

    public void setChefName(String chefName) {
        this.chefName = chefName;
    }

    public String getChefPhoneNumber() {
        return chefPhoneNumber;
    }

    public void setChefPhoneNumber(String chefPhoneNumber) {
        this.chefPhoneNumber = chefPhoneNumber;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public long getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(long numberOfItems) {
        this.numberOfItems = numberOfItems;
    }
}
