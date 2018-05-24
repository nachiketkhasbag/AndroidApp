package com.khasna.cooker.Models;

/**
 * Created by nachiket on 4/27/2018.
 */

public class Collection{

    private static Collection single_instance = null;

    public FireBaseFunctions mFireBaseFunctions;
    public DataBaseFunctions mDataBaseFunctions;
    public ChefActivityFunctions mChefActivityFunctions;
    public GuestActivityFunctions mGuestActivityFunctions;

    private Collection() {
        mFireBaseFunctions = new FireBaseFunctions<>(this);
        mDataBaseFunctions = new DataBaseFunctions<>(this);
        mChefActivityFunctions = new ChefActivityFunctions<>(this);
        mGuestActivityFunctions = new GuestActivityFunctions<>(this);
    }

    // static method to create instance of Singleton class
    public static Collection getInstance()
    {
        if (single_instance == null)
            single_instance = new Collection();

        return single_instance;
    }
}