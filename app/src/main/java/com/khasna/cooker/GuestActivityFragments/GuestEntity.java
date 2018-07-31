package com.khasna.cooker.GuestActivityFragments;

import com.google.firebase.auth.FirebaseUser;
import com.khasna.cooker.Common.CartItem;
import com.khasna.cooker.GuestActivityFragments.ContainerClasses.ChefsListForGuest;
import com.khasna.cooker.GuestActivityFragments.ContainerClasses.GuestItem;
import com.khasna.cooker.GuestActivityFragments.ContainerClasses.GuestProfile;
import com.khasna.cooker.GuestActivityFragments.ContainerClasses.OrderHistoryGuestItem;
import com.khasna.cooker.GuestActivityFragments.ContainerClasses.OrderHistoryGuestItemDetails;

import java.util.ArrayList;

/**
 * Created by nachiket on 8/27/2017.
 */

public class GuestEntity {
    private FirebaseUser firebaseUser;
    private GuestProfile guestProfile;
    private ArrayList<ChefsListForGuest> chefsListForGuestArrayList = new ArrayList<>();
    private ArrayList<GuestItem> guestItemArrayList = new ArrayList<>();
    private ArrayList<CartItem> cartItemArrayList = new ArrayList<>();
    private ArrayList<OrderHistoryGuestItem> orderHistoryGuestItemsArrayList = new ArrayList<>();
    private ArrayList<OrderHistoryGuestItemDetails> orderHistoryGuestItemDetails = new ArrayList<>();

    private static GuestEntity single_instance = null;

    public GuestEntity() {
        guestProfile = new GuestProfile();
    }

    public static GuestEntity getInstance()
    {
        if(single_instance == null){
            single_instance = new GuestEntity();
        }

        return single_instance;
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public void setFirebaseUser(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }

    public GuestProfile getGuestProfile() {
        return guestProfile;
    }

    public void setGuestProfile(GuestProfile guestProfile) {
        this.guestProfile = guestProfile;
    }

    public ArrayList<ChefsListForGuest> getChefsListForGuestArrayList() {
        return chefsListForGuestArrayList;
    }

    public void setChefsListForGuestArrayList(ArrayList<ChefsListForGuest> chefsListForGuestArrayList) {
        this.chefsListForGuestArrayList = chefsListForGuestArrayList;
    }

    public ArrayList<GuestItem> getGuestItemArrayList() {
        return guestItemArrayList;
    }

    public void setGuestItemArrayList(ArrayList<GuestItem> guestItemArrayList) {
        this.guestItemArrayList = guestItemArrayList;
    }

    public ArrayList<CartItem> getCartItemArrayList() {
        return cartItemArrayList;
    }

    public void setCartItemArrayList(ArrayList<CartItem> cartItemArrayList) {
        this.cartItemArrayList = cartItemArrayList;
    }

    public ArrayList<OrderHistoryGuestItem> getOrderHistoryGuestItemsArrayList() {
        return orderHistoryGuestItemsArrayList;
    }

    public void setOrderHistoryGuestItemsArrayList(ArrayList<OrderHistoryGuestItem> orderHistoryGuestItemsArrayList) {
        this.orderHistoryGuestItemsArrayList = orderHistoryGuestItemsArrayList;
    }

    public ArrayList<OrderHistoryGuestItemDetails> getOrderHistoryGuestItemDetails() {
        return orderHistoryGuestItemDetails;
    }

    public void setOrderHistoryGuestItemDetails(ArrayList<OrderHistoryGuestItemDetails> orderHistoryGuestItemDetails) {
        this.orderHistoryGuestItemDetails = orderHistoryGuestItemDetails;
    }
}
