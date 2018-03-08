package com.example.cooker.ChefActivityFragments.ContainerClasses;

/**
 * Created by nachiket on 9/10/2017.
 */

public class OrderHistoryChefItem {
    String OrderDate;
    String OrderCount;

    public OrderHistoryChefItem(String orderDate, String orderCount) {
        OrderDate = orderDate;
        OrderCount = orderCount;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getOrderCount() {
        return OrderCount;
    }

    public void setOrderCount(String orderCount) {
        OrderCount = orderCount;
    }
}
