package com.example.cooker.GuestActivityFragments;

import com.example.cooker.Common.CartItem;
import com.example.cooker.GuestActivityFragments.ContainerClasses.ChefsListForGuest;
import com.example.cooker.GuestActivityFragments.ContainerClasses.GuestItem;
import com.example.cooker.GuestActivityFragments.ContainerClasses.GuestProfile;
import com.example.cooker.GuestActivityFragments.ContainerClasses.OrderHistoryGuestItem;
import com.example.cooker.GuestActivityFragments.ContainerClasses.OrderHistoryGuestItemDetails;

import java.util.ArrayList;

/**
 * Created by nachiket on 8/27/2017.
 */

public class GuestEntity {
    public static GuestProfile guestProfile;
    public static ArrayList<ChefsListForGuest> chefsListForGuestArrayList = new ArrayList<>();
    public static ArrayList<GuestItem> guestItemArrayList = new ArrayList<>();
    public static ArrayList<CartItem> cartItemArrayList = new ArrayList<>();
    public static ArrayList<OrderHistoryGuestItem> orderHistoryGuestItemsArrayList = new ArrayList<>();
    public static ArrayList<OrderHistoryGuestItemDetails> orderHistoryGuestItemDetails = new ArrayList<>();
}
