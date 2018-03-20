package com.example.cooker.ChefActivityFragments.ContainerClasses;

/**
 * Created by nachiket on 9/12/2017.
 */

public class ChefReceivedOrderItem {

    String guestName;
    String guestUid;
    String guestPhoneNumber;
    String itemName;
    String price;
    String orderTime;
    String itemKey;
    String pickUpTime;
    Integer itemQuantity;

    public ChefReceivedOrderItem() {
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
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

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getGuestUid() {
        return guestUid;
    }

    public void setGuestUid(String guestUid) {
        this.guestUid = guestUid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderPickupTime() {
        return pickUpTime;
    }

    public void setOrderPickupTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
    }
}
