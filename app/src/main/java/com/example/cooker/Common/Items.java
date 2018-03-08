package com.example.cooker.Common;

/**
 * Created by nachiket on 5/7/2017.
 */

public class Items {

    protected String itemName;
    protected String itemDescription;
    protected String itemIngredients;
    protected String itemPrice;
    protected Boolean isAvailable;

    public Items()
    {

    }

    public Items(String itemName, String itemDescription, String itemIngredients, String itemPrice, Boolean isAvailable)
    {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemIngredients = itemIngredients;
        this.itemPrice = itemPrice;
        this.isAvailable = isAvailable;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public void setItemIngredients(String itemIngredients) {
        this.itemIngredients = itemIngredients;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getitemName() {
        if (itemName == null){
            return "Name unavailable";
        }
        return itemName;
    }

    public String getitemDescription() {
        if (itemDescription == null){
            return "";
        }
        return itemDescription;
    }

    public String getItemIngredients() {
        if (itemIngredients == null){
            return "";
        }
        return itemIngredients;
    }

    public String getItemPrice() {
        if (itemPrice == null){
            return "0";
        }
        return itemPrice;
    }

    public Boolean getIsAvailable() {
        if (isAvailable == null)
        {
            isAvailable = false;
        }
        return isAvailable;
    }
}
