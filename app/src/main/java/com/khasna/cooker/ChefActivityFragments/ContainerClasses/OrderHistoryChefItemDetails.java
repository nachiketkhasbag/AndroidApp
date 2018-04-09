package com.khasna.cooker.ChefActivityFragments.ContainerClasses;

/**
 * Created by nachiket on 9/11/2017.
 */

public class OrderHistoryChefItemDetails {
    String guestName;
    String guestPhoneNumber;
    String itemName;
    long numberOfItems;

    public OrderHistoryChefItemDetails() {
    }

    public OrderHistoryChefItemDetails(String guestName, String guestPhoneNumber, String itemName, long numberOfItems) {
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

    public long getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(long numberOfItems) {
        this.numberOfItems = numberOfItems;
    }
}
