package com.example.cooker.GuestActivityFragments.ContainerClasses;


import java.io.Console;

public class OrderHistoryGuestItem {
    String itemName;
    Long itemQuantity;
    String orderTime;
    String price;
    String status;
    String chefName;

    public OrderHistoryGuestItem(String itemName, Long itemQuantity, String orderTime, String price, String status, String chefName) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.orderTime = orderTime;
        this.price = price;
        this.status = status;
        this.chefName = chefName;
    }

    public OrderHistoryGuestItem() {
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        System.out.println("ItemSet");
        this.itemName = itemName;
    }

    public Long getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Long itemQuantity) {
        System.out.println("Quantity");
        this.itemQuantity = itemQuantity;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        System.out.println("Ordertime");
        this.orderTime = orderTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        System.out.println("Price");

        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {

        System.out.println("Status");
        this.status = status;
    }

    public String getChefName() {
        return chefName;
    }

    public void setChefName(String chefName) {
        System.out.println("ChefName");

        this.chefName = chefName;
    }

}
