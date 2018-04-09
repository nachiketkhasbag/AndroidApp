package com.khasna.cooker.ChefActivityFragments.ContainerClasses;

/**
 * Created by nachiket on 9/4/2017.
 */

public class ChefOrderItem {
    String guestName;
    String guestPhoneNumber;
    String itemName;
    int numberOfItems;

    public ChefOrderItem() {
    }

    public ChefOrderItem(String guestName, String guestPhoneNumber, String itemName, int numberOfItems) {
        this.guestName = guestName;
        this.guestPhoneNumber = guestPhoneNumber;
        this.itemName = itemName;
        this.numberOfItems = numberOfItems;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestPhoneNumber() {
        return guestPhoneNumber;
    }

    public void setGuestPhoneNumber(String guestPhoneNumber) {
        this.guestPhoneNumber = guestPhoneNumber;
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
