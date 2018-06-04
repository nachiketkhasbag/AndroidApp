package com.khasna.cooker.Common;

/**
 * Created by nachiket on 9/4/2017.
 */

public class CartItem {
    private String chefName;
    private String chefUid;
    private String guestName;
    private String guestUid;
    private String guestPhoneNumber;
    private String chefAddress;
    private Integer itemQuantity;
    private String itemName;
    private String itemPrice;
    private String orderTime;
    private String pickUpTime;
    private String totalPrice;
    private String chefPhoneNumber;
    private String guestAddress;

    public CartItem() {
    }

    public CartItem(String chefName, String chefUid, String guestName, String guestUid,
                    String chefAddress, Integer itemQuantity, String itemName, String guestPhoneNumber,
                    String itemPrice, String pickUpTime, String totalPrice, String chefPhoneNumber, String guestAddress) {
        this.chefName = chefName;
        this.chefUid = chefUid;
        this.guestName = guestName;
        this.guestUid = guestUid;
        this.chefAddress = chefAddress;
        this.itemQuantity = itemQuantity;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.guestPhoneNumber = guestPhoneNumber;
        this.pickUpTime = pickUpTime;
        this.totalPrice = totalPrice;
        this.chefPhoneNumber = chefPhoneNumber;
        this.guestAddress = guestAddress;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public String getGuestPhoneNumber() {
        return guestPhoneNumber;
    }

    public void setGuestPhoneNumber(String guestPhoneNumber) {
        this.guestPhoneNumber = guestPhoneNumber;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getChefName() {
        return chefName;
    }

    public void setChefName(String chefName) {
        this.chefName = chefName;
    }

    public String getChefUid() {
        return chefUid;
    }

    public void setChefUid(String chefUid) {
        this.chefUid = chefUid;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestUid() {
        return guestUid;
    }

    public void setGuestUid(String guestUid) {
        this.guestUid = guestUid;
    }

    public String getChefAddress() {
        return chefAddress;
    }

    public void setChefAddress(String chefAddress) {
        this.chefAddress = chefAddress;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getChefPhoneNumber() {
        return chefPhoneNumber;
    }

    public void setChefPhoneNumber(String chefPhoneNumber) {
        this.chefPhoneNumber = chefPhoneNumber;
    }

    public String getGuestAddress() {
        return guestAddress;
    }

    public void setGuestAddress(String guestAddress) {
        this.guestAddress = guestAddress;
    }
}
