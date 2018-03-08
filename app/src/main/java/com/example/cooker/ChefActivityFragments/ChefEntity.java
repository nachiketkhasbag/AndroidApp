package com.example.cooker.ChefActivityFragments;

import com.example.cooker.ChefActivityFragments.ContainerClasses.ChefItem;
import com.example.cooker.ChefActivityFragments.ContainerClasses.ChefProfile;
import com.example.cooker.ChefActivityFragments.ContainerClasses.ChefReceivedOrderItem;
import com.example.cooker.ChefActivityFragments.ContainerClasses.OrderHistoryChefItem;
import com.example.cooker.ChefActivityFragments.ContainerClasses.OrderHistoryChefItemDetails;

import java.util.ArrayList;

/**
 * Created by Sourabh on 19/08/2017.
 */

public class ChefEntity {
    public static ChefProfile chefProfile;
    public static ArrayList<ChefItem> ChefItemArrayList = new ArrayList<>();
    public static ArrayList<OrderHistoryChefItem> arrayListOrderHistoryChefItem = new ArrayList<>();
    public static ArrayList<OrderHistoryChefItemDetails> arrayListOrderHistoryChefItemDetails = new ArrayList<>();
    public static ArrayList<ChefReceivedOrderItem> chefReceivedOrderItemsArrayList = new ArrayList<>();
}
